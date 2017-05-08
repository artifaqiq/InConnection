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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/dao-context.test.xml"
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private static final String EXIST_EMAIL = "pupkin@mail.ru";
    private static final String EXIST_PASSWORD = "12345678";
    private static final long EXIST_ID = 1;


    @Test
    public void registerTest() {
        UserForRegisterDto goodUserDto = new UserForRegisterDto();
        goodUserDto.setEmail(new Random().nextInt() + "@example.com");
        goodUserDto.setFirstName("T_" + new Random().nextInt() + "_Vasya");
        goodUserDto.setLastName("T_" + new Random().nextInt() + "_Pupkin");
        goodUserDto.setPassword("12345678");
        goodUserDto.setPasswordConfirmation("12345678");

        try {
            assertTrue(userService.register(goodUserDto) > 1L);
        } catch (MismatchPasswordsException | UniqueEmailException e) {
            fail();
        }

        UserForRegisterDto existEmailUserDto =
                new UserForRegisterDto();

        existEmailUserDto.setEmail(EXIST_EMAIL);
        existEmailUserDto.setFirstName("T_" + new Random().nextInt() + "_Vasya");
        existEmailUserDto.setLastName("T_" + new Random().nextInt() + "_Pupkin");
        existEmailUserDto.setPassword("12345678");
        existEmailUserDto.setPasswordConfirmation("12345678");

        try {
            userService.register(existEmailUserDto);
            fail();
        } catch (UniqueEmailException e) {
        } catch (MismatchPasswordsException e) {
            fail();
        }


        UserForRegisterDto mismatchPasswordsUserDto =
                new UserForRegisterDto();

        mismatchPasswordsUserDto.setEmail(new Random().nextInt() + "@example.com");
        mismatchPasswordsUserDto.setFirstName("T_" + new Random().nextInt() + "_Vasya");
        mismatchPasswordsUserDto.setLastName("T_" + new Random().nextInt() + "_Pupkin");
        mismatchPasswordsUserDto.setPassword("12345678");
        mismatchPasswordsUserDto.setPasswordConfirmation("123451378");
        try {
            userService.register(mismatchPasswordsUserDto);
            fail();
        } catch (UniqueEmailException e) {
            fail();
        } catch (MismatchPasswordsException e) {
        }
    }

    @Test
    public void loginTest() throws IncorrectLoginOrPasswordException {
        UserForLoginDto goodUserForLoginDto = new UserForLoginDto(
                EXIST_EMAIL,
                EXIST_PASSWORD
        );

        assertEquals(EXIST_ID, userService.login(goodUserForLoginDto));

        UserForLoginDto badUserForLoginDto = new UserForLoginDto(
                "22525225@eeds.ry",
                "123414151"
        );

        try {
            userService.login(badUserForLoginDto);
            fail();
        } catch (IncorrectLoginOrPasswordException e) {
        }
    }

    @Test(expected = UserEmailNotFoundException.class)
    public void findByEmail() throws UserEmailNotFoundException {
        UserInfoDto userInfoDto = userService.findByEmail("artifaqiq@gmail.com");
        assertEquals("Artur", userInfoDto.getFirstName());

        userService.findByEmail("does't_exist@email.com");


    }


}
