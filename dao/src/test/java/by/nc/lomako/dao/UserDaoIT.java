/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.test.xml")
@Transactional
public class UserDaoIT {
    @Autowired
    private UserDao userDao;

    private User testUser;

    @Before
    public void initTestPojos() {
        testUser = new User();
        testUser.setEmail(new Random().nextInt() + "test@example.com");
        testUser.setEncryptedPassword("24f11213aabcd");
        testUser.setFirstName("John");
        testUser.setLastName("Smith");
    }

    @Test
    public void save() {
        userDao.save(testUser);
    }

    @Test
    public void findById() {
        assertNull(userDao.findOne(12314142L));
    }

    @Test
    public void delete() {
        User user = userDao.findPage(0, 1).iterator().next();
        userDao.delete(user);
    }

    @Test
    public void findAll() {
        assertNotNull(userDao.findAll());
    }

    @Test
    public void count() {
        System.out.println(userDao.count());
    }

    @Test
    public void exists() {
        assertFalse(userDao.exists(1231414L));
    }

    @Test
    public void findByEmail() {
        assertNull(userDao.findByEmail("23452345@example.com"));
    }
}
