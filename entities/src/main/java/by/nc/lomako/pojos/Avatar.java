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
@Table(name = "T_AVATARS")
public final class Avatar implements Serializable {
    private static final long serialVersionUID = -1780394933498189617L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(
            nullable = false,
            name = "CREATED_AT"
    )
    private Timestamp createdDate;

    @Column(
            nullable = false,
            name = "URL"
    )
    private String url;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "USER_ID"
    )
    private User user;

    @PrePersist
    public void prePersist() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }

}
