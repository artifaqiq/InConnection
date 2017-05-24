/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.dao.builders.MessageBuilder;
import by.nc.lomako.dao.builders.UserBuilder;
import by.nc.lomako.pojos.Message;
import by.nc.lomako.pojos.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:dao-context.test.xml")
@ActiveProfiles("test")
@Transactional
public class MessageDaoIT {

    private static final String FIRST_USER_EMAIL = "T_FIRST_USER_EMAIL";
    private static final String SECOND_USER_EMAIL = "T_SECOND_USER_EMAIL";
    private static final String THIRD_USER_EMAIL = "T_THIRD_USER_EMAIL";

    private static final String FIRST_MESSAGE_BODY = "T_FIRST_MESSAGE_BODY";
    private static final String SECOND_MESSAGE_BODY = "T_SECOND_MESSAGE_BODY";
    private static final String THIRD_MESSAGE_BODY = "T_THIRD_MESSAGE_BODY";

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @After
    public void tearDown() throws Exception {
        List<Message> messages = messageDao.findAll();
        for (Message message : messages) {
            messageDao.delete(message);
        }

        List<User> users = userDao.findAll();
        for (User user : users) {
            userDao.delete(user);
        }
    }

    @Test
    public void findLastMessagesBetweenUser() throws Exception {

        User firstUser = buildFirstUser();
        User secondUser = buildSecondUser();

        firstUser = userDao.save(firstUser);
        secondUser = userDao.save(secondUser);


        Message message1 = buildFirstMessage(firstUser, secondUser);
        Message message2 = buildSecondMessage(firstUser, secondUser);
        Message message3 = buildThirdMessage(secondUser, firstUser);

        message1 = messageDao.save(message1);
        Thread.sleep(1001);
        message2 = messageDao.save(message2);
        Thread.sleep(1001);
        message3 = messageDao.save(message3);

        List<Message> messages = messageDao.findLastMessagesByUsers(
                firstUser.getId(), secondUser.getId(), 0, 2);

        assertThat(
                messages.get(0).getBody(),
                equalTo(THIRD_MESSAGE_BODY)
        );

        assertThat(
                messages.get(1).getBody(),
                equalTo(SECOND_MESSAGE_BODY)
        );

    }

    @Test
    public void deleteAllByUser() throws Exception {
        User firstUser = buildFirstUser();
        User secondUser = buildSecondUser();

        firstUser = userDao.save(firstUser);
        secondUser = userDao.save(secondUser);

        Message message1 = buildSecondMessage(firstUser, secondUser);
        Message message2 = buildThirdMessage(secondUser, firstUser);

        message1 = messageDao.save(message1);
        Thread.sleep(1001);
        message2 = messageDao.save(message2);

        List<Message> messages = messageDao.findAll();
        assertThat(
                messages.contains(message1),
                equalTo(true)
        );

        assertThat(
                messages.contains(message2),
                equalTo(true)
        );

        messageDao.deleteAllByUser(firstUser.getId(), secondUser.getId());
        assertThat(
                messageDao.count(),
                equalTo(0L)
        );
    }

    @Test
    public void findLastDialogs() throws Exception {
        User firstUser = buildFirstUser();
        User secondUser = buildSecondUser();
        User thirdUser = buildThirdUser();

        firstUser = userDao.save(firstUser);
        secondUser = userDao.save(secondUser);
        thirdUser = userDao.save(thirdUser);

        Message message1 = buildFirstMessage(firstUser, secondUser);
        Message message2 = buildSecondMessage(firstUser, secondUser);
        Message message3 = buildThirdMessage(thirdUser, firstUser);

        messageDao.save(message1);
        Thread.sleep(1001);
        messageDao.save(message2);
        Thread.sleep(1001);
        messageDao.save(message3);

        System.out.println("messageDao = " + messageDao.findAll());
        System.out.println("messageDao.findLastDialogs(firstUser.getId(), 0, 3) = " + messageDao.findLastDialogs(secondUser.getId(), 0, 3));

        List<Message> dialogs = messageDao.findLastDialogs(firstUser.getId(), 0, 4);

        assertThat(
                dialogs.get(0).getBody(),
                equalTo(THIRD_MESSAGE_BODY)
        );

        assertThat(
                dialogs.get(1).getBody(),
                equalTo(SECOND_MESSAGE_BODY)
        );

    }

    @Test
    public void countByUsers() throws Exception {
        User firstUser = buildFirstUser();
        User secondUser = buildSecondUser();

        firstUser = userDao.save(firstUser);
        secondUser = userDao.save(secondUser);

        assertThat(
                messageDao.countByUsers(firstUser.getId(), secondUser.getId()),
                equalTo(0L)
        );

        Message message1 = buildFirstMessage(firstUser, secondUser);
        Message message2 = buildSecondMessage(firstUser, secondUser);
        Message message3 = buildThirdMessage(secondUser, firstUser);

        messageDao.save(message1);
        messageDao.save(message2);
        messageDao.save(message3);

        assertThat(
                messageDao.countByUsers(firstUser.getId(), secondUser.getId()),
                equalTo(3L)
        );

    }

    @Test
    public void countDialogs() throws Exception {
        User firstUser = buildFirstUser();
        User secondUser = buildSecondUser();
        User thirdUser = buildThirdUser();

        firstUser = userDao.save(firstUser);
        secondUser = userDao.save(secondUser);
        thirdUser = userDao.save(thirdUser);

        assertThat(
                messageDao.countDialogs(firstUser.getId()),
                equalTo(0L)
        );

        Message message1 = buildFirstMessage(firstUser, secondUser);
        Message message2 = buildSecondMessage(firstUser, secondUser);
        Message message3 = buildThirdMessage(thirdUser, firstUser);

        messageDao.save(message1);
        messageDao.save(message2);
        messageDao.save(message3);

        assertThat(
                messageDao.countDialogs(firstUser.getId()),
                equalTo(2L)
        );

        assertThat(
                messageDao.countDialogs(secondUser.getId()),
                equalTo(1L)
        );
    }

    private Message buildFirstMessage(User userFrom, User userTo) {
        return new MessageBuilder()
                .userFrom(userFrom)
                .userTo(userTo)
                .isRead(false)
                .body(FIRST_MESSAGE_BODY)
                .build();
    }

    private Message buildSecondMessage(User userFrom, User userTo) {
        return new MessageBuilder()
                .userFrom(userFrom)
                .userTo(userTo)
                .isRead(false)
                .body(SECOND_MESSAGE_BODY)
                .build();
    }

    private Message buildThirdMessage(User userFrom, User userTo) {
        return new MessageBuilder()
                .userFrom(userFrom)
                .userTo(userTo)
                .isRead(false)
                .body(THIRD_MESSAGE_BODY)
                .build();
    }

    private User buildFirstUser() {
        return new UserBuilder()
                .email(FIRST_USER_EMAIL)
                .build();
    }

    private User buildSecondUser() {
        return new UserBuilder()
                .email(SECOND_USER_EMAIL)
                .build();
    }

    private User buildThirdUser() {
        return new UserBuilder()
                .email(THIRD_USER_EMAIL)
                .build();
    }
}
