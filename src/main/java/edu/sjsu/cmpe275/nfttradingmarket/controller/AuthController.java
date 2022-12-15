package edu.sjsu.cmpe275.nfttradingmarket.controller;

import edu.sjsu.cmpe275.nfttradingmarket.dto.request.LoginRequestDTO;
import edu.sjsu.cmpe275.nfttradingmarket.dto.request.SignUpRequestDTO;
import edu.sjsu.cmpe275.nfttradingmarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Transactional
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(path = "/local/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        return userService.registerUser(signUpRequestDTO);
    }

    @PostMapping(path = "/local/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.loginUser(loginRequestDTO, authenticationManager);
    }

    @PostMapping(path = "/oauth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> oAuthLogin(@Valid @RequestParam String googleIdToken) throws GeneralSecurityException, IOException {
        return userService.oAuthLogin(googleIdToken);
    }

    @PostMapping(path = "/validate/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmEmail(@NotBlank @RequestParam String token) {
        return userService.confirmEmail(token);
    }

    @PostMapping(path = "/resend/validation/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendValidationEmail(@NotBlank @RequestParam String username) {
        return userService.resendValidationEmail(username);
    }


}
