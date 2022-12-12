package edu.sjsu.cmpe275.nfttradingmarket.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.SignUpRequestDTO;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.JWTResponse;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.LoginRequestDTO;
import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import edu.sjsu.cmpe275.nfttradingmarket.entity.*;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Currency;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ConfirmationTokenRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
import edu.sjsu.cmpe275.nfttradingmarket.security.MyUserPrincipal;
import edu.sjsu.cmpe275.nfttradingmarket.security.config.JWTConfig;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JWTConfig jwtConfig;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GoogleIdTokenVerifier verifier;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
        return new MyUserPrincipal(user);
    }

    public ResponseEntity<Object> registerUser(SignUpRequestDTO signUpRequestDTO) {

        if (userRepository.existsByUsername(signUpRequestDTO.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already use!"));
        }

        if (userRepository.existsByNickname(signUpRequestDTO.getNickname())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Nickname is already in taken!"));
        }

        // Create new user's account
        User user = new User(signUpRequestDTO.getUsername(), signUpRequestDTO.getNickname(),
                bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()),
                signUpRequestDTO.getFirstname(), signUpRequestDTO.getLastname(),
                UserRole.USER, false, false);

        // Create Wallet
        Wallet wallet = createWallet(user);
        user.setWallet(wallet);

        // Create ConfirmationToken
        UUID uuidToken = UUID.randomUUID();

        ConfirmationToken confirmationToken = new ConfirmationToken(uuidToken, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), user);
        user.setConfirmationToken(confirmationToken);

        userRepository.save(user);

        emailService.send(user.getUsername(), user.getFirstname(), uuidToken.toString());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private Wallet createWallet(User user){
        Wallet wallet = new Wallet();
        wallet.setUser(user);

        // Create currencies
        Currency currencyBTC = new Currency(0.0, CurrencyType.BTC, wallet);
        Currency currencyETH = new Currency(0.0, CurrencyType.ETH, wallet);

        List<Currency> currencies = new ArrayList<>();
        currencies.add(currencyBTC);
        currencies.add(currencyETH);

        wallet.setCurrencyList(currencies);

        return wallet;
    }

    public ResponseEntity<?> loginUser(LoginRequestDTO loginRequestDTO, AuthenticationManager authenticationManager) {

        if (!userRepository.existsByUsername(loginRequestDTO.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username does not exist!"));
        }

        return authenticateUser(loginRequestDTO.getUsername(), loginRequestDTO.getPassword(), authenticationManager);
    }

    public ResponseEntity<?> authenticateUser(String username, String password, AuthenticationManager authenticationManager) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtConfig.generateJwtToken(authentication);

        MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JWTResponse jwtResponse = new JWTResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername(), roles, userPrincipal.isEnabled());
        return ResponseEntity.ok(jwtResponse);
    }

    public ResponseEntity<?> confirmEmail(String token) {

        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(UUID.fromString(token));
        if(confirmationToken.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token."));
        } else if (confirmationToken.get().getConfirmedOn() != null){
            return ResponseEntity.badRequest().body(new MessageResponse("Token already validated."));
        }else if(confirmationToken.get().getExpiresOn().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Token expired."));
        }
        confirmationToken.get().setConfirmedOn(LocalDateTime.now());
        confirmationToken.get().getUser().setEnabled(true);
        confirmationTokenRepository.save(confirmationToken.get());

        return ResponseEntity.ok(new MessageResponse("User successfully verified!"));
    }

    public ResponseEntity<?> resendValidationEmail(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));

        if(user.getEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User already verified."));
        }

        UUID token = UUID.randomUUID();
        user.getConfirmationToken().setToken(token);
        user.getConfirmationToken().setCreatedOn(LocalDateTime.now());
        user.getConfirmationToken().setExpiresOn(LocalDateTime.now().plusMinutes(15));
        user.getConfirmationToken().setUser(user);
        user.getConfirmationToken().setConfirmedOn(null);

        userRepository.save(user);

        emailService.send(user.getUsername(), user.getFirstname(), token.toString());

        return ResponseEntity.ok(new MessageResponse("Verification email sent successfully!"));

    }

    public ResponseEntity<?> oAuthLogin(String googleIdToken) throws GeneralSecurityException, IOException {

        GoogleIdToken idToken = verifier.verify(googleIdToken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            Optional<User> user = userRepository.findByUsername(payload.getEmail());

            // Create User if not already exists
            if(!user.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User not found."));
            } else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String jwt = jwtConfig.generateJwtToken(authentication);

                MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();

                List<String> roles = userPrincipal.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

                JWTResponse jwtResponse = new JWTResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername(), roles, userPrincipal.isEnabled());
                return ResponseEntity.ok(jwtResponse);
            }

        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid token."));

    }

    public UserDetails provisionNewUser(GoogleIdToken.Payload payload) {

        String randomString = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        String nickname = String.valueOf(payload.get("name")).replace(" ", "").concat(randomString);

        // Create new user's account
        User newUser = new User(payload.getEmail(), nickname, null, String.valueOf(payload.get("given_name")),
                String.valueOf(payload.get("family_name")), UserRole.USER, false, false);

        // Create Wallet
        Wallet wallet = createWallet(newUser);
        newUser.setWallet(wallet);

        // Create ConfirmationToken
        UUID uuidToken = UUID.randomUUID();

        ConfirmationToken confirmationToken = new ConfirmationToken(uuidToken, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), newUser);
        newUser.setConfirmationToken(confirmationToken);

        userRepository.save(newUser);

        emailService.send(newUser.getUsername(), newUser.getFirstname(), uuidToken.toString());

        return new MyUserPrincipal(newUser);
    }

}
