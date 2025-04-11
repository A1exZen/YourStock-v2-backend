package org.example.yourstockv2backend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.yourstockv2backend.config.JwtConfig;
import org.example.yourstockv2backend.dto.*;
import org.example.yourstockv2backend.exception.CustomException;
import org.example.yourstockv2backend.mapper.EmployeeMapper;
import org.example.yourstockv2backend.mapper.PersonalDetailMapper;
import org.example.yourstockv2backend.mapper.ReportMapper;
import org.example.yourstockv2backend.mapper.UserMapper;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.model.Report;
import org.example.yourstockv2backend.model.User;
import org.example.yourstockv2backend.model.enums.Role;
import org.example.yourstockv2backend.repository.PersonalDetailRepository;
import org.example.yourstockv2backend.repository.UserRepository;
import org.example.yourstockv2backend.security.JwtTokenProvider;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonalDetailRepository personalDetailRepository;

    @Autowired
    private PersonalDetailService personalDetailService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private PersonalDetailMapper personalDetailMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Transactional
    public void register(RegisterRequest request) {
        logger.info("Registering new user: {}", request.getUsername());

        try {
            userService.getUserByUsername(request.getUsername());
            throw new CustomException("Username is already taken", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            if (!e.getMessage().contains("User not found")) {
                throw e;
            }
        }

        try {
            personalDetailService.getPersonalDetailByEmail(request.getEmail());
            throw new CustomException("Email is already in use", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            if (!e.getMessage().contains("PersonalDetail not found")) {
                throw e;
            }
        }

        PersonalDetail personalDetail = new PersonalDetail();
        personalDetail.setPhoneNumber(request.getPhoneNumber());
        personalDetail.setFirstName(request.getFirstName());
        personalDetail.setLastName(request.getLastName());
        personalDetail.setEmail(request.getEmail());
        personalDetail.setCity(request.getCity());
        PersonalDetailDTO personalDetailDTO = personalDetailMapper.toDto(personalDetail);
        personalDetail = personalDetailMapper.toEntity(personalDetailService.createPersonalDetail(personalDetailDTO));

        Employee employee = new Employee();
        employee.setPosition(request.getPosition()!= null ? request.getPosition() : "Employee");
        employee.setPersonalDetails(personalDetail);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        employee = employeeMapper.toEntity(employeeService.createEmployee(employeeDTO));

        Role role = request.getRole() != null && !request.getRole().isEmpty()
                ? Role.valueOf(request.getRole())
                : Role.EMPLOYEE;

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmployee(employee);
        user.setRole(role);
        userService.createUser(user);

//        Report report = new Report();
//        report.setAction(Report.Action.USER_REGISTERED);
//        report.setDetails(String.format("Registered new user: %s with role: %s", request.getFirstName(), request.getRole()));
//        report.setEmployee(employee);
//        reportService.createReport(reportMapper.toDto(report));
    }

    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        logger.info("User login attempt: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        String username = jwtTokenProvider.getUsernameFromToken(accessToken);
        String role = jwtTokenProvider.getRoleFromToken(accessToken);

//        User user = userService.findUserEntityByUsername(username);
//        Hibernate.initialize(user.getEmployee());
        UserDTO userDTO = userService.getUserByUsername(username);
        User user = userMapper.toEntity(userDTO);

        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (jwtConfig.getRefreshTokenExpiration() / 1000));
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        response.addCookie(refreshTokenCookie);

        logger.info("User {} logged in successfully with role {}", username, role);
        return new JwtResponse(accessToken, username, role);
    }

    public void logout(String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        User user = userMapper.toEntity(userDTO);

        refreshTokenService.deleteByUserId(user.getId());

    }

    public JwtResponse refreshToken(String refreshToken) {
        Long userId = refreshTokenService.findUserIdByToken(refreshToken);

        UserDTO userDTO = userService.getUserById(userId);
        User user = userMapper.toEntity(userDTO);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(() -> user.getRole().name())
        );
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);


        return new JwtResponse(accessToken, "Bearer", user.getUsername(), user.getRole(), user.getId());
    }
}