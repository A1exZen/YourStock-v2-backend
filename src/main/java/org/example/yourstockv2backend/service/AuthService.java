package org.example.yourstockv2backend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.yourstockv2backend.config.JwtConfig;
import org.example.yourstockv2backend.dto.JwtResponse;
import org.example.yourstockv2backend.dto.LoginRequest;
import org.example.yourstockv2backend.dto.PersonalDetailDTO;
import org.example.yourstockv2backend.dto.RegisterRequest;
import org.example.yourstockv2backend.exception.CustomException;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.model.User;
import org.example.yourstockv2backend.model.enums.Role;
import org.example.yourstockv2backend.repository.PersonalDetailRepository;
import org.example.yourstockv2backend.repository.UserRepository;
import org.example.yourstockv2backend.security.JwtTokenProvider;
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

    @Transactional
    public void register(RegisterRequest request) {
        logger.info("Registering new user: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        if (personalDetailRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email is already in use", HttpStatus.BAD_REQUEST);
        }

        PersonalDetailDTO personalDetailDTO = new PersonalDetailDTO();
        personalDetailDTO.setPhoneNumber(request.getPhoneNumber());
        personalDetailDTO.setFirstName(request.getFirstName());
        personalDetailDTO.setLastName(request.getLastName());
        personalDetailDTO.setEmail(request.getEmail());
        personalDetailDTO.setCity(request.getCity());
        PersonalDetail personalDetail = personalDetailService.createPersonalDetail(personalDetailDTO);

        String position =  request.getPosition() != null ? request.getPosition() : "Employee";
        Employee employee = employeeService.createEmployee(position, personalDetail);

        Role role;
        if (request.getRole() == null || request.getRole().isEmpty()) {
            role = Role.EMPLOYEE;
        } else {
            try {
                role = Role.valueOf(request.getRole());
            } catch (IllegalArgumentException e) {
                throw new CustomException("Invalid role: " + request.getRole(), HttpStatus.BAD_REQUEST);
            }
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userService.createUser(request.getUsername(), encodedPassword, employee, role);
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

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
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
}