/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.FriendRequest;
import by.nc.lomako.pojos.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Iterator;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.test.xml")
@Transactional
public class FriendRequestDaoIT {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendRequestDao friendRequestDao;

    @Test
    public void save() {

        Iterator<User> iterator = userDao.findPage(0, 2).iterator();

        User userFrom = iterator.next();
        User userTo = iterator.next();

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUserFrom(userFrom);
        friendRequest.setUserTo(userTo);

        friendRequestDao.save(friendRequest);
    }

    @Test
    public void findById() {
        assertNull(friendRequestDao.findOne(12314142L));
    }

    @Test
    public void delete() {
        FriendRequest friendRequest = friendRequestDao.findPage(0, 1).iterator().next();
        friendRequestDao.delete(friendRequest);
    }

    @Test
    public void findAll() {
        assertNotNull(friendRequestDao.findAll());
    }

    @Test
    public void count() {
        assertEquals(new Long(2), friendRequestDao.count());
    }

    @Test
    public void exists() {
        assertFalse(friendRequestDao.exists(1231414L));
    }
}
