package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.MakeOfferDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.service.ListingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/listing")
public class ListingController {

    private final ModelMapper modelMapper;
    private final ListingService listingService;

    public ListingController(ModelMapper modelMapper, ListingService _listingService) {
        this.modelMapper = modelMapper;
        this.listingService = _listingService;
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "/makeoffer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeOfferDto> makeAnOfferForAuctionedListing(@RequestBody MakeOfferDto makeOfferDto){
        //Convert DTO to entity
        Offer offerRequest = modelMapper.map(makeOfferDto, Offer.class);

        Offer offer = listingService.makeOffer(offerRequest);

        //entity to DTO
        MakeOfferDto makeOfferResponse = modelMapper.map(offer, MakeOfferDto.class);
        return ResponseEntity.ok().body(makeOfferResponse);
    }

    @GetMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListingDto> getAllListingsByUser(@PathVariable("userId")UUID userId){
        return listingService.getAllListingsById(userId).stream().map(Listing->modelMapper.map(Listing, ListingDto.class))
                .collect(Collectors.toList());
    }
}
