package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Currency;
import edu.sjsu.cmpe275.nfttradingmarket.exception.CurrencyAmountsNotAvailableForUserException;
import edu.sjsu.cmpe275.nfttradingmarket.exception.NftNotFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.exception.UserNotFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.repository.CurrencyRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.WalletRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class DashboardService {
    private final NftRepository nftRepository;
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    public DashboardService(NftRepository nftRepository, WalletRepository walletRepository, ModelMapper modelMapper,
                         CurrencyRepository currencyRepository,
                         UserRepository userRepository) {
        this.nftRepository = nftRepository;
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }
}
