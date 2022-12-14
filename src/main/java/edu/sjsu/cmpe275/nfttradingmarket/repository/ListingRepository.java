package edu.sjsu.cmpe275.nfttradingmarket.repository;


import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingStatus;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;

import org.hibernate.type.TrueFalseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

//@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
   Optional<Listing> findById(UUID id);
   List<Listing> findAllByUser(User user);
   List<Listing> findAllByStatusOrderByListingTimeDesc(ListingStatus status);

   @Query(value = "SELECT count(*) from listing where status='NEW' and sell_type = ?1", nativeQuery = true)
   public long getActiveNFTSForSaleCountBySellType(String sellType);


   @Query(value = "SELECT count(*) from listing where status='NEW' and sell_type = ?1", nativeQuery = true)
   public long getActiveAuctionNFTSCountByOffersCondition(String sellType);

   @Query(value = "SELECT count(*) from offer where status='NEW'", nativeQuery = true)
   public long getTotalActiveOffers();
   
   // @Query(value = "SELECT count(*) from offer where OfferStatus='NEW'", nativeQuery = true)
   // long getActiveNFTSListedWithOutOffers();

   @Query(value = "SELECT count(*) from listing as lis inner join offer as off on lis.nft_token_id = off.nft_token_id where lis.status='NEW'", nativeQuery = true)
   long getActiveNFTSListedWithOffers();
}
