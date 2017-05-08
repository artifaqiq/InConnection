/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dto;

import by.nc.lomako.pojos.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserInfoDto {

    private long id;

    private String email;

    private String encryptedPassword;

    private String firstName;

    private String lastName;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    private Set<AccessLevel> roles;
}
