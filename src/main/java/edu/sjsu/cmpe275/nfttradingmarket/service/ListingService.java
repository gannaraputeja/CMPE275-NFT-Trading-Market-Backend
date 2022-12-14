package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftTransactionDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.ListingRequestDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Currency;
import edu.sjsu.cmpe275.nfttradingmarket.exception.*;
<<<<<<< HEAD
import edu.sjsu.cmpe275.nfttradingmarket.repository.*;
=======
import edu.sjsu.cmpe275.nfttradingmarket.repository.CurrencyRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.PersonalTransactionRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftTransactionRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.WalletRepository;

>>>>>>> db88e6e (Implemented personal transaction logging service)
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final OfferRepository offerRepository;
    private final NftTransactionRepository nftTransactionRepository;
    private final WalletRepository walletRepository;
    private final CurrencyRepository currencyRepository;
    private final PersonalTransactionRepository personalTransactionRepository;
    private final PersonalTransactionService personalTransactionService;


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NftRepository nftRepository;
    private final CurrencyRepository currencyRepository;

<<<<<<< HEAD
    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository, UserRepository userRepository, ModelMapper modelMapper, NftRepository nftRepository, NftTransactionRepository nftTransactionRepository,
                          CurrencyRepository currencyRepository) {
=======
    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository, UserRepository userRepository, ModelMapper modelMapper, NftRepository nftRepository, NftTransactionRepository nftTransactionRepository, WalletRepository _walletRepository, CurrencyRepository _currencyRespository, PersonalTransactionRepository personalTransactionRepository, PersonalTransactionService personalTransactionService) {
>>>>>>> db88e6e (Implemented personal transaction logging service)
        this.listingRepository = listingRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.nftRepository = nftRepository;
        this.nftTransactionRepository = nftTransactionRepository;
<<<<<<< HEAD
        this.currencyRepository = currencyRepository;
=======
        this.walletRepository = _walletRepository;
        this.currencyRepository = _currencyRespository;
        this.personalTransactionRepository = personalTransactionRepository;
        this.personalTransactionService = personalTransactionService;
>>>>>>> db88e6e (Implemented personal transaction logging service)
    }

    public ResponseEntity<MessageResponse> createListing(ListingRequestDto listingRequestDto){

        //Convert DTO to entity
//        this.modelMapper.typeMap(Listing.class, ListingRequestDto.class)
//                .addMapping(src->src.getNft().getTokenId(), ListingRequestDto::setNftTokenId)
//                .addMapping(src->src.getUser().getId(), ListingRequestDto::setUserId);

        Listing listingRequest = modelMapper.map(listingRequestDto, Listing.class);

//        ModelMapper mapper = new ModelMapper();
//        mapper.addMappings(new PropertyMap<Nft, NftDto>() {
//            @Override
//            protected void configure() {
//                // Tells ModelMapper to skip backreference Listing
//                skip().setListing(null);
//            }
//        });

        if(!userRepository.existsById(listingRequest.getUser().getId()))
            throw new UserNotFoundException("User does not exist.");
        listingRequest.setStatus(ListingStatus.NEW);
        listingRequest.setListingTime(new Date());
        listingRepository.save(listingRequest);

        return ResponseEntity.ok().body(new MessageResponse("NFT listed for sale successfully."));
    }

    public ResponseEntity<MessageResponse> makeOffer(Offer offer) throws UserNotFoundException {
        User user = userRepository.findById(offer.getUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Nft nft = nftRepository.findById(offer.getNft().getTokenId()).orElseThrow(() -> new NftNotFoundException("NFT not found."));
        Listing listing = listingRepository.findById(offer.getListing().getId()).orElseThrow(() -> new ListingNotFoundException("Listing not found."));

        Currency currency = user.getWallet().getCurrencyList().stream()
                .filter(cur -> cur.getType().equals(listing.getCurrencyType()))
                .findFirst().orElseThrow(() -> new CurrencyNotFoundException("Currency not found."));

        // total of all active offers amount with same currency type by excluding users earlier offer to same nft
        List<Offer> offers = offerRepository.findAllByUserIdAndStatus(offer.getUser().getId(), OfferStatus.NEW);
        Double totalOffersAmount = offers.stream()
                .filter(off -> !off.getNft().getTokenId().equals(offer.getNft().getTokenId())
                        && off.getListing().getCurrencyType().equals(offer.getListing().getCurrencyType()))
                .mapToDouble(off -> off.getAmount().doubleValue()).sum();

        if(!ListingStatus.NEW.equals(listing.getStatus())){
            throw new InvalidNFTTransactionException("Invalid listing status.");
        } else if(offer.getAmount() > currency.getAmount() ||
                offer.getAmount() > (currency.getAmount() - totalOffersAmount)) {
            throw new InsufficientCurrencyException("User has insufficient currency.");
        }

        offers.stream().filter(off -> off.getNft().getTokenId().equals(offer.getNft().getTokenId()))
                .forEach(off->{
                    off.setStatus(OfferStatus.CANCELLED);
                    offerRepository.save(off);
                });

        offer.setStatus(OfferStatus.NEW);
        offer.setCreatedOn(new Date());
        offerRepository.save(offer);
        return ResponseEntity.ok(new MessageResponse("Make offer successful."));
    }

    public ResponseEntity<ListingDto> updateListingCancellationStatus(UUID listingId)
    {
        Listing updateListing = listingRepository.findById(listingId)
                .orElseThrow(()->new ListingNotFoundException("No listing available with given listingId"));

        updateListing.setStatus(ListingStatus.CANCELLED);

        listingRepository.save(updateListing);

        ListingDto newListingDtoResponse = modelMapper.map(updateListing, ListingDto.class);

        return ResponseEntity.ok().body(newListingDtoResponse);
    }

    public ResponseEntity<MakeOfferDto> updateOfferCancellationStatus(UUID OfferId){
        Offer updateOffer = offerRepository.findById(OfferId)
                .orElseThrow(()-> new OfferNotAvailabeException("No Offer available with given Offer Id"));

        updateOffer.setStatus(OfferStatus.CANCELLED);

        offerRepository.save(updateOffer);

        MakeOfferDto newOfferDtoResponse = modelMapper.map(updateOffer, MakeOfferDto.class);

        return ResponseEntity.ok().body(newOfferDtoResponse);
    }

    public List<ListingDto> getAllListingsById(UUID userId)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not exist."));

        List<Listing> result = listingRepository.findAllByUser(user);

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<Nft, NftDto>() {
            @Override
            protected void configure() {
                // Tells ModelMapper to skip backreference Listing
                skip().setListings(new ArrayList<>());
            }
        });

        List<ListingDto> listingDtoList = result
                .stream()
                .map(Listing -> mapper.map(Listing, ListingDto.class))
                .collect(Collectors.toList());
        
        return listingDtoList;
    }

    public List<NftDto> getAllNftListingsByUser(UUID userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
        {
            List<Listing> result = listingRepository.findAllByUser(user.get());
            if(!result.isEmpty())
            {
                List<Optional<Nft>> nftList = new ArrayList<>();
                for(Listing listing: result) {
                    nftList.add((nftRepository.findById(listing.getNft().getTokenId())));
                }

                if(!nftList.isEmpty())
                {
                    List<NftDto> nftDtoList = nftList
                            .stream()
                            .map(Nft -> modelMapper.map(Nft, NftDto.class))
                            .collect(Collectors.toList());

                    return nftDtoList;
                }
            }
            else
                throw new ResourceNotFoundException();
        }
        else
            throw new ResourceNotFoundException();
        return null;
    }

    public List<ListingDto> getAllNewListingsWithNewOffers(){
        List<Listing> listings = listingRepository.findAllByStatusOrderByListingTimeDesc(ListingStatus.NEW);

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<Nft, NftDto>() {
            @Override
            protected void configure() {
                // Tells ModelMapper to skip backreference Listing
                skip().setListings(new ArrayList<>());
            }
        });

        // Filter Offers with OfferStatus NEW
        listings.stream()
            .forEach(listing -> {
                listing.setOffers(listing.getOffers()
                        .stream().filter(offer -> offer.getStatus().equals(OfferStatus.NEW)).collect(Collectors.toList()));
            });

        List<ListingDto> listingDtoList = listings.stream()
                .map(listing -> mapper.map(listing, ListingDto.class))
                .collect(Collectors.toList());

        // Calc price
        listingDtoList.forEach(listingDto -> {
            if(ListingType.AUCTION.equals(listingDto.getSellType())) {
                if(listingDto.getOffers().size() == 0){
                    listingDto.setPrice(listingDto.getAmount());
                } else {
                    listingDto.setPrice(listingDto.getOffers().stream().map(offer -> offer.getAmount()).max(Comparator.naturalOrder()).get());
                }
            } else {
                listingDto.setPrice(listingDto.getAmount());
            }
        });

        return listingDtoList;
    }

    public List<MakeOfferDto> getAllOffersOfNftAtAuction(UUID listingId){
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(()->new ListingNotFoundException("No listing available with given listingId"));

        List<Offer> offers = offerRepository.findAllByListingId(listingId);

        List<MakeOfferDto> offerDtoList = offers.stream()
                .map(Offer -> modelMapper.map(Offer, MakeOfferDto.class))
                .collect(Collectors.toList());

        return offerDtoList;
    }

    public List<ListingDto> getAllListingsWithOffers(){
        List<Listing>listings = listingRepository.findAll();

        List<Listing> responseList = new ArrayList<>();

        for (Listing listing : listings) {
            List<Offer> offers = offerRepository.findAllByListingId(listing.getId());

            if(offers.size()>0)
                responseList.add(listing);
        }

        List<ListingDto> responseDtoList = responseList.stream().map(Listing -> modelMapper.map(Listing, ListingDto.class))
                .collect(Collectors.toList());

        return responseDtoList;
    }

    public List<ListingDto> getAllListingsWithoutOffers(){
        List<Listing>listings = listingRepository.findAll();

        List<Listing> responseList = new ArrayList<>();

        for (Listing listing : listings) {
            List<Offer> offers = offerRepository.findAllByListingId(listing.getId());

            if(offers.size()==0)
                responseList.add(listing);
        }

        List<ListingDto> responseDtoList = responseList.stream().map(Listing -> modelMapper.map(Listing, ListingDto.class))
                .collect(Collectors.toList());

        return responseDtoList;
    }

    public List<ListingDto> getAllListingsWithActiveOffers(){
        List<Listing>listings = listingRepository.findAll();

        List<Listing> responseList = new ArrayList<>();

        for (Listing listing : listings) {
            List<Offer> offers = offerRepository.findAllByListingId(listing.getId());

            for(Offer offer: offers){
                if(offer.getStatus()==OfferStatus.NEW) {
                    responseList.add(listing);
                    break;
                }
            }
        }

        List<ListingDto> responseDtoList = responseList.stream().map(Listing -> modelMapper.map(Listing, ListingDto.class))
                .collect(Collectors.toList());

        return responseDtoList;
    }

