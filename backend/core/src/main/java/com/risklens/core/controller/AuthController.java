package com.risklens.core.controller;

import com.risklens.core.dto.AuthRequestDto;
import com.risklens.core.dto.AuthResponseDto;
import com.risklens.core.entity.User;
import com.risklens.core.repository.UserRepository;
import com.risklens.core.security.JwtService;
import com.risklens.core.service.RefreshTokenService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final RefreshTokenService refreshTokenService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody AuthRequestDto request) {
    if (request.getEmail() == null
        || request.getEmail().isBlank()
        || request.getPassword() == null
        || request.getPassword().isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    userRepository.save(
        User.builder()
            .email(request.getEmail().trim().toLowerCase())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .role("USER")
            .isActive(true)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    String accessToken = jwtService.generateToken(userDetails);

    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    String refreshToken = refreshTokenService.createRefreshToken(user);
    return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Void> handleAuthenticationFailure() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
