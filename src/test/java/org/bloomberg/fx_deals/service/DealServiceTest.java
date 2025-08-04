package org.bloomberg.fx_deals.service;

import org.bloomberg.fx_deals.Mapper.DealMapper;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.DTO.ImportResultDto;
import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.bloomberg.fx_deals.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper dealMapper;

    @InjectMocks
    private DealService dealService;

    private DealDto validDealDto;
    private Deal validDeal;

    @BeforeEach
    void setUp() {
        validDealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(String.valueOf(Instant.ofEpochMilli(1000L)))
                .dealAmountInOrderingCurrency(new BigDecimal("1000.00"))
                .build();

        validDeal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(Instant.ofEpochMilli(1000L))
                .dealAmountInOrderingCurrency(new BigDecimal("1000.00"))
                .build();
    }

    @Test
    void saveAll_Success() {
        // Given
        List<DealDto> dealDtos = Arrays.asList(validDealDto);
        when(dealMapper.toEntity(validDealDto)).thenReturn(validDeal);
        when(dealRepository.save(validDeal)).thenReturn(validDeal);

        // When
        ImportResultDto result = dealService.saveAll(dealDtos);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getSuccessfulDeals().size());
        assertEquals(0, result.getFailedDeals().size());
        assertEquals("DEAL001", result.getSuccessfulDeals().get(0));

        verify(dealMapper).toEntity(validDealDto);
        verify(dealRepository).save(validDeal);
    }

    @Test
    void saveAll_MultipleDeals_Success() {
        // Given
        DealDto dealDto2 = DealDto.builder()
                .dealUniqueId("DEAL002")
                .fromCurrencyIsoCode("EUR")
                .toCurrencyIsoCode("GBP")
                .dealTimestamp(String.valueOf(Instant.ofEpochMilli(2000L)))
                .dealAmountInOrderingCurrency(new BigDecimal("2000.00"))
                .build();

        Deal deal2 = Deal.builder()
                .dealUniqueId("DEAL002")
                .fromCurrencyIsoCode("EUR")
                .toCurrencyIsoCode("GBP")
                .dealTimestamp(Instant.ofEpochMilli(2000L))
                .dealAmountInOrderingCurrency(new BigDecimal("2000.00"))
                .build();

        List<DealDto> dealDtos = Arrays.asList(validDealDto, dealDto2);

        when(dealMapper.toEntity(validDealDto)).thenReturn(validDeal);
        when(dealMapper.toEntity(dealDto2)).thenReturn(deal2);
        when(dealRepository.save(validDeal)).thenReturn(validDeal);
        when(dealRepository.save(deal2)).thenReturn(deal2);

        // When
        ImportResultDto result = dealService.saveAll(dealDtos);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getSuccessfulDeals().size());
        assertEquals(0, result.getFailedDeals().size());
        assertTrue(result.getSuccessfulDeals().contains("DEAL001"));
        assertTrue(result.getSuccessfulDeals().contains("DEAL002"));

        verify(dealMapper).toEntity(validDealDto);
        verify(dealMapper).toEntity(dealDto2);
        verify(dealRepository).save(validDeal);
        verify(dealRepository).save(deal2);
    }

    @Test
    void saveAll_WithFailures() {
        // Given
        DealDto dealDto2 = DealDto.builder()
                .dealUniqueId("DEAL002")
                .fromCurrencyIsoCode("EUR")
                .toCurrencyIsoCode("GBP")
                .dealTimestamp(String.valueOf(Instant.ofEpochMilli(2000L)))
                .dealAmountInOrderingCurrency(new BigDecimal("2000.00"))
                .build();

        Deal deal2 = Deal.builder()
                .dealUniqueId("DEAL002")
                .fromCurrencyIsoCode("EUR")
                .toCurrencyIsoCode("GBP")
                .dealTimestamp(Instant.ofEpochMilli(2000L))
                .dealAmountInOrderingCurrency(new BigDecimal("2000.00"))
                .build();

        List<DealDto> dealDtos = Arrays.asList(validDealDto, dealDto2);

        when(dealMapper.toEntity(validDealDto)).thenReturn(validDeal);
        when(dealMapper.toEntity(dealDto2)).thenReturn(deal2);
        when(dealRepository.save(validDeal)).thenReturn(validDeal);
        when(dealRepository.save(deal2)).thenThrow(new DataIntegrityViolationException("Database error"));

        // When
        ImportResultDto result = dealService.saveAll(dealDtos);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getSuccessfulDeals().size());
        assertEquals(1, result.getFailedDeals().size());
        assertEquals("DEAL001", result.getSuccessfulDeals().get(0));
        assertEquals("DEAL002", result.getFailedDeals().get(0));

        verify(dealMapper).toEntity(validDealDto);
        verify(dealMapper).toEntity(dealDto2);
        verify(dealRepository).save(validDeal);
        verify(dealRepository).save(deal2);
    }

    @Test
    void saveAll_NullInput() {
        // When
        ImportResultDto result = dealService.saveAll(null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getSuccessfulDeals().size());
        assertEquals(0, result.getFailedDeals().size());

        verify(dealMapper, never()).toEntity(any());
        verify(dealRepository, never()).save(any());
    }

    @Test
    void saveAll_EmptyList() {
        // Given
        List<DealDto> dealDtos = Collections.emptyList();

        // When
        ImportResultDto result = dealService.saveAll(dealDtos);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getSuccessfulDeals().size());
        assertEquals(0, result.getFailedDeals().size());

        verify(dealMapper, never()).toEntity(any());
        verify(dealRepository, never()).save(any());
    }

    @Test
    void saveAll_AllFailures() {
        // Given
        List<DealDto> dealDtos = Arrays.asList(validDealDto);
        when(dealMapper.toEntity(validDealDto)).thenReturn(validDeal);
        when(dealRepository.save(validDeal)).thenThrow(new RuntimeException("Database error"));

        // When
        ImportResultDto result = dealService.saveAll(dealDtos);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getSuccessfulDeals().size());
        assertEquals(1, result.getFailedDeals().size());
        assertEquals("DEAL001", result.getFailedDeals().get(0));

        verify(dealMapper).toEntity(validDealDto);
        verify(dealRepository).save(validDeal);
    }

    @Test
    void saveAll_MapperThrowsException() {
        List<DealDto> dealDtos = Arrays.asList(validDealDto);
        when(dealMapper.toEntity(validDealDto)).thenThrow(new RuntimeException("Mapping error"));

        ImportResultDto result = dealService.saveAll(dealDtos);

        assertNotNull(result);
        assertEquals(0, result.getSuccessfulDeals().size());
        assertEquals(1, result.getFailedDeals().size()); // was 0, now 1 ✅

        verify(dealMapper).toEntity(validDealDto);
        verify(dealRepository, never()).save(any());
    }

} 