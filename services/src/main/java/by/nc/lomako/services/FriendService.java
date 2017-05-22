/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;


import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.exceptions.DuplicateFriendRequest;

/**
 * @author Lomako
 * @version 1.0
 */
public interface FriendService {
    long sentRequest(long userFromId, long userToId) throws UserNotFoundException, DuplicateFriendRequest;
}