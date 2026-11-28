package com.risklens.core.risk;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.risklens.core.marketdata.MarketDataProvider;
import com.risklens.core.portfolio.Position;
import com.risklens.core.risk.dto.ExportFileDto;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
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
  private final MarketDataProvider marketDataProvider;

  private static final String FONT_PATH = "fonts/Metropolis-Black.otf";

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

  private byte[] generatePdfBytes(RiskAssessment assessment) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Document document = new Document(PageSize.A4, 36, 36, 54, 54);

    try {
      PdfWriter.getInstance(document, baos);
      document.open();

      // Load embedded Unicode font (retained for safe character rendering)
      BaseFont baseFont;
      try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream(FONT_PATH)) {
        if (fontStream == null) {
          throw new IOException("Font file not found in classpath: " + FONT_PATH);
        }
        byte[] fontBytes = fontStream.readAllBytes();
        baseFont =
            BaseFont.createFont(
                "Metropolis-Black.otf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED,
                true,
                fontBytes,
                null);
      }

      Font titleFont = new Font(baseFont, 18, Font.BOLD, new Color(33, 37, 41));
      Font headerFont = new Font(baseFont, 12, Font.BOLD, new Color(73, 80, 87));
      Font normalFont = new Font(baseFont, 10, Font.NORMAL, new Color(108, 117, 125));
      Font boldFont = new Font(baseFont, 10, Font.BOLD, new Color(33, 37, 41));

      // 1. Header
      Paragraph title = new Paragraph("Portfolio Risk Assessment Report", titleFont);
      title.setSpacingAfter(15);
      document.add(title);

      // 2. Metadata Block
      PdfPTable metaTable = new PdfPTable(2);
      metaTable.setWidthPercentage(100);
      metaTable.setSpacingAfter(15);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String formattedDate =
          assessment.getCalculatedAt() != null
              ? assessment.getCalculatedAt().format(formatter)
              : "N/A";

      addMetaRow(metaTable, "Assessment ID:", assessment.getId().toString(), normalFont, boldFont);
      addMetaRow(
          metaTable, "Portfolio:", assessment.getPortfolio().getName(), normalFont, boldFont);
      addMetaRow(
          metaTable,
          "Base Currency:",
          assessment.getPortfolio().getBaseCurrency(),
          normalFont,
          boldFont);
      addMetaRow(metaTable, "Calculated At:", formattedDate, normalFont, boldFont);
      document.add(metaTable);

      // 3. Risk Metrics & Score Block
      Paragraph metricsTitle = new Paragraph("Key Metrics & Risk Level", headerFont);
      metricsTitle.setSpacingAfter(8);
      document.add(metricsTitle);

      PdfPTable metricsTable = new PdfPTable(2);
      metricsTable.setWidthPercentage(100);
      metricsTable.setSpacingAfter(20);

      String riskLevel = determineRiskLevel(assessment.getRiskScore());
      addMetaRow(
          metricsTable,
          "VaR (95%):",
          assessment.getVar95() + " " + assessment.getPortfolio().getBaseCurrency(),
          normalFont,
          boldFont);
      addMetaRow(
          metricsTable, "Volatility:", assessment.getVolatility().toString(), normalFont, boldFont);
      addMetaRow(
          metricsTable,
          "Max Drawdown:",
          assessment.getMaxDrawdown().toString(),
          normalFont,
          boldFont);
      addMetaRow(
          metricsTable,
          "Sharpe Ratio:",
          assessment.getSharpeRatio().toString(),
          normalFont,
          boldFont);
      addMetaRow(
          metricsTable,
          "Risk Score:",
          assessment.getRiskScore() + " (" + riskLevel + ")",
          normalFont,
          boldFont);
      document.add(metricsTable);

      // 4. Positions Table
      Paragraph positionsTitle = new Paragraph("Portfolio Positions", headerFont);
      positionsTitle.setSpacingAfter(8);
      document.add(positionsTitle);

      PdfPTable positionsTable = new PdfPTable(4);
      positionsTable.setWidthPercentage(100);
      positionsTable.setWidths(new float[] {3f, 2f, 2f, 2f});

      addTableHeader(positionsTable, "Asset Symbol", boldFont);
      addTableHeader(positionsTable, "Asset Type", boldFont);
      addTableHeader(positionsTable, "Quantity", boldFont);
      addTableHeader(positionsTable, "Avg Buy Price", boldFont);
      addTableHeader(positionsTable, "Current Price", boldFont);

      if (assessment.getPortfolio().getPositions() != null
          && !assessment.getPortfolio().getPositions().isEmpty()) {
        for (Position pos : assessment.getPortfolio().getPositions()) {
          String currentPrice =
              marketDataProvider
                  .getLastPrice(pos.getAssetSymbol())
                  .map(quote -> quote.price().toString())
                  .orElse(pos.getAvgBuyPrice() != null ? pos.getAvgBuyPrice().toString() : "-");
          addTableCell(positionsTable, pos.getAssetSymbol(), normalFont);
          addTableCell(positionsTable, pos.getAssetType(), normalFont);
          addTableCell(positionsTable, pos.getQuantity().toString(), normalFont);
          addTableCell(positionsTable, pos.getAvgBuyPrice().toString(), normalFont);
          addTableCell(positionsTable, currentPrice, normalFont);
        }
      } else {
        PdfPCell cell = new PdfPCell(new Phrase("No positions recorded", normalFont));
        cell.setColspan(4);
        positionsTable.addCell(cell);
      }

      document.add(positionsTable);

    } catch (DocumentException | IOException e) {
      throw new RuntimeException("Error generating PDF report", e);
    } finally {
      document.close();
    }

    return baos.toByteArray();
  }

  private void addMetaRow(
      PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
    PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
    cell1.setBorder(Rectangle.NO_BORDER);
    cell1.setPadding(4);
    table.addCell(cell1);

    PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
    cell2.setBorder(Rectangle.NO_BORDER);
    cell2.setPadding(4);
    table.addCell(cell2);
  }

  private void addTableHeader(PdfPTable table, String text, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setBackgroundColor(new Color(241, 243, 245));
    cell.setPadding(6);
    table.addCell(cell);
  }

  private void addTableCell(PdfPTable table, String text, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setPadding(6);
    table.addCell(cell);
  }

  private String determineRiskLevel(Short score) {
    if (score == null) return "Undefined";
    if (score < 30) return "Low";
    if (score < 70) return "Moderate";
    return "High";
  }

  private String generateCsvContent(RiskAssessment assessment) {
    StringBuilder sb = new StringBuilder();
    sb.append("=== METADATA ===\n");
    sb.append("Assessment ID,").append(assessment.getId()).append("\n");
    sb.append("Portfolio ID,").append(assessment.getPortfolio().getId()).append("\n");
    sb.append("Portfolio Name,")
        .append(escapeCsv(assessment.getPortfolio().getName()))
        .append("\n");
    sb.append("Calculated At,").append(assessment.getCalculatedAt()).append("\n\n");

    sb.append("=== RISK METRICS ===\n");
    sb.append("Metric,Value\n");
    sb.append("VaR (95%),").append(assessment.getVar95()).append("\n");
    sb.append("Volatility,").append(assessment.getVolatility()).append("\n");
    sb.append("Max Drawdown,").append(assessment.getMaxDrawdown()).append("\n");
    sb.append("Sharpe Ratio,").append(assessment.getSharpeRatio()).append("\n");
    sb.append("Risk Score,").append(assessment.getRiskScore()).append("\n\n");

    sb.append("=== PORTFOLIO POSITIONS ===\n");
    sb.append("Asset Symbol,Asset Type,Quantity,Avg Buy Price,Current Price \n");

    if (assessment.getPortfolio().getPositions() != null
        && !assessment.getPortfolio().getPositions().isEmpty()) {
      assessment
          .getPortfolio()
          .getPositions()
          .forEach(
              position -> {
                String currentPrice =
                    marketDataProvider
                        .getLastPrice(position.getAssetSymbol())
                        .map(quote -> quote.price().toString())
                        .orElse(
                            position.getAvgBuyPrice() != null
                                ? position.getAvgBuyPrice().toString()
                                : "");
                sb.append(escapeCsv(position.getAssetSymbol()))
                    .append(",")
                    .append(escapeCsv(position.getAssetType()))
                    .append(",")
                    .append(position.getQuantity())
                    .append(",")
                    .append(position.getAvgBuyPrice())
                    .append(",")
                    .append(escapeCsv(currentPrice))
                    .append("\n");
              });
    } else {
      sb.append("No positions recorded,-,-,-,-\n");
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
}
