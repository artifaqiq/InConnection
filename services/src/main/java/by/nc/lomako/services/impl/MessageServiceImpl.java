/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services.impl;

import by.nc.lomako.dao.MessageDao;
import by.nc.lomako.dao.UserDao;
import by.nc.lomako.dto.message.MessageForSendDto;
import by.nc.lomako.dto.message.MessageForUpdateDto;
import by.nc.lomako.dto.message.MessageInfoDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.pojos.Message;
import by.nc.lomako.pojos.User;
import by.nc.lomako.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final UserDao userDao;

    private final MessageDao messageDao;

    @Autowired
    public MessageServiceImpl(UserDao userDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    @Override
    public long sendMessage(long userFromId, MessageForSendDto messageForSendDto) throws UserNotFoundException {
        User userFrom = userDao.findOne(userFromId);
        if (userFrom == null) {
            throw new UserNotFoundException();
        }

        User userTo = userDao.findOne(messageForSendDto.getUserToId());
        if (userTo == null) {
            throw new UserNotFoundException();
        }

        Message message = new Message();
        message.setUserFrom(userFrom);
        message.setUserTo(userTo);
        message.setBody(messageForSendDto.getBody());
        message.setRead(false);

        message = messageDao.save(message);
        return message.getId();
    }

    @Override
    public List<MessageInfoDto> findLastByUsers(long firstUserId, long secondUserId, int start, int limit)
            throws UserNotFoundException {
        if (userDao.findOne(firstUserId) == null || userDao.findOne(secondUserId) == null) {
            throw new UserNotFoundException();
        }

        List<MessageInfoDto> messageDtos = new LinkedList<>();
        List<Message> messages = messageDao.findLastMessagesByUsers(firstUserId, secondUserId, start, limit);

        for (Message message : messages) {
            MessageInfoDto messageDto = new MessageInfoDto();
            messageDto.setId(message.getId());
            messageDto.setBody(message.getBody());
            messageDto.setCreatedDate(message.getCreatedDate());
            messageDto.setUserFromId(message.getUserFrom().getId());
            messageDto.setUserToId(message.getUserTo().getId());

            messageDtos.add(messageDto);
        }

        return messageDtos;

    }

    @Override
    public void deleteById(long id) throws MessageNotFoundException {
        if (messageDao.findOne(id) == null) {
            throw new MessageNotFoundException();
        }

        messageDao.deleteById(id);
    }

    @Override
    public void deleteAllByUsers(long firstUserId, long secondUserId) {
        messageDao.deleteAllByUser(firstUserId, secondUserId);
    }

    @Override
    public long countByUsers(long firstUserId, long secondUserId) {
        return messageDao.countByUsers(firstUserId, secondUserId);
    }

    @Override
    public MessageInfoDto findById(long id) throws MessageNotFoundException {
        Message message = messageDao.findOne(id);
        if (message == null) {
            throw new MessageNotFoundException();
        }

        MessageInfoDto messageInfoDto = new MessageInfoDto();
        messageInfoDto.setBody(message.getBody());
        messageInfoDto.setUserFromId(message.getUserFrom().getId());
        messageInfoDto.setUserToId(message.getUserTo().getId());
        messageInfoDto.setId(message.getId());
        messageInfoDto.setRead(message.isRead());
        messageInfoDto.setCreatedDate(message.getCreatedDate());

        return messageInfoDto;
    }

    @Override
    public void update(long id, MessageForUpdateDto messageDto) {
        Message message = messageDao.findOne(id);
        message.setUserFrom(userDao.findOne(messageDto.getUserFromId()));
        message.setUserTo(userDao.findOne(messageDto.getUserToId()));
        message.setBody(messageDto.getBody());
        message.setRead(messageDto.isRead());

//        messageDao.save(message);
    }

    @Override
    public List<MessageInfoDto> findLastDialogs(long userId, int start, int limit) throws UserNotFoundException {
        if (userDao.findOne(userId) == null) {
            throw new UserNotFoundException();
        }

        List<Message> lastDialogs = messageDao.findLastDialogs(userId, start, limit);
        LinkedList<MessageInfoDto> lastDialogsDtos = new LinkedList<>();

        for (Message lastDialog : lastDialogs) {
            MessageInfoDto messageInfoDto = new MessageInfoDto();
            messageInfoDto.setRead(lastDialog.isRead());
            messageInfoDto.setCreatedDate(lastDialog.getCreatedDate());
            messageInfoDto.setId(lastDialog.getId());
            messageInfoDto.setUserFromId(lastDialog.getUserFrom().getId());
            messageInfoDto.setUserToId(lastDialog.getUserTo().getId());
            messageInfoDto.setBody(lastDialog.getBody());

            lastDialogsDtos.add(messageInfoDto);
        }

        return lastDialogsDtos;
    }

    @Override
    public long countDialogs(long userId) {
        return messageDao.countDialogs(userId);
    }

    @Override
    public List<MessageInfoDto> findAll() {
        List<Message> messages = messageDao.findAll();
        List<MessageInfoDto> messageDtos = new LinkedList<>();

        for (Message message : messages) {
            MessageInfoDto messageInfoDto = new MessageInfoDto();
            messageInfoDto.setBody(message.getBody());
            messageInfoDto.setUserFromId(message.getUserFrom().getId());
            messageInfoDto.setUserToId(message.getUserTo().getId());
            messageInfoDto.setId(message.getId());
            messageDtos.add(messageInfoDto);
        }

        return messageDtos;
    }
}
