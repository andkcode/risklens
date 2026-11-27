package com.risklens.core.marketdata;

import com.risklens.core.portfolio.PriceHistory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    @Query("""
      SELECT p FROM PriceHistory p
      WHERE UPPER(p.assetSymbol) = :symbol
        AND p.tradeDate >= :from
        AND p.tradeDate <= :to
      ORDER BY p.tradeDate ASC
      """)
    List<PriceHistory> findHistoryBetween(
        @Param("symbol") String symbol,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    Optional<PriceHistory> findFirstByAssetSymbolIgnoreCaseOrderByTradeDateDesc(String assetSymbol);
}
