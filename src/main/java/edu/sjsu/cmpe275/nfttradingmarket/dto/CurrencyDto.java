package edu.sjsu.cmpe275.nfttradingmarket.dto;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CurrencyDto {
    private UUID id;
    private Double amount;
    private CurrencyType type;
    private UUID walletId;
}
