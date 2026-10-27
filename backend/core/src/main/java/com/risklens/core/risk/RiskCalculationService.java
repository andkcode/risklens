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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiskCalculationService {

    private final PortfolioRepository portfolioRepository;
    private final PositionRepository positionRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskMapper riskMapper;

    @Transactional
    public RiskAssessmentResponseDto calculateAndSaveRisk(String userEmail, UUID portfolioId) {
        Portfolio portfolio =
            portfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio Not Found With Id:" + portfolioId));

        if (!portfolio.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access Denied: portfolio does not belong to this User");
        }

        List<Position> positions = positionRepository.findAllByPortfolioId(portfolioId);
        if (positions.isEmpty()) {
            throw new RuntimeException("Cannot calculate Risk for an empty portfolio");
        }

        // Вычисляем демо-метрики риска на основе позиций
        BigDecimal volatility = BigDecimal.valueOf(0.185000); // 18.5%
        BigDecimal var95 = BigDecimal.valueOf(0.042000); // 4.2% VaR
        BigDecimal maxDrawdown = BigDecimal.valueOf(0.125000); // 12.5%
        BigDecimal sharpeRatio = BigDecimal.valueOf(1.450000); // 1.45
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

        Portfolio portfolio =
            portfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio Not Found With Id:" + portfolioId));

        if (!portfolio.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access Denied: portfolio does not belong to this User");
        }

        Page<RiskAssessment> assessments =
            riskAssessmentRepository.findAllByPortfolioId(portfolioId, pageable);

        return assessments.map(riskMapper::toDto);
    }

    public RiskAssessmentResponseDto getRiskAssessmentById(String userEmail, UUID assessmentId) {
        RiskAssessment assessment =
            riskAssessmentRepository
                .findById(assessmentId)
                .orElseThrow(
                    () -> new RuntimeException("Risk Assessment Not Found With Id: " + assessmentId));

        Portfolio portfolio = assessment.getPortfolio();
        if (!portfolio.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access Denied: portfolio does not belong to this User");
        }

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
}
