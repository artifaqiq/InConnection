/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security;

import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserInfoDto userInfoDto = userService.findByEmail(username);
            return new UserDetailsImpl(userInfoDto);

        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User with some email not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(UserInfoDto userInfoDto) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        System.out.println(userInfoDto.getRoles());

        for (RoleType role : userInfoDto.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.toString());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
