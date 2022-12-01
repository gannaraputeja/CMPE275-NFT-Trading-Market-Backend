package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
