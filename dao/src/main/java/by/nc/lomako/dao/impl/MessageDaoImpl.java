/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.MessageDao;
import by.nc.lomako.pojos.Message;
import org.springframework.stereotype.Repository;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class MessageDaoImpl extends AbstractCrudDao<Message, Long> implements MessageDao {

    public MessageDaoImpl() {
        super(Message.class);
    }
}
