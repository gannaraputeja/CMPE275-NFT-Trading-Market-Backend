package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data @NoArgsConstructor
@Entity
public class Currency {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column(precision=10, scale = 2)
    private Double amount;
    @Enumerated(EnumType.STRING)
    private CurrencyType type;
    @ManyToOne
    private Wallet wallet;

    public Currency(Double amount, CurrencyType currencyType, Wallet wallet){
        this.amount = amount;
        this.type = currencyType;
        this.wallet = wallet;
    }
}
