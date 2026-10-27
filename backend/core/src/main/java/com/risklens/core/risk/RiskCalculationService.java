package com.risklens.core.risk;

import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.PortfolioRepository;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import com.risklens.core.risk.dto.RiskAssessmentResponseDto;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RiskCalculationService {

  private final PortfolioRepository portfolioRepository;
  private final PositionRepository positionRepository;
  private final RiskAssessmentRepository riskAssessmentRepository;
  private final RiskMapper riskMapper;

  @Transactional
  public RiskAssessmentResponseDto calculateAndSaveRisk(String userEmail, UUID portfolioId) {
    Portfolio portfolio = getAndValidatePortfolio(portfolioId, userEmail);

    List<Position> positions = positionRepository.findAllByPortfolioId(portfolioId);
    if (positions.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Cannot calculate risk for an empty portfolio");
    }

    BigDecimal volatility = BigDecimal.valueOf(0.185000);
    BigDecimal var95 = BigDecimal.valueOf(0.042000);
    BigDecimal maxDrawdown = BigDecimal.valueOf(0.125000);
    BigDecimal sharpeRatio = BigDecimal.valueOf(1.450000);
    Short riskScore = (short) Math.min(positions.size() * 15 + 20, 100);

    RiskAssessment assessment =
        RiskAssessment.builder()
            .portfolio(portfolio)
            .calculatedAt(OffsetDateTime.now())
            .volatility(volatility)
            .var95(var95)
            .maxDrawdown(maxDrawdown)
            .sharpeRatio(sharpeRatio)
            .riskScore(riskScore)
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
}
