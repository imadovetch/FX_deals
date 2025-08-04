package org.bloomberg.fx_deals.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultDto {
    private List<String> successfulDeals;
    private List<String> failedDeals;
}

