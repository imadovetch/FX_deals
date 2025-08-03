package org.bloomberg.fx_deals.Model.DTO;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

//  I kept the same names for clarity

public class DealDto {

    @NotBlank(message = "Deal Unique Id is required")
    private String dealUniqueId;

    @NotBlank(message = "From Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency ISO Code must be 3 uppercase letters")
    private String fromCurrencyIsoCode;

    @NotBlank(message = "To Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency ISO Code must be 3 uppercase letters")
    private String toCurrencyIsoCode;

    @NotNull(message = "Deal timestamp is required")
    private Long dealTimestamp;  // Epoch millis

    @NotNull(message = "Deal Amount in ordering currency is required")
    @DecimalMin(value = "0.01", message = "Deal Amount must be greater than zero")
    private BigDecimal dealAmountInOrderingCurrency;
}
