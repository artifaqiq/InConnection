/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.user.*;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.services.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.services.exceptions.UniqueEmailException;
import by.nc.lomako.services.exceptions.UserNotFoundException;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface UserService {

    long register(final UserForRegisterDto userDto)
            throws UniqueEmailException;

    long login(final UserForLoginDto userDto) throws IncorrectLoginOrPasswordException;

    UserInfoDto findByEmail(final String s) throws UserNotFoundException;

    UserInfoDto findById(final long id) throws UserNotFoundException;

    List<UserInfoDto> findAll();

    List<UserInfoDto> findAll(final int start, final int limit);

    void update(final long userId, final UserForUpdateDto userDto) throws UniqueEmailException, UserNotFoundException;

    long create(final UserForCreateDto userDto) throws UniqueEmailException;

    List<UserInfoDto> findByRole(final RoleType role, final int start, final int limit);

    void deleteById(final long userId) throws UserNotFoundException;

    void setRoles(final long userId, RoleTypesSetDto roles) throws UserNotFoundException;

    boolean isEmailExist(final String email);

    long count();
}
