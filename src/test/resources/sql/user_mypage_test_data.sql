-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters,
                   total_time_in_seconds, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, NOW(),
        NOW());
SET FOREIGN_KEY_CHECKS = 1;

TRUNCATE TABLE oauth_account;
INSERT INTO oauth_account (id, created_at, deleted_at, updated_at, provider, provider_id, user_id)
    VALUES (1, NOW(), null, NOW(), 'KAKAO', 1234, 1);

-- 보유 아이템
TRUNCATE TABLE user_item;
INSERT INTO user_item (id, user_id, item_id, quantity, created_at, updated_at)
VALUES (1001, 1, 1, 2, NOW(), NOW()),
       (1002, 1, 2, 1, NOW(), NOW());

TRUNCATE TABLE user_love_point;
INSERT INTO user_love_point (id, user_id, amount, created_at, updated_at)
VALUES (1001, 1, 0, NOW(), NOW());


TRUNCATE TABLE running_record;
INSERT INTO running_record (id, user_id, record_public_id, title, started_at, end_at,
                            total_distance, pace_in_milli_seconds, is_rewarded, created_at,
                            updated_at)
VALUES (1, 1, 'record-public-id-1', 'record-title-1', '2025-03-20 13:00:00', '2025-03-20 13:00:00',
        1234, 6666, false, NOW(), NOW()),
       (2, 1, 'record-public-id-2', 'record-title-2', '2025-03-29 13:00:00', '2025-03-29 14:00:00',
        2345, 6700, false, NOW(), NOW());

