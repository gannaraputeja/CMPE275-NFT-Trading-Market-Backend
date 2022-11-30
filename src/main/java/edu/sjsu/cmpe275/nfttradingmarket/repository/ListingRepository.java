package edu.sjsu.cmpe275.nfttradingmarket.repository;


import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListingRepository extends JpaRepository<UUID, Listing> {

}
