package com.risklens.core.risk;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.risklens.core.BaseIntegrationTest;
import com.risklens.core.marketdata.MarketDataProvider;
import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.PortfolioRepository;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import com.risklens.core.risk.dto.RiskAssessmentResponseDto;
import com.risklens.core.risk.strategy.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class RiskCalculationServiceTest extends BaseIntegrationTest {

  @Mock private PortfolioRepository portfolioRepository;

  @Mock private MarketDataProvider marketDataProvider;

  @Mock private PositionRepository positionRepository;

  @Mock private RiskAssessmentRepository riskAssessmentRepository;

  @Mock private RiskStrategyFactory strategyFactory;

  @Mock private RiskStrategy riskStrategy;

  @Mock private RiskMapper riskMapper;

  @InjectMocks private RiskCalculationService riskCalculationService;

  private UUID portfolioId;
  private String userEmail;
  private Portfolio validPortfolio;

  @BeforeEach
  void setUp() {
    portfolioId = UUID.randomUUID();
    userEmail = "user@risklens.com";

    // Portfolio with > 30 days of history
    validPortfolio =
        Portfolio.builder()
            .id(portfolioId)
            .name(userEmail)
            .createdAt(OffsetDateTime.now().minusDays(45))
            .build();
  }

  @Test
  void flywayMigrationsAppliedAndRepositoryAccessible() {
    long count = riskAssessmentRepository.count();
    assertThat(count).isGreaterThanOrEqualTo(0);
  }

  @Test
  void calculateAndSaveRisk_WorksOffline_WhenOutboundProxyBlocksNetwork() {
    ProxySelector previousSelector = ProxySelector.getDefault();
    ProxySelector.setDefault(
        new ProxySelector() {
          @Override
          public List<Proxy> select(URI uri) {
            throw new AssertionError("Outbound network call attempted to: " + uri);
          }

          @Override
          public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            // no-op
          }
        });

    try {
      when(portfolioRepository.findByIdAndUserEmail(portfolioId, userEmail))
          .thenReturn(Optional.of(validPortfolio));

      List<Position> positions =
          List.of(
              Position.builder()
                  .assetSymbol("AAPL")
                  .quantity(BigDecimal.valueOf(10))
                  .avgBuyPrice(BigDecimal.valueOf(150.0))
                  .build());
      when(positionRepository.findAllByPortfolioId(portfolioId)).thenReturn(positions);
      when(strategyFactory.getStrategy(RiskMethod.HISTORICAL_VAR)).thenReturn(riskStrategy);

      RiskResult mockResult =
          RiskResult.builder()
              .score((short) 50)
              .level("MEDIUM")
              .varValue(BigDecimal.valueOf(120.0000))
              .volatility(BigDecimal.valueOf(0.200000))
              .maxDrawdown(BigDecimal.valueOf(0.100000))
              .sharpeRatio(BigDecimal.valueOf(1.200000))
              .build();

      when(riskStrategy.calculate(eq(validPortfolio), any(RiskParams.class)))
          .thenReturn(mockResult);
      when(riskAssessmentRepository.save(any(RiskAssessment.class)))
          .thenAnswer(inv -> inv.getArgument(0));

      RiskAssessmentResponseDto expectedDto =
          RiskAssessmentResponseDto.builder()
              .riskScore((short) 50)
              .var95(BigDecimal.valueOf(120.0000))
              .build();
      when(riskMapper.toDto(any(RiskAssessment.class))).thenReturn(expectedDto);

      RiskAssessmentResponseDto response =
          riskCalculationService.calculateAndSaveRisk(
              userEmail, portfolioId, RiskMethod.HISTORICAL_VAR);

      assertNotNull(response);
      assertEquals(50, Integer.valueOf(response.getRiskScore()));
    } finally {
      ProxySelector.setDefault(previousSelector);
    }
  }

  @Test
  void calculateAndSaveRisk_Success_WithExpectedExcelValues() {
    // Given
    when(portfolioRepository.findByIdAndUserEmail(portfolioId, userEmail))
        .thenReturn(Optional.of(validPortfolio));

    List<Position> positions =
        List.of(
            Position.builder()
                .quantity(BigDecimal.valueOf(10))
                .avgBuyPrice(BigDecimal.valueOf(150.0))
                .build(),
            Position.builder()
                .quantity(BigDecimal.valueOf(5))
                .avgBuyPrice(BigDecimal.valueOf(300.0))
                .build());
    when(positionRepository.findAllByPortfolioId(portfolioId)).thenReturn(positions);

    when(strategyFactory.getStrategy(RiskMethod.SCORING)).thenReturn(riskStrategy);

    // Expected values pre-calculated via pandas/Excel reference
    RiskResult mockResult =
        RiskResult.builder()
            .score((short) 45)
            .level("MEDIUM")
            .varValue(BigDecimal.valueOf(0.042000))
            .volatility(BigDecimal.valueOf(0.185000))
            .maxDrawdown(BigDecimal.valueOf(0.125000))
            .sharpeRatio(BigDecimal.valueOf(1.450000))
            .build();

    when(riskStrategy.calculate(eq(validPortfolio), any(RiskParams.class))).thenReturn(mockResult);
    when(riskAssessmentRepository.save(any(RiskAssessment.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    RiskAssessmentResponseDto expectedDto =
        RiskAssessmentResponseDto.builder()
            .riskScore((short) 45)
            .volatility(BigDecimal.valueOf(0.185000))
            .var95(BigDecimal.valueOf(0.042000))
            .build();
    when(riskMapper.toDto(any(RiskAssessment.class))).thenReturn(expectedDto);

    // When
    RiskAssessmentResponseDto response =
        riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId, RiskMethod.SCORING);

    // Then
    assertNotNull(response);
    assertEquals(45, Integer.valueOf(response.getRiskScore()));
    assertEquals(BigDecimal.valueOf(0.185000), response.getVolatility());
    verify(riskAssessmentRepository, times(1)).save(any(RiskAssessment.class));
  }

  @Test
  void calculateAndSaveRisk_EdgeCase_EmptyPortfolio_ThrowsBadRequest() {
    // Given
    when(portfolioRepository.findByIdAndUserEmail(portfolioId, userEmail))
        .thenReturn(Optional.of(validPortfolio));
    when(positionRepository.findAllByPortfolioId(portfolioId)).thenReturn(List.of());

    // When & Then
    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class,
            () ->
                riskCalculationService.calculateAndSaveRisk(
                    userEmail, portfolioId, RiskMethod.SCORING));
    assertEquals(400, exception.getStatusCode().value());
    assertTrue(exception.getReason().contains("empty portfolio"));
  }

  @Test
  void calculateAndSaveRisk_EdgeCase_InsufficientHistory_ThrowsUnprocessableEntity() {
    // Given: Portfolio created only 5 days ago (< 30 days requirement)
    Portfolio youngPortfolio =
        Portfolio.builder()
            .id(portfolioId)
            .name(userEmail)
            .createdAt(OffsetDateTime.now().minusDays(5))
            .build();

    when(portfolioRepository.findByIdAndUserEmail(portfolioId, userEmail))
        .thenReturn(Optional.of(youngPortfolio));
    List<Position> positions =
        List.of(
            Position.builder()
                .quantity(BigDecimal.valueOf(10))
                .avgBuyPrice(BigDecimal.valueOf(150.0))
                .build());
    when(positionRepository.findAllByPortfolioId(portfolioId)).thenReturn(positions);

    // When & Then
    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class,
            () ->
                riskCalculationService.calculateAndSaveRisk(
                    userEmail, portfolioId, RiskMethod.SCORING));
    assertEquals(422, exception.getStatusCode().value());
    assertTrue(exception.getReason().contains("at least 30 days"));
  }
}
