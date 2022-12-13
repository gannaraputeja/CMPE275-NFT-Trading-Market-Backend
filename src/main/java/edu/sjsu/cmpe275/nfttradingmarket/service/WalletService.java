package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
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
        nft.setSmartContractAddress(UUID.randomUUID());
        nft.setCreatedOn(new Date());
        nft.setLastRecordedTime(new Date());
        return nftRepository.save(nft);
    }

    public Wallet createWallet(Wallet wallet){
        return walletRepository.save(wallet);
    }

    public List<NftDto> getNfsByUserId(UUID userId){
        List<Nft> nftList = nftRepository.findAllByOwnerIdOrderByCreatedOnDesc(userId);

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<Listing, ListingDto>() {
            @Override
            protected void configure() {
                // Tells ModelMapper to skip backreference Listing
                skip().setNft(null);
            }
        });

        List<NftDto> nftDtoList = nftList
                .stream()
                .map(Nft -> mapper.map(Nft, NftDto.class))
                .collect(Collectors.toList());

        return nftDtoList;
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
