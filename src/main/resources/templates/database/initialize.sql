-- Create the 'fitfusion' database
CREATE DATABASE fitfusion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the 'fitfusion' database
USE fitfusion;

-- Create the 'users' table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    registration_date DATETIME
);

-- Create the 'account_info' table
CREATE TABLE account_info (
    userID UUID,
    workouts INT,
    followers INT,
    following INT,
    FOREIGN KEY (userID) REFERENCES users(id)
);

-- Create the 'workouts' table
CREATE TABLE workouts (
    userID UUID,
    body_part VARCHAR(255),
    duration INT,
    FOREIGN KEY (userID) REFERENCES users(id)
);

-- Create the 'followers' table
CREATE TABLE followers (
    fromUserID UUID,
    toUserID UUID,
    FOREIGN KEY (fromUserID) REFERENCES users(id),
    FOREIGN KEY (toUserID) REFERENCES users(id)
);

-- Create the 'following' table
CREATE TABLE following (
    userID UUID,
    whoUserID UUID,
    FOREIGN KEY (userID) REFERENCES users(id),
    FOREIGN KEY (whoUserID) REFERENCES users(id)
);

CREATE TABLE posts (
		id UUID PRIMARY KEY,
  	userID UUID,
  	image MEDIUMBLOB,
  	description VARCHAR(255),
  	FOREIGN KEY (userID) REFERENCES users(id)
)