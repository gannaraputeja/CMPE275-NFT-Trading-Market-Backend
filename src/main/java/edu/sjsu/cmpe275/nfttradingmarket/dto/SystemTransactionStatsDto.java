package edu.sjsu.cmpe275.nfttradingmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
public class SystemTransactionStatsDto {
    private long totalDeposits;
    private long totalDepositCurrencyAmount;

    private long totalWithDrawals;
    private long totalWithDrawalCurrencyAmount;

    private long initialSystemBalance;
    private long currentSystemBalance;

    private long totalNFTSales;
    private long totalNFTSalesCurrencyAmount;
}
