package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data @NoArgsConstructor
@Entity
public class Nft {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID tokenId;
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID smartContractAddress;
    private String name;
    private String type;
    private String description;
    private String imageURL;
    private String assetURL;
    private Date lastRecordedTime;
    private Date createdOn;
    @ManyToOne()
    @ToString.Exclude
    private User creator;
    @ManyToOne()
    @ToString.Exclude
    private User owner;
    @ManyToOne()
    @ToString.Exclude
    private User user;
    private ListingType listingType;
    @OneToMany(mappedBy = "nft")
    @ToString.Exclude
    private List<NftTransaction> transactions;

}
