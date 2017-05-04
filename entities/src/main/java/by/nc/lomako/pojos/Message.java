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
public final class Message implements Serializable {
    private static final long serialVersionUID = -2670224366962524946L;

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User userTo;

    @Column(nullable = false)
    private Timestamp createdDate;

    @Column(nullable = false)
    private String body;

    @Column(
            columnDefinition = "bit default false",
            nullable = false
    )
    private boolean isRead;

    @PrePersist
    public void prePersist() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }

}
