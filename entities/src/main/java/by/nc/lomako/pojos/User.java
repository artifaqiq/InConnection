/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.pojos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@EqualsAndHashCode(exclude = {
        "accessLevels",
        "friends",
        "sentMessages",
        "recvMessages",
        "posts",
        "avatars"
})
@ToString(exclude = {
        "accessLevels",
        "friends",
        "sentMessages",
        "recvMessages",
        "posts",
        "avatars"
})
@Entity
public final class User implements Serializable {
    private static final long serialVersionUID = -6528822142825371828L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(
            unique = true,
            nullable = false
    )
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(nullable = false)
    private Timestamp createdDate;

    @Column(nullable = false)
    private Timestamp updatedDate;

    @ManyToMany(cascade = ALL)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}

    )
    private Set<AccessLevel> accessLevels;

    @ManyToMany
    @JoinTable(
            name = "USER_FRIEND",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private Set<User> friends;

    @OneToMany(mappedBy = "userFrom")
    private Set<Message> sentMessages;

    @OneToMany(mappedBy = "userTo")
    private Set<Message> recvMessages;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    private Set<Avatar> avatars;

    @PrePersist
    public void prePersist() {
        createdDate = updatedDate = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Timestamp(System.currentTimeMillis());
    }

}
