package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingStatus;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor
public class ListingDto {
    private UUID id;
    private Double amount;
    private CurrencyType currencyType;
    private ListingType sellType;
    private UUID nftTokenId;
    private UUID userId;
    private NftDto nft;
    private UserDTO user;
    private ListingStatus listingStatus;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date listingTime;
    private List<MakeOfferDto> offers;
}
