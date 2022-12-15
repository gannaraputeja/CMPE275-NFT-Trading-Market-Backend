package edu.sjsu.cmpe275.nfttradingmarket.controller;
import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.PersonalTransactionDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.WalletDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.CreateNftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.PersonalTransaction;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import edu.sjsu.cmpe275.nfttradingmarket.service.PersonalTransactionService;
import edu.sjsu.cmpe275.nfttradingmarket.service.WalletService;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/transactions")
public class PersonalTransactionsController {
    private final ModelMapper modelMapper;
    private final WalletService walletService;
    private final PersonalTransactionService personalTransactionService;

    public PersonalTransactionsController(ModelMapper modelMapper, WalletService walletService, PersonalTransactionService personalTransactionService) {
        this.modelMapper = modelMapper;
        this.walletService = walletService;
        this.personalTransactionService = personalTransactionService;
    }

    @GetMapping(path="/personal/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonalTransactionDto> getPersonalTransactions(@PathVariable("userId") String userId,@RequestParam Integer period, @RequestParam List<String> currencyType) {
        return personalTransactionService.getAllPersonalTransactions(userId, period, currencyType);
        
    }
}
