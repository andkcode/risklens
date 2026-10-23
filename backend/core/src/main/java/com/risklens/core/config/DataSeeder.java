package com.risklens.core.config;

import com.risklens.core.entity.Portfolio;
import com.risklens.core.entity.Position;
import com.risklens.core.entity.PriceHistory;
import com.risklens.core.entity.User;
import com.risklens.core.repository.PortfolioRepository;
import com.risklens.core.repository.PositionRepository;
import com.risklens.core.repository.PriceHistoryRepository;
import com.risklens.core.repository.UserRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile("demo")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PositionRepository positionRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping demo data initialization.");
            return;
        }

        log.info("Starting demo data seeding...");

        OffsetDateTime now = OffsetDateTime.now();
        String defaultHash =
            passwordEncoder.encode("12345678");

        User regularUser =
            User.builder()
                .email("user@risklens.com")
                .passwordHash(defaultHash)
                .createdAt(now)
                .updatedAt(now)
                .role("USER")
                .isActive(true)
                .build();
        User analystUser =
            User.builder()
                .email("analyst@risklens.com")
                .passwordHash(defaultHash)
                .createdAt(now)
                .updatedAt(now)
                .role("ANALYST")
                .isActive(true)
                .build();
        User adminUser =
            User.builder()
                .email("admin@risklens.com")
                .passwordHash(defaultHash)
                .createdAt(now)
                .updatedAt(now)
                .role("ADMIN")
                .isActive(true)
                .build();

        userRepository.saveAll(List.of(regularUser, analystUser, adminUser));

        Portfolio p1 =
            Portfolio.builder()
                .user(regularUser)
                .name("Growth Portfolio")
                .baseCurrency("USD")
                .createdAt(now)
                .updatedAt(now)
                .build();
        Portfolio p2 =
            Portfolio.builder()
                .user(analystUser)
                .name("Crypto & Tech")
                .baseCurrency("USD")
                .createdAt(now)
                .updatedAt(now)
                .build();
        Portfolio p3 =
            Portfolio.builder()
                .user(adminUser)
                .name("Conservative Assets")
                .baseCurrency("USD")
                .createdAt(now)
                .updatedAt(now)
                .build();

        portfolioRepository.saveAll(List.of(p1, p2, p3));

        Position pos1 =
            Position.builder()
                .portfolio(p1)
                .assetSymbol("AAPL")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(10))
                .avgBuyPrice(BigDecimal.valueOf(175.50))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos2 =
            Position.builder()
                .portfolio(p1)
                .assetSymbol("MSFT")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(5))
                .avgBuyPrice(BigDecimal.valueOf(400.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos3 =
            Position.builder()
                .portfolio(p1)
                .assetSymbol("GOOGL")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(8))
                .avgBuyPrice(BigDecimal.valueOf(140.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos4 =
            Position.builder()
                .portfolio(p1)
                .assetSymbol("AMZN")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(12))
                .avgBuyPrice(BigDecimal.valueOf(150.00))
                .createdAt(now)
                .updatedAt(now)
                .build();

        Position pos5 =
            Position.builder()
                .portfolio(p2)
                .assetSymbol("BTC")
                .assetType("CRYPTO")
                .quantity(BigDecimal.valueOf(0.5))
                .avgBuyPrice(BigDecimal.valueOf(42000.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos6 =
            Position.builder()
                .portfolio(p2)
                .assetSymbol("ETH")
                .assetType("CRYPTO")
                .quantity(BigDecimal.valueOf(4.0))
                .avgBuyPrice(BigDecimal.valueOf(2200.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos7 =
            Position.builder()
                .portfolio(p2)
                .assetSymbol("NVDA")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(15))
                .avgBuyPrice(BigDecimal.valueOf(500.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos8 =
            Position.builder()
                .portfolio(p2)
                .assetSymbol("TSLA")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(10))
                .avgBuyPrice(BigDecimal.valueOf(240.00))
                .createdAt(now)
                .updatedAt(now)
                .build();

        Position pos9 =
            Position.builder()
                .portfolio(p3)
                .assetSymbol("AAPL")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(20))
                .avgBuyPrice(BigDecimal.valueOf(170.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos10 =
            Position.builder()
                .portfolio(p3)
                .assetSymbol("MSFT")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(15))
                .avgBuyPrice(BigDecimal.valueOf(390.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos11 =
            Position.builder()
                .portfolio(p3)
                .assetSymbol("GOOGL")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(10))
                .avgBuyPrice(BigDecimal.valueOf(135.00))
                .createdAt(now)
                .updatedAt(now)
                .build();
        Position pos12 =
            Position.builder()
                .portfolio(p3)
                .assetSymbol("AMZN")
                .assetType("STOCK")
                .quantity(BigDecimal.valueOf(25))
                .avgBuyPrice(BigDecimal.valueOf(145.00))
                .createdAt(now)
                .updatedAt(now)
                .build();

        positionRepository.saveAll(
            List.of(pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12));

        // Load Price History from CSV Fixture
        loadPriceHistoryFromCsv();

        log.info("Demo data seeding completed successfully!");
    }

    private void loadPriceHistoryFromCsv() {
        ClassPathResource resource = new ClassPathResource("db/seed/price_history_seed.csv");
        try (BufferedReader reader =
                 new BufferedReader(
                     new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String symbol = parts[0].trim();
                    LocalDate tradeDate = LocalDate.parse(parts[1].trim());

                    if (!priceHistoryRepository.existsByAssetSymbolAndTradeDate(symbol, tradeDate)) {
                        PriceHistory history =
                            PriceHistory.builder()
                                .assetSymbol(symbol)
                                .tradeDate(tradeDate)
                                .openPrice(new BigDecimal(parts[2].trim()))
                                .closePrice(new BigDecimal(parts[3].trim()))
                                .highPrice(new BigDecimal(parts[4].trim()))
                                .lowPrice(new BigDecimal(parts[5].trim()))
                                .volume(new BigDecimal(parts[6].trim()))
                                .build();
                        priceHistoryRepository.save(history);
                    }
                }
            }
            log.info("Price history successfully loaded from CSV fixture.");
        } catch (Exception e) {
            log.error("Failed to load price history CSV fixture: {}", e.getMessage(), e);
        }
    }
}
