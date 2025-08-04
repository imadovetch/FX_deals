package org.bloomberg.fx_deals.aspect;

import org.bloomberg.fx_deals.Corevalidation.DealCoreValidation;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.DTO.ImportResultDto;
import org.bloomberg.fx_deals.context.DuplicateDealsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.aspectj.lang.ProceedingJoinPoint;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealSaveAspectTest {

    @Mock
    private DealCoreValidation coreValidation;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @InjectMocks
    private DealSaveAspect dealSaveAspect;

    private List<DealDto> dealDtos;

    @BeforeEach
    void setUp() {
        dealDtos = Arrays.asList(
                createDealDto("DEAL001"),
                createDealDto("DEAL002"),
                createDealDto("DEAL003")
        );
        DuplicateDealsContext.clear();
    }

    @Test
    void filterDuplicatesBeforeSave_AllUnique_ProceedsWithAllDeals() throws Throwable {
        // Given
        when(coreValidation.isDealUnique("DEAL001")).thenReturn(true);
        when(coreValidation.isDealUnique("DEAL002")).thenReturn(true);
        when(coreValidation.isDealUnique("DEAL003")).thenReturn(true);
        when(proceedingJoinPoint.proceed(any())).thenReturn("success");

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, dealDtos);

        // Then
        assertEquals("success", result);
        verify(proceedingJoinPoint).proceed(argThat(args -> {
            List<DealDto> filtered = (List<DealDto>) args[0];
            return filtered.size() == 3 && 
                   filtered.get(0).getDealUniqueId().equals("DEAL001") &&
                   filtered.get(1).getDealUniqueId().equals("DEAL002") &&
                   filtered.get(2).getDealUniqueId().equals("DEAL003");
        }));
    }

    @Test
    void filterDuplicatesBeforeSave_SomeDuplicates_FiltersOutDuplicates() throws Throwable {
        // Given
        when(coreValidation.isDealUnique("DEAL001")).thenReturn(true);
        when(coreValidation.isDealUnique("DEAL002")).thenReturn(false); // Duplicate
        when(coreValidation.isDealUnique("DEAL003")).thenReturn(true);
        when(proceedingJoinPoint.proceed(any())).thenReturn("success");

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, dealDtos);

        // Then
        assertEquals("success", result);
        verify(proceedingJoinPoint).proceed(argThat(args -> {
            List<DealDto> filtered = (List<DealDto>) args[0];
            return filtered.size() == 2 && 
                   filtered.get(0).getDealUniqueId().equals("DEAL001") &&
                   filtered.get(1).getDealUniqueId().equals("DEAL003");
        }));
    }

    @Test
    void filterDuplicatesBeforeSave_AllDuplicates_ReturnsImportResultDto() throws Throwable {
        // Given
        when(coreValidation.isDealUnique("DEAL001")).thenReturn(false);
        when(coreValidation.isDealUnique("DEAL002")).thenReturn(false);
        when(coreValidation.isDealUnique("DEAL003")).thenReturn(false);

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, dealDtos);

        // Then
        assertTrue(result instanceof ImportResultDto);
        ImportResultDto resultDto = (ImportResultDto) result;
        assertTrue(resultDto.getSuccessfulDeals().isEmpty());
        assertTrue(resultDto.getFailedDeals().isEmpty());
        verify(proceedingJoinPoint, never()).proceed(any());
    }

    @Test
    void filterDuplicatesBeforeSave_EmptyInput_ReturnsImportResultDto() throws Throwable {
        // Given
        List<DealDto> emptyList = Collections.emptyList();

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, emptyList);

        // Then
        assertTrue(result instanceof ImportResultDto);
        ImportResultDto resultDto = (ImportResultDto) result;
        assertTrue(resultDto.getSuccessfulDeals().isEmpty());
        assertTrue(resultDto.getFailedDeals().isEmpty());
        verify(proceedingJoinPoint, never()).proceed(any());
        verify(coreValidation, never()).isDealUnique(anyString());
    }

    @Test
    void filterDuplicatesBeforeSave_ProceedingJoinPointThrowsException_PropagatesException() throws Throwable {
        // Given
        when(coreValidation.isDealUnique("DEAL001")).thenReturn(true);
        when(coreValidation.isDealUnique("DEAL002")).thenReturn(true);
        when(coreValidation.isDealUnique("DEAL003")).thenReturn(true);
        when(proceedingJoinPoint.proceed(any())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, dealDtos)
        );
    }

    @Test
    void filterDuplicatesBeforeSave_NullInput_ReturnsImportResultDto() throws Throwable {
        // Given
        List<DealDto> nullList = null;

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, nullList);

        // Then
        assertTrue(result instanceof ImportResultDto);
        ImportResultDto resultDto = (ImportResultDto) result;
        assertTrue(resultDto.getSuccessfulDeals().isEmpty());
        assertTrue(resultDto.getFailedDeals().isEmpty());
        verify(proceedingJoinPoint, never()).proceed(any());
        verify(coreValidation, never()).isDealUnique(anyString());
    }

    @Test
    void filterDuplicatesBeforeSave_SingleDeal_Unique() throws Throwable {
        // Given
        List<DealDto> singleDeal = Arrays.asList(createDealDto("DEAL001"));
        when(coreValidation.isDealUnique("DEAL001")).thenReturn(true);
        when(proceedingJoinPoint.proceed(any())).thenReturn("success");

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, singleDeal);

        // Then
        assertEquals("success", result);
        verify(proceedingJoinPoint).proceed(argThat(args -> {
            List<DealDto> filtered = (List<DealDto>) args[0];
            return filtered.size() == 1 && filtered.get(0).getDealUniqueId().equals("DEAL001");
        }));
    }

    @Test
    void filterDuplicatesBeforeSave_SingleDeal_Duplicate() throws Throwable {
        // Given
        List<DealDto> singleDeal = Arrays.asList(createDealDto("DEAL001"));
        when(coreValidation.isDealUnique("DEAL001")).thenReturn(false);

        // When
        Object result = dealSaveAspect.filterDuplicatesBeforeSave(proceedingJoinPoint, singleDeal);

        // Then
        assertTrue(result instanceof ImportResultDto);
        ImportResultDto resultDto = (ImportResultDto) result;
        assertTrue(resultDto.getSuccessfulDeals().isEmpty());
        assertTrue(resultDto.getFailedDeals().isEmpty());
        verify(proceedingJoinPoint, never()).proceed(any());
    }

    private DealDto createDealDto(String dealId) {
        return DealDto.builder()
                .dealUniqueId(dealId)
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(1000L)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.00"))
                .build();
    }
} 