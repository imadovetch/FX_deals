package org.bloomberg.fx_deals.Service;

import lombok.RequiredArgsConstructor;

import org.bloomberg.fx_deals.Mapper.DealMapper;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.DTO.ImportResultDto;
import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.bloomberg.fx_deals.Repository.DealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

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

//    @MarkFunction("SaveMultipleDeals")

    public ImportResultDto saveAll(List<DealDto> dealDtos) {
        if (dealDtos == null || dealDtos.isEmpty()) {
            logger.warn("No deals provided for saving");
            return new ImportResultDto(List.of(), List.of());
        }

        List<String> successfulDeals = new ArrayList<>();
        List<String> failedDeals = new ArrayList<>();

        for (DealDto dto : dealDtos) {
            try {
                Deal deal = dealMapper.toEntity(dto); // catch mapping errors here
                dealRepository.save(deal);
                successfulDeals.add(deal.getDealUniqueId());
                logger.info("Saved deal with ID: {}", deal.getDealUniqueId());
            } catch (Exception e) {
                failedDeals.add(dto.getDealUniqueId());
                logger.error("Failed to save deal with ID: {}. Error: {}", dto.getDealUniqueId(), e.getMessage(), e);
            }
        }

        logger.info("Import completed. Successful: {}, Failed: {}", successfulDeals.size(), failedDeals.size());

        return new ImportResultDto(successfulDeals, failedDeals);
    }


}
