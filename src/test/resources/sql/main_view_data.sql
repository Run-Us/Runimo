-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters,
                   total_time_in_seconds, created_at,
                   updated_at, main_runimo_id)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 3000, 3600, NOW(),NOW(),1),
       (2, 'test-user-uuid-2', 'HEHH', 'https://example.com/images/user2.png', 3000, 3600, NOW(), NOW(), null);
SET FOREIGN_KEY_CHECKS = 1;

TRUNCATE TABLE item;
INSERT INTO item (name, item_code, description, item_type, img_url, dtype, egg_type,
                  hatch_require_amount, created_at,
                  updated_at)
VALUES ('마당알', 'A100', '마당알: 기본 알', 'USABLE', 'example.url', 'EGG', 'MADANG', 10, NOW(), NOW());

INSERT INTO item (name, item_code, description, item_type, img_url, dtype, egg_type,
                  hatch_require_amount, created_at,
                  updated_at)
VALUES ('숲알', 'A101', '숲알: 기본 알', 'USABLE', 'example1.url', 'EGG', 'FOREST', 20, NOW(), NOW());

TRUNCATE TABLE runimo_definition;
INSERT INTO runimo_definition (id, name, code, description, img_url, egg_type, created_at,
                               updated_at)
VALUES (1, '토끼', 'R-101', '마당 토끼예여', 'http://dummy1', 'MADANG', NOW(), NOW()),
       (2, '강아지', 'R-102', '마당 강아지예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
       (3, '오리', 'R-103', '마당 오리예여', 'http://dummy3', 'MADANG', NOW(), NOW()),
       (4, '늑대', 'R-104', '주인이 다른 마당 늑대예여', 'http://dummy4', 'MADANG', NOW(), NOW());


TRUNCATE TABLE running_record;
INSERT INTO running_record (id, user_id, record_public_id, title, started_at, end_at,
                            total_distance, pace_in_milli_seconds, is_rewarded, created_at,
                            updated_at)
VALUES (1, 1, 'record-public-id-1', 'record-title-1', NOW(), NOW(), 1000, 100, false, NOW(), NOW()),
       (2, 1, 'record-public-id-2', 'record-title-2', NOW(), NOW(), 2000, 200, false, NOW(), NOW());

TRUNCATE TABLE runimo;
INSERT INTO runimo (id, user_id, runimo_definition_id, total_run_count, total_distance_in_meters, created_at, updated_at, deleted_at)
VALUES ( 1, 1, 1, 2, 3456, NOW(), NOW(), null );


-- 보유 아이템
TRUNCATE TABLE user_item;
INSERT INTO user_item (id, user_id, item_id, quantity, created_at, updated_at)
VALUES (1001, 1, 1, 2, NOW(), NOW()),
       (1002, 1, 2, 1, NOW(), NOW()),
        (1003, 2, 1, 3, NOW(), NOW()),
       (1004, 2, 2, 4, NOW(), NOW());

TRUNCATE TABLE user_love_point;
INSERT INTO user_love_point (id, user_id, amount, created_at, updated_at)
VALUES (1001, 1, 100, NOW(), NOW()),
       (2001, 2, 23, NOW(), NOW());