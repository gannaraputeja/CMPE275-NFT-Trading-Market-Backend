package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftTransactionDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.ListingRequestDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.exception.*;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftTransactionRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NftRepository nftRepository;

    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository, UserRepository userRepository, ModelMapper modelMapper, NftRepository nftRepository, NftTransactionRepository nftTransactionRepository) {
        this.listingRepository = listingRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.nftRepository = nftRepository;
        this.nftTransactionRepository = nftTransactionRepository;
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

    public Offer makeOffer(Offer offer){
        return offerRepository.save(offer);
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

    public void createNFTTransaction(Offer offer) {
        User buyer = offer.getUser();
        Nft currentNft = offer.getNft();
        User seller = currentNft.getOwner();
        NftTransaction nftTransaction = new NftTransaction();
        nftTransaction.setBuyer(buyer);
        nftTransaction.setSeller(seller);
        nftTransaction.setCurrencyType(offer.getListing().getCurrencyType());
        nftTransaction.setListingType(offer.getListing().getSellType());
        nftTransaction.setNft(currentNft);
        nftTransaction.setCreatedOn(new Date());
        NftTransaction nftT = nftTransactionRepository.save(nftTransaction);
    }

    public ResponseEntity<MakeOfferDto> updateOfferAcceptedStatus(UUID offerId){
        Offer updateOffer = offerRepository.findById(offerId)
                .orElseThrow(()-> new OfferNotAvailabeException("Offer not available to cancel"));

        updateOffer.setStatus(OfferStatus.ACCEPTED);
        createNFTTransaction(updateOffer);

        offerRepository.save(updateOffer);

        MakeOfferDto newOfferDtoResponse = modelMapper.map(updateOffer, MakeOfferDto.class);
        return ResponseEntity.ok().body(newOfferDtoResponse);
    }
}
