package edu.sjsu.cmpe275.nfttradingmarket.repository;


import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.ListingStatus;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;

import org.hibernate.type.TrueFalseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

//@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
   long totalNFTSales = 0;

Optional<Listing> findById(UUID id);
   List<Listing> findAllByUser(User user);
   List<Listing> findAllByStatusOrderByListingTimeDesc(ListingStatus status);


   // Dashboard
   @Query(value = "SELECT count(*) from listing where status='NEW' and sell_type = ?1", nativeQuery = true)
   public long getActiveNFTSForSaleCountBySellType(String sellType);

   @Query(value = "SELECT count(*) from listing where status='NEW' and sell_type = ?1", nativeQuery = true)
   public long getActiveAuctionNFTSCountByOffersCondition(String sellType);

   @Query(value = "SELECT count(*) from offer where status='NEW'", nativeQuery = true)
   public long getTotalActiveOffers();
   
   @Query(value = "SELECT count(*) from listing as lis left join offer as off on lis.nft_token_id = off.nft_token_id where lis.status='NEW'", nativeQuery = true)
   public long getActiveNFTSListedWithOffers();

   //System Transaction Stats

   @Query(value = "SELECT count(*) from currency_transaction where type='DEPOSIT' and created_on >= ?1 and created_on <= ?2 and currency_type in ?3", nativeQuery = true)
   public long getTotalDepositsCount(Date pastDate, Date currDate, List<String> currencyType);

   @Query(value = "SELECT count(*) from currency_transaction where type='WITHDRAW' and created_on >= ?1 and created_on <= ?2 and currency_type in ?3", nativeQuery = true)
   public long getTotalWithdrawalsCount(Date pastDate, Date currDate, List<String> currencyType);

   @Query(value = "SELECT COALESCE(sum(amount), 0) from currency_transaction where type='DEPOSIT' and created_on >= ?1 and created_on <= ?2 and currency_type in ?3", nativeQuery = true)
   public long getTotalDepositCurrencyAmount(Date pastDate, Date currDate, List<String> currencyType);

   @Query(value = "SELECT COALESCE(sum(amount), 0) from currency_transaction where type='WITHDRAW' and created_on >= ?1 and created_on <= ?2 and currency_type in ?3", nativeQuery = true)
   public long getTotalWithdrawCurrencyAmount(Date pastDate, Date currDate, List<String> currencyType);

   @Query(value = "SELECT count(*) from nft_transaction where created_on >= ?1 and created_on <= ?2 and currency_type in ?3", nativeQuery = true)
   public long getTotalNFTSales(Date pastDate, Date currDate, List<String> currencyType);

   @Query(value = "SELECT COALESCE(sum(amount), 0) from currency", nativeQuery = true)
   public long getCurrentSystemBalance(Date pastDate, Date currDate, List<String> currencyType);


   @Query(value = "SELECT COALESCE(sum(price), 0) from nft_transaction where created_on >= ?1 and created_on <= ?2 and currency_type in ?3", nativeQuery = true)
   public long getTotalNFTSalesCurrencyAmount(Date pastDate, Date currDate, List<String> currencyType);



}
