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

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@EqualsAndHashCode(exclude = {
        "user"
})
@ToString(exclude = {
        "user"
})
@Entity
@Table(
        name = "T_POSTS",
        indexes = {@Index(columnList = "USER_ID")}
)
public final class Post implements Serializable {
    private static final long serialVersionUID = -2007000771409705812L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "USER_ID"
    )
    private User user;

    @Column(
            nullable = false,
            name = "CREATED_AT"
    )
    private Timestamp createdDate;

    @Column(name = "BODY")
    private String body;

    @PrePersist
    public void prePersist() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }

}
