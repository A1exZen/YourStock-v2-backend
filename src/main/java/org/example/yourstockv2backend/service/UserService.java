package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.dto.UserDTO;
import org.example.yourstockv2backend.exception.CustomException;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.User;
import org.example.yourstockv2backend.model.enums.Role;
import org.example.yourstockv2backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PersonalDetailService personalDetailService;

    @Transactional
    public User createUser(String username, String password, Employee employee, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmployee(employee);
        user.setRole(role);
        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public UserDTO getUserDTOById(Long id) {
        User user = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return toDTO(user);
    }
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        if (!user.getUsername().equals(userDTO.getUsername()) &&
                userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new CustomException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());


        if (userDTO.getEmployee() != null) {
            Employee employee = user.getEmployee();
            employeeService.updateEmployee(employee, userDTO.getEmployee());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id, HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setEmployee(employeeService.toDTO(user.getEmployee()));
        return dto;
    }
}