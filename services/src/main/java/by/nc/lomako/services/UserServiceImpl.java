/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dao.AccessLevelDao;
import by.nc.lomako.dao.UserDao;
import by.nc.lomako.dto.UserForLoginDto;
import by.nc.lomako.dto.UserForRegisterDto;
import by.nc.lomako.dto.UserInfoDto;
import by.nc.lomako.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.exceptions.MismatchPasswordsException;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.exceptions.UserEmailNotFoundException;
import by.nc.lomako.pojos.AccessLevel;
import by.nc.lomako.pojos.AccessLevelType;
import by.nc.lomako.pojos.User;
import by.nc.lomako.utils.PasswordCryptographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Lomako
 * @version 1.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final AccessLevelDao accessLevelDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, AccessLevelDao accessLevelDao) {
        this.userDao = userDao;
        this.accessLevelDao = accessLevelDao;
    }

    @Override
    public long register(@Valid UserForRegisterDto userDto)
            throws UniqueEmailException, MismatchPasswordsException {

        if (userDao.findByEmail(userDto.getEmail()) != null) {
            throw new UniqueEmailException();
        }

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new MismatchPasswordsException();
        }

        User user = new User();

        Set<AccessLevel> accessLevels = new HashSet<AccessLevel>();
        AccessLevel accessLevel = accessLevelDao.findByName(AccessLevelType.USER);
        accessLevels.add(accessLevel);

        user.setAccessLevels(accessLevels);
        user.setEmail(userDto.getEmail());
        user.setEncryptedPassword(
                PasswordCryptographyUtil.encryptPassword(userDto.getEmail(), userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        user = userDao.save(user);

        user.getAccessLevels().add(accessLevel);

        return user.getId();
    }

    @Override
    public long login(UserForLoginDto userDto) throws IncorrectLoginOrPasswordException {
        User user = userDao.findByEmail(userDto.getEmail());

        if (user == null) {
            throw new IncorrectLoginOrPasswordException();
        } else if (!PasswordCryptographyUtil.checkPassword(
                userDto.getEmail(), userDto.getPassword(), user.getEncryptedPassword())) {
            throw new IncorrectLoginOrPasswordException();

        } else {
            return user.getId();
        }

    }

    @Override
    public UserInfoDto findByEmail(String email) throws UserEmailNotFoundException {
        User user = userDao.findByEmail(email);

        if (user == null) {
            throw new UserEmailNotFoundException();
        } else {

            return new UserInfoDto(
                    user.getId(),
                    user.getEmail(),
                    user.getEncryptedPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCreatedDate(),
                    user.getUpdatedDate(),
                    user.getAccessLevels()
            );
        }
    }
}
