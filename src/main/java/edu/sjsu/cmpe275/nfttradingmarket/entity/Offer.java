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
public class Offer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Nft nft;
    @ManyToOne
    private Listing listing;
    private Double amount;
    private Date createdOn;
    private Date expirationTime;
    @Enumerated(EnumType.STRING)
    private OfferStatus status;

}
