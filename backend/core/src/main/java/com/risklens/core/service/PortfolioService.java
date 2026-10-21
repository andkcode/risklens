package com.risklens.core.service;

import com.risklens.core.dto.PortfolioCreateRequestDto;
import com.risklens.core.dto.PortfolioResponseDto;
import com.risklens.core.entity.Portfolio;
import com.risklens.core.entity.User;
import com.risklens.core.repository.PortfolioRepository;
import com.risklens.core.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Transactional
    public PortfolioResponseDto createPortfolio(
        String userEmail, PortfolioCreateRequestDto createRequestDto) {
        User user =
            userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Portfolio portfolio =
            Portfolio.builder()
                .user(user)
                .name(createRequestDto.getName())
                .baseCurrency(createRequestDto.getBaseCurrency().toUpperCase())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioResponseDto.builder()
            .id(savedPortfolio.getId())
            .name(savedPortfolio.getName())
            .baseCurrency(savedPortfolio.getBaseCurrency())
            .createdAt(savedPortfolio.getCreatedAt())
            .updatedAt(savedPortfolio.getUpdatedAt())
            .build();
    }

    public Page<PortfolioResponseDto> getPortfolios(String userEmail, Pageable pageable) {
        User user =
            userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Page<Portfolio> portfolios = portfolioRepository.findAllByUser(user, pageable);

        return portfolios.map(
            portfolio ->
                PortfolioResponseDto.builder()
                    .id(portfolio.getId())
                    .name(portfolio.getName())
                    .baseCurrency(portfolio.getBaseCurrency())
                    .createdAt(portfolio.getCreatedAt())
                    .updatedAt(portfolio.getUpdatedAt())
                    .build());
    }
}
