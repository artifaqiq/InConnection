/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.FriendRequestDao;
import by.nc.lomako.pojos.FriendRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class FriendRequestDaoImpl extends AbstractCrudDao<FriendRequest, Long> implements FriendRequestDao {

    public FriendRequestDaoImpl() {
        super(FriendRequest.class);
    }

    @Override
    public FriendRequest findByUsers(long userFromId, long userToId) {

        List results = em
                .createQuery("from FriendRequest fr where fr.userFrom.id = :userFromId and fr.userTo.id = :userToId")
                .setParameter("userFromId", userFromId)
                .setParameter("userToId", userToId)
                .getResultList();

        if (results.isEmpty()) {
            return null;
        } else {
            return (FriendRequest) results.get(0);
        }

    }
}
