package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyTransactionType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CurrencyTransactionDto {
    private UUID id;
    private Double amount;
    private UUID userId;
    private CurrencyType currencyType;
    private CurrencyTransactionType type;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Los_Angeles")
    private Date createdOn;
}
