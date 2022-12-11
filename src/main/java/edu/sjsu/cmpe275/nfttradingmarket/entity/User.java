package edu.sjsu.cmpe275.nfttradingmarket.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
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
    @Column(unique = true, length = 320)
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
    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private List<Nft> ownedNfts;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Wallet wallet;
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<CurrencyTransaction> currencyTransactionList;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ConfirmationToken confirmationToken;

    public User(String username, String nickname, String password, String firstname, String lastname,
                UserRole role, Boolean enabled, Boolean locked) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.enabled = enabled;
        this.locked = locked;
    }
}
