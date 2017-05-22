/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.user.UserForRegisterDto;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.security.SecurityService;
import by.nc.lomako.services.UserService;
import by.nc.lomako.validation.UserForRegisterDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
public class RestAuthController {
    private UserService userService;

    private SecurityService securityService;

    private UserForRegisterDtoValidator userForRegisterDtoValidator;

    @Autowired
    public RestAuthController(
            UserService userService,
            SecurityService securityService,
            UserForRegisterDtoValidator userForRegisterDtoValidator
    ) {
        this.userService = userService;
        this.securityService = securityService;
        this.userForRegisterDtoValidator = userForRegisterDtoValidator;
    }

    @RequestMapping(value = "api/v1/auth/register", method = POST)
    public ResponseEntity<?> register(
            @RequestBody UserForRegisterDto userDto,
            BindingResult bindingResult
    ) {

        userForRegisterDtoValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new OperationStatusDto(HttpStatus.BAD_REQUEST, "Incorrect json format"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            long id = userService.register(userDto);

            return new ResponseEntity<>(
                    new OperationStatusDto(HttpStatus.OK, "success"),
                    HttpStatus.OK
            );


        } catch (UniqueEmailException e) {
            return new ResponseEntity<>(
                    new OperationStatusDto(HttpStatus.FORBIDDEN, "User with some email already exist"),
                    HttpStatus.FORBIDDEN
            );
        }


    }
}
