package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.CurrencyDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
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
public class WalletService {
    private final NftRepository nftRepository;
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final NftTransactionRepository nftTransactionRepository;
    private final OfferRepository offerRepository;
    private final PersonalTransactionService personalTransactionService;

    public WalletService(NftRepository nftRepository, WalletRepository walletRepository, ModelMapper modelMapper,
                         CurrencyRepository currencyRepository, UserRepository userRepository,
                         ListingRepository listingRepository, NftTransactionRepository nftTransactionRepository,
                         OfferRepository offerRepository, PersonalTransactionService personalTransactionService) {
        this.nftRepository = nftRepository;
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.nftTransactionRepository = nftTransactionRepository;
        this.offerRepository = offerRepository;
        this.personalTransactionService = personalTransactionService;
    }

    public ResponseEntity<MessageResponse> createNFT(Nft nft){
        nft.setSmartContractAddress(UUID.randomUUID());
        nft.setCreatedOn(new Date());
        nft.setLastRecordedTime(new Date());
        nftRepository.save(nft);
        return ResponseEntity.ok().body(new MessageResponse("NFT created successfully."));
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

    public ResponseEntity<MessageResponse> buyNft(BuyNftDto buyNftDto) throws UserNotFoundException, ListingNotFoundException
        , NftNotFoundException, InvalidNFTTransactionException, InsufficientCurrencyException {
        User user = userRepository.findById(buyNftDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User does not exist."));
        Currency currency = user.getWallet().getCurrencyList().stream()
                .filter( cur -> cur.getType().equals(buyNftDto.getCurrencyType()))
                .findFirst().orElseThrow(() -> new CurrencyNotFoundException("Currency not found."));
        Listing listing = listingRepository.findById(buyNftDto.getListingId()).orElseThrow(() -> new ListingNotFoundException("Listing not found."));
        Nft nft = nftRepository.findById(buyNftDto.getNftTokenId()).orElseThrow(() -> new NftNotFoundException("NFT not found."));

        // total of all active offers amount with same currency type
        List<Offer> offers = offerRepository.findAllByUserIdAndStatus(buyNftDto.getUserId(), OfferStatus.NEW);
        Double totalOffersAmount = offers.stream()
                .filter(offer -> offer.getListing().getCurrencyType().equals(buyNftDto.getCurrencyType()))
                .mapToDouble(offer -> offer.getAmount().doubleValue()).sum();

        if(!ListingStatus.NEW.equals(listing.getStatus())){
            throw new InvalidNFTTransactionException("Invalid listing status.");
        } else if(listing.getAmount() > currency.getAmount() ||
                listing.getAmount() > (currency.getAmount() - totalOffersAmount)) {
            throw new InsufficientCurrencyException("User has insufficient currency.");
        }
        User currentOwner = nft.getOwner();
        User futureOwner = user;

        NftTransaction nftTransaction = new NftTransaction();
        nftTransaction.setNft(nft);
        nftTransaction.setBuyer(user);
        nftTransaction.setSeller(nft.getOwner());
        nftTransaction.setPrice(listing.getAmount());
        nftTransaction.setCurrencyType(listing.getCurrencyType());
        nftTransaction.setListingType(listing.getSellType());
        nftTransaction.setCreatedOn(new Date());
        nftTransactionRepository.save(nftTransaction);
        // deduct currency amount
        currency.setAmount(currency.getAmount() - listing.getAmount());
        currencyRepository.save(currency);
        listing.setStatus(ListingStatus.SOLD);
        listingRepository.save(listing);
        nft.setSmartContractAddress(UUID.randomUUID());
        nft.setOwner(user);
        nft.setLastRecordedTime(new Date());
        nftRepository.save(nft);
        personalTransactionService.createBuyNFTTransaction(currentOwner, futureOwner, listing);
        return ResponseEntity.ok(new MessageResponse("Transaction successful."));
    }

}
