package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Service
public class UserService {

    @Autowired
    private UserRespository userRespository;

}
