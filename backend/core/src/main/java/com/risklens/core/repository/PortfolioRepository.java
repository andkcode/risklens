package com.risklens.core.repository;

import com.risklens.core.entity.Portfolio;
import com.risklens.core.entity.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    Page<Portfolio> findAllByUser(User user, Pageable pageable);
}
