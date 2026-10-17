package com.risklens.core.controller;

import com.risklens.core.dto.AuthRequestDto;
import com.risklens.core.dto.AuthResponseDto;
import com.risklens.core.entity.User;
import com.risklens.core.repository.UserRepository;
import com.risklens.core.security.JwtService;
import com.risklens.core.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
}
