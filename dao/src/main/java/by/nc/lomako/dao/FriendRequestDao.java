/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.FriendRequest;

/**
 * @author Lomako
 * @version 1.0
 */
public interface FriendRequestDao extends CrudDao<FriendRequest, Long> {

    FriendRequest findByUsers(long userFromId, long userToId);

}
