package edu.sjsu.cmpe275.nfttradingmarket.controller;
import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.SystemTransactionStatsDto;
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
@RequestMapping("/api/v1/systemtransaction")
public class SystemTransactionStats {
    private final ModelMapper modelMapper;
    private final ListingRepository listingRepository;


    public SystemTransactionStats(ModelMapper modelMapper, ListingRepository listingRepository) {
        this.modelMapper = modelMapper;
        this.listingRepository = listingRepository;
    }

    @GetMapping(path="/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemTransactionStatsDto> getSystemTransactionStats() {

        long totalDeposits              = listingRepository.getTotalDepositsCount();
        long totalDepositCurrencyAmount = listingRepository.getTotalDepositCurrencyAmount();
        long totalWithDrawals           = listingRepository.getTotalWithdrawalsCount();
        long totalWithDrawalCurrencyAmount = listingRepository.getTotalDepositCurrencyAmount();
        long initialSystemBalance = 2000;
        long currentSystemBalance = listingRepository.getCurrentSystemBalance();
        long totalNFTSales = listingRepository.getTotalNFTSales();
        long totalNFTSalesCurrencyAmount = listingRepository.getTotalNFTSalesCurrencyAmount();
        
        SystemTransactionStatsDto systemTransactionStatsDto = new SystemTransactionStatsDto();
        
        systemTransactionStatsDto.setTotalDeposits(totalDeposits);
        systemTransactionStatsDto.setTotalDepositCurrencyAmount(totalDepositCurrencyAmount);
        systemTransactionStatsDto.setTotalWithDrawals(totalWithDrawals);
        systemTransactionStatsDto.setTotalWithDrawalCurrencyAmount(totalWithDrawalCurrencyAmount);
        systemTransactionStatsDto.setCurrentSystemBalance(currentSystemBalance);
        systemTransactionStatsDto.setTotalNFTSales(totalNFTSales);
        systemTransactionStatsDto.setTotalNFTSalesCurrencyAmount(totalNFTSalesCurrencyAmount);
        
        return ResponseEntity.ok().body(systemTransactionStatsDto);
    }
}
