package com.risklens.core.auth;

import com.risklens.core.auth.dto.AuthRequestDto;
import com.risklens.core.auth.dto.AuthResponseDto;
import com.risklens.core.exception.UserAlreadyExistsException;
import com.risklens.core.security.JwtService;
import com.risklens.core.user.User;
import com.risklens.core.user.UserRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponseDto register(AuthRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                "User with email " + request.getEmail() + " already exists");
        }

        User user =
            User.builder()
                .email(request.getEmail().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto authenticate(AuthRequestDto request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = jwtService.generateToken(userDetails);

        User user =
            userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto refreshToken(String requestRefreshToken) {
        return refreshTokenService
            .findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(
                user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String accessToken = jwtService.generateToken(userDetails);
                    return new AuthResponseDto(accessToken, requestRefreshToken);
                })
            .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);
        }
    }
}
