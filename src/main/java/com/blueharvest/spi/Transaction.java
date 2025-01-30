package com.blueharvest.spi;

import com.blueharvest.domain.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_generator")
    @SequenceGenerator(name = "transaction_id_generator", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long transactionId;
    private String description;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private BigDecimal amount;
    private Instant ts;

    public Transaction(TransactionType type, String description, BigDecimal amount) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.ts = Instant.now();
    }
}
