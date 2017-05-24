/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.services;

import by.nc.lomako.dto.message.MessageForSendDto;
import by.nc.lomako.dto.message.MessageForUpdateDto;
import by.nc.lomako.dto.message.MessageInfoDto;
import by.nc.lomako.dto.user.UserForRegisterDto;
import by.nc.lomako.dto.user.UserInfoDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.services.utils.DaoTestsHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:dao-context.test.xml")
@ActiveProfiles("test")
@Transactional
public class MessageServiceIT {

    private static final String FIRST_MESSAGE_BODY = "T_FIRST_MESSAGE_BODY";
    private static final String SECOND_MESSAGE_BODY = "T_SECOND_MESSAGE_BODY";
    private static final String THIRD_MESSAGE_BODY = "T_THIRD_MESSAGE_BODY";

    private static final String FIRST_USER_EMAIL = "T_FIRST_USER_EMAIL";
    private static final String SECOND_USER_EMAIL = "T_SECOND_USER_EMAIL";
    private static final String THIRD_USER_EMAIL = "T_THIRD_USER_EMAIL";

    private static final String USER_FIRST_NAME = "T_USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "T_USER_LAST_NAME";
    private static final String USER_PASSWORD = "T_USER_PASSWORD";

    private static final long NOT_EXIST_ID = 9999L;

    private static final String NEW_MESSAGE_BODY = "T_NEW_MESSAGE_BODY";
    private static final boolean NEW_MESSAGE_IS_READ = true;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private DaoTestsHelper daoTestsHelper;

    @Before
    public void setUp() throws Exception {
        daoTestsHelper.persistRoles();
    }

    @After
    public void tearDown() throws Exception {
        List<MessageInfoDto> messages = messageService.findAll();
        for (MessageInfoDto message : messages) {
            messageService.deleteById(message.getId());
        }

        List<UserInfoDto> users = userService.findAll();
        for (UserInfoDto user : users) {
            userService.deleteById(user.getId());
        }

        daoTestsHelper.dropRoles();
    }

    @Test
    public void sendMessage() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        long id = messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        MessageInfoDto findMessage = messageService.findById(id);

