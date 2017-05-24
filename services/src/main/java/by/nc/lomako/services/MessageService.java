/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.message.MessageForSendDto;
import by.nc.lomako.dto.message.MessageForUpdateDto;
import by.nc.lomako.dto.message.MessageInfoDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface MessageService {

    long sendMessage(long userFromId, MessageForSendDto messageForSendDto) throws UserNotFoundException;

    List<MessageInfoDto> findLastByUsers(long firstUserId, long secondUserId, int start, int limit) throws UserNotFoundException;

    long countByUsers(long firstUserId, long secondUserId);

    void deleteById(long id) throws MessageNotFoundException;

    void deleteAllByUsers(long firstUserId, long secondUserId);

    MessageInfoDto findById(long id) throws MessageNotFoundException;

    void update(long id, MessageForUpdateDto messageDto) throws MessageNotFoundException, UserNotFoundException;

    List<MessageInfoDto> findLastDialogs(long userId, int start, int limit) throws UserNotFoundException;

    long countDialogs(long userId);

    List<MessageInfoDto> findAll();

    long count();

}
