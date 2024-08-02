INSERT INTO USERS (user_id, email, level, current_points,completed_stretchings,tier) VALUES (1, 'user1@example.com', 0, 0,0,'Bronze');
INSERT INTO USERS (user_id, email, level, current_points,completed_stretchings,tier) VALUES (2, 'user2@example.com', 0, 0,0,'Bronze');

INSERT INTO BODY_PARTS ( part_name, count, user_id) VALUES ( 'head', 0, 1);
INSERT INTO BODY_PARTS ( part_name, count, user_id) VALUES ( 'torso', 0, 1);

INSERT INTO BODY_PARTS ( part_name, count, user_id) VALUES ( 'head', 0, 2);
INSERT INTO BODY_PARTS ( part_name, count, user_id) VALUES ( 'torso', 0, 2);
