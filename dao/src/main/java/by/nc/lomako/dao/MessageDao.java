/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.Message;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface MessageDao extends CrudDao<Message, Long> {

    List<Message> findLastMessagesByUsers(long firstUserId, long secondUserId, int start, int limit);

    Long countByUsers(long firstUserId, long secondUserId);

    void deleteAllByUser(long firstUserId, long secondUserId);

    List<Message> findLastDialogs(long userId, int start, int limit);

    Long countDialogs(long userId);

}
