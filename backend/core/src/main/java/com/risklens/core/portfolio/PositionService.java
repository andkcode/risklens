package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PositionCreateRequestDto;
import com.risklens.core.portfolio.dto.PositionResponseDto;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PositionService {

  private final PositionRepository positionRepository;
  private final PortfolioRepository portfolioRepository;
  private final PositionMapper positionMapper;

  @Transactional
  public PositionResponseDto addPosition(
      String userEmail, UUID portfolioId, PositionCreateRequestDto createRequestDto) {

    Portfolio portfolio =
        portfolioRepository
            .findByIdAndUserEmail(portfolioId, userEmail)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Portfolio not found with id: " + portfolioId));

    String normalizedAssetSymbol = createRequestDto.getAssetSymbol().toUpperCase();

    if (positionRepository.existsByPortfolioIdAndAssetSymbol(portfolioId, normalizedAssetSymbol)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Position with symbol " + normalizedAssetSymbol + " already exists in this portfolio");
    }

    Position position =
        Position.builder()
            .portfolio(portfolio)
            .assetSymbol(createRequestDto.getAssetSymbol().toUpperCase())
            .assetType(createRequestDto.getAssetType().toUpperCase())
            .quantity(createRequestDto.getQuantity())
            .avgBuyPrice(createRequestDto.getAvgBuyPrice())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

    Position savedPosition = positionRepository.save(position);

    return positionMapper.toDto(savedPosition);
  }

  public Page<PositionResponseDto> getPositions(
      String userEmail, UUID portfolioId, Pageable pageable) {

    Portfolio portfolio =
        portfolioRepository
            .findByIdAndUserEmail(portfolioId, userEmail)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Portfolio not found with id: " + portfolioId));

    Page<Position> positions = positionRepository.findAllByPortfolioId(portfolioId, pageable);

    return positions.map(positionMapper::toDto);
  }
}
