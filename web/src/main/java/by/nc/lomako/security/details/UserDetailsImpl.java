/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security.details;

import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.pojos.RoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Lomako
 * @version 1.0
 */
public class UserDetailsImpl implements UserDetails {

    @Getter
    private final UserInfoDto user;

    public UserDetailsImpl(UserInfoDto user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (RoleType role : user.getRoles()) {
            grantedAuthorities.add(new GrantedAuthorityImpl(role));
        }

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