        assertThat(
                findMessage.getBody(),
                equalTo(FIRST_MESSAGE_BODY)
        );

    }

    @Test
    public void findLastByUsers() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        messageService.sendMessage(firstUserId, buildMessage(secondUserId, SECOND_MESSAGE_BODY));
        messageService.sendMessage(secondUserId, buildMessage(firstUserId, THIRD_MESSAGE_BODY));
        List<MessageInfoDto> messages = messageService.findLastByUsers(firstUserId, secondUserId, 0, 4);

        assertThat(
                messages.get(0).getBody(),
                equalTo(FIRST_MESSAGE_BODY)
        );
        assertThat(
                messages.get(1).getBody(),
                equalTo(SECOND_MESSAGE_BODY)
        );
        assertThat(
                messages.get(2).getBody(),
                equalTo(THIRD_MESSAGE_BODY)
        );
    }

    @Test
    public void countByUsers() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        assertThat(
                messageService.countByUsers(firstUserId, secondUserId),
                equalTo(0L)
        );

        messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        messageService.sendMessage(firstUserId, buildMessage(secondUserId, SECOND_MESSAGE_BODY));
        messageService.sendMessage(secondUserId, buildMessage(firstUserId, THIRD_MESSAGE_BODY));

        assertThat(
                messageService.countByUsers(firstUserId, secondUserId),
                equalTo(3L)
        );
        assertThat(
                messageService.countByUsers(secondUserId, firstUserId),
                equalTo(3L)
        );
    }

    @Test
    public void deleteById() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        long id = messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        assertThat(
                messageService.findById(id),
                is(notNullValue())
        );

        messageService.deleteById(id);

        assertThat(
                messageService.countByUsers(firstUserId, secondUserId),
                equalTo(0L)
        );
    }

    @Test
    public void deleteAllByUsers() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        messageService.sendMessage(firstUserId, buildMessage(secondUserId, SECOND_MESSAGE_BODY));
        messageService.sendMessage(secondUserId, buildMessage(firstUserId, THIRD_MESSAGE_BODY));

        assertThat(
                messageService.countByUsers(firstUserId, secondUserId),
                equalTo(3L)
        );

        messageService.deleteAllByUsers(firstUserId, secondUserId);

        assertThat(
                messageService.countByUsers(firstUserId, secondUserId),
                equalTo(0L)
        );
    }

    @Test(expected = MessageNotFoundException.class)
    public void findById() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        long messageId = messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        MessageInfoDto byId = messageService.findById(messageId);

        assertThat(
                byId.getBody(),
                equalTo(FIRST_MESSAGE_BODY)
        );

        messageService.findById(NOT_EXIST_ID);

    }

    @Test
    public void update() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        long messageId = messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));

        MessageForUpdateDto messageForUpdateDto = buildMessageForUpdate(secondUserId, firstUserId);
        messageService.update(messageId, messageForUpdateDto);

        MessageInfoDto byId = messageService.findById(messageId);

        assertThat(
                byId.getBody(),
                equalTo(NEW_MESSAGE_BODY)
        );
        assertThat(
                byId.isRead(),
                equalTo(NEW_MESSAGE_IS_READ)
        );
        assertThat(
                byId.getUserFromId(),
                equalTo(secondUserId)
        );
        assertThat(
                byId.getUserToId(),
                equalTo(firstUserId)
        );
    }

    @Test
    public void findLastDialogs() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        long firstMessageId = messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        long secondMessageId = messageService.sendMessage(firstUserId, buildMessage(secondUserId, SECOND_MESSAGE_BODY));
        long thirdMessageId = messageService.sendMessage(secondUserId, buildMessage(firstUserId, THIRD_MESSAGE_BODY));

        List<MessageInfoDto> lastDialogs = messageService.findLastDialogs(firstUserId, 0, 4);

        assertThat(
                lastDialogs.get(0).getBody(),
                equalTo(FIRST_MESSAGE_BODY)
        );

    }

    @Test
    public void findAll() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        messageService.sendMessage(firstUserId, buildMessage(secondUserId, SECOND_MESSAGE_BODY));
        messageService.sendMessage(secondUserId, buildMessage(firstUserId, THIRD_MESSAGE_BODY));

        List<MessageInfoDto> all = messageService.findAll();

        assertThat(
                all.size(),
                equalTo(3)
        );
        assertThat(
                all.stream()
                        .map(MessageInfoDto::getBody)
                        .filter(body -> body.equals(SECOND_MESSAGE_BODY))
                        .collect(Collectors.toList())
                        .size(),
                equalTo(1)
        );

    }

    @Test
    public void countDialogs() throws Exception {
        UserForRegisterDto firstUser = buildUser(FIRST_USER_EMAIL);
        UserForRegisterDto secondUser = buildUser(SECOND_USER_EMAIL);

        long firstUserId = userService.register(firstUser);
        long secondUserId = userService.register(secondUser);

        messageService.sendMessage(firstUserId, buildMessage(secondUserId, FIRST_MESSAGE_BODY));
        messageService.sendMessage(firstUserId, buildMessage(secondUserId, SECOND_MESSAGE_BODY));
        messageService.sendMessage(secondUserId, buildMessage(firstUserId, THIRD_MESSAGE_BODY));

        long countDialogs = messageService.countDialogs(secondUserId);
        assertThat(
                countDialogs,
                equalTo(1L)
        );
    }

    private MessageForUpdateDto buildMessageForUpdate(long userFromId, long userToId) {
        MessageForUpdateDto messageDto = new MessageForUpdateDto();
        messageDto.setBody(NEW_MESSAGE_BODY);
        messageDto.setRead(NEW_MESSAGE_IS_READ);
        messageDto.setUserFromId(userFromId);
        messageDto.setUserToId(userToId);

        return messageDto;
    }

    private MessageForSendDto buildMessage(long userToId, String body) {
        MessageForSendDto messageDto = new MessageForSendDto();
        messageDto.setBody(body);
        messageDto.setUserToId(userToId);
        return messageDto;
    }

    private UserForRegisterDto buildUser(String email) {
        UserForRegisterDto userDto = new UserForRegisterDto();
        userDto.setEmail(email);
        userDto.setPassword(USER_PASSWORD);
        userDto.setFirstName(USER_FIRST_NAME);
        userDto.setLastName(USER_LAST_NAME);
        return userDto;
    }

}
