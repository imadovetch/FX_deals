package org.bloomberg.fx_deals.Mapper;

import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.Entity.Deal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealMapper {
    Deal toEntity(DealDto dealDto);
    DealDto toDto(Deal deal);
}
