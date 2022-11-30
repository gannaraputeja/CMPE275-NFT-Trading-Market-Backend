package edu.sjsu.cmpe275.nfttradingmarket.repository;

import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Repository
public interface UserRespository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

}
