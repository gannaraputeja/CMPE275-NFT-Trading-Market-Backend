package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NewListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.service.ListingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/listing")
public class ListingController {

    private final ModelMapper modelMapper;
    private final ListingService _listingService;

    public ListingController(ModelMapper modelMapper, ListingService _listingService) {
        this.modelMapper = modelMapper;
        this._listingService = _listingService;
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewListingDto> createListing(@RequestBody NewListingDto newListingDto){
        //Convert DTO to entity
        Listing listingRequest = modelMapper.map(newListingDto, Listing.class);

        Listing listing = _listingService.createNFT(listingRequest);

        //entity to DTO
        NewListingDto NewListingResponse = modelMapper.map(listing, NewListingDto.class);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/makeoffer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeOfferDto> makeAnOfferForAuctionedListing(@RequestBody MakeOfferDto makeOfferDto){
        //Convert DTO to entity
        Offer offerRequest = modelMapper.map(makeOfferDto, Offer.class);

        Offer offer = _listingService.makeOffer(offerRequest);

        //entity to DTO
        MakeOfferDto makeOfferResponse = modelMapper.map(offer, MakeOfferDto.class);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
