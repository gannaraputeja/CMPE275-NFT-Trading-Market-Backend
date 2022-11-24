package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data @NoArgsConstructor
@Entity
public class CurrencyTransaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private Double amount;
    private Double availableAmount;
    @ManyToOne
    private User user;
    private CurrencyType currencyType;
    private CurrencyTransactionType type;
    private Date createdOn;

}
