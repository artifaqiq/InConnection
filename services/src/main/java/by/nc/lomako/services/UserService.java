/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.UserForLoginDto;
import by.nc.lomako.dto.UserForRegisterDto;
import by.nc.lomako.dto.UserInfoDto;
import by.nc.lomako.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.exceptions.MismatchPasswordsException;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.exceptions.UserEmailNotFoundException;

import javax.validation.Valid;

/**
 * @author Lomako
 * @version 1.0
 */
public interface UserService {
    long register(@Valid UserForRegisterDto userDto)
            throws UniqueEmailException, MismatchPasswordsException;


    long login(UserForLoginDto userDto) throws IncorrectLoginOrPasswordException;

    UserInfoDto findByEmail(String s) throws UserEmailNotFoundException;
}
