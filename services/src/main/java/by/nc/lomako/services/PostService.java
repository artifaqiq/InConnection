/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostForUpdateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.services.exceptions.PostNotFoundException;
import by.nc.lomako.services.exceptions.UserNotFoundException;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface PostService {

    long create(long userId, PostForCreateDto postDto) throws UserNotFoundException;

    PostInfoDto findById(long id) throws PostNotFoundException;

    void update(long id, PostForUpdateDto post) throws PostNotFoundException, UserNotFoundException;

    void deleteById(long id) throws PostNotFoundException;

    List<PostInfoDto> findLastByUser(long userId, int start, int limit) throws UserNotFoundException;

    long countByUser(long userId) throws UserNotFoundException;

    List<PostInfoDto> findAll();

    long count();
}
