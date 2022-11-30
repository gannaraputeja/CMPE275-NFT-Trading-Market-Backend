package edu.sjsu.cmpe275.nfttradingmarket.dto;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingStatus;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class NewListingDto {
    private UUID id;
    private Double amount;
    private CurrencyType currencyType;
    private ListingType sellType;
    private Nft Nft;
    private ListingStatus listingStatus;
    private Date listedTime;
}
