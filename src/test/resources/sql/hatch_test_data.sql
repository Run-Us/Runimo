-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters, total_time_in_seconds, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, NOW(), NOW()),
       (2, 'test-user-uuid-2', 'Moon', 'https://example.com/images/user2.png', 5000, 1800, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;

-- 부화할 알
TRUNCATE TABLE incubating_egg;
INSERT INTO incubating_egg (id, user_id, egg_id, current_love_point_amount, hatch_require_amount, egg_status, created_at, updated_at)
VALUES (1, 1, 1, 0L, 0L, 'INCUBATED', NOW(), NOW()),
       (2, 1, 2, 50, 30000L, 'INCUBATING', NOW(), NOW()),
       (3, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (4, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (5, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (6, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (7, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (8, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (9, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW()),
       (10, 1, 2, 30000L, 30000L, 'INCUBATED', NOW(), NOW());



-- Item (Egg) - 정적데이터
-- TRUNCATE TABLE item;
-- INSERT INTO item (id, name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at, updated_at)
-- VALUES (1, '마당', 'A100', '기본 알', 'USABLE', 'https://example.com/images/egg1.png', 'EGG', 'MADANG', 0L, NOW(), NOW()),
--        (2, '숲', 'A101', '두번째 단계 알', 'USABLE', 'https://example.com/images/egg2.png', 'EGG', 'FOREST', 30000L, NOW(), NOW()),
--        (3, '초원', 'A102', '세번째 단계 알', 'USABLE', 'https://example.com/images/egg3.png', 'EGG', 'GRASSLAND', 50000L, NOW(), NOW());

-- 러니모 - 정적 데이터
-- TRUNCATE TABLE runimo_definition;
-- INSERT INTO runimo_definition (id, name, code, description, img_url, egg_type, created_at, updated_at)
-- VALUES (1, '강아지', 'R-101', '마당-강아지예여', 'http://dummy1', 'MADANG', NOW(), NOW()),
--        (2, '고양이', 'R-102', '마당-고양이예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
--        (3, '토끼', 'R-103', '마당-토끼예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
--        (4, '오리', 'R-104', '마당-오리예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
--        (5, '늑대 강아지', 'R-105', '늑대 강아지예여', 'http://dummy2', 'FOREST', NOW(), NOW()),
--        (6, '숲 고양이', 'R-106', '숲 고양이예여', 'http://dummy2', 'FOREST', NOW(), NOW()),
--        (7, '나뭇잎 토끼', 'R-107', '나뭇잎 토끼예여', 'http://dummy2', 'FOREST', NOW(), NOW()),
--        (8, '숲 오리', 'R-108', '숲 오리예여', 'http://dummy2', 'FOREST', NOW(), NOW());

-- 사용자-러니모 맵핑
-- TRUNCATE TABLE runimo;
-- INSERT INTO runimo (id, user_id, runimo_definition_id, created_at, updated_at)
-- VALUES (1, 1, 2, NOW(), NOW());