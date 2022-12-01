package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class NftDto {
    private UUID tokenId;
    private UUID smartContractAddress;
    private String name;
    private String type;
    private String description;
    private String imageURL;
    private String assetURL;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MM-dd-yyyy HH:mm:ss")
    private Date lastRecordedTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MM-dd-yyyy HH:mm:ss")
    private Date createdOn;
    private UUID creatorId;
    private UUID ownerId;
    private ListingType listingType;
}
