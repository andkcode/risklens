package com.risklens.core.portfolio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.risklens.core.BaseIntegrationTest;
import com.risklens.core.user.User;
import com.risklens.core.user.UserRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class PortfolioPositionIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PortfolioRepository portfolioRepository;

  @Autowired private PositionRepository positionRepository;

  private User testUser;
  private Portfolio testPortfolio;

  @BeforeEach
  void setUp() {
    // Clean database state before each test
    positionRepository.deleteAll();
    portfolioRepository.deleteAll();
    userRepository.deleteAll();

    // Persist test owner matching MockUser username
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

    // Persist test portfolio linked to the user
    testPortfolio =
        portfolioRepository.save(
            Portfolio.builder()
                .user(testUser)
                .name("Global Equities")
                .baseCurrency("USD")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build());
  }

  @Test
  @WithMockUser(username = "trader@risklens.com")
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
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.assetSymbol").value("AAPL"))
        .andExpect(jsonPath("$.assetType").value("STOCK"))
        .andExpect(jsonPath("$.quantity").value(15.5))
        .andExpect(jsonPath("$.avgBuyPrice").value(185.25));

    // Verify database persistence and entity mapping
    assertThat(positionRepository.findAll()).hasSize(1);
  }

  @Test
  @WithMockUser(username = "trader@risklens.com")
  void addPosition_DuplicateAssetSymbol_ReturnsConflict409() throws Exception {
    // Save initial position to trigger unique constraint on (portfolio_id, asset_symbol)
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

    // Attempting to add the same symbol again must result in 409 Conflict
    mockMvc
        .perform(
            post("/api/v1/portfolios/" + testPortfolio.getId() + "/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateRequestJson))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser(username = "intruder@risklens.com")
  void accessPortfolio_OwnerIsolation_ReturnsNotFound404() throws Exception {
    // A different user tries to query a portfolio they do not own
    mockMvc
        .perform(get("/api/v1/portfolios/" + testPortfolio.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "trader@risklens.com")
  void getPortfolioById_HappyPath_ReturnsPortfolio() throws Exception {
    mockMvc
        .perform(get("/api/v1/portfolios/" + testPortfolio.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(testPortfolio.getId().toString()))
        .andExpect(jsonPath("$.name").value("Global Equities"))
        .andExpect(jsonPath("$.baseCurrency").value("USD"));
  }
}
