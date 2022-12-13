package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ListingRequestDto {
    private Double amount;
    private UUID userId;
    private CurrencyType currencyType;
    private ListingType sellType;
    private UUID nftTokenId;
}
