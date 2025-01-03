CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       VARCHAR(36) NOT NULL,
    name       VARCHAR(64) NOT NULL,
    email      VARCHAR(64) NOT NULL,
    birth_date DATE        NOT NULL
);

CREATE INDEX idx_user_uuid ON user (uuid);
CREATE INDEX idx_user_email ON user (email);