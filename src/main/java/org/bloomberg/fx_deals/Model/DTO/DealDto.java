package org.bloomberg.fx_deals.Model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bloomberg.fx_deals.Annotation.VerifyInstantFormat;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DealDto {

    @NotBlank(message = "Deal Unique Id is required")
    @Size(max = 255, message = "Deal Unique Id must not exceed 255 characters")
    private String dealUniqueId;

    @NotBlank(message = "From Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency ISO Code must be 3 uppercase letters")
    private String fromCurrencyIsoCode;

    @NotBlank(message = "To Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency ISO Code must be 3 uppercase letters")
    private String toCurrencyIsoCode;

    @NotNull(message = "Deal timestamp is required")
    @VerifyInstantFormat
    private String dealTimestamp;

    @NotNull(message = "Deal Amount in ordering currency is required")
    @DecimalMin(value = "0.01", message = "Deal Amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Deal Amount must have at most 15 digits and 2 decimal places")
    private BigDecimal dealAmountInOrderingCurrency;
}