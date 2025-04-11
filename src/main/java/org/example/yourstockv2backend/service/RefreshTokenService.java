package org.example.yourstockv2backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String createRefreshToken(Long userId) {
        String token = UUID.randomUUID().toString();
        String key = "refreshToken:" + token;
        String value = userId + ":" + (System.currentTimeMillis() + refreshTokenExpiration);
        redisTemplate.opsForValue().set(key, value, refreshTokenExpiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForSet().add("userTokens:" + userId, token);
        return token;
    }

    public Long verifyRefreshToken(String token) {
        return findUserIdByToken(token);
    }

    public Long findUserIdByToken(String token) {
        String key = "refreshToken:" + token;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new RuntimeException("Refresh token not found or expired");
        }
        String[] parts = value.split(":");
        Long userId = Long.parseLong(parts[0]);
        long expiryDate = Long.parseLong(parts[1]);
        if (System.currentTimeMillis() > expiryDate) {
            redisTemplate.delete(key);
            redisTemplate.opsForSet().remove("userTokens:" + userId, token);
            throw new RuntimeException("Refresh token has expired");
        }
        return userId;
    }

    public void deleteByUserId(Long userId) {
        String userTokensKey = "userTokens:" + userId;
        var tokens = redisTemplate.opsForSet().members(userTokensKey);
        if (tokens != null) {
            for (Object token : tokens) {
                String tokenKey = "refreshToken:" + token;
                redisTemplate.delete(tokenKey);
            }
        }
        redisTemplate.delete(userTokensKey);
    }

    public void deleteRefreshToken(String token) {
        Long userId = findUserIdByToken(token);
        String key = "refreshToken:" + token;
        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove("userTokens:" + userId, token);
    }
}