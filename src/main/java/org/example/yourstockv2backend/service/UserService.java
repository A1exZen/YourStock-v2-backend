package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.dto.UserDTO;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.model.User;
import org.example.yourstockv2backend.model.enums.Role;
import org.example.yourstockv2backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PersonalDetailService personalDetailService;

    public User createUser(String username, String password, Employee employee, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmployee(employee);
        user.setRole(role);
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setEmployee(employeeService.toDTO(user.getEmployee()));
        return dto;
    }

}