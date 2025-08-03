package org.bloomberg.fx_deals.Model.Mapper;

import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.mapstruct.*;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface DealMapper {

    @Mapping(target = "dealTimestamp", expression = "java(toInstant(dealDto.getDealTimestamp()))")
    Deal toEntity(DealDto dealDto);

    @Mapping(target = "dealTimestamp", expression = "java(fromInstant(deal.getDealTimestamp()))")
    DealDto toDto(Deal deal);

    default Instant toInstant(Long millis) {
        return millis != null ? Instant.ofEpochMilli(millis) : null;
    }

    default Long fromInstant(Instant instant) {
        return instant != null ? instant.toEpochMilli() : null;
    }
}
