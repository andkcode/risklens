package com.risklens.core.repository;

import com.risklens.core.entity.Portfolio;
import com.risklens.core.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
  Optional<Portfolio> findAllByUser(User user);
}
