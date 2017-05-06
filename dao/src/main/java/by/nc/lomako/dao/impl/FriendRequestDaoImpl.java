/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.FriendRequestDao;
import by.nc.lomako.pojos.FriendRequest;
import org.springframework.stereotype.Repository;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class FriendRequestDaoImpl extends AbstractCrudDao<FriendRequest, Long> implements FriendRequestDao {

    public FriendRequestDaoImpl() {
        super(FriendRequest.class);
    }
}
