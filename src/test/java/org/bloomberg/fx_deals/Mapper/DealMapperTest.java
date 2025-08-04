package org.bloomberg.fx_deals.Mapper;

import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DealMapperTest {

    private DealMapper dealMapper;

    @BeforeEach
    void setUp() {
        dealMapper = Mappers.getMapper(DealMapper.class);
    }

    @Test
    void toEntity_ValidDealDto_ReturnsCorrectDeal() {
        // Given
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(1640995200000L) // 2022-01-01 00:00:00 UTC
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        Deal deal = dealMapper.toEntity(dealDto);

        // Then
        assertNotNull(deal);
        assertEquals("DEAL001", deal.getDealUniqueId());
        assertEquals("USD", deal.getFromCurrencyIsoCode());
        assertEquals("EUR", deal.getToCurrencyIsoCode());
        assertEquals(Instant.ofEpochMilli(1640995200000L), deal.getDealTimestamp());
        assertEquals(new BigDecimal("1000.50"), deal.getDealAmountInOrderingCurrency());
    }

    @Test
    void toEntity_NullTimestamp_ReturnsNullTimestamp() {
        // Given
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(null)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        Deal deal = dealMapper.toEntity(dealDto);

        // Then
        assertNotNull(deal);
        assertEquals("DEAL001", deal.getDealUniqueId());
        assertNull(deal.getDealTimestamp());
    }

    @Test
    void toDto_ValidDeal_ReturnsCorrectDealDto() {
        // Given
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(Instant.ofEpochMilli(1640995200000L))
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        DealDto dealDto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dealDto);
        assertEquals("DEAL001", dealDto.getDealUniqueId());
        assertEquals("USD", dealDto.getFromCurrencyIsoCode());
        assertEquals("EUR", dealDto.getToCurrencyIsoCode());
        assertEquals(1640995200000L, dealDto.getDealTimestamp());
        assertEquals(new BigDecimal("1000.50"), dealDto.getDealAmountInOrderingCurrency());
    }

    @Test
    void toDto_NullTimestamp_ReturnsNullTimestamp() {
        // Given
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(null)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        DealDto dealDto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dealDto);
        assertEquals("DEAL001", dealDto.getDealUniqueId());
        assertNull(dealDto.getDealTimestamp());
    }

    @Test
    void toEntity_ZeroTimestamp_ReturnsCorrectInstant() {
        // Given
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(0L)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        Deal deal = dealMapper.toEntity(dealDto);

        // Then
        assertNotNull(deal);
        assertEquals(Instant.ofEpochMilli(0L), deal.getDealTimestamp());
    }

    @Test
    void toDto_ZeroTimestamp_ReturnsCorrectLong() {
        // Given
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(Instant.ofEpochMilli(0L))
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        DealDto dealDto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dealDto);
        assertEquals(0L, dealDto.getDealTimestamp());
    }

    @Test
    void toEntity_LargeTimestamp_ReturnsCorrectInstant() {
        // Given
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(253402300799999L) // Year 9999
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        Deal deal = dealMapper.toEntity(dealDto);

        // Then
        assertNotNull(deal);
        assertEquals(Instant.ofEpochMilli(253402300799999L), deal.getDealTimestamp());
    }

    @Test
    void toDto_LargeTimestamp_ReturnsCorrectLong() {
        // Given
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(Instant.ofEpochMilli(253402300799999L))
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        // When
        DealDto dealDto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dealDto);
        assertEquals(253402300799999L, dealDto.getDealTimestamp());
    }
} 