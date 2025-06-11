-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters,
                   total_time_in_seconds, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 3000, 3600, NOW(),
        NOW());
SET FOREIGN_KEY_CHECKS = 1;



TRUNCATE TABLE running_record;

INSERT INTO running_record (id, user_id, record_public_id, title, started_at, end_at,
                            total_distance, total_time_in_seconds, pace_in_milli_seconds,
                            is_rewarded, created_at, updated_at)
VALUES (1, 1, 'record-public-id-1', 'record-title-1', '2025-03-31 10:00:00', '2025-03-31 11:00:00',
        1000, 3600, 100, false, NOW(), NOW()),
       (2, 1, 'record-public-id-2', 'record-title-2', '2025-04-01 10:00:00', '2025-04-01 11:00:00',
        2000, 3600, 200, false, NOW(), NOW()),
       (3, 1, 'record-public-id-3', 'record-title-3', '2025-04-02 10:00:00', '2025-04-02 11:00:00',
        3000, 3600, 300, false, NOW(), NOW()),
       (4, 1, 'record-public-id-4', 'record-title-4', '2025-04-03 10:00:00', '2025-04-03 11:00:00',
        4000, 3600, 400, false, NOW(), NOW()),
       (5, 1, 'record-public-id-5', 'record-title-5', '2025-04-04 10:00:00', '2025-04-04 11:00:00',
        5000, 3600, 500, false, NOW(), NOW()),
       (6, 1, 'record-public-id-6', 'record-title-6', '2025-04-05 10:00:00', '2025-04-05 11:00:00',
        6000, 3600, 600, false, NOW(), NOW()),
       (7, 1, 'record-public-id-7', 'record-title-7', '2025-04-06 10:00:00', '2025-04-06 11:00:00',
        7000, 3600, 700, false, NOW(), NOW()),
       (8, 1, 'record-public-id-8', 'record-title-8', '2025-04-06 10:00:00', '2025-04-06 11:00:00',
        7000, 3600, 700, false, NOW(), NOW());
