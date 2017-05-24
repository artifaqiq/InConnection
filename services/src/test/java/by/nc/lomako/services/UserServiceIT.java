/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.user.*;
import by.nc.lomako.exceptions.IncorrectLoginOrPasswordException;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.services.utils.DaoTestsHelper;
import by.nc.lomako.utils.PasswordCryptographyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:dao-context.test.xml")
@ActiveProfiles("test")
@Transactional
public class UserServiceIT {

    private static final String FIRST_USER_EMAIL = "T_USER_EMAIL";
    private static final String FIRST_USER_FIRST_NAME = "T_USER_FIRST_NAME";
    private static final String FIRST_USER_LAST_NAME = "T_USER_LAST_NAME";
    private static final String FIRST_USER_PASSWORD = "T_USER_PASSWORD";

    private static final String SECOND_USER_EMAIL = "T_USER_2_EMAIL";
    private static final String SECOND_USER_FIRST_NAME = "T_USER_2_FIRST_NAME";
    private static final String SECOND_USER_LAST_NAME = "T_USER_2_LAST_NAME";
    private static final String SECOND_USER_PASSWORD = "T_USER_2_PASSWORD";

    private static final String INCORRECT_USER_EMAIL = "T_BAD_EMAIL";
    private static final String INCORRECT_USER_PASSWORD = "T_BAD_PASSWORD";
    private static final long INCORRECT_USER_ID = 9999L;

    private static final String USER_NEW_EMAIL = "T_USER_NEW_EMAIL";
    private static final String USER_NEW_FIRST_NAME = "T_USER_NEW_FIRST_NAME";
    private static final String USER_NEW_LAST_NAME = "T_USER_NEW_LAST_NAME";
    private static final String USER_NEW_PASSWORD = "T_USER_NEW_PASSWORD";

    @Autowired
    private UserService userService;
    @Autowired
    private DaoTestsHelper daoTestsHelper;

    @Before
    public void setUp() throws Exception {
        daoTestsHelper.persistRoles();
    }

    @After
    public void tearDown() throws Exception {
        List<UserInfoDto> users = userService.findAll();
        for (UserInfoDto user : users) {
            userService.deleteById(user.getId());
        }

        daoTestsHelper.dropRoles();
    }

    @Test
    public void register() throws Exception {
        UserForRegisterDto userDto = buildFirstUserForRegisterDto();
        long id = userService.register(userDto);

        UserInfoDto findUserDto = userService.findByEmail(FIRST_USER_EMAIL);
        assertThat(
                id,
                equalTo(findUserDto.getId())
        );
        assertThat(
                FIRST_USER_EMAIL,
                equalTo(findUserDto.getEmail())
        );
        assertThat(
                PasswordCryptographyUtil.checkPassword(FIRST_USER_PASSWORD, findUserDto.getEncryptedPassword()),
                is(true)
        );

    }

    @Test(expected = UniqueEmailException.class)
    public void registerWithExistEmail() throws Exception {
        UserForRegisterDto firstUserDto = buildFirstUserForRegisterDto();
        UserForRegisterDto secondUserDto = buildFirstUserForRegisterDto();

        userService.register(firstUserDto);
        userService.register(secondUserDto);
    }

    @Test
    public void login() throws Exception {
        UserForRegisterDto userDto = buildFirstUserForRegisterDto();
        long registerId = userService.register(userDto);

        UserForLoginDto userForLoginDto = buildCorrectUserForLoginDto();
        long loginId = userService.login(userForLoginDto);

        assertThat(
                registerId,
                equalTo(loginId)
        );
    }

    @Test(expected = IncorrectLoginOrPasswordException.class)
    public void loginWithIncorrectPassword() throws Exception {
        UserForRegisterDto userDto = buildFirstUserForRegisterDto();
        userService.register(userDto);

        UserForLoginDto userForLoginDto = buildUserForLoginDtoWithIncorrectPassword();
        userService.login(userForLoginDto);
    }

    @Test(expected = IncorrectLoginOrPasswordException.class)
    public void loginWithIncorrectEmail() throws Exception {
        UserForRegisterDto userDto = buildFirstUserForRegisterDto();
        userService.register(userDto);

        UserForLoginDto userForLoginDto = buildUserForLoginDtoWithIncorrectEmail();
        userService.login(userForLoginDto);
    }

