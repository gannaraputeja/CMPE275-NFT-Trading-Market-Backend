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

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        return userService.registerUser(signUpRequestDTO);
    }

    @GetMapping(path = "/validate/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmEmail(@NotBlank @RequestParam String token) {
        return userService.confirmEmail(token);
    }

    @PostMapping(path = "/resend/validation/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendValidationEmail(@NotBlank @RequestParam String username) {
        return userService.resendValidationEmail(username);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.loginUser(loginRequestDTO, authenticationManager);
    }
}