//    public void createNFTTransaction(Offer offer) {
//        User buyer = offer.getUser();
//        Nft currentNft = offer.getNft();
//        User seller = currentNft.getOwner();
//        NftTransaction nftTransaction = new NftTransaction();
//        nftTransaction.setBuyer(buyer);
//        nftTransaction.setSeller(seller);
//        nftTransaction.setCurrencyType(offer.getListing().getCurrencyType());
//        nftTransaction.setListingType(offer.getListing().getSellType());
//        nftTransaction.setNft(currentNft);
//        nftTransaction.setCreatedOn(new Date());
//        NftTransaction nftT = nftTransactionRepository.save(nftTransaction);
//    }

    public ResponseEntity<MessageResponse> updateOfferAcceptedStatus(UUID offerId){
        Offer updateOffer = offerRepository.findById(offerId)
                .orElseThrow(()-> new OfferNotAvailabeException("Offer not available to cancel"));

        User buyer = updateOffer.getUser();
        Nft currentNft = updateOffer.getNft();
        User seller = currentNft.getOwner();

        Currency currency = buyer.getWallet().getCurrencyList().stream().
                filter(cur->cur.getType().equals(updateOffer.getListing().getCurrencyType()))
                .findFirst().orElseThrow(() -> new CurrencyNotFoundException("No currency found"));

        // total of all active offers amount with same currency type
        List<Offer> offers = offerRepository.findAllByUserIdAndStatus(buyer.getId(), OfferStatus.NEW);
        Double totalOffersAmount = offers.stream()
                .filter(offer -> offer.getListing().getCurrencyType().equals(updateOffer.getListing().getCurrencyType()))
                .mapToDouble(offer -> offer.getAmount().doubleValue()).sum();

        if(currency.getAmount() < totalOffersAmount){
            throw new InsufficientCurrencyException("Insufficient balance for buyer");
        }
        //deducting amount for offer made to buy NFT at auction
        currency.setAmount(currency.getAmount()-updateOffer.getAmount());
        currencyRepository.save(currency);

        //Created NFT transaction from seller to buyer
        NftTransaction nftTransaction = new NftTransaction();
        nftTransaction.setBuyer(buyer);
        nftTransaction.setSeller(seller);
        nftTransaction.setCurrencyType(updateOffer.getListing().getCurrencyType());
        nftTransaction.setListingType(updateOffer.getListing().getSellType());
        nftTransaction.setNft(currentNft);
        nftTransaction.setCreatedOn(new Date());
        NftTransaction nftT = nftTransactionRepository.save(nftTransaction);

        //update NFT
        currentNft.setOwner(buyer);
        currentNft.setLastRecordedTime(new Date());
        currentNft.setSmartContractAddress(UUID.randomUUID());
        nftRepository.save(currentNft);

        //Update Listing status to SOLD
        Listing listing = updateOffer.getListing();
        listing.setStatus(ListingStatus.SOLD);
        listingRepository.save(listing);

        //Update offer status to accepted
        updateOffer.setStatus(OfferStatus.ACCEPTED);
        offerRepository.save(updateOffer);

        //Update all other offers of listing with status NEW to rejected
        offerRepository.findAllByListingIdAndStatus(updateOffer.getListing().getId(), OfferStatus.NEW)
                .stream().filter(offer-> !offer.getUser().getId().equals(buyer.getId()))
                .forEach(offer->{
                    offer.setStatus(OfferStatus.REJECTED);
                    offerRepository.save(offer);
                });

        return ResponseEntity.ok().body(new MessageResponse("Successfully accepted offer."));
    }
}
