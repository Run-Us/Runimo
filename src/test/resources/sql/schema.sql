SET FOREIGN_KEY_CHECKS = 0;

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
    `total_distance_in_meters` BIGINT    NOT NULL DEFAULT 0,
    `total_time_in_seconds`    BIGINT    NOT NULL DEFAULT 0,
    `main_runimo_id`           BIGINT,
    `gender`                   VARCHAR(24),
    `updated_at`               TIMESTAMP          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `created_at`               TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    `deleted_at`               TIMESTAMP NULL
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
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`      BIGINT       NOT NULL,
    `refresh_token` VARCHAR(255) NOT NULL,
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`   TIMESTAMP    NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `signup_token`
(
    `token`       VARCHAR(255) PRIMARY KEY NOT NULL UNIQUE ,
    `provider_id` VARCHAR(255) NOT NULL,
    `refresh_token` VARCHAR(255),
    `provider` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE `running_record`
(
    `id`                    INTEGER PRIMARY KEY AUTO_INCREMENT,
    `user_id`               INTEGER      NOT NULL,
    `record_public_id`      VARCHAR(255) NOT NULL,
    `title`                 VARCHAR(255),
    `started_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `end_at`                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `total_distance`        INTEGER,
    `pace_in_milli_seconds` INTEGER,
    `is_rewarded`           BOOLEAN,
    `pace_per_km`           VARCHAR(10000),
    `created_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`            TIMESTAMP    NULL
);

CREATE TABLE `item`
(
    `id`                   INTEGER PRIMARY KEY AUTO_INCREMENT,
    `name`                 VARCHAR(255) NOT NULL,
    `item_code`            VARCHAR(255) NOT NULL,
    `description`          VARCHAR(255),
    `item_type`            VARCHAR(255) NOT NULL,
    `img_url`              VARCHAR(255),
    `dtype`                VARCHAR(255),
    `egg_type`             VARCHAR(255),
    `hatch_require_amount` BIGINT,
    `created_at`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`           TIMESTAMP    NULL
);

CREATE TABLE `item_activity`
(
    `id`                  INTEGER PRIMARY KEY AUTO_INCREMENT,
    `activity_user_id`    INTEGER      NOT NULL,
    `activity_item_id`    INTEGER      NOT NULL,
    `activity_event_type` VARCHAR(255) NOT NULL,
    `quantity`            INTEGER,
    `created_at`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`          TIMESTAMP    NULL
);

CREATE TABLE `user_item`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`    INTEGER   NOT NULL,
    `item_id`    INTEGER   NOT NULL,
    `quantity`   INTEGER   NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
);

CREATE TABLE `incubating_egg`
(
    `id`                        BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`                   INTEGER   NOT NULL,
    `egg_id`                    INTEGER   NOT NULL,
    `current_love_point_amount` INTEGER,
    `hatch_require_amount`      INTEGER,
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
    `egg_type`    varchar(255) NOT NULL,
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`  TIMESTAMP    NULL
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

ALTER TABLE `user_token`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `oauth_account`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

-- insert static data
INSERT INTO runimo_definition (id, name, code, description, img_url, egg_type, created_at, updated_at)
VALUES (1, '강아지', 'R-101', '마당-강아지예여', 'http://dummy1', 'MADANG', NOW(), NOW()),
       (2, '고양이', 'R-102', '마당-고양이예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
       (3, '토끼', 'R-103', '마당-토끼예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
       (4, '오리', 'R-104', '마당-오리예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
       (5, '늑대 강아지', 'R-105', '늑대 강아지예여', 'http://dummy2', 'FOREST', NOW(), NOW()),
       (6, '숲 고양이', 'R-106', '숲 고양이예여', 'http://dummy2', 'FOREST', NOW(), NOW()),
       (7, '나뭇잎 토끼', 'R-107', '나뭇잎 토끼예여', 'http://dummy2', 'FOREST', NOW(), NOW()),
       (8, '숲 오리', 'R-108', '숲 오리예여', 'http://dummy2', 'FOREST', NOW(), NOW());

INSERT INTO item (id, name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at, updated_at)
VALUES (1, '마당', 'A100', '기본 알', 'USABLE', 'https://example.com/images/egg1.png', 'EGG', 'MADANG', 0L, NOW(), NOW()),
       (2, '숲', 'A101', '두번째 단계 알', 'USABLE', 'https://example.com/images/egg2.png', 'EGG', 'FOREST', 30000L, NOW(), NOW()),
       (3, '초원', 'A102', '세번째 단계 알', 'USABLE', 'https://example.com/images/egg3.png', 'EGG', 'GRASSLAND', 50000L, NOW(), NOW());
