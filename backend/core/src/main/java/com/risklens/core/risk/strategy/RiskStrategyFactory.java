package com.risklens.core.risk.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RiskStrategyFactory {

    private final Map<RiskMethod, RiskStrategy> strategies;

    public RiskStrategyFactory(List<RiskStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(RiskStrategy::getSupportedMethod, Function.identity()));
    }

    public RiskStrategy getStrategy(RiskMethod method) {
        RiskMethod resolvedMethod = method != null ? method : RiskMethod.SCORING;
        RiskStrategy strategy = strategies.get(resolvedMethod);

        if (strategy == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Unsupported risk calculation method: " + resolvedMethod);
        }

        return strategy;
    }
}
