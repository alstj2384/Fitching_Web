CREATE DATABASE IF NOT EXISTS fitchingdb;
CREATE USER IF NOT EXISTS 'fitchingmaster'@'%' IDENTIFIED BY '12341234!';
GRANT ALL PRIVILEGES ON fitchingdb.* TO 'fitchingmaster'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    current_points INT NOT NULL,
    completed_stretchings INT NOT NULL,
    tier VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    provider_id VARCHAR(255),
    refresh_token VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS body_parts (
    part_id INT AUTO_INCREMENT PRIMARY KEY,
    part_name VARCHAR(255) NOT NULL,
    count INT NOT NULL,
    user_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

INSERT INTO users (email, level, current_points, completed_stretchings,  tier, name, role, password) VALUES ('admin', 0, 0 ,0 ,'Bronze', 'admin', 'USER', '$2a$10$.8ALtrQsc17C4lLU2xRDGejsJnm3ZqBZ3UIiSlnj2geP8uD4gu93S');
INSERT INTO body_parts (part_name, count, user_id) VALUES ('head', 0, 1);
INSERT INTO body_parts (part_name, count, user_id) VALUES ('torso', 0, 1);


