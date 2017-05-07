/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.pojos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
public final class AccessLevel implements Serializable {
    private static final long serialVersionUID = -6055459587145221633L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(
            columnDefinition = "enum('ADMIN', 'MODERATOR', 'USER')",
            unique = true,
            nullable = false
    )
    @Enumerated(STRING)
    private AccessLevelType accessLevelType;

    @ManyToMany
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}

    )
    private Set<User> users;
}
