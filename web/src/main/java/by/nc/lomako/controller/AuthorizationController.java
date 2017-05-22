/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.user.UserForLoginDto;
import by.nc.lomako.dto.user.UserForRegisterDto;
import by.nc.lomako.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.exceptions.MismatchPasswordsException;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.security.SecurityService;
import by.nc.lomako.services.UserService;
import by.nc.lomako.validation.UserForRegisterDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Lomako
 * @version 1.0
 */
@Controller
public class AuthorizationController {

    private UserService userService;

    private SecurityService securityService;

    private UserForRegisterDtoValidator userForRegisterDtoValidator;

    @Autowired
    public AuthorizationController(UserService userService, SecurityService securityService,
                                   UserForRegisterDtoValidator userForRegisterDtoValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userForRegisterDtoValidator = userForRegisterDtoValidator;
    }

    @RequestMapping(value = "/register", method = GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new UserForRegisterDto());

        return "registration";
    }

    @RequestMapping(value = "/register", method = POST)
    public String registration(
            @ModelAttribute("userForm") UserForRegisterDto userForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    )
            throws MismatchPasswordsException, UniqueEmailException {

        userForRegisterDtoValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/register?error";
        }

        long id = userService.register(userForm);
        securityService.authenticate(userForm.getEmail(), userForm.getPassword());

        return "redirect:/user/" + id;
    }

    @RequestMapping(value = "/login", method = GET)
    public String login(Model model) {
        model.addAttribute("userForm", new UserForLoginDto());

        return "login";
    }

    @RequestMapping(value = "/login", method = POST)
    public String login(
            @ModelAttribute("userForm") UserForLoginDto userForm,
            BindingResult bindingResult,
            Model model
    )
            throws IncorrectLoginOrPasswordException {

        if (bindingResult.hasErrors()) {
            return "register?error";
        }

        long id = userService.login(userForm);
        securityService.authenticate(userForm.getEmail(), userForm.getPassword());

        return "redirect:/user/" + id;
    }
}
