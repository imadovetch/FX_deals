package org.bloomberg.fx_deals.Repository;

import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, String> {
    boolean existsByDealUniqueId(String dealId);
}
