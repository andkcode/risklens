package com.risklens.core.risk;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, UUID> {
  Page<RiskAssessment> findAllByPortfolioId(UUID portfolioId, Pageable pageable);

  Optional<RiskAssessment> findByIdAndPortfolioUserEmail(UUID id, String userEmail);
}
