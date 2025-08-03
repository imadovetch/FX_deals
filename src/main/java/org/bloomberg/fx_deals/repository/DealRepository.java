package org.bloomberg.fx_deals.repository;

import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, String> {
    boolean existsByDealUniqueId(String dealId);
}
