package org.example.yourstockv2backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordHashingTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordHashingAndVerification() {

        String rawPassword = "password123";
        String encodedPassword = "$2a$10$...";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        String hashedPassword = passwordEncoder.encode(rawPassword);
        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);

        assertNotNull(hashedPassword);
        assertTrue(isMatch);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}