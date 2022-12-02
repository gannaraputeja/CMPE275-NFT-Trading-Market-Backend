package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data
public class LoginRequestDTO {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
