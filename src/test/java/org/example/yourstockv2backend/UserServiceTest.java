//package org.example.yourstockv2backend;
//
//import org.example.yourstockv2backend.dto.UserDTO;
//import org.example.yourstockv2backend.mapper.UserMapper;
//import org.example.yourstockv2backend.model.User;
//import org.example.yourstockv2backend.repository.UserRepository;
//import org.example.yourstockv2backend.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void testRegisterUser_Success() {
//        // Arrange
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("testuser");
//        userDTO.setPassword("password123");
//        userDTO.setEmail("test@example.com");
//        userDTO.setRole("ROLE_USER");
//
//        User user = new User();
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//        user.setRole("ROLE_USER");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
//        when(userMapper.toEntity(userDTO)).thenReturn(user);
//        when(userRepository.save(user)).thenReturn(user);
//        when(userMapper.toDTO(user)).thenReturn(userDTO);
//
//        // Act
//        UserDTO result = userService.registerUser(userDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("testuser", result.getUsername());
//        verify(userRepository, times(1)).findByUsername("testuser");
//        verify(passwordEncoder, times(1)).encode("password123");
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void testFindUserByUsername_Success() {
//        // Arrange
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//        user.setRole("ROLE_USER");
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(1L);
//        userDTO.setUsername("testuser");
//        userDTO.setEmail("test@example.com");
//        userDTO.setRole("ROLE_USER");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//        when(userMapper.toDTO(user)).thenReturn(userDTO);
//
//        // Act
//        UserDTO result = userService.findUserByUsername("testuser");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("testuser", result.getUsername());
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//
//    @Test
//    void testFindUserByUsername_NotFound() {
//        // Arrange
//        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> userService.findUserByUsername("nonexistent"));
//        verify(userRepository, times(1)).findByUsername("nonexistent");
//    }
//}