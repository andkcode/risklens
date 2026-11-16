package com.risklens.core.risk.cache;

import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component("riskCacheKeyGenerator")
@RequiredArgsConstructor
public class RiskCacheKeyGenerator implements KeyGenerator {
  private final PositionRepository positionRepository;

  @Override
  public Object generate(Object target, Method method, Object... params) {
    UUID portfolioId = null;

    for (Object param : params) {
      if (param instanceof UUID uuid) {
        portfolioId = uuid;
        break;
      }
    }

    int positionsHash = 0;
    if (portfolioId != null) {
      List<Position> positions = positionRepository.findAllByPortfolioId(portfolioId);
      positionsHash =
          positions.stream()
              .mapToInt(p -> Objects.hash(p.getAssetSymbol(), p.getQuantity(), p.getAvgBuyPrice()))
              .sum();
    }
    return new RiskCacheKey(
        portfolioId, method.getName(), Arrays.deepHashCode(params), positionsHash);
  }

  public record RiskCacheKey(
      UUID portfolioId, String methodName, int paramsHash, int positionsHash) {}
}
