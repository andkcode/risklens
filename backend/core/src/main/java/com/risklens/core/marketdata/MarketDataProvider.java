package com.risklens.core.marketdata;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MarketDataProvider {

    List<PriceBar> getHistory(String ticker, LocalDate from, LocalDate to);

    Optional<LastPrice> getLastPrice(String ticker);

    record PriceBar(
        String ticker,
        LocalDate date,
        BigDecimal open,
        BigDecimal high,
        BigDecimal low,
        BigDecimal close,
        BigDecimal volume) {}

    record LastPrice(
        String ticker,
        BigDecimal price,
        Instant timestamp) {}
}
