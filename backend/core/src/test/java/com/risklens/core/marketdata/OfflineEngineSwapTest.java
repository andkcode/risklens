package com.risklens.core.marketdata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.risklens.core.marketdata.MarketDataProvider.LastPrice;
import com.risklens.core.marketdata.MarketDataProvider.PriceBar;
import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import com.risklens.core.risk.strategy.HistoricalVarRiskStrategy;
import com.risklens.core.risk.strategy.RiskMethod;
import com.risklens.core.risk.strategy.RiskParams;
import com.risklens.core.risk.strategy.RiskResult;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OfflineEngineSwapTest {

  @Test
  void engineWorksWithoutNetworkAndSwapsProviderWithoutChangingEngine() {
    ProxySelector previous = ProxySelector.getDefault();
    ProxySelector.setDefault(
        new ProxySelector() {
          @Override
          public List<Proxy> select(URI uri) {
            throw new AssertionError("Network access is forbidden: " + uri);
          }

          @Override
          public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {}
        });

    try {
      PositionRepository positionRepository = mock(PositionRepository.class);
      UUID portfolioId = UUID.randomUUID();
      Portfolio portfolio =
          Portfolio.builder()
              .id(portfolioId)
              .name("Test")
              .baseCurrency("USD")
              .createdAt(OffsetDateTime.now().minusDays(60))
              .build();

      Position pos =
          Position.builder()
              .portfolio(portfolio)
              .assetSymbol("AAPL")
              .assetType("STOCK")
              .quantity(BigDecimal.valueOf(10))
              .avgBuyPrice(BigDecimal.valueOf(150))
              .build();

      when(positionRepository.findAllByPortfolioId(portfolioId)).thenReturn(List.of(pos));

      // Реализация 1 (офлайн / локальная)
      MarketDataProvider offlineProvider = stubProvider(BigDecimal.valueOf(180));
      HistoricalVarRiskStrategy engine =
          new HistoricalVarRiskStrategy(positionRepository, offlineProvider);

      RiskParams params =
          RiskParams.builder()
              .riskMethod(RiskMethod.HISTORICAL_VAR)
              .confidenceLevel(BigDecimal.valueOf(0.95))
              .timeHorizonDays(250)
              .build();

      RiskResult offlineResult = engine.calculate(portfolio, params);
      assertThat(offlineResult.getVarValue()).isGreaterThanOrEqualTo(BigDecimal.ZERO);

      // Имитация Фазы 2: подменяем адаптер MarketDataProvider, код движка не трогаем
      MarketDataProvider phase2Adapter = stubProvider(BigDecimal.valueOf(220));
      HistoricalVarRiskStrategy sameEngineCodeWithNewAdapter =
          new HistoricalVarRiskStrategy(positionRepository, phase2Adapter);

      RiskResult phase2Result = sameEngineCodeWithNewAdapter.calculate(portfolio, params);
      assertThat(phase2Result.getVarValue()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    } finally {
      ProxySelector.setDefault(previous);
    }
  }

  private MarketDataProvider stubProvider(BigDecimal lastPrice) {
    LocalDate today = LocalDate.now();
    List<PriceBar> history =
        List.of(
            bar("AAPL", today.minusDays(3), BigDecimal.valueOf(170)),
            bar("AAPL", today.minusDays(2), BigDecimal.valueOf(165)),
            bar("AAPL", today.minusDays(1), BigDecimal.valueOf(172)),
            bar("AAPL", today, lastPrice));

    return new MarketDataProvider() {
      @Override
      public List<PriceBar> getHistory(String ticker, LocalDate from, LocalDate to) {
        return history;
      }

      @Override
      public Optional<LastPrice> getLastPrice(String ticker) {
        return Optional.of(new LastPrice(ticker, lastPrice, Instant.now()));
      }
    };
  }

  private PriceBar bar(String ticker, LocalDate date, BigDecimal close) {
    return new PriceBar(ticker, date, close, close, close, close, BigDecimal.valueOf(1000));
  }
}
