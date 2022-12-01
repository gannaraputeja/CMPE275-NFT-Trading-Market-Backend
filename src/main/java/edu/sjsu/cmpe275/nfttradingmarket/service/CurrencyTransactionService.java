package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyTransaction;
import edu.sjsu.cmpe275.nfttradingmarket.repository.CurrencyTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyTransactionService {
    private final CurrencyTransactionRepository currencyTransactionRepository;

    public CurrencyTransactionService(CurrencyTransactionRepository currencyTransactionRepository) {
        this.currencyTransactionRepository = currencyTransactionRepository;
    }

    public CurrencyTransaction createCurrencyTransaction(CurrencyTransaction currencyTransaction){
        return currencyTransactionRepository.save(currencyTransaction);
    }
}
