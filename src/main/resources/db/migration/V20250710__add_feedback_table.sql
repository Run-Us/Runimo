create table `user_feedback`
(
    `id`         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`    BIGINT NOT NULL,
    `rate`       INT,
    `content`    VARCHAR(128),
    `created_at` TIMESTAMP,
    `updated_at` TIMESTAMP
);