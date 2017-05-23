/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<?> showUser(
            @PathVariable(value = "id") String idString
    ) throws UserNotFoundException {

        long id = Long.parseLong(idString);
        UserInfoDto userInfoDto = userService.findById(id);

        return new ResponseEntity<>(
                userInfoDto,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/search", method = GET)
    public ResponseEntity<?> findUser(
            @RequestParam("email") String email
    ) throws UserNotFoundException {
        UserInfoDto userInfoDto = userService.findByEmail(email);
        return new ResponseEntity<>(
                userInfoDto,
                HttpStatus.OK
        );

    }

    @RequestMapping(value = "/index", method = GET)
    public ResponseEntity<List<UserInfoDto>> findAll(
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString
    ) {
        int start = Integer.parseInt(startString);
        int limit = Integer.parseInt(limitString);

        return new ResponseEntity<>(
                userService.findAll(start, limit),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/is_email_free", method = GET)
    public boolean isEmailFree(
            @RequestParam("email") String email
    ) {
        return !userService.isEmailExist(email);
    }

    @RequestMapping(value = "/count", method = GET)
    public ResponseEntity<?> count() {
        long count = userService.count();

        return new ResponseEntity<>(
                count,
                HttpStatus.OK
        );
    }
}
