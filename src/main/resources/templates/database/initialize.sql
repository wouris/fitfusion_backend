DROP DATABASE IF EXISTS fitfusion;

-- Create the 'fitfusion' database
CREATE DATABASE fitfusion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the 'fitfusion' database
USE fitfusion;

-- Create the 'users' table
CREATE TABLE users
(
    id                UUID PRIMARY KEY,
    username          VARCHAR(255) UNIQUE,
    email             VARCHAR(255) UNIQUE,
    password          VARCHAR(255) NOT NULL,
    registration_date DATETIME     DEFAULT CURRENT_TIMESTAMP,
    role              VARCHAR(255) DEFAULT 'USER',
    avatar            LONGTEXT
);

-- Create the 'workouts' table
-- TODO: Complete the 'workouts' table
CREATE TABLE workouts
(
    user_id   UUID NOT NULL,
    body_part VARCHAR(255),
    duration  INT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Create the 'following' table
CREATE TABLE follows
(
    follower_id  UUID NOT NULL,
    following_id UUID NOT NULL,
    FOREIGN KEY (follower_id) REFERENCES users (id),
    FOREIGN KEY (following_id) REFERENCES users (id)
);

CREATE TABLE posts
(
    id          UUID PRIMARY KEY,
    user_id     UUID     NOT NULL,
    image       LONGTEXT NOT NULL,
    description LONGTEXT DEFAULT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE comments
(
    id         UUID PRIMARY KEY,
    user_id    UUID     NOT NULL,
    post_id    UUID     NOT NULL,
    content    LONGTEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE likes
(
    user_id UUID NOT NULL,
    post_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE comment_likes
(
    user_id    UUID NOT NULL,
    comment_id UUID,
    reply_id   UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (comment_id) REFERENCES comments (id),
    FOREIGN KEY (reply_id) REFERENCES comment_replies (id)
);

CREATE TABLE comment_replies
(
    id         UUID PRIMARY KEY,
    user_id    UUID     NOT NULL,
    comment_id UUID     NOT NULL,
    content    LONGTEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (comment_id) REFERENCES comments (id)
);