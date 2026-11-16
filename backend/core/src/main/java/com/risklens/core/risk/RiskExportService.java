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

    // Verify ownership via Portfolio -> User relation using email
    if (!assessment.getPortfolio().getUser().getEmail().equals(email)) {
      throw new AccessDeniedException("You do not have permission to export this assessment");
    }

    if ("csv".equalsIgnoreCase(format)) {
      String csvContent = generateCsvContent(assessment);

      // Add UTF-8 BOM for Microsoft Excel compatibility
      byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
      byte[] contentBytes = csvContent.getBytes(StandardCharsets.UTF_8);
      byte[] csvBytesWithBom = new byte[bom.length + contentBytes.length];
      System.arraycopy(bom, 0, csvBytesWithBom, 0, bom.length);
      System.arraycopy(contentBytes, 0, csvBytesWithBom, bom.length, contentBytes.length);

      Resource resource = new ByteArrayResource(csvBytesWithBom);
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

    // 1. Metadata Section
    sb.append("=== METADATA ===\n");
    sb.append("Assessment ID,").append(assessment.getId()).append("\n");
    sb.append("Portfolio ID,").append(assessment.getPortfolio().getId()).append("\n");
    sb.append("Portfolio Name,")
        .append(escapeCsv(assessment.getPortfolio().getName()))
        .append("\n");
    sb.append("Calculated At,").append(assessment.getCalculatedAt()).append("\n\n");

    // 2. Risk Metrics Section
    sb.append("=== RISK METRICS ===\n");
    sb.append("Metric,Value\n");
    sb.append("VaR (95%),").append(assessment.getVar95()).append("\n");
    sb.append("Volatility,").append(assessment.getVolatility()).append("\n");
    sb.append("Max Drawdown,").append(assessment.getMaxDrawdown()).append("\n");
    sb.append("Sharpe Ratio,").append(assessment.getSharpeRatio()).append("\n");
    sb.append("Risk Score,").append(assessment.getRiskScore()).append("\n\n");

    // 3. Portfolio Positions Section (Using correct Position entity fields)
    sb.append("=== PORTFOLIO POSITIONS ===\n");
    sb.append("Asset Symbol,Asset Type,Quantity,Avg Buy Price\n");

    if (assessment.getPortfolio().getPositions() != null
        && !assessment.getPortfolio().getPositions().isEmpty()) {
      assessment
          .getPortfolio()
          .getPositions()
          .forEach(
              position -> {
                sb.append(escapeCsv(position.getAssetSymbol()))
                    .append(",")
                    .append(escapeCsv(position.getAssetType()))
                    .append(",")
                    .append(position.getQuantity())
                    .append(",")
                    .append(position.getAvgBuyPrice())
                    .append("\n");
              });
    } else {
      sb.append("No positions recorded,-,-\n");
    }

    return sb.toString();
  }

  private String escapeCsv(String value) {
    if (value == null) {
      return "";
    }
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }

  private byte[] generatePdfBytes(RiskAssessment assessment) {
    String dummyPdfText = "Risk Assessment Report: " + assessment.getId();
    return dummyPdfText.getBytes(StandardCharsets.UTF_8);
  }
}
