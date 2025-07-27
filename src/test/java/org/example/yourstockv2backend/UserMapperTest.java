package org.example.yourstockv2backend;

import org.example.yourstockv2backend.dto.UserDTO;
import org.example.yourstockv2backend.mapper.UserMapper;
import org.example.yourstockv2backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void testUserToUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        UserDTO userDTO = userMapper.toDto(user);

        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
    }

    @Test
    void testUserDTOToUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");

        User user = userMapper.toEntity(userDTO);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
    }
}