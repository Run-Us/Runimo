ALTER TABLE `signup_token`
    ADD COLUMN `version` BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `signup_token`
    ADD COLUMN `used` BOOLEAN NOT NULL DEFAULT FALSE;

-- 기존 토큰들은 사용되지 않은 것으로 처리
UPDATE `signup_token`
SET `used`    = FALSE,
    `version` = 0;