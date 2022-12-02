package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data
public class SignUpRequestDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    @Email
    private String username;

    @NotBlank
    @Size(min = 3, max = 40)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    @NotBlank
    @Size(max = 40)
    private String firstname;

    @NotBlank
    @Size(max = 40)
    private String lastname;


}
