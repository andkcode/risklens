package com.risklens.core.portfolio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.risklens.core.BaseIntegrationTest;
import com.risklens.core.security.JwtService; // Fixed import: JwtService is in security package
import com.risklens.core.user.User;
import com.risklens.core.user.UserRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class PortfolioPositionIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PortfolioRepository portfolioRepository;

  @Autowired private PositionRepository positionRepository;

  @Autowired private JwtService jwtService; // Injected JwtService directly

  @Autowired
  private UserDetailsService userDetailsService; // Needed to build UserDetails for token generation

  private User testUser;
  private Portfolio testPortfolio;
  private String jwtToken;

  @BeforeEach
  void setUp() {
    positionRepository.deleteAll();
    portfolioRepository.deleteAll();
    userRepository.deleteAll();

    testUser =
        userRepository.save(
            User.builder()
                .email("trader@risklens.com")
                .passwordHash("hashed_secret")
                .role("USER")
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build());

    testPortfolio =
        portfolioRepository.save(
            Portfolio.builder()
                .user(testUser)
                .name("Global Equities")
                .baseCurrency("USD")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build());

    // Generate valid JWT token using JwtService and UserDetails
    jwtToken = jwtService.generateToken(userDetailsService.loadUserByUsername(testUser.getEmail()));
  }

  @Test
  void addPosition_HappyPath_ReturnsCreated() throws Exception {
    String requestJson =
        """
                    {
                        "assetSymbol": "AAPL",
                        "assetType": "STOCK",
                        "quantity": 15.50000000,
                        "avgBuyPrice": 185.25000000
                    }
                    """;

    mockMvc
        .perform(
            post("/api/v1/portfolios/" + testPortfolio.getId() + "/positions")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.assetSymbol").value("AAPL"));

    assertThat(positionRepository.findAll()).hasSize(1);
  }

  @Test
  void addPosition_DuplicateAssetSymbol_ReturnsConflict409() throws Exception {
    positionRepository.save(
        Position.builder()
            .portfolio(testPortfolio)
            .assetSymbol("MSFT")
            .assetType("STOCK")
            .quantity(BigDecimal.TEN)
            .avgBuyPrice(BigDecimal.valueOf(400.0))
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build());

    String duplicateRequestJson =
        """
                    {
                        "assetSymbol": "MSFT",
                        "assetType": "STOCK",
                        "quantity": 5.0,
                        "avgBuyPrice": 410.0
                    }
                    """;

    mockMvc
        .perform(
            post("/api/v1/portfolios/" + testPortfolio.getId() + "/positions")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateRequestJson))
        .andExpect(status().isConflict());
  }

  @Test
  void accessPortfolio_OwnerIsolation_ReturnsNotFound404() throws Exception {
    User intruder =
        userRepository.save(
            User.builder()
                .email("intruder@risklens.com")
                .passwordHash("hash")
                .role("USER")
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build());

    String intruderToken =
        jwtService.generateToken(userDetailsService.loadUserByUsername(intruder.getEmail()));

    mockMvc
        .perform(
            get("/api/v1/portfolios/" + testPortfolio.getId())
                .header("Authorization", "Bearer " + intruderToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void getPortfolioById_HappyPath_ReturnsPortfolio() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/portfolios/" + testPortfolio.getId())
                .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(testPortfolio.getId().toString()))
        .andExpect(jsonPath("$.name").value("Global Equities"));
  }
}
