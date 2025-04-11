-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters, total_time_in_seconds, main_runimo_id, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, 1, NOW(), NOW()),
       (2, 'test-user-uuid-2', 'Moon', 'https://example.com/images/user2.png', 5000, 1800, null, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;

-- 러니모 - 정적 데이터
-- TRUNCATE TABLE runimo_definition;
-- INSERT INTO runimo_definition (id, name, code, description, img_url, egg_type, created_at, updated_at)
-- VALUES (1, '토끼', 'R-101', '마당 토끼예여', 'http://dummy1', 'MADANG', NOW(), NOW()),
--        (2, '강아지', 'R-102', '마당 강아지예여', 'http://dummy2', 'MADANG', NOW(), NOW()),
--        (3, '오리', 'R-103', '마당 오리예여', 'http://dummy3', 'MADANG', NOW(), NOW()),
--        (4, '늑대', 'R-104', '주인이 다른 마당 늑대예여', 'http://dummy4', 'MADANG', NOW(), NOW());

-- 러니모
TRUNCATE TABLE runimo;
INSERT INTO runimo (id, user_id, runimo_definition_id, total_run_count, total_distance_in_meters, created_at, updated_at)
VALUES (1, 1, 1, 3, 1000, NOW(), NOW()),
       (2, 1, 2, 0, 0, NOW(), NOW()),
       (3, 1, 3, 0, 0, NOW(), NOW()),
       (4, 2, 4, 0, 0, NOW(), NOW());