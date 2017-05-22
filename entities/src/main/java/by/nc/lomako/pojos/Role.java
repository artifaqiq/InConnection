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
@Table(
        name = "T_ROLES",
        indexes = {@Index(columnList = "ROLES")}
)
public final class Role implements Serializable {
    private static final long serialVersionUID = -6055459587145221633L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(
            name = "ROLES",
            columnDefinition = "enum('ADMIN', 'MODERATOR', 'USER')",
            unique = true,
            nullable = false
    )
    @Enumerated(STRING)
    private RoleType roleType;

    @ManyToMany
    @JoinTable(
            name = "T_M2M_USER_ROLE",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")}

    )
    private Set<User> users;
}
