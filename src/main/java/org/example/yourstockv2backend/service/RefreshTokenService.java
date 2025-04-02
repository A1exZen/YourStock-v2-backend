package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.config.JwtConfig;
import org.example.yourstockv2backend.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtConfig jwtConfig;

    public String createRefreshToken(Long userId) {
        String token = UUID.randomUUID().toString();
        String key = REFRESH_TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(key, userId.toString(), jwtConfig.getRefreshTokenExpiration(), TimeUnit.MILLISECONDS);
        return token;
    }

    public Long verifyRefreshToken(String token) {
        String key = REFRESH_TOKEN_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(key);

        if (userId == null) {
            throw new CustomException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }

        return Long.valueOf(userId);
    }

    public void deleteRefreshToken(String token) {
        String key = REFRESH_TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }
}