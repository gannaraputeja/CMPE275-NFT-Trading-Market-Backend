package edu.sjsu.cmpe275.nfttradingmarket.controller;
import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.WalletDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.CreateNftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import edu.sjsu.cmpe275.nfttradingmarket.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class PersonalTransactionsController {
    private final ModelMapper modelMapper;
    private final WalletService walletService;

    public PersonalTransactionsController(ModelMapper modelMapper, WalletService walletService) {
        this.modelMapper = modelMapper;
        this.walletService = walletService;
    }

    @GetMapping(path="/personal/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getPersonalTransactions(@PathVariable("userId") UUID userId) {
        
    }
}
