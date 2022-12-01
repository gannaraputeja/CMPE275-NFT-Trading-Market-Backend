package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRespository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final OfferRepository offerRepository;

    private final UserRespository userRespository;

    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository, UserRespository userRespository) {
        this.listingRepository = listingRepository;
        this.offerRepository = offerRepository;
        this.userRespository = userRespository;
    }

    public Listing createNFT(Listing listing){
        return listingRepository.save(listing);
    }

    public Offer makeOffer(Offer offer){
        return offerRepository.save(offer);
    }

    public List<Listing> getAllListingsById(UUID userId){
        Optional<User> user = userRespository.findById(userId);

        List<Listing> result = listingRepository.findAllByUserId(user.get());

        if(!result.isEmpty())
            return result;
        else
            throw new ResourceNotFoundException();
    }
}
