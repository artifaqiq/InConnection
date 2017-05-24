/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services.impl;

import by.nc.lomako.dao.RoleDao;
import by.nc.lomako.dao.UserDao;
import by.nc.lomako.dto.user.*;
import by.nc.lomako.pojos.Role;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.pojos.User;
import by.nc.lomako.services.UserService;
import by.nc.lomako.services.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.services.exceptions.UniqueEmailException;
import by.nc.lomako.services.exceptions.UserNotFoundException;
import by.nc.lomako.utils.PasswordCryptographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author Lomako
 * @version 1.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public long register(final UserForRegisterDto userDto)
            throws UniqueEmailException {

        if (userDao.findByEmail(userDto.getEmail()) != null) {
            throw new UniqueEmailException();
        }

        User user = new User();

        Set<Role> roles = new HashSet<>();
        Role role = roleDao.findByName(RoleType.USER);
        roles.add(role);

        user.setRoles(roles);
        user.setEmail(userDto.getEmail());
        user.setEncryptedPassword(
                PasswordCryptographyUtil.encryptPassword(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        user = userDao.save(user);

        user.getRoles().add(role);

        return user.getId();
    }

    @Override
    public long login(final UserForLoginDto userDto) throws IncorrectLoginOrPasswordException {
        User user = userDao.findByEmail(userDto.getEmail());

        if (user == null) {
            throw new IncorrectLoginOrPasswordException();
        } else if (!PasswordCryptographyUtil.checkPassword(
                userDto.getPassword(), user.getEncryptedPassword())) {
            throw new IncorrectLoginOrPasswordException();

        } else {
            return user.getId();
        }

    }

    @Override
    public UserInfoDto findByEmail(String email) throws UserNotFoundException {
        User user = userDao.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException();
        } else {

            return new UserInfoDto(
                    user.getId(),
                    user.getEmail(),
                    user.getEncryptedPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCreatedDate(),
                    user.getUpdatedDate(),
                    user.getRoles().stream()
                            .map(Role::getRoleType)
                            .collect(Collectors.toSet())
            );
        }
    }

    @Override
    public UserInfoDto findById(long id) throws UserNotFoundException {
        User user = userDao.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getEncryptedPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedDate(),
                user.getUpdatedDate(),
                user.getRoles().stream()
                        .map(Role::getRoleType)
                        .collect(Collectors.toSet())
        );

    }

    @Override
    public List<UserInfoDto> findAll() {
        List<UserInfoDto> dtos = new LinkedList<>();
        Iterable<User> users = userDao.findAll();

        for (User user : users) {
            dtos.add(new UserInfoDto(
                    user.getId(),
                    user.getEmail(),
                    user.getEncryptedPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCreatedDate(),
                    user.getUpdatedDate(),
                    user.getRoles().stream()
                            .map(Role::getRoleType)
                            .collect(Collectors.toSet())
            ));
        }

        return dtos;

    }

    @Override
    public List<UserInfoDto> findAll(int start, int limit) {
        List<UserInfoDto> dtos = new LinkedList<>();
        Iterable<User> users = userDao.findPage(start, limit);

        for (User user : users) {
            dtos.add(new UserInfoDto(
                    user.getId(),
                    user.getEmail(),
                    user.getEncryptedPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCreatedDate(),
                    user.getUpdatedDate(),
                    user.getRoles().stream()
                            .map(Role::getRoleType)
                            .collect(Collectors.toSet())
            ));
        }

        return dtos;
    }

    @Override
    public void update(final long userId, final UserForUpdateDto userDto) throws UniqueEmailException, UserNotFoundException {

        User userWithSomeEmail = userDao.findByEmail(userDto.getEmail());
        if (userWithSomeEmail != null && userWithSomeEmail.getId() != userId) {
            throw new UniqueEmailException();
        }

        User user = userDao.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        userDao.save(user);
    }

    @Override
    public long create(final UserForCreateDto userDto) throws UniqueEmailException {
        if (userDao.findByEmail(userDto.getEmail()) != null) {
            throw new UniqueEmailException();
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEncryptedPassword(
                PasswordCryptographyUtil.encryptPassword(userDto.getPassword())
        );

        Set<Role> roles = new HashSet<>();
        Role role = roleDao.findByName(RoleType.USER);
        roles.add(role);

        user.setRoles(roles);

        user = userDao.save(user);
        return user.getId();
    }

    @Override
    public List<UserInfoDto> findByRole(RoleType role, int start, int limit) {
        List<UserInfoDto> dtos = new LinkedList<>();
        Iterable<User> users = userDao.findByRole(role, start, limit);

        for (User user : users) {
            dtos.add(new UserInfoDto(
                    user.getId(),
                    user.getEmail(),
                    user.getEncryptedPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCreatedDate(),
                    user.getUpdatedDate(),
                    user.getRoles().stream()
                            .map(Role::getRoleType)
                            .collect(Collectors.toSet())
            ));
        }

        return dtos;
    }

    @Override
    public void deleteById(long id) throws UserNotFoundException {
        User user = userDao.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userDao.delete(user);
    }

    @Override
    public void setRoles(long userId, RoleTypesSetDto roles) throws UserNotFoundException {
        User user = userDao.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        Set<RoleType> oldRoles = user.getRoles().stream()
                .map(Role::getRoleType)
                .collect(Collectors.toSet());
        Set<RoleType> newRoles = roles.getRoleTypes();

        RoleType[] allRoleTypes = RoleType.values();
        for (RoleType roleType : allRoleTypes) {
            if (oldRoles.contains(roleType) && !newRoles.contains(roleType)) {
                user.getRoles().remove(roleDao.findByName(roleType));
            } else if (!oldRoles.contains(roleType) && newRoles.contains(roleType)) {
                user.getRoles().add(roleDao.findByName(roleType));
            }
        }
    }

    @Override
    public boolean isEmailExist(String email) {
        return userDao.findByEmail(email) != null;
    }

    @Override
    public long count() {
        return userDao.count();
    }
}
