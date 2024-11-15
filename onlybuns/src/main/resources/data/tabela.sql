-- Insert users
-- Insert users with dtype column
-- Insert users with dtype, address, and posts_count columns
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, posts_count)
VALUES
    ('testr1@gmail.com', 'password123', 'Korisnik1', 'ROLE_REGISTERED', 'Test', 'User2', 'Administrator', '4'),
    ('testra@gmail.com', 'password123', 'tester4', 'ROLE_ADMIN', 'Test', 'User4', 'RegisteredUser',  4),
    ('testr2@gmail.com', 'password123', 'Korisnik2', 'ROLE_REGISTERED', 'Test', 'User7', 'RegisteredUser', 4);



-- Insert posts
INSERT INTO posts (user_id, description, image_url, created_time, likes_count)
VALUES
    (3, 'content1', '/images/slika1.jpg', '2024-11-05 14:30:00', 4),
    (3, 'content1', '/images/slika2.jpg', '2024-11-04 10:00:00', 4),
    (3, 'content2', '/images/slika3.jpg', '2024-11-03 18:45:00', 5),
    (2, 'content3', '/images/slika4.jpg', '2024-11-02 09:20:00', 6),
    (2, 'content1', '/images/slika5.jpg', '2024-11-01 13:00:00', 2),
    (2, 'content2', '/images/slika6.jpg', '2024-11-06 11:30:00', 2);


-- Insert comments
INSERT INTO comments (post_id, user_id, content, created_at)
VALUES
    (1, 2, 'com1', '2024-11-06 12:00:00'),
    (1, 2, 'com2', '2024-11-06 11:00:00'),
    (2, 3, 'com1', '2024-11-05 09:00:00'),
    (1, 1, 'com3', '2024-11-06 08:00:00'),
    (1, 1, 'com1', '2024-11-05 12:00:00'),
    (3, 1, 'com1', '2024-11-05 12:10:00');


-- Insert likes
/*INSERT INTO likes (post_id, user_id, liked_time)
VALUES
    (1, 1, '2024-11-06 13:00:00'),
    (1, 1, '2024-11-06 13:05:00'),
    (2, 1, '2024-11-05 14:00:00'),
    (2, 1, '2024-11-05 14:10:00'),
    (2, 1, '2024-11-05 14:15:00'),
    (3, 2, '2024-11-05 15:00:00'),
    (3, 2, '2024-11-05 15:10:00'),
    (3, 2, '2024-11-05 15:20:00'),
    (2, 2, '2024-11-05 16:00:00');
*/