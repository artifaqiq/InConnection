/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.Avatar;
import by.nc.lomako.pojos.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.test.xml")
@Transactional
public class AvatarDaoIT {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AvatarDao avatarDao;

    @Test
    public void save() {

        User user = userDao.findPage(0, 1).iterator().next();

        Avatar avatar = new Avatar();
        avatar.setUrl("url");
        avatar.setUser(user);

        avatarDao.save(avatar);

    }

    @Test
    public void findById() {
        assertNull(avatarDao.findOne(12314142L));
    }

    @Test
    public void delete() {
        Avatar avatar = avatarDao.findPage(0, 1).iterator().next();
        avatarDao.delete(avatar);
    }

    @Test
    public void findAll() {
        assertNotNull(avatarDao.findAll());
    }

    @Test
    public void count() {
        assertEquals(new Long(4), avatarDao.count());
    }

    @Test
    public void exists() {
        assertFalse(avatarDao.exists(1231414L));
    }
}
