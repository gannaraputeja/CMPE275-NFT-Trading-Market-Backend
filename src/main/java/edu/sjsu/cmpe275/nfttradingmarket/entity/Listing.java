package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor
@Entity
public class Listing {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private Double amount;
    @ManyToOne
    private User user;
    private CurrencyType currencyType;
    private SellType sellType;
    @OneToOne
    private Nft nft;
    private ListingStatus status;
    private Date listingTime;
    @OneToMany(mappedBy = "listing")
    private List<Offer> offer;

}
