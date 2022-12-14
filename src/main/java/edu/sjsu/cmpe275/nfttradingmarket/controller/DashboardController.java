package edu.sjsu.cmpe275.nfttradingmarket.controller;
import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.WalletDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.CreateNftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.DashboardMetricsDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final ModelMapper modelMapper;
    private final ListingRepository listingRepository;


    public DashboardController(ModelMapper modelMapper, ListingRepository listingRepository) {
        this.modelMapper = modelMapper;
        this.listingRepository = listingRepository;
    }

    @GetMapping(path="/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardMetricsDto> getDashboardMetrics() {
        long pricedNFTSCount  = listingRepository.getActiveNFTSForSaleCountBySellType("PRICED");
        long auctionedNFTSCount  = listingRepository.getActiveNFTSForSaleCountBySellType("AUCTION");
        System.out.println(" pricedNFTSCount : " + pricedNFTSCount + auctionedNFTSCount);
        DashboardMetricsDto dashboardMetricsdto = new DashboardMetricsDto();
        
        dashboardMetricsdto.setPricedActiveNFTSForSale(pricedNFTSCount);
        dashboardMetricsdto.setAuctionedActiveNFTSForSale(auctionedNFTSCount);
        dashboardMetricsdto.setTotalActiveNFTSForSale(pricedNFTSCount + auctionedNFTSCount);

        long totalActiveOffers = listingRepository.getTotalActiveOffers();

        dashboardMetricsdto.setTotalAllActiveOffers(totalActiveOffers);

        long activeNFTSListedWithOffers = listingRepository.getActiveNFTSListedWithOffers();
        dashboardMetricsdto.setActiveNFTSListedWithOffers(activeNFTSListedWithOffers);
        dashboardMetricsdto.setActiveNFTSListedWithOutOffers(auctionedNFTSCount - activeNFTSListedWithOffers);

        return ResponseEntity.ok().body(dashboardMetricsdto);
    }
}
