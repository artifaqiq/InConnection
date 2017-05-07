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
public final class FriendRequest implements Serializable {
    private static final long serialVersionUID = -924562099395241385L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false)
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User userTo;

    @PrePersist
    public void prePersist() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }

}
