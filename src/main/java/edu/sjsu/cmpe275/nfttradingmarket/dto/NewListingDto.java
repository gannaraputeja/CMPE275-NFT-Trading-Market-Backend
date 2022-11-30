package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingStatus;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingType;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class NewListingDto {
    private UUID id;
    private Double amount;
    private CurrencyType currencyType;
    private ListingType sellType;
    private UUID nftId;
    private UUID userId;
    private ListingStatus listingStatus;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MM-dd-yyyy HH:mm:ss")
    private Date listedTime;
}
