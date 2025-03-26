-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters, total_time_in_seconds, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, NOW(), NOW()),
       (2, 'test-user-uuid-2', 'Moon', 'https://example.com/images/user2.png', 5000, 1800, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;


TRUNCATE TABLE items;
INSERT INTO items (name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at,
                   updated_at)
VALUES ('마당알', 'A100', '마당알: 기본 알', 'USABLE', 'example.url', 'Egg', 'MADANG', 10, NOW(), NOW());

INSERT INTO items (name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at,
                   updated_at)
VALUES ('숲알', 'A101', '숲알: 기본 알', 'USABLE', 'example1.url', 'Egg', 'FOREST', 20, NOW(), NOW());


-- 보유 아이템
TRUNCATE TABLE user_item;
INSERT INTO user_item (id, user_id, item_id, quantity, created_at, updated_at)
VALUES (1001, 1, 1, 2, NOW(), NOW()),
       (1002, 1, 2, 1, NOW(), NOW())
