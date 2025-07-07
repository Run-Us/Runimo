create table user_withdrawal_reason
(
    `id`            BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`       BIGINT      NOT NULL,
    `reason`        VARCHAR(32) NOT NULL,
    `custom_reason` VARCHAR(255)
);
