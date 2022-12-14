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
    private final PersonalTransactionService personalTransactionService;

    public CurrencyTransactionService(CurrencyTransactionRepository currencyTransactionRepository,
                                      CurrencyRepository currencyRepository,
                                      UserRepository userRepository, PersonalTransactionService personalTransactionService) {
        this.currencyTransactionRepository = currencyTransactionRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.personalTransactionService = personalTransactionService;
    }

    public CurrencyTransaction createCurrencyTransaction(CurrencyTransaction currencyTransaction){

        User user = userRepository.findById(currencyTransaction.getUser().getId())
                .orElseThrow(()-> new UserNotFoundException("User not available with given user"));

        Currency currency = currencyRepository.findByWalletIdAndType(user.getWallet().getId() , currencyTransaction.getCurrencyType());

        if(currencyTransaction.getType() == CurrencyTransactionType.WITHDRAW)
            currency.setAmount(currency.getAmount() - currencyTransaction.getAmount());

        if(currencyTransaction.getType() == CurrencyTransactionType.DEPOSIT)
            currency.setAmount(currency.getAmount() + currencyTransaction.getAmount());

        currencyTransaction.setAvailableAmount(currency.getAmount());
        currencyRepository.save(currency);
        currencyRepository.save(currency);
        personalTransactionService.createCurrencyTransaction(currencyTransaction);
        return currencyTransactionRepository.save(currencyTransaction);
    }
}
