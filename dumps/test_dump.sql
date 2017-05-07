INSERT INTO in_connection_test.AccessLevel (id, accessLevelType) VALUES (3, 'ADMIN');
INSERT INTO in_connection_test.AccessLevel (id, accessLevelType) VALUES (2, 'MODERATOR');
INSERT INTO in_connection_test.AccessLevel (id, accessLevelType) VALUES (1, 'USER');

# password: 12345678
INSERT INTO in_connection_test.User (id, createdDate, email, encryptedPassword, firstName, lastName, updatedDate) VALUES (1, '2017-05-03 19:47:35', 'pupkin@mail.ru', '$2a$10$n8VXfHCun5JoaUZTf32EJ.CupD2fGV/u8jRZje5eENgEpgswfMrpa', 'Vasya', 'Pupkin', '2017-05-03 19:48:02');
INSERT INTO in_connection_test.User (id, createdDate, email, encryptedPassword, firstName, lastName, updatedDate) VALUES (2, '2017-05-03 19:48:09', 'hlebnikova@yandex.ru', '2334adfsaf324as', 'Katya', 'Hlebnikova', '2017-05-03 19:48:40');
INSERT INTO in_connection_test.User (id, createdDate, email, encryptedPassword, firstName, lastName, updatedDate) VALUES (3, '2017-05-03 19:48:45', 'artifaqiq@gmail.com', '12345678', 'Artur', 'Lomako', '2017-05-03 19:49:05');

INSERT INTO in_connection_test.USER_ROLE (role_id, user_id) VALUES (1, 1);
INSERT INTO in_connection_test.USER_ROLE (role_id, user_id) VALUES (1, 2);
INSERT INTO in_connection_test.USER_ROLE (role_id, user_id) VALUES (2, 2);
INSERT INTO in_connection_test.USER_ROLE (role_id, user_id) VALUES (1, 3);
INSERT INTO in_connection_test.USER_ROLE (role_id, user_id) VALUES (2, 3);
INSERT INTO in_connection_test.USER_ROLE (role_id, user_id) VALUES (3, 3);

INSERT INTO in_connection_test.Avatar (id, createdDate, url, user_id) VALUES (1, '2017-05-03 19:50:59', 'https://example.png', 1);
INSERT INTO in_connection_test.Avatar (id, createdDate, url, user_id) VALUES (2, '2017-05-03 19:51:17', '', 2);
INSERT INTO in_connection_test.Avatar (id, createdDate, url, user_id) VALUES (3, '2017-05-03 19:51:41', 'https://example.png', 3);
INSERT INTO in_connection_test.Avatar (id, createdDate, url, user_id) VALUES (4, '2017-05-03 19:51:54', 'https://image.jpg', 3);

INSERT INTO in_connection_test.FriendRequest (createdDate, userFrom_id, userTo_id) VALUES ('2017-05-03 19:52:27', 1, 2);
INSERT INTO in_connection_test.FriendRequest (createdDate, userFrom_id, userTo_id) VALUES ('2017-05-03 19:52:36', 1, 3);

INSERT INTO in_connection_test.Message (body, createdDate, isRead, userFrom_id, userTo_id) VALUES ('Hello User#3', '2017-05-03 19:53:36', false, 1, 3);
INSERT INTO in_connection_test.Message (body, createdDate, isRead, userFrom_id, userTo_id) VALUES ('Hi User#2', '2017-05-03 19:53:59', false , 1, 2);

INSERT INTO in_connection_test.Post (body, createdDate, user_id) VALUES ('My first post', '2017-05-03 19:54:24', 1);
INSERT INTO in_connection_test.Post (body, createdDate, user_id) VALUES ('My second post', '2017-05-03 19:54:33', 1);
INSERT INTO in_connection_test.Post (body, createdDate, user_id) VALUES ('I''m user#2', '2017-05-03 19:54:44', 2);

INSERT INTO in_connection_test.USER_FRIEND (user_id, friend_id) VALUES (2, 3);
