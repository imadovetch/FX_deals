package org.bloomberg.fx_deals.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bloomberg.fx_deals.Helpers.ControllerHelper;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.DTO.ImportResultDto;
import org.bloomberg.fx_deals.context.DuplicateDealsContext;
import org.bloomberg.fx_deals.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@Validated
@Tag(name = "FX Deals", description = "API for managing foreign exchange deals")
public class DealController {

    private final DealService dealService;
    private final ControllerHelper controllerHelper;
    /**
     * Endpoint to import one or multiple deals.
     * No rollback: saves all valid deals, skips duplicates.
     */
    @PostMapping("/import")

    public ResponseEntity<?> importDeals(@Valid @RequestBody List<DealDto> dealDtos) {
        ImportResultDto result = dealService.saveAll(dealDtos);

        List<String> duplicateIds = DuplicateDealsContext.get();
        duplicateIds = (duplicateIds != null) ? duplicateIds : List.of();

        int successCount = result.getSuccessfulDeals().size();
        int failCount = result.getFailedDeals().size();
        int dupCount = duplicateIds.size();

        String message = controllerHelper.getUserNotification(successCount, failCount, dupCount) +
                " | Success: " + successCount +
                " | Failed: " + failCount +
                " | Duplicates: " + dupCount;

        return ResponseEntity.ok(Map.of(
                "message", message,
                "successfulDeals", result.getSuccessfulDeals(),
                "failedDeals", result.getFailedDeals(),
                "duplicateDeals", duplicateIds
        ));
    }

}
