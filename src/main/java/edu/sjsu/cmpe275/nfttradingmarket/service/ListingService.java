package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
import org.springframework.stereotype.Service;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final OfferRepository offerRepository;

    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository) {
        this.listingRepository = listingRepository;
        this.offerRepository = offerRepository;
    }

    public Listing createNFT(Listing listing){
        return listingRepository.save(listing);
    }

    public Offer makeOffer(Offer offer){
        return offerRepository.save(offer);
    }
}
