package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.request.SignUpRequest;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.JWTResponse;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.LoginRequest;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRespository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.WalletRepository;
import edu.sjsu.cmpe275.nfttradingmarket.security.MyUserPrincipal;
import edu.sjsu.cmpe275.nfttradingmarket.security.config.JWTConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JWTConfig jwtConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRespository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
        return new MyUserPrincipal(user);
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {

        if (userRespository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already use!"));
        }

        if (userRespository.existsByNickname(signUpRequest.getNickname())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Nickname is already in taken!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getNickname(),
                bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
        user.setFirstname(signUpRequest.getFirstname());
        user.setLastname(signUpRequest.getLastname());
        user.setRole(UserRole.USER);
        user.setEnabled(true); // TODO: Set to false until email validation
        user.setLocked(false);

        Wallet wallet = createWallet(user);
        user.setWallet(wallet);

        userRespository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private Wallet createWallet(User user){
        Wallet wallet = new Wallet();
        wallet.setUser(user);

        Currency currencyBTC = new Currency(0.0, CurrencyType.BTC, wallet);
        Currency currencyETH = new Currency(0.0, CurrencyType.ETH, wallet);

        List<Currency> currencies = new ArrayList<>();
        currencies.add(currencyBTC);
        currencies.add(currencyETH);

        wallet.setCurrencyList(currencies);

        return wallet;
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest, AuthenticationManager authenticationManager) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtConfig.generateJwtToken(authentication);

        MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JWTResponse jwtResponse = new JWTResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername(), roles, userPrincipal.isEnabled());
        return ResponseEntity.ok(jwtResponse);
    }

}
