package com.risklens.core.risk;

import com.risklens.core.risk.dto.RiskAssessmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RiskMapper {
  @Mapping(source = "portfolio.id", target = "portfolioId")
  RiskAssessmentResponseDto toDto(RiskAssessment riskAssessment);
}
