package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.PersonalTransactionDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.BuyNftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Currency;
import edu.sjsu.cmpe275.nfttradingmarket.exception.*;
import edu.sjsu.cmpe275.nfttradingmarket.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class PersonalTransactionService {
    private final NftRepository nftRepository;
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final NftTransactionRepository nftTransactionRepository;
    private final OfferRepository offerRepository;
    private final PersonalTransactionRepository personalTransactionRepository;

    public PersonalTransactionService(NftRepository nftRepository, WalletRepository walletRepository, ModelMapper modelMapper,
                         CurrencyRepository currencyRepository, UserRepository userRepository,
                         ListingRepository listingRepository, NftTransactionRepository nftTransactionRepository,
                         OfferRepository offerRepository, PersonalTransactionRepository personalTransactionRepository) {
        this.nftRepository = nftRepository;
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.nftTransactionRepository = nftTransactionRepository;
        this.offerRepository = offerRepository;
        this.personalTransactionRepository = personalTransactionRepository;

    }

    public void createNFTTransaction(Offer offer) {
        User buyer = offer.getUser();
        Nft currentNft = offer.getNft();
        User seller = currentNft.getOwner();
        
        //PURCHASE LOG
        Currency purchaseCurrency = currencyRepository.findByWalletIdAndType(buyer.getWallet().getId() , offer.getListing().getCurrencyType());
        PersonalTransaction personalTransaction = new PersonalTransaction();
        personalTransaction.setCreatedOn(new Date());
        personalTransaction.setActionType(ActionType.PURCHASE);
        personalTransaction.setNft(currentNft);
        personalTransaction.setCurrencyType(offer.getListing().getCurrencyType());
        personalTransaction.setAmount(offer.getAmount());
        personalTransaction.setAvailableAmount(purchaseCurrency.getAmount());
        personalTransaction.setUser(buyer);
        personalTransaction.setPreviousUser(seller);
        personalTransactionRepository.save(personalTransaction);

        //SELL LOG
        Currency sellCurrency = currencyRepository.findByWalletIdAndType(seller.getWallet().getId() , offer.getListing().getCurrencyType());
        PersonalTransaction personalTransaction1 = new PersonalTransaction();
        personalTransaction1.setCreatedOn(new Date());
        personalTransaction1.setActionType(ActionType.SELL);
        personalTransaction1.setNft(currentNft);
        personalTransaction1.setCurrencyType(offer.getListing().getCurrencyType());
        personalTransaction1.setAmount(offer.getAmount());
        personalTransaction1.setAvailableAmount(sellCurrency.getAmount());
        personalTransaction1.setUser(seller);
        personalTransaction1.setPreviousUser(buyer);
        personalTransactionRepository.save(personalTransaction1);
        
        //NFT Transaction Repository - DEPRECATED
        NftTransaction nftTransaction = new NftTransaction();
        nftTransaction.setBuyer(buyer);
        nftTransaction.setSeller(seller);
        nftTransaction.setCurrencyType(offer.getListing().getCurrencyType());
        nftTransaction.setListingType(offer.getListing().getSellType());
        nftTransaction.setNft(currentNft);
        nftTransaction.setCreatedOn(new Date());
        NftTransaction nftT = nftTransactionRepository.save(nftTransaction);
    }

    public void createBuyNFTTransaction(User buyer, Listing listing) {
        Nft currentNft = listing.getNft();
        User seller = currentNft.getOwner();
        
        //PURCHASE LOG
        Currency purchaseCurrency = currencyRepository.findByWalletIdAndType(buyer.getWallet().getId() , listing.getCurrencyType());
        PersonalTransaction personalTransaction = new PersonalTransaction();
        personalTransaction.setCreatedOn(new Date());
        personalTransaction.setActionType(ActionType.PURCHASE);
        personalTransaction.setNft(currentNft);
        personalTransaction.setCurrencyType(listing.getCurrencyType());
        personalTransaction.setAmount(listing.getAmount());
        personalTransaction.setAvailableAmount(purchaseCurrency.getAmount());
        personalTransaction.setUser(buyer);
        personalTransaction.setPreviousUser(seller);
        personalTransactionRepository.save(personalTransaction);

        //SELL LOG
        Currency sellCurrency = currencyRepository.findByWalletIdAndType(seller.getWallet().getId() , listing.getCurrencyType());
        PersonalTransaction personalTransaction1 = new PersonalTransaction();
        personalTransaction1.setCreatedOn(new Date());
        personalTransaction1.setActionType(ActionType.SELL);
        personalTransaction1.setNft(currentNft);
        personalTransaction1.setCurrencyType(listing.getCurrencyType());
        personalTransaction1.setAmount(listing.getAmount());
        personalTransaction1.setAvailableAmount(sellCurrency.getAmount());
        personalTransaction1.setUser(seller);
        personalTransaction1.setPreviousUser(buyer);
        personalTransactionRepository.save(personalTransaction1);
    }

    public void createCurrencyTransaction(CurrencyTransaction currencyTransaction, Currency currency) {
        //PURCHASE LOG
        User user = currencyTransaction.getUser();
        PersonalTransaction personalTransaction = new PersonalTransaction();
        personalTransaction.setCreatedOn(new Date());
        
        if (currencyTransaction.getType() == CurrencyTransactionType.WITHDRAW)
            personalTransaction.setActionType(ActionType.WITHDRAW);
        else {
            personalTransaction.setActionType(ActionType.DEPOSIT);
        }
        // personalTransaction.setNft(currentNft);
        personalTransaction.setCurrencyType(currencyTransaction.getCurrencyType());
        personalTransaction.setAmount(currencyTransaction.getAmount());
        personalTransaction.setAvailableAmount(currency.getAmount());
        personalTransaction.setUser(user);
        // personalTransaction.setPreviousUser(seller);
        personalTransactionRepository.save(personalTransaction);

        
    }

    public List<PersonalTransactionDto> getAllPersonalTransactions(UUID UserId) {
        List<PersonalTransaction> personalTransactions = personalTransactionRepository.findAllByUserId(UserId);


        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<Listing, PersonalTransactionDto>() {
            @Override
            protected void configure() {
                // Tells ModelMapper to skip backreference Listing
                skip().setUser(null);
                skip().setPreviousUser(null);
                // skip().setNft(null);
            }
        });

        List<PersonalTransactionDto> personalTransactionDtoList = personalTransactions
                .stream()
                .map(pt -> mapper.map(pt, PersonalTransactionDto.class))
                .collect(Collectors.toList());

        return personalTransactionDtoList;
    }


}
