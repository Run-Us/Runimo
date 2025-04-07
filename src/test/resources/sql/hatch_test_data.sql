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
VALUES (1, 1, 1, 100, 100, 'INCUBATED', NOW(), NOW()),
       (2, 1, 2, 50, 100, 'INCUBATING', NOW(), NOW());

-- 러니모
TRUNCATE TABLE runimo_definition;
INSERT INTO runimo_definition (id, name, code, description, img_url, egg_type, created_at, updated_at)
VALUES (1, '토끼', 'R-101', '마당 토끼예여', 'http://dummy1', 'MADANG', NOW(), NOW()),
       (2, '강아지', 'R-102', '이미 소유하고 있는 마당 강아지예여', 'http://dummy2', 'MADANG', NOW(), NOW());


-- 사용자-러니모 맵핑
TRUNCATE TABLE runimo;
INSERT INTO runimo (id, user_id, runimo_definition_id, created_at, updated_at)
VALUES (1, 1, 2, NOW(), NOW());