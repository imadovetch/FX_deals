package org.bloomberg.fx_deals.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "deals")
@Data
@NoArgsConstructor
@AllArgsConstructor


//  I kept the same names for clarity

public class Deal {

    @Id
    @Column(name = "deal_unique_id", nullable = false, unique = true)
    private String dealUniqueId;

    @Column(name = "from_currency_iso_code", nullable = false, length = 3)
    private String fromCurrencyIsoCode;

    @Column(name = "to_currency_iso_code", nullable = false, length = 3)
    private String toCurrencyIsoCode;

    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;       // Deal timestamp

    @Column(name = "deal_amount_in_ordering_currency", nullable = false)
    private BigDecimal dealAmountInOrderingCurrency;
}
