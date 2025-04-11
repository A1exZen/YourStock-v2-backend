package org.example.yourstockv2backend.service;

import lombok.RequiredArgsConstructor;
import org.example.yourstockv2backend.dto.UserDTO;
import org.example.yourstockv2backend.mapper.UserMapper;
import org.example.yourstockv2backend.model.User;
import org.example.yourstockv2backend.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        Hibernate.initialize(user.getEmployee());
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        Hibernate.initialize(user.getEmployee());
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Hibernate.initialize(user.getEmployee());
                    return userMapper.toDto(user);
                })
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        User updatedUser = userMapper.toEntity(userDTO);
        updatedUser.setId(existingUser.getId());
        updatedUser = userRepository.save(updatedUser);
        Hibernate.initialize(updatedUser.getEmployee());
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}