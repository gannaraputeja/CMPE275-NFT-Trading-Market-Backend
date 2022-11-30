package edu.sjsu.cmpe275.nfttradingmarket.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@Data @NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column(unique = true)
    private String username;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(columnDefinition = "boolean default false")
    private Boolean enabled;
    @Column(columnDefinition = "boolean default false")
    private Boolean locked;
    private String password;
    @OneToMany(mappedBy = "creator")
    @ToString.Exclude
    private List<Nft> createdNfts;
    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Wallet wallet;

}
