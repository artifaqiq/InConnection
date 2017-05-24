/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostForUpdateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.dto.user.UserForRegisterDto;
import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.services.exceptions.PostNotFoundException;
import by.nc.lomako.services.exceptions.UserNotFoundException;
import by.nc.lomako.services.utils.DaoTestsHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
public class PostServiceIT {

    private static final String FIRST_USER_EMAIL = "T_FIRST_USER_EMAIL";
    private static final String SECOND_USER_EMAIL = "T_SECOND_USER_EMAIL";

    private static final String USER_PASSWORD = "T_USER_PASSWORD";
    private static final String USER_FIRST_NAME = "T_USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "T_USER_LAST_NAME";

    private static final String FIRST_POST_BODY = "T_FIRST_POST_BODY";
    private static final String SECOND_POST_BODY = "T_SECOND_POST_BODY";
    private static final String THIRD_POST_BODY = "T_THIRD_POST_BODY";

    private static final long NOT_EXIST_ID = 9999L;
    private static final String NEW_POST_BODY = "T_NEW_POST_BODY";

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
            postService.deleteById(post.getId());
        }

        List<UserInfoDto> users = userService.findAll();
        for (UserInfoDto user : users) {
            userService.deleteById(user.getId());
        }

        daoTestsHelper.dropRoles();
    }

    @Test
    public void findById() throws Exception {
        long userId = userService.register(buildUser(FIRST_USER_EMAIL));
        long postId = postService.create(userId, buildPostForCreateDto(FIRST_POST_BODY));

        assertThat(
                postService.findById(postId).getBody(),
                equalTo(FIRST_POST_BODY)
        );
    }

    @Test(expected = PostNotFoundException.class)
    public void findByNotExistId() throws Exception {
        postService.findById(NOT_EXIST_ID);
    }

    @Test
    public void updatePost() throws Exception {
        long firstUserId = userService.register(buildUser(FIRST_USER_EMAIL));
        long secondUserId = userService.register(buildUser(SECOND_USER_EMAIL));
        long postId = postService.create(firstUserId, buildPostForCreateDto(FIRST_POST_BODY));

        postService.update(postId, buildPostForUpdateDto(secondUserId));
        PostInfoDto byId = postService.findById(postId);

        assertThat(
                byId.getBody(),
                equalTo(NEW_POST_BODY)
        );

        assertThat(
                byId.getUserId(),
                equalTo(secondUserId)
        );
    }

    @Test(expected = UserNotFoundException.class)
    public void updateWithNotExistUser() throws Exception {
        long firstUserId = userService.register(buildUser(FIRST_USER_EMAIL));
        long postId = postService.create(firstUserId, buildPostForCreateDto(FIRST_POST_BODY));

        postService.update(postId, buildPostForUpdateDto(NOT_EXIST_ID));
    }

    @Test(expected = PostNotFoundException.class)
    public void updateNotExistPost() throws Exception {
        long firstUserId = userService.register(buildUser(FIRST_USER_EMAIL));
        postService.update(NOT_EXIST_ID, buildPostForUpdateDto(firstUserId));
    }

    @Test
    public void createPost() throws Exception {
        long userId = userService.register(buildUser(FIRST_USER_EMAIL));
        long postId = postService.create(userId, buildPostForCreateDto(FIRST_POST_BODY));

        assertThat(
                postService.findById(postId).getBody(),
                equalTo(FIRST_POST_BODY)
        );
    }

    @Test(expected = UserNotFoundException.class)
    public void createPostWithNotExistUser() throws Exception {
        postService.create(NOT_EXIST_ID, buildPostForCreateDto(FIRST_POST_BODY));
    }

    @Test
    public void delete() throws Exception {
        long userId = userService.register(buildUser(FIRST_USER_EMAIL));
        long postId = postService.create(userId, buildPostForCreateDto(FIRST_POST_BODY));

        postService.deleteById(postId);

        assertThat(
                postService.findAll().size(),
                equalTo(0)
        );
    }

    @Test(expected = PostNotFoundException.class)
    public void deleteNoExistPost() throws Exception {
        postService.deleteById(NOT_EXIST_ID);
    }

    @Test
    public void findLastByUser() throws Exception {
        long userId = userService.register(buildUser(FIRST_USER_EMAIL));
        long firstPostId = postService.create(userId, buildPostForCreateDto(FIRST_POST_BODY));
        Thread.sleep(1001);
        long secondPostId = postService.create(userId, buildPostForCreateDto(SECOND_POST_BODY));
        Thread.sleep(1001);
        long thirdPostId = postService.create(userId, buildPostForCreateDto(THIRD_POST_BODY));

        List<PostInfoDto> posts = postService.findLastByUser(userId, 0, 4);
        assertThat(
                posts.size(),
                equalTo(3)
        );
        assertThat(
                posts.get(2).getBody(),
                equalTo(FIRST_POST_BODY)
        );
        assertThat(
                posts.get(0).getId(),
                equalTo(thirdPostId)
        );
    }

    @Test(expected = UserNotFoundException.class)
    public void findLastByNotExistUser() throws Exception {
        postService.findLastByUser(NOT_EXIST_ID, 0, 5);
    }

    @Test
    public void countByUser() throws Exception {
        long userId = userService.register(buildUser(FIRST_USER_EMAIL));
        long firstPostId = postService.create(userId, buildPostForCreateDto(FIRST_POST_BODY));
        Thread.sleep(1001);
        long secondPostId = postService.create(userId, buildPostForCreateDto(SECOND_POST_BODY));
        Thread.sleep(1001);
        long thirdPostId = postService.create(userId, buildPostForCreateDto(THIRD_POST_BODY));

        long count = postService.countByUser(userId);

        assertThat(
                count,
                equalTo(3L)
        );
    }

    @Test(expected = UserNotFoundException.class)
    public void countByNotExistUser() throws Exception {
        postService.countByUser(NOT_EXIST_ID);
    }

    @Test
    public void findAll() throws Exception {

        long firstUserId = userService.register(buildUser(FIRST_USER_EMAIL));
        long secondUserId = userService.register(buildUser(SECOND_USER_EMAIL));
        long firstPostId = postService.create(firstUserId, buildPostForCreateDto(FIRST_POST_BODY));
        Thread.sleep(1001);
        long secondPostId = postService.create(firstUserId, buildPostForCreateDto(SECOND_POST_BODY));
        Thread.sleep(1001);
        long thirdPostId = postService.create(secondUserId, buildPostForCreateDto(THIRD_POST_BODY));

        List<PostInfoDto> posts = postService.findAll();
        assertThat(
                posts.size(),
                equalTo(3)
        );

        assertThat(
                posts.stream()
                        .map(PostInfoDto::getBody)
                        .filter(body -> body.equals(SECOND_POST_BODY))
                        .collect(Collectors.toList())
                        .size(),
                equalTo(1)
        );

        assertThat(
                posts.stream()
                        .map(PostInfoDto::getUserId)
                        .filter(userId -> userId == firstUserId)
                        .collect(Collectors.toList())
                        .size(),
                equalTo(2)
        );
    }

    private PostForUpdateDto buildPostForUpdateDto(long userId) {
        PostForUpdateDto postForUpdateDto = new PostForUpdateDto();
        postForUpdateDto.setBody(NEW_POST_BODY);
        postForUpdateDto.setUserId(userId);

        return postForUpdateDto;
    }

    private PostForCreateDto buildPostForCreateDto(String body) {
        PostForCreateDto postForCreateDto = new PostForCreateDto();
        postForCreateDto.setBody(body);
        return postForCreateDto;
    }

    private UserForRegisterDto buildUser(String email) {
        UserForRegisterDto userDto = new UserForRegisterDto();
        userDto.setEmail(email);
        userDto.setPassword(USER_PASSWORD);
        userDto.setFirstName(USER_FIRST_NAME);
        userDto.setLastName(USER_LAST_NAME);
        return userDto;
    }
}
