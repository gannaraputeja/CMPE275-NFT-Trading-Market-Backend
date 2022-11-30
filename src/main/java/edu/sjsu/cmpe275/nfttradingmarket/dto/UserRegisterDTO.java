package edu.sjsu.cmpe275.nfttradingmarket.dto;

import lombok.Data;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data
public class UserRegisterDTO {

    private String username;
    private String firstname;
    private String lastname;
    private String nickname;
    private String password;

}
