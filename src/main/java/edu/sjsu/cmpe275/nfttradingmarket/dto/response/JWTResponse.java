package edu.sjsu.cmpe275.nfttradingmarket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data @NoArgsConstructor @AllArgsConstructor
public class JWTResponse implements Serializable {

    private String jwt;
    private UUID id;
    private String username;
    private List<String> roles;
    private Boolean enabled;

}
