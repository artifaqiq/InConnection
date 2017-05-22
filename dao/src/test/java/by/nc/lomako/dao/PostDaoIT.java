/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.dao.builders.PostBuilder;
import by.nc.lomako.dao.builders.UserBuilder;
import by.nc.lomako.pojos.Post;
import by.nc.lomako.pojos.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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

    private static final String FIRST_POST_BODY = "T_POST_1_BODY";
    private static final String SECOND_POST_BODY = "T_POST_2_BODY";

    @After
    public void tearDown() throws Exception {
        List<Post> posts = postDao.findAll();
        List<User> users = userDao.findAll();

        for (Post post : posts) {
            postDao.delete(post);
        }

        for (User user : users) {
            userDao.delete(user);
        }
    }

    @Test
    public void findLastByUser() throws Exception {
        User user = buildUser();
        user = userDao.save(user);

        Post post1 = buildFirstPost(user);
        Post post2 = buildSecondPost(user);

        post1 = postDao.save(post1);

        Thread.sleep(1001);

        post2 = postDao.save(post2);

        List<Post> posts = postDao.findLastByUser(user.getId(), 0, 2);

        System.out.println("------ " + posts);

        assertThat(
                posts.get(0),
                equalTo(post2)
        );

        assertThat(
                posts.get(1),
                equalTo(post1)
        );

    }

    private Post buildFirstPost(User user) {
        return new PostBuilder()
                .user(user)
                .body(FIRST_POST_BODY)
                .build();
    }

    private Post buildSecondPost(User user) {
        return new PostBuilder()
                .user(user)
                .body(SECOND_POST_BODY)
                .build();
    }

    private User buildUser() {
        return new UserBuilder()
                .email("test_user@example.com")
                .firstName("T_USER_FIRST_NAME")
                .lastName("T_USER_LAST_NAME")
                .encryptedPassword("T_USER_ENC_PASSWORD")
                .build();
    }
}
