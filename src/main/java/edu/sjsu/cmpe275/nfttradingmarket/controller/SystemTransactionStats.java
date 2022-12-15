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

import java.util.Calendar;
import java.util.Date;
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
    public ResponseEntity<SystemTransactionStatsDto> getSystemTransactionStats(@RequestParam String period, @RequestParam List<String> currencyType) {

        Date currDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);

        if(currencyType.isEmpty()) {
            currencyType.add("BTC");
            currencyType.add("ETH");
        }
        if (period.isEmpty()) {
            period = "1";
        } 
        if (period == "1") {
            c.add(Calendar.HOUR, -24);
        } else if (period == "2") {
            c.add(Calendar.DATE, -7);
        } else {
            c.add(Calendar.MONTH, -1);
        }
        Date pastDate = c.getTime();
        
        long totalDeposits              = listingRepository.getTotalDepositsCount(pastDate, currDate, currencyType);
        long totalDepositCurrencyAmount = listingRepository.getTotalDepositCurrencyAmount(pastDate, currDate, currencyType);
        long totalWithDrawals           = listingRepository.getTotalWithdrawalsCount(pastDate, currDate, currencyType);
        long totalWithDrawalCurrencyAmount = listingRepository.getTotalWithdrawCurrencyAmount(pastDate, currDate, currencyType);
        long initialSystemBalance = 2000;
        long currentSystemBalance = listingRepository.getCurrentSystemBalance(pastDate, currDate, currencyType);
        long totalNFTSales = listingRepository.getTotalNFTSales(pastDate, currDate, currencyType);
        long totalNFTSalesCurrencyAmount = listingRepository.getTotalNFTSalesCurrencyAmount(pastDate, currDate, currencyType);
        
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
