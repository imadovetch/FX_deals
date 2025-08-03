package org.bloomberg.fx_deals.controller;

import lombok.RequiredArgsConstructor;

import org.bloomberg.fx_deals.Corevalidation.DealCoreValidation;

import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;
    private final DealCoreValidation dealCoreValidation;

    /**
     * Endpoint to import one or multiple deals.
     * No rollback: save all valid deals, skip duplicates.
     */
    @PostMapping("/import")
    public ResponseEntity<?> importDeals(@Valid @RequestBody List<DealDto> dealDtos, BindingResult bindingResult) {

        // We ll use AoP for validation to keep controllers light

        dealService.saveAll(dealDtos);
        return ResponseEntity.ok("Deals imported successfully.");
    }
}
