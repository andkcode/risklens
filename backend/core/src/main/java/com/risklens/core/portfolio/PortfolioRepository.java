package com.risklens.core.portfolio;

import com.risklens.core.user.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    Page<Portfolio> findAllByUser(User user, Pageable pageable);
}
