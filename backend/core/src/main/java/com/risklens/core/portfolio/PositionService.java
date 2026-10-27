package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PositionCreateRequestDto;
import com.risklens.core.portfolio.dto.PositionResponseDto;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public PositionResponseDto addPosition(
        String userEmail, UUID portfolioId, PositionCreateRequestDto createRequestDto) {

        Portfolio portfolio =
            portfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio Not Found With Id: " + portfolioId));

        if (!portfolio.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access denied: portfolio does not belong to the user");
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

        return PositionResponseDto.builder()
            .id(savedPosition.getId())
            .portfolioId(portfolio.getId())
            .assetSymbol(savedPosition.getAssetSymbol())
            .assetType(savedPosition.getAssetType())
            .quantity(savedPosition.getQuantity())
            .avgBuyPrice(savedPosition.getAvgBuyPrice())
            .createdAt(savedPosition.getCreatedAt())
            .updatedAt(savedPosition.getUpdatedAt())
            .build();
    }

    public Page<PositionResponseDto> getPositions(
        String userEmail, UUID portfolioId, Pageable pageable) {

        Portfolio portfolio =
            portfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio Not Found With Id: " + portfolioId));

        if (!portfolio.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access denied: portfolio does not belong to the user");
        }

        Page<Position> positions = positionRepository.findAllByPortfolioId(portfolioId, pageable);

        return positions.map(position -> PositionResponseDto.builder()
            .id(position.getId())
            .portfolioId(portfolio.getId())
            .assetSymbol(position.getAssetSymbol())
            .assetType(position.getAssetType())
            .quantity(position.getQuantity())
            .avgBuyPrice(position.getAvgBuyPrice())
            .createdAt(position.getCreatedAt())
            .updatedAt(position.getUpdatedAt())
            .build());
    }
}
