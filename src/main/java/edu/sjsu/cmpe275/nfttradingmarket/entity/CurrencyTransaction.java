package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data @NoArgsConstructor
@Entity
public class CurrencyTransaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column(precision=10, scale = 2)
    private Double amount;
    @Column(precision=10, scale = 2)
    private Double availableAmount;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;
    @Enumerated(EnumType.STRING)
    private CurrencyTransactionType type;
    private Date createdOn;

}
