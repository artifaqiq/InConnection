/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.pojos;

import by.nc.lomako.pojos.constants.StringLength;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@EqualsAndHashCode(exclude = {
        "roles",
        "sentMessages",
        "recvMessages",
        "posts",
})
@ToString(exclude = {
        "roles",
        "sentMessages",
        "recvMessages",
        "posts",
})
@Entity
@Table(name = "T_USERS")
public final class User implements Serializable {
    private static final long serialVersionUID = -6528822142825371828L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(
            name = "EMAIL",
            unique = true,
            nullable = false,
            length = StringLength.EMAIL
    )
    private String email;

    @Column(
            name = "FIRST_NAME",
            nullable = false,
            length = StringLength.FIRST_NAME
    )
    private String firstName;

    @Column(
            name = "LAST_NAME",
            nullable = false,
            length = StringLength.LAST_NAME
    )
    private String lastName;

    @Column(
            name = "B_CRYPT_PASSWORD",
            nullable = false
    )
    private String encryptedPassword;

    @Column(
            nullable = false,
            name = "CREATED_AT"
    )
    private Timestamp createdDate;

    @Column(
            nullable = false,
            name = "UPDATED_AT"
    )
    private Timestamp updatedDate;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "T_M2M_USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "T_M2M_USER_FRIEND",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FRIEND_USER_ID", referencedColumnName = "ID")
    )
    private Set<User> friends;

    @OneToMany(mappedBy = "userFrom")
    private Set<Message> sentMessages;

    @OneToMany(mappedBy = "userTo")
    private Set<Message> recvMessages;

    @OneToMany(mappedBy = "user", cascade = REMOVE)
    private Set<Post> posts;

    @PrePersist
    public void prePersist() {
        createdDate = updatedDate = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Timestamp(System.currentTimeMillis());
    }

}
