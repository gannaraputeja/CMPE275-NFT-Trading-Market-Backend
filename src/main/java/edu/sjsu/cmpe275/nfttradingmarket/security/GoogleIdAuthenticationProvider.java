package edu.sjsu.cmpe275.nfttradingmarket.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import edu.sjsu.cmpe275.nfttradingmarket.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleIdAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(GoogleIdAuthenticationProvider.class);

    @Value("${app.google.client-id}")
    private String CLIENT_ID;

    @Autowired
    private UserService userService;

    private HttpTransport httpTransport = new ApacheHttpTransport();
    private JsonFactory jsonFactory = new GsonFactory();

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("This authentication provider does not support instances of type %s", authentication.getClass().getName()));
            }
            return null;
        }

        GoogleIdAuthenticationToken googleIdAuthenticationToken = (GoogleIdAuthenticationToken) authentication;

        if (logger.isDebugEnabled())
            logger.debug(String.format("Validating google login with token '%s'", googleIdAuthenticationToken.getCredentials()));


        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken googleIdToken = null;
        try {

            googleIdToken = verifier.verify((String) googleIdAuthenticationToken.getCredentials());

            if (googleIdToken == null) {
                throw new BadCredentialsException("Unable to verify token");
            }
        } catch (IOException|GeneralSecurityException e) {
            throw new BadCredentialsException("Unable to verify token", e);
        }

        Payload payload = googleIdToken.getPayload();

        // Get profile information from payload
        String email = payload.getEmail();

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Loading user details for email '%s'", email));
        }
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(email);
            ((GoogleIdAuthenticationToken) authentication).setPrincipal(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (!userDetails.isAccountNonLocked()) {
                throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            }

            if (!userDetails.isEnabled()) {
                throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            }

            if (!userDetails.isAccountNonExpired()) {
                throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        } catch (UsernameNotFoundException e) {
            // provision a new user?
            //throw e;
            userDetails = userService.provisionNewUser(payload);
            ((GoogleIdAuthenticationToken) authentication).setPrincipal(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return new GoogleIdAuthenticationToken((String) googleIdAuthenticationToken.getCredentials(), userDetails, userDetails.getAuthorities(), authentication.getDetails());
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (GoogleIdAuthenticationToken.class.isAssignableFrom(authentication));
    }

}