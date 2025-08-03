package org.bloomberg.fx_deals.service;

import lombok.RequiredArgsConstructor;

import org.bloomberg.fx_deals.Mapper.DealMapper;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.bloomberg.fx_deals.repository.DealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DealService {

    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    /**
     * Saves all deals individually, ensuring no rollback on failure.
     * Already filtered duplicates by validation AOP.
     */


    public void saveAll(List<DealDto> dealDtos) {

        List<Deal> deals = dealDtos.stream()
                .map(dealMapper::toEntity)
                .toList();

        for (Deal deal : deals) {
            try {
                dealRepository.save(deal);
                logger.info("Saved deal with ID: {}", deal.getDealUniqueId());
            } catch (Exception e) {
                logger.error("Failed to save deal with ID: {}. Continuing with others.", deal.getDealUniqueId(), e);
            }
        }
    }
}
