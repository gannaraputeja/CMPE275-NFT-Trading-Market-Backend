package edu.sjsu.cmpe275.nfttradingmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CancelListingDto {
    private UUID listingId;
}
