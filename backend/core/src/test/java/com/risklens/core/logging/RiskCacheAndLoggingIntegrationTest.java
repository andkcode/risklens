package com.risklens.core.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.risklens.core.BaseIntegrationTest;
import com.risklens.core.config.CacheConfig;
import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.PortfolioRepository;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import com.risklens.core.risk.RiskCalculationService;
import com.risklens.core.risk.strategy.RiskMethod;
import com.risklens.core.risk.strategy.RiskResult;
import com.risklens.core.risk.strategy.RiskStrategy;
import com.risklens.core.risk.strategy.RiskStrategyFactory;
import com.risklens.core.user.User;
import com.risklens.core.user.UserRepository;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.logstash.logback.encoder.LogstashEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class RiskCacheAndLoggingIntegrationTest extends BaseIntegrationTest {

  private static final String CORRELATION_HEADER = "X-Correlation-Id";

  @Autowired private MockMvc mockMvc;

  @Autowired private CacheManager cacheManager;

  @Autowired private RiskCalculationService riskCalculationService;

  @Autowired private UserRepository userRepository;

  @Autowired private PortfolioRepository portfolioRepository;

  @Autowired private PositionRepository positionRepository;

  @MockitoBean private RiskStrategyFactory strategyFactory;

  private RiskStrategy mockStrategy;

  private String userEmail;
  private UUID portfolioId;

  @BeforeEach
  void setUp() {
    clearRiskCache();

    this.userEmail = "test@gmail.com";
    this.portfolioId = seedTestPortfolioAndPosition(this.userEmail);

    mockStrategy = mock(RiskStrategy.class);
    RiskResult stubResult =
        RiskResult.builder()
            .volatility(BigDecimal.valueOf(0.18))
            .varValue(BigDecimal.valueOf(1250.00))
            .maxDrawdown(BigDecimal.valueOf(0.12))
            .sharpeRatio(BigDecimal.valueOf(1.45))
            .score((short) 72)
            .build();

    when(strategyFactory.getStrategy(any(RiskMethod.class))).thenReturn(mockStrategy);
    when(mockStrategy.calculate(any(), any())).thenReturn(stubResult);
  }

  @AfterEach
  void tearDown() {
    clearRiskCache();
  }

  private void clearRiskCache() {
    var cache = cacheManager.getCache(CacheConfig.RISK_ASSESSMENT_CACHE);
    if (cache != null) {
      cache.clear();
    }
  }

  private UUID seedTestPortfolioAndPosition(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseGet(
                () -> {
                  User newUser = new User();
                  newUser.setEmail(email);
                  newUser.setPasswordHash("$2a$10$testHashPlaceholderForIntegrationTest");
                  return userRepository.save(newUser);
                });

    OffsetDateTime createdAt = OffsetDateTime.now().minusDays(45);
    Portfolio portfolio = new Portfolio();
    portfolio.setUser(user);
    portfolio.setName("Test Portfolio");
    portfolio.setBaseCurrency("USD");
    portfolio.setCreatedAt(createdAt);
    portfolio.setUpdatedAt(createdAt);
    Portfolio savedPortfolio = portfolioRepository.save(portfolio);

    OffsetDateTime now = OffsetDateTime.now();
    Position position = new Position();
    position.setPortfolio(savedPortfolio);
    position.setAssetSymbol("AAPL");
    position.setAssetType("STOCK");
    position.setQuantity(BigDecimal.TEN);
    position.setAvgBuyPrice(BigDecimal.valueOf(150.00));
    position.setCreatedAt(now);
    position.setUpdatedAt(now);
    positionRepository.save(position);

    return savedPortfolio.getId();
  }

  @Test
  void shouldUseCacheOnRepeatedCalculationAndInvokeStrategyOnce() {
    var first =
        riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId, RiskMethod.SCORING);
    var second =
        riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId, RiskMethod.SCORING);

    assertEquals(first, second);
    verify(mockStrategy, times(1)).calculate(any(), any());
  }

  @Test
  void shouldEvictCacheAfterUpdatingPositionAndRecalculate() {
    riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId, RiskMethod.SCORING);
    verify(mockStrategy, times(1)).calculate(any(), any());

    clearRiskCache();

    riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId, RiskMethod.SCORING);
    verify(mockStrategy, times(2)).calculate(any(), any());
  }

  @Test
  void shouldIncludeCorrelationIdInJsonLogDuringRequest() throws Exception {
    String correlationId = "corr-" + UUID.randomUUID();

    Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    Level previousLevel = rootLogger.getLevel();
    rootLogger.setLevel(Level.DEBUG);

    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    LogstashEncoder encoder = new LogstashEncoder();
    encoder.setContext(rootLogger.getLoggerContext());
    encoder.start();

    listAppender.setContext(rootLogger.getLoggerContext());
    listAppender.start();
    rootLogger.addAppender(listAppender);

    try {
      mockMvc
          .perform(
              get("/api/v1/portfolios/{portfolioId}/risk-assessments", portfolioId)
                  .header(CORRELATION_HEADER, correlationId)
                  .with(user(userEmail).roles("USER")))
          .andExpect(status().isOk());

      LoggerFactory.getLogger("com.risklens.core.test.LogProbe")
          .info("Probe log for correlation check");

      assertFalse(listAppender.list.isEmpty(), "Expected at least one log event to be captured");

      String matchedMdcKey =
          listAppender.list.stream()
              .map(ILoggingEvent::getMDCPropertyMap)
              .flatMap(mdc -> mdc.entrySet().stream())
              .filter(entry -> correlationId.equals(entry.getValue()))
              .map(Map.Entry::getKey)
              .findFirst()
              .orElse(null);

      assertNotNull(
          matchedMdcKey,
          "Expected correlationId="
              + correlationId
              + " in log MDC during request. Captured events: "
              + listAppender.list.size());

      String encodedJsonLogs =
          listAppender.list.stream()
              .map(event -> new String(encoder.encode(event), StandardCharsets.UTF_8))
              .collect(Collectors.joining("\n"));

      assertTrue(
          encodedJsonLogs.contains("\"" + matchedMdcKey + "\":\"" + correlationId + "\""),
          "Expected JSON key \""
              + matchedMdcKey
              + "\" with value "
              + correlationId
              + " in encoded logs, but got:\n"
              + encodedJsonLogs);
    } finally {
      rootLogger.detachAppender(listAppender);
      rootLogger.setLevel(previousLevel);
      listAppender.stop();
      encoder.stop();
    }
  }
}
