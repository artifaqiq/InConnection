/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.Post;
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
public class PostDaoIT {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Test
    public void save() {

        User user = userDao.findPage(0, 1).iterator().next();

        Post post = new Post();
        post.setBody("My post");
        post.setUser(user);

        postDao.save(post);

    }

    @Test
    public void findById() {
        assertNull(postDao.findOne(12314142L));
    }

    @Test
    public void delete() {
        Post post = postDao.findPage(0, 1).iterator().next();
        postDao.delete(post);
    }

    @Test
    public void findAll() {
        assertNotNull(postDao.findAll());
    }

    @Test
    public void count() {
        assertEquals(new Long(3), postDao.count());
    }

    @Test
    public void exists() {
        assertFalse(postDao.exists(1231414L));
    }
}
