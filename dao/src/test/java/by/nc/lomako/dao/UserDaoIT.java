/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.dao.builders.UserBuilder;
import by.nc.lomako.dao.utils.DaoTestsHelper;
import by.nc.lomako.pojos.Role;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.pojos.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:dao-context.test.xml")
@ActiveProfiles("test")
@Transactional
public class UserDaoIT {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DaoTestsHelper daoTestsHelper;

    private static final String USER_1_EMAIL = "test_user_1@example.com";
    private static final String USER_1_LAST_NAME = "T_USER_1_LAST_NAME";
    private static final String USER_1_FIRST_NAME = "T_USER_1_FIRST_NAME";
    private static final String USER_1_ENCRYPTED_PASSWORD = "T_USER_1_ENC_PASSWORD";

    private static final String USER_2_EMAIL = "test_user_2@example.com";
    private static final String USER_2_LAST_NAME = "T_USER_2_LAST_NAME";
    private static final String USER_2_FIRST_NAME = "T_USER_2_FIRST_NAME";
    private static final String USER_2_ENCRYPTED_PASSWORD = "T_USER_2_ENC_PASSWORD";

    private static final Long NOT_EXIST_USER_ID = 9999L;
    private static final String NOT_EXIST_EMAIL = "9999@example.com";

    @After
    public void tearDown() throws Exception {
        List<User> users = userDao.findAll();

        for (User user : users) {
            userDao.delete(user);
        }
    }

    @Test
    public void save() throws Exception {

        User userPersisted = userDao.save(new UserBuilder().build());
        User userLoaded = userDao.findOne(userPersisted.getId());

        assertThat(
                userPersisted,
                equalTo(userLoaded)
        );

    }

    @Test
    public void findOne() throws Exception {
        User userPersisted = userDao.save(new UserBuilder().build());

        assertThat(
                userDao.findOne(userPersisted.getId()),
                equalTo(userPersisted)
        );

        assertThat(
                userDao.findOne(NOT_EXIST_USER_ID),
                is(nullValue())
        );
    }

    @Test
    public void findAll() throws Exception {

        assertThat(
                0,
                equalTo(userDao.findAll().size())

        );

        User user1 = buildFirstUser();
        User user2 = buildSecondUser();

        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();

        long count1 = users.stream()
                .filter(user -> USER_1_EMAIL.equals(user.getEmail()))
                .count();

        long count2 = users.stream()
                .filter(user -> USER_2_ENCRYPTED_PASSWORD.equals(user.getEncryptedPassword()))
                .count();

        assertThat(
                1L,
                equalTo(count1)
        );

        assertThat(
                1L,
                equalTo(count2)
        );
    }

    @Test
    public void count() throws Exception {
        assertThat(
                0L,
                equalTo(userDao.count())
        );

        User user1 = buildFirstUser();
        User user2 = buildSecondUser();

        userDao.save(user1);
        userDao.save(user2);

        assertThat(
                2L,
                equalTo(userDao.count())
        );

        assertThat(
                (long) userDao.findAll().size(),
                equalTo(userDao.count())
        );

    }

    @Test
    public void delete() throws Exception {
        User user = buildFirstUser();
        user = userDao.save(user);
        long id = user.getId();

        assertThat(
                userDao.findOne(id),
                equalTo(user)
        );

        userDao.delete(user);

        assertThat(
                userDao.findOne(id),
                is(nullValue())
        );

    }

    @Test
    public void deleteById() throws Exception {
        User user = buildFirstUser();
        user = userDao.save(user);
        long id = user.getId();

        assertThat(
                userDao.findOne(id),
                equalTo(user)
        );

        userDao.deleteById(id);

        assertThat(
                userDao.count(),
                equalTo(0L)
        );
    }

    @Test
    public void exist() throws Exception {
        User user = buildFirstUser();
        user = userDao.save(user);
        long id = user.getId();

        assertThat(
                userDao.exist(id),
                equalTo(true)
        );

        assertThat(
                userDao.exist(NOT_EXIST_USER_ID),
                equalTo(false)
        );
    }

    @Test
    public void findPage() throws Exception {
        User user1 = buildFirstUser();
        User user2 = buildSecondUser();

        userDao.save(user1);
        userDao.save(user2);

        assertThat(
                userDao.findPage(0, 1).size(),
                equalTo(1)
        );
    }

    @Test
    public void findByEmail() throws Exception {
        User user = buildFirstUser();
        user = userDao.save(user);

        System.out.println("userDao = " + userDao.findByEmail(USER_1_EMAIL));

        assertThat(
                userDao.findByEmail(USER_1_EMAIL),
                equalTo(user)
        );

        assertThat(
                userDao.findByEmail(NOT_EXIST_EMAIL),
                nullValue()
        );
    }

    @Test
    public void findByRole() throws Exception {
        daoTestsHelper.persistRoles();

        User user = buildFirstUser();
        Role role1 = roleDao.findByName(RoleType.USER);
        Role role2 = roleDao.findByName(RoleType.MODERATOR);

        user = userDao.save(user);

        System.out.println("user.getRoles() = " + user.getRoles());

        user.getRoles().add(role1);
        user.getRoles().add(role2);

        assertThat(
                userDao.findByRole(RoleType.USER, 0, 2).contains(user),
                is(true)
        );
        assertThat(
                userDao.findByRole(RoleType.MODERATOR, 0, 2).contains(user),
                is(true)
        );
        assertThat(
                userDao.findByRole(RoleType.ADMIN, 0, 2).contains(user),
                is(false)
        );

        daoTestsHelper.dropRoles();
    }

    private User buildFirstUser() {
        return new UserBuilder()
                .email(USER_1_EMAIL)
                .firstName(USER_1_FIRST_NAME)
                .lastName(USER_1_LAST_NAME)
                .encryptedPassword(USER_1_ENCRYPTED_PASSWORD)
                .build();
    }

    private User buildSecondUser() {
        return new UserBuilder()
                .email(USER_2_EMAIL)
                .firstName(USER_2_FIRST_NAME)
                .lastName(USER_2_LAST_NAME)
                .encryptedPassword(USER_2_ENCRYPTED_PASSWORD)
                .build();
    }

}
