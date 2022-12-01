package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    @Email
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String nickname;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(max = 40)
    private String firstname;

    @NotBlank
    @Size(max = 40)
    private String lastname;


}
