/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services.impl;

import by.nc.lomako.dao.PostDao;
import by.nc.lomako.dao.UserDao;
import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostForUpdateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.pojos.Post;
import by.nc.lomako.pojos.User;
import by.nc.lomako.services.PostService;
import by.nc.lomako.services.exceptions.PostNotFoundException;
import by.nc.lomako.services.exceptions.UserNotFoundException;
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

    private final PostDao postDao;

    private final UserDao userDao;

    @Autowired
    public PostServiceImpl(PostDao postDao, UserDao userDao) {
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @Override
    public long create(long userId, PostForCreateDto postDto) throws UserNotFoundException {
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
    public void deleteById(long id) throws PostNotFoundException {
        Post post = postDao.findOne(id);

        if (post == null) {
            throw new PostNotFoundException();
        }

        postDao.delete(post);
    }

    @Override
    public List<PostInfoDto> findLastByUser(long userId, int start, int limit) throws UserNotFoundException {
        List<PostInfoDto> posts = new LinkedList<>();

        if(userDao.findOne(userId) == null) {
            throw new UserNotFoundException();
        }

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
        if(userDao.findOne(postDto.getUserId()) == null) {
            throw new UserNotFoundException();
        }

        Post post = postDao.findOne(id);
        if (post == null) {
            throw new PostNotFoundException();
        }

        post.setUser(userDao.findOne(postDto.getUserId()));
        post.setBody(postDto.getBody());
    }

    @Override
    public long countByUser(long userId) throws UserNotFoundException {
        if(userDao.findOne(userId) == null) {
            throw new UserNotFoundException();
        }

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

    @Override
    public long count() {
        return postDao.count();
    }
}
