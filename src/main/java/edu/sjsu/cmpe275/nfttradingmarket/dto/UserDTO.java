package edu.sjsu.cmpe275.nfttradingmarket.dto;

import edu.sjsu.cmpe275.nfttradingmarket.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data @NoArgsConstructor
public class UserDTO implements Serializable {

    private UUID id;
    private String username;
    private String firstname;
    private String lastname;
    private String nickname;
    private String password;
    private Boolean enabled;
    private Boolean locked;
    private UserRole role;

}
