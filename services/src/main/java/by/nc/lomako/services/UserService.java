/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.user.*;
import by.nc.lomako.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.RoleType;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface UserService {

    long register(final UserForRegisterDto userDto)
            throws UniqueEmailException;

    long login(final UserForLoginDto userDto) throws IncorrectLoginOrPasswordException;

    UserInfoDto findByEmail(String s) throws UserNotFoundException;

    UserInfoDto findById(long id) throws UserNotFoundException;

    List<UserInfoDto> findAll();

    List<UserInfoDto> findAll(int start, int limit);

    void update(final UserForUpdateDto userDto) throws UniqueEmailException;

    long create(final UserForCreateDto userDto) throws UniqueEmailException;

    List<UserInfoDto> findByRole(RoleType role, int start, int limit);

    void deleteById(long userId);

    void setRoles(long userId, RoleTypesSetDto roles);

    boolean isEmailExist(String email);

    long count();
}
