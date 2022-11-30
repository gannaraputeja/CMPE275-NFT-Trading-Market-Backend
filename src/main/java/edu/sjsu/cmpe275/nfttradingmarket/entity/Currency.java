package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Currency {
    @Id
    private int id;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private CurrencyType type;
    @ManyToOne
    private Wallet wallet;
}
