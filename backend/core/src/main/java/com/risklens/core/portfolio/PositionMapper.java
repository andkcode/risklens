package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PositionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PositionMapper {
    @Mapping(source = "portfolio.id", target = "portfolioId")
    PositionResponseDto toDto(Position position);
}
