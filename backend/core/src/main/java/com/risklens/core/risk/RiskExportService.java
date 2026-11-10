package com.risklens.core.risk;

import com.risklens.core.risk.dto.ExportFileDto;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiskExportService {

    private final RiskAssessmentRepository riskAssessmentRepository;

    @Transactional(readOnly = true)
    public ExportFileDto exportAssessment(String email, UUID assessmentId, String format) {
        RiskAssessment assessment =
            riskAssessmentRepository
                .findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Risk assessment not found"));

        // Verify ownership via Portfolio -> User relation
        if (!assessment.getPortfolio().getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not have permission to export this assessment");
        }

        if ("csv".equalsIgnoreCase(format)) {
            String csvContent = generateCsvContent(assessment);
            Resource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
            String filename = "risk-assessment-" + assessmentId + ".csv";
            return new ExportFileDto(resource, MediaType.parseMediaType("text/csv"), filename);
        } else {
            byte[] pdfBytes = generatePdfBytes(assessment);
            Resource resource = new ByteArrayResource(pdfBytes);
            String filename = "risk-assessment-" + assessmentId + ".pdf";
            return new ExportFileDto(resource, MediaType.APPLICATION_PDF, filename);
        }
    }

    private String generateCsvContent(RiskAssessment assessment) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Portfolio ID,VaR 95,Volatility,Max Drawdown,Sharpe Ratio,Risk Score,Calculated At\n");
        sb.append(assessment.getId())
            .append(",")
            .append(assessment.getPortfolio().getId())
            .append(",")
            .append(assessment.getVar95())
            .append(",")
            .append(assessment.getVolatility())
            .append(",")
            .append(assessment.getMaxDrawdown())
            .append(",")
            .append(assessment.getSharpeRatio())
            .append(",")
            .append(assessment.getRiskScore())
            .append(",")
            .append(assessment.getCalculatedAt());
        return sb.toString();
    }

    private byte[] generatePdfBytes(RiskAssessment assessment) {
        String dummyPdfText = "Risk Assessment Report: " + assessment.getId();
        return dummyPdfText.getBytes(StandardCharsets.UTF_8);
    }
}
