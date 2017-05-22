/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Lomako
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = GET)
    public String show(
            @PathVariable(value = "id") String idString,
            Model model
    ) throws UserNotFoundException {

        long id = Long.valueOf(idString);
        UserInfoDto userInfoDto = userService.findById(id);
        model.addAttribute("userDto", userInfoDto);
        return "user/show";
    }
}
