/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security.details;

import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.services.UserService;
import by.nc.lomako.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

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
}
