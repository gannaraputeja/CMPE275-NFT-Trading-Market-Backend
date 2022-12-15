package edu.sjsu.cmpe275.nfttradingmarket.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data @NoArgsConstructor
public class UserDetailsUpdateDTO {

    private UUID id;
    private String username;
    private String firstname;
    private String lastname;
    private String nickname;
    @Size(min = 8, max = 40)
    private String password;
}
