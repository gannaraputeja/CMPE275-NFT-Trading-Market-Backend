package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class NftDto {
    private UUID tokenId;
    private UUID smartContractAddress;
    private String name;
    private String type;
    private String description;
    private String imageURL;
    private String assetURL;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date lastRecordedTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date createdOn;
    private UUID creatorId;
    private UUID ownerId;
    private UserDTO creator;
    private UserDTO owner;
    private List<ListingDto> listings;
}
