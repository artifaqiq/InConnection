/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.builders;

import by.nc.lomako.pojos.Post;
import by.nc.lomako.pojos.User;

/**
 * @author Lomako
 * @version 1.0
 */
public class PostBuilder {
    private User user;

    private String body;

    public PostBuilder user(User user) {
        this.user = user;
        return this;
    }

    public PostBuilder body(String body) {
        this.body = body;
        return this;
    }

    public Post build() {
        Post post = new Post();
        post.setUser(user);
        post.setBody(body);

        return post;
    }

}
