package org.bloomberg.fx_deals.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "deals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Deal {

    @Id
    @Column(name = "deal_unique_id", nullable = false, unique = true, length = 255)
    private String dealUniqueId;

    @Column(name = "from_currency_iso_code", nullable = false, length = 3)
    private String fromCurrencyIsoCode;

    @Column(name = "to_currency_iso_code", nullable = false, length = 3)
    private String toCurrencyIsoCode;

    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;       // Deal timestamp

    @Column(name = "deal_amount_in_ordering_currency", nullable = false, precision = 17, scale = 2)
    private BigDecimal dealAmountInOrderingCurrency;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
