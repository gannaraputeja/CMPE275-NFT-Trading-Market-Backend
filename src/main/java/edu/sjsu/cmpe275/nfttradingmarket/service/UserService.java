package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRespository;
import edu.sjsu.cmpe275.nfttradingmarket.security.config.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRespository userRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRespository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
        return new MyUserPrincipal(user);
    }
}
