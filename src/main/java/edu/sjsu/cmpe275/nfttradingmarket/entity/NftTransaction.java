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
public class NftTransaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private Double price;
    @OneToOne
    private User seller;
    @OneToOne
    private User buyer;
    private CurrencyType currencyType;
    private SellType sellType;
    @ManyToOne
    private Nft nft;
    private Date createdOn;

}
