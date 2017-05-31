/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dto.user;

import by.nc.lomako.pojos.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String encryptedPassword;
    private String firstName;
    private String lastName;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private Set<RoleType> roles;
}
