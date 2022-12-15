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
public class PersonalTransaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private Date createdOn;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    @ManyToOne
    private Nft nft;
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;
    private Double amount;
    private Double availableAmount;
    @OneToOne
    private User user;
    @OneToOne
    private User previousUser;

}
