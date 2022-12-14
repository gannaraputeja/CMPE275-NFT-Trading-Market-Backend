package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.ListingRequestDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
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
    public ResponseEntity<MessageResponse> createListing(@RequestBody ListingRequestDto listingRequestDto){
        return listingService.createListing(listingRequestDto);
    }

    @PostMapping(path = "/makeOffer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> makeAnOfferForAuctionedListing(@RequestBody MakeOfferDto makeOfferDto){
        //Convert DTO to entity
        Offer offerRequest = modelMapper.map(makeOfferDto, Offer.class);

        return listingService.makeOffer(offerRequest);
    }

    @GetMapping(path = "/getAllListings/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllListingsByUser(@PathVariable("userId") UUID userId){
        return listingService.getAllListingsById(userId);
    }

    @GetMapping(path = "/getAllNftsForSale/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NftDto> getAllNftListingsByUser(@PathVariable("userId") UUID userId){
        return listingService.getAllNftListingsByUser(userId);
    }

    @GetMapping(path = "/getAllNewListingsWithNewOffers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllNewListingsWithNewOffers(){
        return listingService.getAllNewListingsWithNewOffers();
    }

    @PutMapping(path = "/cancelListing/{listingId}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "/acceptOffer/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> acceptOffer(@PathVariable("offerId") UUID offerId){
        return listingService.updateOfferAcceptedStatus(offerId);
    }
}
