/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.user.UserForLoginDto;
import by.nc.lomako.dto.user.UserForRegisterDto;
import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.security.service.AuthService;
import by.nc.lomako.services.UserService;
import by.nc.lomako.services.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.services.exceptions.UniqueEmailException;
import by.nc.lomako.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    private final AuthService authService;

    private final UserForRegisterDto.DtoValidator userForRegisterDtoValidator;

    @Autowired
    public AuthenticationController(UserService userService,
                                    AuthService authService,
                                    UserForRegisterDto.DtoValidator userForRegisterDtoValidator
    ) {
        this.userService = userService;
        this.authService = authService;
        this.userForRegisterDtoValidator = userForRegisterDtoValidator;
    }

    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<?> register(
            @RequestBody UserForRegisterDto userDto,
            BindingResult bindingResult
    ) throws UserNotFoundException {

        userForRegisterDtoValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.BAD_REQUEST,
                            bindingResult.getAllErrors().stream()
                                    .map(ObjectError::getCode)
                                    .collect(Collectors.joining("; "))),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            long id = userService.register(userDto);
            UserInfoDto userInfoDto = userService.findById(id);

            authService.authenticate(userDto.getEmail(), userDto.getPassword());

            return new ResponseEntity<>(
                    userInfoDto,
                    HttpStatus.OK
            );


        } catch (UniqueEmailException e) {
            return new ResponseEntity<>(
                    new OperationStatusDto(HttpStatus.FORBIDDEN, "User with some email already exist"),
                    HttpStatus.FORBIDDEN
            );
        }

    }

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<?> login(
            @RequestBody UserForLoginDto userForLoginDto
    ) {
        try {
            long id = userService.login(userForLoginDto);
            authService.authenticate(userForLoginDto.getEmail(), userForLoginDto.getPassword());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, "/api/v1/users/" + id);

            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.OK,
                            "success"
                    ),
                    headers,
                    HttpStatus.OK
            );

        } catch (IncorrectLoginOrPasswordException e) {
            return new ResponseEntity<Object>(
                    new OperationStatusDto(
                            HttpStatus.UNAUTHORIZED,
                            "Incorrect login or password"
                    ),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}
