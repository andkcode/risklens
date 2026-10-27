package com.risklens.core.risk;

import com.risklens.core.risk.dto.RiskAssessmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RiskMapper {

  RiskAssessmentResponseDto toDto(RiskAssessment riskAssessment);
}
