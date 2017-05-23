/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services.impl;

import by.nc.lomako.dao.FriendRequestDao;
import by.nc.lomako.dao.UserDao;
import by.nc.lomako.exceptions.DuplicateFriendRequest;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.FriendRequest;
import by.nc.lomako.pojos.User;
import by.nc.lomako.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lomako
 * @version 1.0
 */
@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRequestDao friendRequestDao;

    private final UserDao userDao;

    @Autowired
    public FriendServiceImpl(FriendRequestDao friendRequestDao, UserDao userDao) {
        this.friendRequestDao = friendRequestDao;
        this.userDao = userDao;
    }

    @Override
    public long sentRequest(long userFromId, long userToId) throws UserNotFoundException, DuplicateFriendRequest {
        if(userFromId == userToId) {
            throw new DuplicateFriendRequest();
        }

        final User userFrom = userDao.findOne(userFromId);
        if (userFrom == null) {
            throw new UserNotFoundException();
        }

        final User userTo = userDao.findOne(userToId);
        if (userTo == null) {
            throw new UserNotFoundException();
        }

        if(friendRequestDao.findByUsers(userFromId, userToId) != null) {
            throw new DuplicateFriendRequest();
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUserFrom(userFrom);
        friendRequest.setUserTo(userTo);

        friendRequest = friendRequestDao.save(friendRequest);
        return friendRequest.getId();
    }
}
