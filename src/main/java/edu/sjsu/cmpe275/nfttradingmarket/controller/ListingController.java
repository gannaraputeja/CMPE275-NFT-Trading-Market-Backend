package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.service.ListingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/listing")
public class ListingController {

    private final ModelMapper modelMapper;
    private final ListingService listingService;

    public ListingController(ModelMapper modelMapper, ListingService _listingService) {
        this.modelMapper = modelMapper;
        this.listingService = _listingService;
    }

    @PostMapping(path = "/createListing", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListingDto> createListing(@RequestBody ListingDto listingDto){
        //Convert DTO to entity
        this.modelMapper.typeMap(Listing.class, ListingDto.class)
                .addMapping(src->src.getNft().getTokenId(), ListingDto::setNftTokenId)
                .addMapping(src->src.getUser().getId(), ListingDto::setUserId);

        Listing listingRequest = modelMapper.map(listingDto, Listing.class);

        Listing listing = listingService.createNFT(listingRequest);

        //entity to DTO
        ListingDto NewListingResponse = modelMapper.map(listing, ListingDto.class);
        return ResponseEntity.ok().body(NewListingResponse);
    }

    @PostMapping(path = "/makeOffer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeOfferDto> makeAnOfferForAuctionedListing(@RequestBody MakeOfferDto makeOfferDto){
        //Convert DTO to entity
        Offer offerRequest = modelMapper.map(makeOfferDto, Offer.class);

        Offer offer = listingService.makeOffer(offerRequest);

        //entity to DTO
        MakeOfferDto makeOfferResponse = modelMapper.map(offer, MakeOfferDto.class);
        return ResponseEntity.ok().body(makeOfferResponse);
    }

    @GetMapping(path = "/getAllListings/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllListingsByUser(@PathVariable("userId") UUID userId){
        return listingService.getAllListingsById(userId);
    }

    @GetMapping(path = "/getAllNftsForSale/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NftDto> getAllNftListingsByUser(@PathVariable("userId") UUID userId){
        return listingService.getAllNftListingsByUser(userId);
    }

    @GetMapping(path = "/getAllNewListedNfts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NftDto> getAllNewListedNFTs(){
        return listingService.getAllNewListedNFTs();
    }

    @PutMapping(path = "/cancelListing/{listingId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListingDto> cancelListing(@PathVariable("listingId") UUID listingId)
    {
        return listingService.updateListingCancellationStatus(listingId);
    }

    @PutMapping(path="cancelOffer/{OfferId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeOfferDto> cancelOffer(@PathVariable("OfferId") UUID OfferId){
        return listingService.updateOfferCancellationStatus(OfferId);
    }

    @GetMapping(path= "/offers/{listingId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MakeOfferDto> getAllOffersOfNftAtAuction(@PathVariable("listingId") UUID listingId)
    {
        return listingService.getAllOffersOfNftAtAuction(listingId);
    }

    @GetMapping(path="/getAllListingsWithOffers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllListingsWithOffers(){
        return listingService.getAllListingsWithOffers();
    }

    @GetMapping(path="/getAllListingsWithoutOffers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllListingsWithoutOffers(){
        return listingService.getAllListingsWithoutOffers();
    }

    @GetMapping(path="/getAllListingsWithActiveOffers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllListingsWithActiveOffers() {
        return listingService.getAllListingsWithActiveOffers();
    }

    @PutMapping(path = "/acceptOffer/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeOfferDto> acceptOffer(@PathVariable("offerId") UUID offerId){
        return listingService.updateOfferAcceptedStatus(offerId);
    }
}
