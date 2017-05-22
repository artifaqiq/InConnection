/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.Post;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface PostDao extends CrudDao<Post, Long> {
    List<Post> findLastByUser(long userId, int start, int limit);
    long countByUser(long userId);
}
