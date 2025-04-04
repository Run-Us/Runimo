SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user_token;
DROP TABLE IF EXISTS oauth_account;
DROP TABLE IF EXISTS running_record;
DROP TABLE IF EXISTS user_item;
DROP TABLE IF EXISTS incubator;
DROP TABLE IF EXISTS user_runimo;
DROP TABLE IF EXISTS item_activity;
DROP TABLE IF EXISTS runimo;
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
    `total_distance_in_meters` BIGINT NOT NULL DEFAULT 0,
    `total_time_in_seconds`    BIGINT NOT NULL DEFAULT 0,
    `main_runimo_id`           BIGINT,
    `updated_at`               TIMESTAMP,
    `created_at`               TIMESTAMP,
    `deleted_at`               TIMESTAMP
);

CREATE TABLE `user_token`
(
    `user_id`      BIGINT       NOT NULL,
    `device_token` VARCHAR(255) NOT NULL,
    `created_at`   TIMESTAMP,
    `updated_at`   TIMESTAMP,
    `deleted_at`   TIMESTAMP,

    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `user_love_point`
(
    `id`        BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`   BIGINT NOT NULL,
    `amount` BIGINT NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP,
    `updated_at` TIMESTAMP,
    `deleted_at` TIMESTAMP
);

CREATE TABLE `oauth_account`
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`     BIGINT NOT NULL,
    `provider`    VARCHAR(255),
    `provider_id` VARCHAR(255),
    `created_at`  TIMESTAMP,
    `updated_at`  TIMESTAMP,
    `deleted_at`  TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);


CREATE TABLE `running_record`
(
    `id`                    integer PRIMARY KEY AUTO_INCREMENT,
    `user_id`               integer      NOT NULL,
    `record_public_id`      VARCHAR(255) NOT NULL,
    `title`                 varchar(255),
    `started_at`            timestamp,
    `end_at`                timestamp,
    `total_distance`        integer,
    `pace_in_milli_seconds` integer,
    `is_rewarded`           boolean,
    `pace_per_km`           VARCHAR(10000),
    `created_at`            timestamp,
    `updated_at`            timestamp,
    `deleted_at`            TIMESTAMP
);

CREATE TABLE `item`
(
    `id`                   integer PRIMARY KEY AUTO_INCREMENT,
    `name`                 varchar(255) NOT NULL,
    `item_code`            varchar(255) NOT NULL,
    `description`          varchar(255),
    `item_type`            varchar(255) NOT NULL,
    `img_url`              varchar(255),
    `dtype`                varchar(255),
    `egg_type`             varchar(255),
    `hatch_require_amount` long,
    `created_at`           timestamp,
    `updated_at`           timestamp,
    `deleted_at`           TIMESTAMP
);

CREATE TABLE `item_activity`
(
    `id`                  integer PRIMARY KEY AUTO_INCREMENT,
    `activity_user_id`    integer      NOT NULL,
    `activity_item_id`    integer      NOT NULL,
    `activity_event_type` varchar(255) NOT NULL,
    `quantity`            integer,
    `created_at`          timestamp,
    `updated_at`          timestamp,
    `deleted_at`          TIMESTAMP
);

CREATE TABLE `user_item`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`    integer NOT NULL,
    `item_id`    integer NOT NULL,
    `quantity`   integer NOT NULL,
    `created_at` timestamp,
    `updated_at` timestamp,
    `deleted_at` TIMESTAMP
);

CREATE TABLE `incubating_egg`
(
    `id`
                                BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`                integer      NOT NULL,
    `egg_id`                 integer      NOT NULL,
    `current_love_point_amount` integer,
    `hatch_require_amount`   integer,
    `egg_status`             varchar(255),
    `created_at`             timestamp,
    `updated_at`             timestamp,
    `deleted_at`             TIMESTAMP
);

CREATE TABLE `runimo`
(
    `id`            BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`          varchar(255),
    `code`          varchar(255) NOT NULL,
    `description`   varchar(255),
    `img_url`       varchar(255),
    `egg_type`      varchar(255) NOT NULL,
    `created_at`    timestamp,
    `updated_at`    timestamp,
    `deleted_at`    timestamp
);

CREATE TABLE `user_runimo`
(
    `id`            BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`       BIGINT NOT NULL,
    `runimo_id`     BIGINT NOT NULL,
    `created_at`    timestamp,
    `updated_at`    timestamp,
    `deleted_at`    timestamp
);

ALTER TABLE `user_token`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `oauth_account`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
