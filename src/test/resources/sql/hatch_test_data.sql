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

