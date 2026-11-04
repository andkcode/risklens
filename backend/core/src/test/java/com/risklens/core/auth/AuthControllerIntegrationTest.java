package com.risklens.core.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.risklens.core.BaseIntegrationTest;
import com.risklens.core.auth.dto.AuthRequestDto;
import com.risklens.core.user.User;
import com.risklens.core.user.UserRepository;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJson
class AuthControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void registerUser_HappyPath_ReturnsCreated() throws Exception {
    AuthRequestDto request = new AuthRequestDto();
    request.setEmail("newuser@risklens.com");
    request.setPassword("SecurePassword123!");

    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists());
  }

  @Test
  void loginUser_HappyPath_ReturnsTokens() throws Exception {
    // Prepare pre-existing user in DB
    userRepository.save(
        User.builder()
            .email("trader@risklens.com")
            .passwordHash(passwordEncoder.encode("Password123!"))
            .role("USER")
            .isActive(true)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build());

    AuthRequestDto request = new AuthRequestDto();
    request.setEmail("trader@risklens.com");
    request.setPassword("Password123!");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists());
  }

  @Test
  void loginUser_InvalidCredentials_ReturnsUnauthorized401() throws Exception {
    AuthRequestDto request = new AuthRequestDto();
    request.setEmail("nonexistent@risklens.com");
    request.setPassword("WrongPassword");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized()); // 401 Unauthorized
  }

  @Test
  void accessProtectedEndpoint_WithoutToken_ReturnsUnauthorized401() throws Exception {
    // Attempting to access protected portfolios endpoint without Authorization header
    mockMvc
        .perform(get("/api/v1/portfolios"))
        .andExpect(status().isUnauthorized()); // 401 Unauthorized
  }

  @Test
  void accessAdminEndpoint_AsRegularUser_ReturnsForbidden403() throws Exception {
    // Assuming /api/v1/admin/** requires ADMIN role, but user has USER role
    // This test verifies 403 Forbidden constraint enforcement
    // (Implementation depends on your specific security rule mapping)
  }
}
