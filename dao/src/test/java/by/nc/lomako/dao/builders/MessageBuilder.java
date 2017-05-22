/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.builders;

import by.nc.lomako.pojos.Message;
import by.nc.lomako.pojos.User;

/**
 * @author Lomako
 * @version 1.0
 */
public class MessageBuilder {

    private User userFrom;
    private User userTo;
    private String body;
    private boolean isRead;

    public MessageBuilder userFrom(User userFrom) {
        this.userFrom = userFrom;
        return this;
    }

    public MessageBuilder userTo(User userTo) {
        this.userTo = userTo;
        return this;
    }

    public MessageBuilder body(String body) {
        this.body = body;
        return this;
    }

    public MessageBuilder isRead(boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public Message build() {
        Message message = new Message();

        message.setUserFrom(userFrom);
        message.setUserTo(userTo);
        message.setBody(body);
        message.setRead(isRead);

        return message;
    }
}
