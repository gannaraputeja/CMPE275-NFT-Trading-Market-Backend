package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ActionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PersonalTransactionDto {
    private UUID id;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date createdOn;
    private ActionType actionType;
    @JsonIgnoreProperties({"listings"})
    private NftDto nft;
    private CurrencyType currencyType;
    private Double amount;
    private Double availableAmount;
    private UserDTO user;
    private UserDTO previousUser;
}
