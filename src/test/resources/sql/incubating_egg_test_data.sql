SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
TRUNCATE TABLE item;
TRUNCATE TABLE incubating_egg;
TRUNCATE TABLE user_item;
TRUNCATE TABLE user_love_point;
SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters, total_time_in_seconds, created_at, updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 3000, 3600, NOW(), NOW());

INSERT INTO user_love_point (id, user_id, amount, created_at, updated_at)
VALUES (1, 1, 20, NOW(), NOW());

-- 보유 아이템
INSERT INTO user_item (id, user_id, item_id, quantity, created_at, updated_at)
VALUES (1001, 1, 1, 2, NOW(), NOW()),
       (1002, 1, 2, 1, NOW(), NOW());

INSERT INTO item (id, name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at, updated_at)
VALUES (1, '마당알', 'A100', '기본 알', 'USABLE', 'https://example.com/images/egg.png', 'EGG', 'MADANG', 100, NOW(), NOW());

INSERT INTO incubating_egg (id, user_id, egg_id, current_love_point_amount, hatch_require_amount, egg_status, created_at, updated_at)
VALUES (1, 1, 1, 50, 100, 'INCUBATING', NOW(), NOW());
