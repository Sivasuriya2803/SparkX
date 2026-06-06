package com.revcast.auth.service;

import com.revcast.auth.dto.LoginRequest;
import com.revcast.auth.dto.LoginResponse;
import com.revcast.auth.dto.UserDTO;
import com.revcast.common.exception.UnauthorizedException;
import com.revcast.security.JwtTokenProvider;
import com.revcast.user.entity.User;
import com.revcast.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service
 */
@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Attempting login for user: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UnauthorizedException("User not found"));

            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .roleName(user.getRole().getName())
                    .isActive(user.getIsActive())
                    .build();

            log.info("User login successful: {}", request.getUsername());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400000L) // 24 hours in milliseconds
                    .user(userDTO)
                    .build();

        } catch (Exception ex) {
            log.error("Login failed for user: {}", request.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    /**
     * Logout user
     */
    public void logout(String username) {
        log.info("User logged out: {}", username);
        // Token-based, no server-side logout needed
    }
}

