/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security;

import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.pojos.RoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Lomako
 * @version 1.0
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    @Getter
    private final RoleType roleType;

    public GrantedAuthorityImpl(RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    public String getAuthority() {
        return roleType.name();
    }
}
