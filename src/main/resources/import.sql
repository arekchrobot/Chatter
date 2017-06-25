INSERT INTO chatter_role(id, name) VALUES (1, 'USER_ROLE');

INSERT INTO chatter_user(username, pass, chatter_role_id) VALUES ('asd', '$2a$11$MGiglSqnssbmabwhq03Vue3EJwE6pRd/e5azu4/mgRPFz19Eg0l8m', 1); --zxc pass
INSERT INTO chatter_user(username, pass, chatter_role_id) VALUES ('artur', '$2a$11$MGiglSqnssbmabwhq03Vue3EJwE6pRd/e5azu4/mgRPFz19Eg0l8m', 1);
INSERT INTO chatter_user(username, pass, chatter_role_id) VALUES ('test', '$2a$11$MGiglSqnssbmabwhq03Vue3EJwE6pRd/e5azu4/mgRPFz19Eg0l8m', 1);
INSERT INTO chatter_user(username, pass, chatter_role_id) VALUES ('agh', '$2a$11$MGiglSqnssbmabwhq03Vue3EJwE6pRd/e5azu4/mgRPFz19Eg0l8m', 1);

INSERT INTO chat(name) VALUES ('asd_artur');
INSERT INTO chat(name) VALUES ('asd_test');
INSERT INTO chat(name) VALUES ('artur_test');

INSERT INTO chat_message(content, created, read, receiver, sender, chat_id) VALUES ('Hello', NOW(), false, 'artur', 'asd', 1);
INSERT INTO chat_message(content, created, read, receiver, sender, chat_id) VALUES ('Hey', NOW() - INTERVAL '1 minute', false, 'asd', 'artur', 1);
INSERT INTO chat_message(content, created, read, receiver, sender, chat_id) VALUES ('Sup?', NOW() - INTERVAL '3 minute', false, 'artur', 'asd', 1);
INSERT INTO chat_message(content, created, read, receiver, sender, chat_id) VALUES ('You there?', NOW() - INTERVAL '5 minute', false, 'test', 'asd', 2);