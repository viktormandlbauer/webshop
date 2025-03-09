USE webshop;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO webshop.user
(id, username, password, created_at)
VALUES(0, 'admin', 'admin', CURRENT_TIMESTAMP);