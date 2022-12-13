package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Currency;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyTransaction;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyTransactionType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import edu.sjsu.cmpe275.nfttradingmarket.exception.UserNotFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.repository.CurrencyRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.CurrencyTransactionRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class CurrencyTransactionService {

    private final CurrencyTransactionRepository currencyTransactionRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    public CurrencyTransactionService(CurrencyTransactionRepository currencyTransactionRepository,
                                      CurrencyRepository currencyRepository,
                                      UserRepository userRepository) {
        this.currencyTransactionRepository = currencyTransactionRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    public CurrencyTransaction createCurrencyTransaction(CurrencyTransaction currencyTransaction){

        User user = userRepository.findById(currencyTransaction.getUser().getId())
                .orElseThrow(()-> new UserNotFoundException("User not available with given user"));

        Currency currency = currencyRepository.findByWalletIdAndType(user.getWallet().getId() , currencyTransaction.getCurrencyType());

        if(currencyTransaction.getType() == CurrencyTransactionType.WITHDRAW)
            currency.setAmount(currency.getAmount() - currencyTransaction.getAmount());

        if(currencyTransaction.getType() == CurrencyTransactionType.DEPOSIT)
            currency.setAmount(currency.getAmount() + currencyTransaction.getAmount());

        currencyRepository.save(currency);
        currencyRepository.save(currency);

        return currencyTransactionRepository.save(currencyTransaction);
    }
}
