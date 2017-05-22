/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.dto.user.UserForCreateDto;
import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.exceptions.PostNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.services.utils.DaoTestsHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:dao-context.test.xml")
public class PostServiceIT {

    @Autowired
    private PostService postService;

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
        List<PostInfoDto> posts = postService.findAll();
        for (PostInfoDto post : posts) {
            postService.delete(post.getId());
        }

        List<UserInfoDto> users = userService.findAll();
        for (UserInfoDto user : users) {
            userService.deleteById(user.getId());
        }

        daoTestsHelper.dropRoles();
    }

    @Test
    public void findById() throws Exception {

    }
}
