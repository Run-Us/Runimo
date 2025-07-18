-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters, total_time_in_seconds, main_runimo_id, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, 1, NOW(), NOW()),
       (2, 'test-user-uuid-2', 'Moon', 'https://example.com/images/user2.png', 5000, 1800, null, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;

-- 보유 아이템
TRUNCATE TABLE user_item;
INSERT INTO user_item (id, user_id, item_id, quantity, created_at, updated_at)
VALUES (1001, 1, 1, 2, NOW(), NOW()),
       (1002, 1, 2, 1, NOW(), NOW());

TRUNCATE TABLE user_love_point;
INSERT INTO user_love_point (id, user_id, amount, created_at, updated_at)
VALUES (1001, 1, 0, NOW(), NOW());

-- 러니모
TRUNCATE TABLE runimo;
INSERT INTO runimo (id, user_id, runimo_definition_id, total_run_count, total_distance_in_meters, created_at, updated_at)
VALUES (1, 1, 1, 3, 1000, NOW(), NOW());