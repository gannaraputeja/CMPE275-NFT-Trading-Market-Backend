package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Data
public class NftTransactionDto {
    private UUID id;
    private Double price;
    private UserDTO seller;
    private UserDTO buyer;
    private CurrencyType currencyType;
    private ListingType listingType;
    private NftDto nft;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date lastRecordedTime;
}
