package edu.sjsu.cmpe275.nfttradingmarket.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data @NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID token;
    @OneToOne
    private User user;
    private LocalDateTime createdOn;
    private LocalDateTime expiresOn;
    private LocalDateTime confirmedOn;

    public ConfirmationToken(UUID token, LocalDateTime createdOn, LocalDateTime expiresOn, User user) {
        this.token = token;
        this.user = user;
        this.createdOn = createdOn;
        this.expiresOn = expiresOn;
        this.confirmedOn = confirmedOn;
    }

}
