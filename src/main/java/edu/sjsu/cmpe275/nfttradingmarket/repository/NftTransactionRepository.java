package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.NftTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NftTransactionRepository extends JpaRepository<UUID, NftTransaction> {
}
