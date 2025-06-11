SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS egg_type;
DROP TABLE IF EXISTS signup_token;
DROP TABLE IF EXISTS apple_user_token;
DROP TABLE IF EXISTS user_token;
DROP TABLE IF EXISTS oauth_account;
DROP TABLE IF EXISTS running_record;
DROP TABLE IF EXISTS user_item;
DROP TABLE IF EXISTS incubator;
DROP TABLE IF EXISTS runimo;
DROP TABLE IF EXISTS item_activity;
DROP TABLE IF EXISTS runimo_definition;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS user_refresh_token;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_love_point;
DROP TABLE IF EXISTS incubating_egg;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `users`
(
    `id`                       BIGINT PRIMARY KEY AUTO_INCREMENT,
    `public_id`                VARCHAR(255),
    `nickname`                 VARCHAR(255),
    `img_url`                  VARCHAR(255),
    `total_distance_in_meters` BIGINT      NOT NULL DEFAULT 0,
    `total_time_in_seconds`    BIGINT      NOT NULL DEFAULT 0,
    `main_runimo_id`           BIGINT,
    `gender`                   VARCHAR(24),
    `role`                     VARCHAR(24) NOT NULL DEFAULT 'USER',
    `updated_at`               TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `created_at`               TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    `deleted_at`               TIMESTAMP   NULL
);

CREATE TABLE `user_token`
(
    `user_id`      BIGINT       NOT NULL,
    `device_token` VARCHAR(255) NOT NULL,
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`   TIMESTAMP    NULL,

    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `user_love_point`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`    BIGINT    NOT NULL,
    `amount`     BIGINT    NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
);

CREATE TABLE `oauth_account`
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`     BIGINT    NOT NULL,
    `provider`    VARCHAR(255),
    `provider_id` VARCHAR(255),
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  TIMESTAMP NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);


CREATE TABLE `apple_user_token`
(
    `id`            BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`       BIGINT       NOT NULL,
    `refresh_token` VARCHAR(255) NOT NULL,
    `created_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`    TIMESTAMP    NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `signup_token`
(
    `token`         VARCHAR(255) PRIMARY KEY NOT NULL UNIQUE,
    `provider_id`   VARCHAR(255)             NOT NULL,
    `refresh_token` VARCHAR(255),
    `provider`      VARCHAR(255),
    `created_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE `running_record`
(
    `id`                    BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`               BIGINT       NOT NULL,
    `record_public_id`      VARCHAR(255) NOT NULL,
    `title`                 VARCHAR(255),
    `description`           VARCHAR(255),
    `img_url`               VARCHAR(255),
    `started_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `end_at`                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `total_time_in_seconds` BIGINT,
    `total_distance`        BIGINT,
    `pace_in_milli_seconds` BIGINT,
    `is_rewarded`           BOOLEAN,
    `pace_per_km`           VARCHAR(10000),
    `created_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`            TIMESTAMP    NULL
);

CREATE TABLE `item`
(
    `id`                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`                 VARCHAR(255) NOT NULL,
    `item_code`            VARCHAR(255) NOT NULL,
    `description`          VARCHAR(255),
    `item_type`            VARCHAR(255) NOT NULL,
    `img_url`              VARCHAR(255),
    `dtype`                VARCHAR(255),
    `egg_type_id`          BIGINT,
    `hatch_require_amount` BIGINT,
    `created_at`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`           TIMESTAMP    NULL
);

CREATE TABLE `egg_type`
(
    `id`                          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`                        VARCHAR(64) NOT NULL,
    `code`                        VARCHAR(64) NOT NULL,
    `required_distance_in_meters` BIGINT,
    `level`                       INTEGER,
    `created_at`                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`                  TIMESTAMP   NULL

);

CREATE TABLE `item_activity`
(
    `id`                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    `activity_user_id`    BIGINT       NOT NULL,
    `activity_item_id`    BIGINT       NOT NULL,
    `activity_event_type` VARCHAR(255) NOT NULL,
    `quantity`            BIGINT,
    `created_at`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`          TIMESTAMP    NULL
);

CREATE TABLE `user_item`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`    BIGINT    NOT NULL,
    `item_id`    BIGINT    NOT NULL,
    `quantity`   BIGINT    NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
);

CREATE TABLE `incubating_egg`
(
    `id`                        BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`                   BIGINT    NOT NULL,
    `egg_id`                    BIGINT    NOT NULL,
    `current_love_point_amount` BIGINT,
    `hatch_require_amount`      BIGINT,
    `egg_status`                VARCHAR(255),
    `created_at`                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`                TIMESTAMP NULL
);

CREATE TABLE `runimo_definition`
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(255),
    `code`        VARCHAR(255),
    `description` VARCHAR(255),
    `img_url`     varchar(255),
    `egg_type_id` BIGINT    NOT NULL,
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  TIMESTAMP NULL
);

CREATE TABLE `runimo`
(
    `id`                       BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`                  BIGINT    NOT NULL,
    `runimo_definition_id`     BIGINT    NOT NULL,
    `total_run_count`          BIGINT    NOT NULL DEFAULT 0,
    `total_distance_in_meters` BIGINT    NOT NULL DEFAULT 0,
    `created_at`               TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    `updated_at`               TIMESTAMP          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`               TIMESTAMP NULL
);

CREATE TABLE `user_refresh_token`
(
    `id`            BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`       BIGINT NOT NULL UNIQUE,
    `refresh_token` TEXT   NOT NULL,
    `created_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE `user_token`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `oauth_account`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

-- insert static data

INSERT INTO egg_type (id, name, code, required_distance_in_meters, level, created_at, updated_at)
VALUES (1, '마당', 'A100', 0, 1, NOW(), NOW()),
       (2, '숲', 'A101', 30000, 2, NOW(), NOW());


INSERT INTO runimo_definition (id, name, code, description, img_url, egg_type_id, created_at,
                               updated_at)
VALUES (1, '강아지', 'R-101', '마당-강아지예여', 'http://dummy1', 1, NOW(), NOW()),
       (2, '고양이', 'R-102', '마당-고양이예여', 'http://dummy1', 1, NOW(), NOW()),
       (3, '토끼', 'R-103', '마당-토끼예여', 'http://dummy1', 1, NOW(), NOW()),
       (4, '오리', 'R-104', '마당-오리예여', 'http://dummy1', 1, NOW(), NOW()),
       (5, '늑대 강아지', 'R-105', '늑대 강아지예여', 'http://dummy2', 2, NOW(), NOW()),
       (6, '숲 고양이', 'R-106', '숲 고양이예여', 'http://dummy2', 2, NOW(), NOW()),
       (7, '나뭇잎 토끼', 'R-107', '나뭇잎 토끼예여', 'http://dummy2', 2, NOW(), NOW()),
       (8, '숲 오리', 'R-108', '숲 오리예여', 'http://dummy2', 2, NOW(), NOW());

INSERT INTO item (id, name, item_code, description, item_type, img_url, dtype, egg_type_id,
                  hatch_require_amount, created_at, updated_at)
VALUES (1, '마당', 'A100', '기본 알', 'USABLE', 'https://example.com/images/egg1.png', 'EGG', 1,
        100, NOW(), NOW()),
       (2, '숲', 'A101', '두번째 단계 알', 'USABLE', 'https://example.com/images/egg2.png', 'EGG',
        2, 30000, NOW(), NOW());