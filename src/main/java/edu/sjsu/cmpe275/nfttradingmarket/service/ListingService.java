package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.exception.*;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
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

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NftRepository nftRepository;

    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository, UserRepository userRepository, ModelMapper modelMapper, NftRepository nftRepository) {
        this.listingRepository = listingRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.nftRepository = nftRepository;
    }

    public Listing createListing(Listing listing){
        if(!userRepository.existsById(listing.getUser().getId()))
            throw new UserNotFoundException("User does not exist.");
        listing.setStatus(ListingStatus.NEW);
        listing.setListingTime(new Date());
        return listingRepository.save(listing);
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

    public ResponseEntity<MakeOfferDto> updateOfferAcceptedStatus(UUID offerId){
        Offer updateOffer = offerRepository.findById(offerId)
                .orElseThrow(()-> new OfferNotAvailabeException("Offer not available to cancel"));

        updateOffer.setStatus(OfferStatus.ACCEPTED);

        offerRepository.save(updateOffer);

        MakeOfferDto newOfferDtoResponse = modelMapper.map(updateOffer, MakeOfferDto.class);

        return ResponseEntity.ok().body(newOfferDtoResponse);
    }
}
