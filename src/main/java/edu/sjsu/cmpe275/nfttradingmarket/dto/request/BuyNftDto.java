package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor
public class BuyNftDto {
    private UUID listingId;
    private UUID nftTokenId;
    private UUID userId;
    private CurrencyType currencyType;
}
