package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PortfolioCreateRequestDto;
import com.risklens.core.portfolio.dto.PortfolioResponseDto;
import com.risklens.core.user.User;
import com.risklens.core.user.UserRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final PortfolioMapper portfolioMapper;

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

        return portfolioMapper.toDto(savedPortfolio);
    }

    public Page<PortfolioResponseDto> getPortfolios(String userEmail, Pageable pageable) {
        User user =
            userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Page<Portfolio> portfolios = portfolioRepository.findAllByUser(user, pageable);

        return portfolios.map(portfolioMapper::toDto);
    }

    public PortfolioResponseDto getPortfolioById(String userEmail, UUID portfolioId) {
        User user =
            userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Portfolio portfolio =
            portfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + portfolioId));

        if (!portfolio.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied: portfolio does not belong to this User");
        }

        return portfolioMapper.toDto(portfolio);
    }

    @Transactional
    public void deletePortfolio(String userEmail, UUID portfolioId) {
        User user =
            userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Portfolio portfolio =
            portfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + portfolioId));

        if (!portfolio.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied: portfolio does not belong to this User");
        }

        portfolioRepository.delete(portfolio);
    }
}
