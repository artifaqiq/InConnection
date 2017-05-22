/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services.impl;

import by.nc.lomako.dao.PostDao;
import by.nc.lomako.dao.UserDao;
import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostForUpdateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.Post;
import by.nc.lomako.pojos.User;
import by.nc.lomako.exceptions.PostNotFoundException;
import by.nc.lomako.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostDao postDao;
    private UserDao userDao;

    @Autowired
    public PostServiceImpl(PostDao postDao, UserDao userDao) {
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @Override
    public long create(PostForCreateDto postDto, long userId) throws UserNotFoundException {
        User user = userDao.findOne(userId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        Post post = new Post();
        post.setUser(user);
        post.setBody(postDto.getBody());

        post = postDao.save(post);
        return post.getId();
    }

    @Override
    public void delete(long id) throws PostNotFoundException {
        Post post = postDao.findOne(id);

        if (post == null) {
            throw new PostNotFoundException();
        }

        postDao.delete(post);
    }

    @Override
    public List<PostInfoDto> findByUser(long userId, int start, int limit) {
        List<PostInfoDto> posts = new LinkedList<>();

        for (Post post : postDao.findLastByUser(userId, start, limit)) {
            posts.add(new PostInfoDto(post.getId(), post.getBody(), post.getUser().getId()));
        }

        return posts;
    }

    @Override
    public PostInfoDto findById(long id) throws PostNotFoundException {
        Post post = postDao.findOne(id);
        if (post == null) {
            throw new PostNotFoundException();
        }

        PostInfoDto postInfoDto = new PostInfoDto();
        postInfoDto.setId(post.getId());
        postInfoDto.setBody(post.getBody());
        postInfoDto.setUserId(post.getUser().getId());

        return postInfoDto;
    }

    @Override
    public void update(long id, PostForUpdateDto postDto) throws PostNotFoundException, UserNotFoundException {
        Post post = postDao.findOne(id);

        post.setUser(userDao.findOne(postDto.getUserId()));
        post.setBody(postDto.getBody());
    }

    @Override
    public long countByUser(long userId) {
        return postDao.countByUser(userId);
    }

    @Override
    public List<PostInfoDto> findAll() {
        List<Post> posts = postDao.findAll();
        LinkedList<PostInfoDto> postDtos = new LinkedList<>();
        for (Post post : posts) {
            PostInfoDto postInfoDto = new PostInfoDto();
            postInfoDto.setId(post.getId());
            postInfoDto.setBody(post.getBody());
            postInfoDto.setUserId(post.getUser().getId());

            postDtos.add(postInfoDto);
        }

        return postDtos;
    }
}
