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
        "userFrom",
        "userTo"
})
@ToString(exclude = {
        "userFrom",
        "userTo"
})
@Entity
@Table(name = "T_MESSAGES")
public final class Message implements Serializable {
    private static final long serialVersionUID = -2670224366962524946L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(
            name = "USER_FROM_ID",
            nullable = false
    )
    private User userFrom;

    @ManyToOne
    @JoinColumn(
            name = "USER_TO_ID",
            nullable = false
    )
    private User userTo;

    @Column(
            name = "CREATED_AT",
            nullable = false
    )
    private Timestamp createdDate;

    @Column(
            name = "BODY",
            nullable = false
    )
    private String body;

    @Column(
            name = "IS_READ",
            columnDefinition = "bit default false",
            nullable = false
    )
    private boolean isRead;

    @PrePersist
    public void prePersist() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }

}
