package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PortfolioResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PortfolioMapper {

    PortfolioResponseDto toDto(Portfolio portfolio);
}
