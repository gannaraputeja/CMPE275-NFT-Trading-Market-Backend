package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NftRepository extends JpaRepository<UUID, Nft> {

}
