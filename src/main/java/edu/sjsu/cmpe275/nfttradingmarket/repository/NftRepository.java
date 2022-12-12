package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

public interface NftRepository extends JpaRepository<Nft, UUID> {
    List<Nft> findAllByOwnerIdOrderByCreatedOnDesc(UUID userId);
}
