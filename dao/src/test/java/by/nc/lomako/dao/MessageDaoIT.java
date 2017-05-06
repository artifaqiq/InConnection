/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.Message;
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
public class MessageDaoIT {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Test
    public void save() {
        Iterator<User> iterator = userDao.findPage(0, 2).iterator();
        User userFrom = iterator.next();
        User userTo = iterator.next();

        Message message = new Message();
        message.setBody("My message");
        message.setUserFrom(userFrom);
        message.setUserTo(userTo);

        messageDao.save(message);
    }

    @Test
    public void findById() {
        assertNull(messageDao.findOne(12314142L));
    }

    @Test
    public void delete() {
        Message message = messageDao.findPage(0, 1).iterator().next();
        messageDao.delete(message);
    }

    @Test
    public void findAll() {
        assertNotNull(messageDao.findAll());
    }

    @Test
    public void count() {
        assertEquals(new Long(2),
                messageDao.count());
    }

    @Test
    public void exists() {
        assertFalse(messageDao.exists(1231414L));
    }
}
