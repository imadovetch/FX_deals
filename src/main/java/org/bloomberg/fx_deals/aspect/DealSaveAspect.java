package org.bloomberg.fx_deals.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bloomberg.fx_deals.Corevalidation.DealCoreValidation;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.DTO.ImportResultDto;
import org.bloomberg.fx_deals.context.DuplicateDealsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class DealSaveAspect {

    private static final Logger logger = LoggerFactory.getLogger(DealSaveAspect.class);

    private final DealCoreValidation coreValidation;

    @Around("execution(* org.bloomberg.fx_deals.service.DealService.saveAll(..)) && args(dealDtos)")
    public Object filterDuplicatesBeforeSave(ProceedingJoinPoint pjp, List<DealDto> dealDtos) throws Throwable {
        if (dealDtos == null || dealDtos.isEmpty()) {
            logger.info("No deals were passed");
            return new ImportResultDto(List.of(), List.of());
        }

        List<String> existingDealIds = dealDtos.stream()
                .map(DealDto::getDealUniqueId)
                .filter(id -> !coreValidation.isDealUnique(id))
                .toList();
        System.out.println("existingDealIds");
        System.out.println(existingDealIds);

        // I store the deals that already exist so we return them via response
        DuplicateDealsContext.set(existingDealIds);


        List<DealDto> newDeals = dealDtos.stream()
                .filter(deal -> !existingDealIds.contains(deal.getDealUniqueId()))
                .toList();

        try {
            // this is to catch the errors of db etc ...  to keep service only for Business logic

            if (newDeals.isEmpty()) {
                logger.info("No new deals to save after filtering duplicates.");
                return new ImportResultDto(List.of(), List.of());
            }
            return pjp.proceed(new Object[]{newDeals});
        } catch (Exception e) {
            logger.error("Error occurred while saving deals: {}", e.getMessage(), e);
            throw e;
        }
//        finally {
//            Memory free
//            DuplicateDealsContext.clear();
//        }
    }
}
