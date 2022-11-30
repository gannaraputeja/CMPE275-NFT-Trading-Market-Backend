package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyTransactionRepository extends JpaRepository<UUID, CurrencyTransaction> {
}
