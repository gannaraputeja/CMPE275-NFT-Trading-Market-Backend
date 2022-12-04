package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyTransactionDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyTransaction;
import edu.sjsu.cmpe275.nfttradingmarket.service.CurrencyTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currencyTransaction")
public class CurrencyTransactionController {

    private final ModelMapper modelMapper;

    private final CurrencyTransactionService currencyTransactionService;

    public CurrencyTransactionController(ModelMapper modelMapper, CurrencyTransactionService currencyTransactionService) {
        this.modelMapper = modelMapper;
        this.currencyTransactionService = currencyTransactionService;
    }

    @PostMapping(path= "/createDepositOrWithdrawTransaction", consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyTransactionDto> createCurrencyTransaction(@RequestBody CurrencyTransactionDto currencyTransactionDto){
        //convert DTO to entity
        this.modelMapper.typeMap(CurrencyTransaction.class, CurrencyTransactionDto.class).
                addMapping(src->src.getUser().getId(), CurrencyTransactionDto::setUserId);

        CurrencyTransaction currencyTransactionRequest = modelMapper.map(currencyTransactionDto, CurrencyTransaction.class);

        CurrencyTransaction currencyTransaction = currencyTransactionService.createCurrencyTransaction(currencyTransactionRequest);

        //Convert Entity to DTO
        CurrencyTransactionDto NewTransactionResponse = modelMapper.map(currencyTransaction, CurrencyTransactionDto.class);
        return ResponseEntity.ok().body(NewTransactionResponse);
    }
}
