/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.UserDao;
import by.nc.lomako.pojos.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class UserDaoImpl extends AbstractCrudDao<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User findByEmail(String email) {
        List resultList = em.createQuery("from User u where u.email = :email")
                .setParameter("email", email)
                .getResultList();
        em.flush();
        return (User) resultList.stream().findFirst().orElse(null);
    }
}
