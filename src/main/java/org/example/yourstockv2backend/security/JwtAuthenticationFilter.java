package org.example.yourstockv2backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.yourstockv2backend.exception.CustomException;
import org.example.yourstockv2backend.model.User;
import org.example.yourstockv2backend.service.RefreshTokenService;
import org.example.yourstockv2backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Пропускаем OPTIONS-запросы (preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            logger.debug("Skipping JWT filter for OPTIONS request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs/")) {
            logger.debug("Skipping JWT filter for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getJwtFromRequest(request);
        String refreshToken = getRefreshTokenFromCookies(request);

        try {
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                logger.info("Access token is valid for request: {}", request.getRequestURI());
                setAuthentication(request, accessToken);
                filterChain.doFilter(request, response);
                return;
            }

            logger.warn("Access token is invalid or expired for request: {}", request.getRequestURI());

            if (refreshToken != null) {
                logger.info("Attempting to refresh access token using refresh token for request: {}", request.getRequestURI());
                Long userId = refreshTokenService.verifyRefreshToken(refreshToken);
                User user = userService.findById(userId)
                        .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
                );

                accessToken = jwtTokenProvider.generateAccessToken(authentication);
                logger.info("New access token generated for user: {}", user.getUsername());
                setAuthentication(request, accessToken);

                response.setHeader("X-New-Access-Token", accessToken);
                filterChain.doFilter(request, response);
            } else {
                logger.error("Refresh token is missing for request: {}", request.getRequestURI());
                throw new CustomException("Refresh token is missing", HttpStatus.UNAUTHORIZED);
            }
        } catch (CustomException e) {
            logger.error("Authentication error for request {}: {}", request.getRequestURI(), e.getMessage());
            response.setStatus(e.getStatus().value());
            response.getWriter().write(e.getMessage());
            response.setContentType("application/json");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void setAuthentication(HttpServletRequest request, String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        String role = jwtTokenProvider.getRoleFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}