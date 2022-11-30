package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
