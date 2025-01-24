package com.blueharvest.spi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private BigDecimal balance;

    public Account(String name) {
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }
}
