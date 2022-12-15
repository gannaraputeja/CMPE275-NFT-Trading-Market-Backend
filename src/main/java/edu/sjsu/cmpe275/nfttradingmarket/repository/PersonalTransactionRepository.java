package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.PersonalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

public interface PersonalTransactionRepository extends JpaRepository<PersonalTransaction, UUID> {
    List<PersonalTransaction> findAllByUserId(UUID userId);

}
