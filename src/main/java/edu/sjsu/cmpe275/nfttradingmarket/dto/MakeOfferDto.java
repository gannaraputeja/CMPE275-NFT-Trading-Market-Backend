package edu.sjsu.cmpe275.nfttradingmarket.dto;

import edu.sjsu.cmpe275.nfttradingmarket.entity.OfferStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class MakeOfferDto {
    private UUID id;
    private Double amount;
    private UUID nftId;
    private UUID userId;
    private Date createdOn;
    private Date expirationTime;
    private OfferStatus status;
}