    @Test
    public void findByEmail() throws Exception {
        UserForRegisterDto firstUserDto = buildFirstUserForRegisterDto();
        long id = userService.register(firstUserDto);

        UserInfoDto userInfoDto = userService.findByEmail(FIRST_USER_EMAIL);
        assertThat(
                id,
                equalTo(userInfoDto.getId())
        );

        assertThat(
                FIRST_USER_EMAIL,
                equalTo(userInfoDto.getEmail())
        );
    }

    @Test(expected = UserNotFoundException.class)
    public void findByIncorrectEmail() throws Exception {
        UserForRegisterDto firstUserDto = buildFirstUserForRegisterDto();
        userService.register(firstUserDto);

        userService.findByEmail(INCORRECT_USER_EMAIL);
    }

    @Test
    public void findById() throws Exception {
        UserForRegisterDto userForRegisterDto = buildFirstUserForRegisterDto();
        long id = userService.register(userForRegisterDto);

        UserInfoDto userDto = userService.findById(id);
        assertThat(
                id,
                equalTo(userDto.getId())
        );

        assertThat(
                FIRST_USER_EMAIL,
                equalTo(userDto.getEmail())
        );

    }

    @Test(expected = UserNotFoundException.class)
    public void findByIncorrectId() throws Exception {
        userService.findById(INCORRECT_USER_ID);
    }

    @Test
    public void findAll() throws Exception {
        assertThat(
                userService.findAll().isEmpty(),
                equalTo(true)
        );

        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        userService.register(firstUserForRegisterDto);

        UserForRegisterDto secondUserForRegisterDto = buildSecondUserForRegisterDto();
        userService.register(secondUserForRegisterDto);

        List<UserInfoDto> users = userService.findAll();
        assertThat(
                users.get(0).getEmail(),
                equalTo(FIRST_USER_EMAIL)
        );
        assertThat(
                users.get(1).getFirstName(),
                equalTo(SECOND_USER_FIRST_NAME)
        );

    }

    @Test
    public void findPage() throws Exception {
        assertThat(
                userService.findAll(0, 2).isEmpty(),
                equalTo(true)
        );

        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        userService.register(firstUserForRegisterDto);

        UserForRegisterDto secondUserForRegisterDto = buildSecondUserForRegisterDto();
        userService.register(secondUserForRegisterDto);

        List<UserInfoDto> users = userService.findAll(0, 2);
        assertThat(
                users.get(0).getEmail(),
                equalTo(FIRST_USER_EMAIL)
        );
        assertThat(
                users.get(1).getFirstName(),
                equalTo(SECOND_USER_FIRST_NAME)
        );

    }

    @Test
    public void findByRole() throws Exception {
        assertThat(
                userService.findByRole(RoleType.USER, 0, 2).isEmpty(),
                equalTo(true)
        );

        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        userService.register(firstUserForRegisterDto);

        UserForRegisterDto secondUserForRegisterDto = buildSecondUserForRegisterDto();
        userService.register(secondUserForRegisterDto);

        List<UserInfoDto> users = userService.findByRole(RoleType.USER, 0, 2);
        assertThat(
                users.get(0).getEmail(),
                equalTo(FIRST_USER_EMAIL)
        );
        assertThat(
                users.get(1).getFirstName(),
                equalTo(SECOND_USER_FIRST_NAME)
        );
    }

    @Test
    public void update() throws Exception {
        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        long id = userService.register(firstUserForRegisterDto);

        UserForUpdateDto userForUpdateDto = buildUserForUpdateDto();
        userService.update(id, userForUpdateDto);

        userService.login(new UserForLoginDto(USER_NEW_EMAIL, FIRST_USER_PASSWORD));
    }

