package edu.sjsu.cmpe275.nfttradingmarket.repository;


import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

//@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
    List<Listing> findAllByUserId(User user);
}
