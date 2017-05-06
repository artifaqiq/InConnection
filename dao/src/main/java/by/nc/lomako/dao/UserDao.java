/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.User;

/**
 * @author Lomako
 * @version 1.0
 */
public interface UserDao extends CrudDao<User, Long> {

    User findByEmail(String email);
}