    @Test
    public void create() throws Exception {
        UserForCreateDto userForCreateDto = buildUserForCreateDto();
        long idExpected = userService.create(userForCreateDto);

        long idActual = userService.login(new UserForLoginDto(USER_NEW_EMAIL, USER_NEW_PASSWORD));

        assertThat(
                idExpected,
                equalTo(idActual)
        );
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteById() throws Exception {
        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        long id = userService.register(firstUserForRegisterDto);

        userService.deleteById(id);
        userService.findById(id);
    }

    @Test
    public void setRoles() throws Exception {
        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        long id = userService.register(firstUserForRegisterDto);

        RoleTypesSetDto newRoles = buildRoleSetWithAllRoles();
        userService.setRoles(id, newRoles);

        UserInfoDto userInfoDto = userService.findById(id);
        Set<RoleType> expectedRoleTypes = userInfoDto.getRoles();

        assertThat(
                newRoles.getRoleTypes(),
                equalTo(expectedRoleTypes)
        );

    }

    @Test
    public void isEmailExist() throws Exception {
        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        userService.register(firstUserForRegisterDto);

        assertThat(
                userService.isEmailExist(FIRST_USER_EMAIL),
                equalTo(true)
        );

        assertThat(
                userService.isEmailExist(INCORRECT_USER_EMAIL),
                equalTo(false)
        );

    }

    @Test
    public void count() throws Exception {
        assertThat(
                userService.count(),
                equalTo(0L)
        );

        UserForRegisterDto firstUserForRegisterDto = buildFirstUserForRegisterDto();
        userService.register(firstUserForRegisterDto);

        UserForRegisterDto secondUserForRegisterDto = buildSecondUserForRegisterDto();
        userService.register(secondUserForRegisterDto);

        assertThat(
                userService.count(),
                equalTo(2L)
        );

    }

    private UserForRegisterDto buildSecondUserForRegisterDto() {
        UserForRegisterDto userDto = new UserForRegisterDto();
        userDto.setEmail(SECOND_USER_EMAIL);
        userDto.setFirstName(SECOND_USER_FIRST_NAME);
        userDto.setLastName(SECOND_USER_LAST_NAME);
        userDto.setPassword(SECOND_USER_PASSWORD);

        return userDto;
    }

    private UserForRegisterDto buildFirstUserForRegisterDto() {
        UserForRegisterDto userDto = new UserForRegisterDto();
        userDto.setEmail(FIRST_USER_EMAIL);
        userDto.setFirstName(FIRST_USER_FIRST_NAME);
        userDto.setLastName(FIRST_USER_LAST_NAME);
        userDto.setPassword(FIRST_USER_PASSWORD);

        return userDto;
    }

    private UserForLoginDto buildCorrectUserForLoginDto() {
        UserForLoginDto userDto = new UserForLoginDto();
        userDto.setEmail(FIRST_USER_EMAIL);
        userDto.setPassword(FIRST_USER_PASSWORD);
        return userDto;
    }

    private UserForLoginDto buildUserForLoginDtoWithIncorrectEmail() {
        UserForLoginDto userDto = new UserForLoginDto();
        userDto.setEmail(INCORRECT_USER_EMAIL);
        userDto.setPassword(FIRST_USER_PASSWORD);
        return userDto;
    }

    private UserForLoginDto buildUserForLoginDtoWithIncorrectPassword() {
        UserForLoginDto userDto = new UserForLoginDto();
        userDto.setEmail(FIRST_USER_EMAIL);
        userDto.setPassword(INCORRECT_USER_PASSWORD);
        return userDto;
    }

    private UserForUpdateDto buildUserForUpdateDto() {
        UserForUpdateDto userDto = new UserForUpdateDto();
        userDto.setEmail(USER_NEW_EMAIL);
        userDto.setFirstName(USER_NEW_FIRST_NAME);
        userDto.setLastName(USER_NEW_LAST_NAME);

        return userDto;
    }

    private UserForCreateDto buildUserForCreateDto() {
        UserForCreateDto userForCreateDto = new UserForCreateDto();

        userForCreateDto.setEmail(USER_NEW_EMAIL);
        userForCreateDto.setFirstName(USER_NEW_FIRST_NAME);
        userForCreateDto.setLastName(USER_NEW_LAST_NAME);
        userForCreateDto.setPassword(USER_NEW_PASSWORD);

        return userForCreateDto;
    }

    private RoleTypesSetDto buildRoleSetWithAllRoles() {
        Set<RoleType> roleTypes = EnumSet.of(RoleType.ADMIN, RoleType.MODERATOR, RoleType.USER);
        RoleTypesSetDto roleTypesSetDto = new RoleTypesSetDto();
        roleTypesSetDto.setRoleTypes(roleTypes);

        return roleTypesSetDto;
    }

}
