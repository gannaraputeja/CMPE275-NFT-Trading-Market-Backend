package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.dto.PersonalTransactionDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyType;
import edu.sjsu.cmpe275.nfttradingmarket.entity.PersonalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

import javax.validation.constraints.NotBlank;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

public interface PersonalTransactionRepository extends JpaRepository<PersonalTransaction, UUID> {
    @Query(value = "SELECT * from personal_transaction where user_id = ?1 and currency_type in ?2 and created_on >= ?3 and created_on <= ?4 ORDER BY created_on DESC", nativeQuery = true)
    public List<PersonalTransaction> getAllPersonalTransactions(String userId, List<String> currencyType, Date pastDate, Date currDate);
 
    // @Query(value = "SELECT * from personal_transaction where user_id = ?1", nativeQuery = true)
    // public List<PersonalTransaction> getAllPersonalTransactions(String userId, @NotBlank CurrencyType currencyType, Date pastDate, Date currDate);
 
    // List<PersonalTransaction> findAllByUserId(UUID userId);
    // List<PersonalTransaction> findAllByUserIdAndCurrencyTypeAndDateBetween(UUID userId, CurrencyType type, Date startDate, Date endDate);
}
