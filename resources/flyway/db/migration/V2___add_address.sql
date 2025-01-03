CREATE TABLE address
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT      NOT NULL,
    country        VARCHAR(64) NOT NULL,
    state          VARCHAR(64) NOT NULL,
    city           VARCHAR(64) NOT NULL,
    zip_code       VARCHAR(64) NOT NULL,
    street_address VARCHAR(64) NOT NULL
);

CREATE INDEX idx_address_user_id ON address (user_id);
