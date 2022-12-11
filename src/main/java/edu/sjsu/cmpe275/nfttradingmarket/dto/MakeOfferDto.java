package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private UUID nftTokenId;
    private UUID userId;
    private UUID listingId;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MM-dd-yyyy HH:mm:ss")
    private Date createdOn;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MM-dd-yyyy HH:mm:ss")
    private Date expirationTime;
    private OfferStatus status;
}
