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
        Instant timestamp = Instant.parse("2022-01-01T00:00:00Z");
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(timestamp.toString())
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        Deal deal = dealMapper.toEntity(dealDto);

        assertNotNull(deal);
        assertEquals("DEAL001", deal.getDealUniqueId());
        assertEquals("USD", deal.getFromCurrencyIsoCode());
        assertEquals("EUR", deal.getToCurrencyIsoCode());
        assertEquals(timestamp, deal.getDealTimestamp());
        assertEquals(new BigDecimal("1000.50"), deal.getDealAmountInOrderingCurrency());
    }

    @Test
    void toEntity_NullTimestamp_ReturnsNullTimestamp() {
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(null)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        Deal deal = dealMapper.toEntity(dealDto);

        assertNotNull(deal);
        assertEquals("DEAL001", deal.getDealUniqueId());
        assertNull(deal.getDealTimestamp());
    }

    @Test
    void toDto_ValidDeal_ReturnsCorrectDealDto() {
        Instant timestamp = Instant.parse("2022-01-01T00:00:00Z");
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(timestamp)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        DealDto dealDto = dealMapper.toDto(deal);

        assertNotNull(dealDto);
        assertEquals("DEAL001", dealDto.getDealUniqueId());
        assertEquals("USD", dealDto.getFromCurrencyIsoCode());
        assertEquals("EUR", dealDto.getToCurrencyIsoCode());
        assertEquals(timestamp.toString(), dealDto.getDealTimestamp()); // String in DTO
        assertEquals(new BigDecimal("1000.50"), dealDto.getDealAmountInOrderingCurrency());
    }

    @Test
    void toDto_NullTimestamp_ReturnsNullTimestamp() {
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(null)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        DealDto dealDto = dealMapper.toDto(deal);

        assertNotNull(dealDto);
        assertEquals("DEAL001", dealDto.getDealUniqueId());
        assertNull(dealDto.getDealTimestamp());
    }

    @Test
    void toEntity_ZeroTimestamp_ReturnsCorrectInstant() {
        Instant zeroInstant = Instant.EPOCH;
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(zeroInstant.toString())
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        Deal deal = dealMapper.toEntity(dealDto);

        assertNotNull(deal);
        assertEquals(zeroInstant, deal.getDealTimestamp());
    }

    @Test
    void toDto_ZeroTimestamp_ReturnsCorrectInstant() {
        Instant zeroInstant = Instant.EPOCH;
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(zeroInstant)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        DealDto dealDto = dealMapper.toDto(deal);

        assertNotNull(dealDto);
        assertEquals(zeroInstant.toString(), dealDto.getDealTimestamp());
    }

    @Test
    void toEntity_LargeTimestamp_ReturnsCorrectInstant() {
        Instant farFuture = Instant.parse("9999-12-31T23:59:59Z");
        DealDto dealDto = DealDto.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(farFuture.toString())
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        Deal deal = dealMapper.toEntity(dealDto);

        assertNotNull(deal);
        assertEquals(farFuture, deal.getDealTimestamp());
    }

    @Test
    void toDto_LargeTimestamp_ReturnsCorrectInstant() {
        Instant farFuture = Instant.parse("9999-12-31T23:59:59Z");
        Deal deal = Deal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(farFuture)
                .dealAmountInOrderingCurrency(new BigDecimal("1000.50"))
                .build();

        DealDto dealDto = dealMapper.toDto(deal);

        assertNotNull(dealDto);
        assertEquals(farFuture.toString(), dealDto.getDealTimestamp());
    }
}
