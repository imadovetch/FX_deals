package org.bloomberg.fx_deals.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.bloomberg.fx_deals.Corevalidation.DealCoreValidation;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
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
        /**
         * Aspect advice to intercept saveAll(List<DealDto>) calls.
         * It filters out any deals whose dealUniqueId already exists in the database,
         * preventing duplicates from being saved.
         *
         * If all deals are filtered out (no new deals to save), returns an empty list
         * immediately without calling the saveAll method.
         *
         * @param pjp the proceeding join point representing the intercepted method call
         * @param dealDtos the original list of deals to save
         * @return the result of the saveAll method or an empty list if nothing to save
         * @throws Throwable if the proceeding join point throws an exception
         */


        List<DealDto> filtered = dealDtos.stream()
                .filter(dealDto -> {
                    boolean isUnique = coreValidation.isDealUnique(dealDto.getDealUniqueId());
                    if (!isUnique) {
                        logger.warn("Skipping duplicate deal with ID: {}", dealDto.getDealUniqueId());
                    }
                    return isUnique;
                })
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            logger.info("No new deals to save after filtering duplicates.");
            return List.of();
        }


        return pjp.proceed(new Object[]{filtered});
    }

}
