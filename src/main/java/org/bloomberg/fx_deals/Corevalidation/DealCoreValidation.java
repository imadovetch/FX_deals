package org.bloomberg.fx_deals.Corevalidation;

import lombok.RequiredArgsConstructor;
import org.bloomberg.fx_deals.repository.DealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DealCoreValidation {

    private static final Logger logger = LoggerFactory.getLogger(DealCoreValidation.class);
    private final DealRepository dealRepository;

    public boolean isDealUnique(String dealUniqueId) {
        try {
            if (dealUniqueId == null || dealUniqueId.isBlank()) {
                logger.warn("Empty deal ID check");
                return false;
            }

            boolean exists = dealRepository.existsByDealUniqueId(dealUniqueId);
            logger.debug("Deal ID {} exists: {}", dealUniqueId, exists);

            return !exists;

        } catch (DataAccessException e) {
            logger.error("DB error checking deal ID {}: {}", dealUniqueId, e.getMessage());
            return false;
        }
    }
}