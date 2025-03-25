SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user_token;
DROP TABLE IF EXISTS oauth_accounts;
DROP TABLE IF EXISTS running_records;
DROP TABLE IF EXISTS user_item;
DROP TABLE IF EXISTS incubator;
DROP TABLE IF EXISTS user_runimo;
DROP TABLE IF EXISTS item_activity;
DROP TABLE IF EXISTS runimo;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `users` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `public_id` VARCHAR(255),
                         `nickname` VARCHAR(255),
                         `img_url` VARCHAR(255),
                         `total_distance_in_meters` BIGINT NOT NULL DEFAULT 0,
                         `total_time_in_seconds` BIGINT NOT NULL DEFAULT 0,
                         `updated_at` TIMESTAMP,
                         `created_at` TIMESTAMP,
                         `deleted_at` TIMESTAMP
);

CREATE TABLE `user_token` (
                              `user_id` BIGINT NOT NULL,
                              `device_token` VARCHAR(255) NOT NULL,
                              `created_at` TIMESTAMP,
                              `updated_at` TIMESTAMP,
                              `deleted_at` TIMESTAMP,
                              FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
);

CREATE TABLE `oauth_accounts` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `user_id` BIGINT NOT NULL,
                                  `provider` VARCHAR(255),
                                  `provider_id` VARCHAR(255),
                                  `created_at` TIMESTAMP,
                                  `updated_at` TIMESTAMP,
                                  `deleted_at` TIMESTAMP,
                                  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
);

CREATE TABLE `running_records` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   `user_id` BIGINT NOT NULL,
                                   `record_public_id` VARCHAR(255) NOT NULL,
                                   `start_at` TIMESTAMP,
                                   `end_at` TIMESTAMP,
                                   `total_distance` BIGINT,
                                   `pace_in_milli_seconds` BIGINT,
                                   `created_at` TIMESTAMP,
                                   `updated_at` TIMESTAMP,
                                   `deleted_at` TIMESTAMP
);

CREATE TABLE `items` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `name` VARCHAR(255) NOT NULL,
                         `item_code` VARCHAR(255) NOT NULL,
                         `description` VARCHAR(255),
                         `item_type` VARCHAR(255) NOT NULL,
                         `img_url` VARCHAR(255),
                         `dtype` VARCHAR(255),
                         `egg_type` VARCHAR(255),
                         `hatch_require_amount` BIGINT,
                         `created_at` TIMESTAMP,
                         `updated_at` TIMESTAMP,
                         `deleted_at` TIMESTAMP
);

CREATE TABLE `item_activity` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `activity_user_id` BIGINT NOT NULL,
                                 `activity_item_id` BIGINT NOT NULL,
                                 `activity_event_type` VARCHAR(255) NOT NULL,
                                 `created_at` TIMESTAMP,
                                 `updated_at` TIMESTAMP,
                                 `deleted_at` TIMESTAMP
);

CREATE TABLE `user_item` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `user_id` BIGINT NOT NULL,
                             `item_id` BIGINT NOT NULL,
                             `quantity` BIGINT NOT NULL,
                             `created_at` TIMESTAMP,
                             `updated_at` TIMESTAMP,
                             `deleted_at` TIMESTAMP
);

CREATE TABLE `incubator` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `user_id` BIGINT NOT NULL,
                             `slot` BIGINT NOT NULL,
                             `egg_id` BIGINT NOT NULL,
                             `progress` BIGINT DEFAULT 0,
                             `created_at` TIMESTAMP,
                             `updated_at` TIMESTAMP,
                             `deleted_at` TIMESTAMP
);

CREATE TABLE `runimo` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `name` VARCHAR(255),
                          `description` VARCHAR(255),
                          `type` VARCHAR(255) NOT NULL,
                          `created_at` TIMESTAMP,
                          `updated_at` TIMESTAMP,
                          `deleted_at` TIMESTAMP
);

CREATE TABLE `user_runimo` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                               `user_id` BIGINT NOT NULL,
                               `runimo_id` BIGINT NOT NULL,
                               `created_at` TIMESTAMP,
                               `updated_at` TIMESTAMP,
                               `deleted_at` TIMESTAMP
);

ALTER TABLE `user_token` ADD FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE;
ALTER TABLE `oauth_accounts` ADD FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE;