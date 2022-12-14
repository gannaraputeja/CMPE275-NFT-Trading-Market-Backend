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
public class DashboardMetricsDto {
    private long totalActiveNFTSForSale;
    private long pricedActiveNFTSForSale;
    private long auctionedActiveNFTSForSale;
    private long totalAllActiveOffers;
    private long activeNFTSListedWithOffers;
    private long activeNFTSListedWithOutOffers;
}
