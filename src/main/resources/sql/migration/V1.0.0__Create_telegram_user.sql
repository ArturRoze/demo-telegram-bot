CREATE TABLE telegram_user
(
    id               BIGSERIAL    NOT NULL PRIMARY KEY,
    chat_id          BIGINT       NOT NULL UNIQUE,
    user_id          BIGINT       UNIQUE,
    login            VARCHAR(255) UNIQUE,
    user_locale      VARCHAR(4)   NOT NULL
);