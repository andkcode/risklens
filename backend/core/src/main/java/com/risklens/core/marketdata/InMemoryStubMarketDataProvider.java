package com.risklens.core.marketdata;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(MarketDataProvider.class)
public class InMemoryStubMarketDataProvider implements MarketDataProvider {

    @Override
    public List<PriceBar> getHistory(String ticker, LocalDate from, LocalDate to) {
        String symbol = normalize(ticker);
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from must not be after to");
        }

        BigDecimal price = BigDecimal.valueOf(100);
        return from.datesUntil(to.plusDays(1))
            .map(d -> new PriceBar(symbol, d, price, price, price, price, BigDecimal.ZERO))
            .toList();
    }

    @Override
    public Optional<LastPrice> getLastPrice(String ticker) {
        String symbol = normalize(ticker);
        return Optional.of(new LastPrice(symbol, BigDecimal.valueOf(100), Instant.now()));
    }

    private String normalize(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("ticker must not be blank");
        }
        return ticker.trim().toUpperCase(Locale.ROOT);
    }
}
