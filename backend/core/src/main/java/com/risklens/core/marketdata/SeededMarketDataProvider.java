package com.risklens.core.marketdata;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Transactional(readOnly = true)
public class SeededMarketDataProvider implements MarketDataProvider {

  private final PriceHistoryRepository priceHistoryRepository;

  public SeededMarketDataProvider(PriceHistoryRepository priceHistoryRepository) {
    this.priceHistoryRepository = priceHistoryRepository;
  }

  @Override
  public List<PriceBar> getHistory(String ticker, LocalDate from, LocalDate to) {
    String normalizedTicker = normalizeTicker(ticker);
    Objects.requireNonNull(from, "from must not be null");
    Objects.requireNonNull(to, "to must not be null");
    if (from.isAfter(to)) {
      throw new IllegalArgumentException("'from' date must not be after 'to' date");
    }

    return priceHistoryRepository.findHistoryBetween(normalizedTicker, from, to).stream()
        .map(
            row ->
                new PriceBar(
                    normalizedTicker,
                    row.getTradeDate(),
                    row.getOpenPrice(),
                    row.getHighPrice(),
                    row.getLowPrice(),
                    row.getClosePrice(),
                    row.getVolume()))
        .toList();
  }

  @Override
  public Optional<LastPrice> getLastPrice(String ticker) {
    String normalizedTicker = normalizeTicker(ticker);
    return priceHistoryRepository
        .findFirstByAssetSymbolIgnoreCaseOrderByTradeDateDesc(normalizedTicker)
        .map(
            row ->
                new LastPrice(
                    normalizedTicker,
                    row.getClosePrice(),
                    row.getTradeDate().atStartOfDay().toInstant(ZoneOffset.UTC)));
  }

  private String normalizeTicker(String ticker) {
    if (ticker == null || ticker.isBlank()) {
      throw new IllegalArgumentException("ticker must not be blank");
    }
    return ticker.trim().toUpperCase(Locale.ROOT);
  }
}
