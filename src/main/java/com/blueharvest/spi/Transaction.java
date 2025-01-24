package com.blueharvest.spi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_generator")
    @SequenceGenerator(name = "transaction_id_generator", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private BigDecimal amount;

    public Transaction(String description, BigDecimal amount) {
        this.name = description;
        this.amount = amount;
    }
}
