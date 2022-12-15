package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.cmpe275.nfttradingmarket.entity.OfferStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MakeOfferDto {
    private UUID id;
    private Double amount;
    private UUID nftTokenId;
    private UUID userId;
    private UserDTO user;
    private UUID listingId;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date createdOn;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date expirationTime;
    private OfferStatus status;
}
