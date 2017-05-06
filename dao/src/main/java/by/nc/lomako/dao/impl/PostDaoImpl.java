/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.PostDao;
import by.nc.lomako.pojos.Post;
import org.springframework.stereotype.Repository;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class PostDaoImpl extends AbstractCrudDao<Post, Long> implements PostDao {

    public PostDaoImpl() {
        super(Post.class);
    }
}
