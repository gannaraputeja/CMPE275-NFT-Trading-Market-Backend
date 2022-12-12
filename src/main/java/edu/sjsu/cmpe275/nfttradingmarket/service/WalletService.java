package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Currency;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import edu.sjsu.cmpe275.nfttradingmarket.exception.CurrencyAmountsNotAvailableForUserException;
import edu.sjsu.cmpe275.nfttradingmarket.exception.NftNotFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.exception.UserNotFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.repository.CurrencyRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.WalletRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final NftRepository nftRepository;
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    public WalletService(NftRepository nftRepository, WalletRepository walletRepository, ModelMapper modelMapper,
                         CurrencyRepository currencyRepository,
                         UserRepository userRepository) {
        this.nftRepository = nftRepository;
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    public Nft createNFT(Nft nft){
        return nftRepository.save(nft);
    }

    public Wallet createWallet(Wallet wallet){
        return walletRepository.save(wallet);
    }

    public List<NftDto> getNfsById(UUID userId){
        List<Nft> nftList = nftRepository.findAllByOwnerId(userId);
        if(!nftList.isEmpty())
        {
            List<NftDto> nftDtoList = nftList
                    .stream()
                    .map(Nft -> modelMapper.map(Nft, NftDto.class))
                    .collect(Collectors.toList());

            return nftDtoList;
        }
        else
            throw new NftNotFoundException("No NFTs available for user ID");
    }

    public List<CurrencyDto> getCurrencyAmountsById(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not exist."));

        List<Currency> currencyList = currencyRepository.findAllByWalletId(user.getWallet().getId());

        if(!currencyList.isEmpty())
        {
            List<CurrencyDto> currencyDtoList = currencyList.stream().map(Currency -> modelMapper.map(Currency, CurrencyDto.class))
                    .collect(Collectors.toList());

            return currencyDtoList;
        }
        else
            throw new CurrencyAmountsNotAvailableForUserException("No currency amounts available for User Id");
    }
}
