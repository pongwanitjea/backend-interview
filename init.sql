-- my_database.user_auth definition

CREATE TABLE `user_auth` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO my_database.user_auth
(username, password)
VALUES('user1', '$2a$12$jjE5SH.qvYhv4pby/yrwhOlAYqE0kil1bxbqN5dwnlxBjAKmGHh62');
