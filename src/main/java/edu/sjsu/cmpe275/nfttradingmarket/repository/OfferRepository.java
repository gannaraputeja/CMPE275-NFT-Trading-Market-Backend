package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.entity.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findAllByListingId(UUID listingId);
    List<Offer> findAllByStatus(OfferStatus status);
    List<Offer> findAllByUserIdAndStatus(UUID userId, OfferStatus status);

    List<Offer> findAllByListingIdAndStatus(UUID listingId, OfferStatus status);
}
