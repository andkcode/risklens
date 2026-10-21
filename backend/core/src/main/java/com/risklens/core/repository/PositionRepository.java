package com.risklens.core.repository;

import com.risklens.core.entity.Position;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
  Page<Position> findAllByPortfolioId(UUID portfolio, Pageable pageable);

  List<Position> findAllByPortfolioId(UUID portfolioId);
}
