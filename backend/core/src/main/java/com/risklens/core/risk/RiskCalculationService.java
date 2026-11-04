package com.risklens.core.risk;

import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.PortfolioRepository;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import com.risklens.core.risk.dto.RiskAssessmentResponseDto;
import com.risklens.core.risk.strategy.RiskMethod;
import com.risklens.core.risk.strategy.RiskParams;
import com.risklens.core.risk.strategy.RiskResult;
import com.risklens.core.risk.strategy.RiskStrategy;
import com.risklens.core.risk.strategy.RiskStrategyFactory;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RiskCalculationService {

  private final PortfolioRepository portfolioRepository;
  private final PositionRepository positionRepository;
  private final RiskAssessmentRepository riskAssessmentRepository;
  private final RiskStrategyFactory strategyFactory;
  private final RiskMapper riskMapper;

  @Transactional
  public RiskAssessmentResponseDto calculateAndSaveRisk(
      String userEmail, UUID portfolioId, RiskMethod requestedMethod) {
    Portfolio portfolio = getAndValidatePortfolio(portfolioId, userEmail);

    List<Position> positions = positionRepository.findAllByPortfolioId(portfolioId);
    if (positions.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Cannot calculate risk for an empty portfolio");
    }

    // Check portfolio history: < 30 days of history -> 422 Unprocessable Entity
    validateHistoryDays(portfolio);

    RiskMethod method = requestedMethod != null ? requestedMethod : RiskMethod.SCORING;
    RiskStrategy strategy = strategyFactory.getStrategy(method);

    RiskParams params =
        RiskParams.builder()
            .riskMethod(method)
            .confidenceLevel(java.math.BigDecimal.valueOf(0.95))
            .timeHorizonDays(1)
            .build();

    RiskResult result = strategy.calculate(portfolio, params);

    RiskAssessment assessment =
        RiskAssessment.builder()
            .portfolio(portfolio)
            .calculatedAt(OffsetDateTime.now())
            .volatility(result.getVolatility())
            .var95(result.getVarValue())
            .maxDrawdown(result.getMaxDrawdown())
            .sharpeRatio(result.getSharpeRatio())
            .riskScore(result.getScore())
            .build();

    RiskAssessment savedAssessment = riskAssessmentRepository.save(assessment);

    return riskMapper.toDto(savedAssessment);
  }

  public Page<RiskAssessmentResponseDto> getRiskAssessments(
      String userEmail, UUID portfolioId, Pageable pageable) {

    getAndValidatePortfolio(portfolioId, userEmail);

    Page<RiskAssessment> assessments =
        riskAssessmentRepository.findAllByPortfolioId(portfolioId, pageable);

    return assessments.map(riskMapper::toDto);
  }

  public RiskAssessmentResponseDto getRiskAssessmentById(String userEmail, UUID assessmentId) {
    RiskAssessment assessment =
        riskAssessmentRepository
            .findByIdAndPortfolioUserEmail(assessmentId, userEmail)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Risk Assessment not found with id: " + assessmentId));

    return riskMapper.toDto(assessment);
  }

  public byte[] exportToCsv(String userEmail, UUID assessmentId) {
    RiskAssessmentResponseDto dto = getRiskAssessmentById(userEmail, assessmentId);
    String csvContent =
        "ID,PortfolioID,CalculatedAt,Volatility,VaR95,MaxDrawdown,SharpeRatio,RiskScore\n"
            + dto.getId()
            + ","
            + dto.getPortfolioId()
            + ","
            + dto.getCalculatedAt()
            + ","
            + dto.getVolatility()
            + ","
            + dto.getVar95()
            + ","
            + dto.getMaxDrawdown()
            + ","
            + dto.getSharpeRatio()
            + ","
            + dto.getRiskScore();
    return csvContent.getBytes(StandardCharsets.UTF_8);
  }

  public byte[] exportToPdf(String userEmail, UUID assessmentId) {
    RiskAssessmentResponseDto dto = getRiskAssessmentById(userEmail, assessmentId);
    String pdfMock =
        "PDF Report for Risk Assessment: " + dto.getId() + " | Risk Score: " + dto.getRiskScore();
    return pdfMock.getBytes(StandardCharsets.UTF_8);
  }

  private Portfolio getAndValidatePortfolio(UUID portfolioId, String userEmail) {
    return portfolioRepository
        .findByIdAndUserEmail(portfolioId, userEmail)
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Portfolio not found with id: " + portfolioId));
  }

  private void validateHistoryDays(Portfolio portfolio) {
    if (portfolio.getCreatedAt() != null) {
      long daysBetween = ChronoUnit.DAYS.between(portfolio.getCreatedAt(), OffsetDateTime.now());
      if (daysBetween < 30) {
        throw new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Insufficient history: Portfolio must have at least 30 days of data for risk assessment.");
      }
    }
  }
}
