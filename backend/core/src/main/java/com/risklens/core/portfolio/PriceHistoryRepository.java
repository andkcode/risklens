package com.risklens.core.portfolio;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findAllByAssetSymbolOrderByTradeDateDesc(String assetSymbol);

    boolean existsByAssetSymbolAndTradeDate(String assetSymbol, LocalDate tradeDate);
}
