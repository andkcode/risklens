package com.risklens.core.repository;

import com.risklens.core.entity.RiskAssessment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, UUID> {
  Optional<RiskAssessment> findTopByPortfolioIdOrderByCalculatedAtDesc(UUID portfolioId);
}
