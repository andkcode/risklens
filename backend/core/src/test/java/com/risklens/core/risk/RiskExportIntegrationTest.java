package com.risklens.core.risk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.risklens.core.BaseIntegrationTest;
import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.risk.dto.ExportFileDto;
import com.risklens.core.user.User;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class RiskExportServiceTest extends BaseIntegrationTest {

  @Mock private RiskAssessmentRepository riskAssessmentRepository;

  @InjectMocks private RiskExportService riskExportService;

  private UUID assessmentId;
  private String userEmail;
  private RiskAssessment mockAssessment;

  @BeforeEach
  void setUp() {
    assessmentId = UUID.randomUUID();
    userEmail = "test@risklens.com";

    User user = User.builder().email(userEmail).build();

    Portfolio portfolio =
        Portfolio.builder()
            .id(UUID.randomUUID())
            .name("Tech Portfolio")
            .baseCurrency("USD")
            .user(user)
            .build();

    mockAssessment =
        RiskAssessment.builder()
            .id(assessmentId)
            .portfolio(portfolio)
            .calculatedAt(OffsetDateTime.now())
            .volatility(BigDecimal.valueOf(0.15))
            .var95(BigDecimal.valueOf(1250.50))
            .maxDrawdown(BigDecimal.valueOf(0.08))
            .sharpeRatio(BigDecimal.valueOf(1.85))
            .riskScore((short) 45)
            .build();
  }

  @Test
  void exportAssessment_Csv_Success() {
    when(riskAssessmentRepository.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));

    ExportFileDto result = riskExportService.exportAssessment(userEmail, assessmentId, "csv");

    assertThat(result).isNotNull();
    assertThat(result.getFilename()).isEqualTo("risk-assessment-" + assessmentId + ".csv");
    assertThat(result.getMediaType().toString()).isEqualTo("text/csv");
    assertThat(result.getResource()).isNotNull();
  }

  @Test
  void exportAssessment_Pdf_Success() {
    when(riskAssessmentRepository.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));

    ExportFileDto result = riskExportService.exportAssessment(userEmail, assessmentId, "pdf");

    assertThat(result).isNotNull();
    assertThat(result.getFilename()).isEqualTo("risk-assessment-" + assessmentId + ".pdf");
    assertThat(result.getMediaType().toString()).isEqualTo("application/pdf");
    assertThat(result.getResource()).isNotNull();
  }

  @Test
  void exportAssessment_Idor_ThrowsAccessDenied() {
    when(riskAssessmentRepository.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));

    assertThrows(
        AccessDeniedException.class,
        () -> {
          riskExportService.exportAssessment("unauthorized@risklens.com", assessmentId, "csv");
        });
  }
}
