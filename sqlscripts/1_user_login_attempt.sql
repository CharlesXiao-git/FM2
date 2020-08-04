USE freightmate_db;

DROP TABLE IF EXISTS user_login_attempt;

CREATE TABLE IF NOT EXISTS user_login_attempt
(
    id               INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username         VARCHAR(100) NOT NULL,
    origin_ip        VARCHAR(20)  NOT NULL,
    login_attempt_at DATETIME DEFAULT NOW()
);