
-- Insert users
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active, latitude, longitude)
VALUES
    ('user1@example.com', 'password123', 'tester2', 'admin', 'test', 'User2', 'adresa', TRUE, 45.2671, 19.8335),
    ('user2@example.com', 'password123', 'tester3', 'admin', 'test', 'User3', 'adresa', TRUE, 45.2680, 19.8345),
    ('user3@example.com', 'password123', 'tester1', 'registered_user', 'test', 'User1', 'adresa', TRUE, 45.2655, 19.8360),
    ('user4@example.com', 'password123', 'tester4', 'registered_user', 'test', 'User4', 'adresa', TRUE, 45.2660, 19.8320),
    ('user5@example.com', 'password123', 'tester5', 'registered_user', 'test', 'User5', 'adresa', TRUE, 45.2675, 19.8330),
    ('user6@example.com', 'password123', 'tester6', 'registered_user', 'test', 'User6', 'adresa', TRUE, 45.2668, 19.8348),
    ('user7@example.com', 'password123', 'tester7', 'registered_user', 'test', 'User7', 'adresa', TRUE, 45.2679, 19.8359),
    ('user8@example.com', 'password123', 'tester8', 'registered_user', 'test', 'User8', 'adresa', TRUE, 45.2672, 19.8327),
    ('moracajelena9@gmail.com', 'password123', 'jelena123', 'registered_user', 'Jelena', 'mor', 'adresa', TRUE, 45.2665, 19.8332);

-- Insert posts
--INSERT INTO posts (is_deleted, user_id, description, image_url, created_at)
--VALUES
  --  (false, 4, 'content1', '/images/slika1.jpg', '2024-11-11 14:30:00'),
  --  (false, 5, 'content1', '/images/slika2.jpg', '2024-11-04 10:00:00'),
  --  (false, 5, 'content2', '/images/slika3.jpg', '2024-11-03 18:45:00'),
  --  (false, 5, 'content3', '/images/slika4.jpg', '2024-11-02 09:20:00'),
  --  (false, 7, 'content1', '/images/slika5.jpg', '2024-11-04 13:00:00'),
  --  (false, 7, 'content2', '/images/slika6.jpg', '2024-11-03 11:30:00'),
  --  (false, 9, 'content2', '/images/slika1.jpg', '2024-11-03 11:30:00'),
  --  (false, 9, 'content2', '/images/slika2.jpg', '2024-12-12 11:30:00'),
 --   (false, 5, 'content2', '/images/slika7.jpg', '2024-11-03 11:30:00'),
 --   (false, 9, 'content2', '/images/slika8.jpg', '2024-12-03 11:30:00'),
  --  (false, 9, 'content2', '/images/slika1.jpg', '2024-12-12 11:30:00');

INSERT INTO posts ( user_id, description, image_url, created_at)
VALUES
    ( 4, 'opis1', '/images/slika1.jpg', '2025-11-11 14:30:00'),
    ( 5, 'opis2', '/images/slika2.jpg', '2025-11-04 10:00:00'),
    ( 5, 'opis3', '/images/slika3.jpg', '2025-11-03 18:45:00'),
    ( 5, 'opis4', '/images/slika4.jpg', '2025-11-02 06:20:00'),
    ( 7, 'opis3', '/images/slika5.jpg', '2025-11-04 13:00:00'),
    ( 7, 'opis2', '/images/slika6.jpg', '2025-11-03 13:30:00'),
    ( 9, 'opis7', '/images/slika1.jpg', '2025-11-03 01:30:00'),
    ( 9, 'opis7', '/images/slika2.jpg', '2025-12-12 11:30:00'),
    ( 5, 'opis8', '/images/slika7.jpg', '2025-11-03 01:30:00'),
    (9, 'opis10', '/images/slika8.jpg', '2025-12-03 11:30:00'),
    (9, 'opis11', '/images/slika1.jpg', '2025-12-12 11:30:00');



--INSERT INTO posts (image_url, description, user_id, created_at, latitude, longitude)
--VALUES (
         --  '/images/14.png',   -- URL slike
         --  'Post ', -- Opis posta
        --   7,                                -- ID korisnika (postojeći user_id iz tabele korisnika)
        --   false,
        --   NOW(),                            -- Trenutno vreme za kreiranje posta
        --   45.2539,                          -- Geografska širina (latitude)
        --   19.8482
     --  );


-- Insert comments
INSERT INTO comments (post_id, user_id, content, created_at)
VALUES
    (1, 7, 'komentar', '2025-11-06 12:00:00'),
    (1, 3, 'komentar1', '2025-11-06 12:00:00'),
    (1, 5, 'komentar2', '2025-11-06 11:00:00'),
    (2, 7, 'komentar3', '2025-11-05 09:00:00'),
    (1, 4, 'komentar4', '2025-11-06 08:00:00'),
    (6, 7, 'komentar5', '2025-11-05 12:00:00'),
    (5, 5, 'komentar6', '2025-11-05 12:10:00');










