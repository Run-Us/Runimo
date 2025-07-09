-- notification_all_users_test_data.sql
-- ALL_USERS 조건 테스트를 위한 데이터
-- 알림 허용된 모든 활성 사용자 대상

-- 사용자 데이터 (7명 - 활성 5명, 삭제된 사용자 2명)
INSERT INTO users (id, public_id, nickname, created_at, updated_at, deleted_at)
VALUES (1, 'active-user-1', '활성사용자1', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
       (2, 'active-user-2', '활성사용자2', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
       (3, 'active-user-3', '활성사용자3', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
       (4, 'active-user-4', '활성사용자4', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
       (5, 'active-user-5', '활성사용자5', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
-- 삭제된 사용자들 (deleted_at이 NULL이 아님)
       (6, 'deleted-user-1', '삭제된사용자1', '2025-01-01 00:00:00', '2025-01-01 00:00:00',
        '2025-01-02 00:00:00'),
       (7, 'deleted-user-2', '삭제된사용자2', '2025-01-01 00:00:00', '2025-01-01 00:00:00',
        '2025-01-03 00:00:00');

INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters,
                   total_time_in_seconds, created_at,
                   updated_at, role)
VALUES (11, 'admin-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600,
        NOW(),
        NOW(), 'ADMIN');

-- 사용자별 device token
-- 활성 사용자 5명은 모두 알림 허용, 삭제된 사용자는 제외
INSERT INTO user_token (user_id, device_token, platform, notification_allowed, created_at,
                        updated_at)
VALUES (1, 'active-device-token-1', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (2, 'active-device-token-2', 'APNS', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (3, 'active-device-token-3', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (4, 'active-device-token-4', 'APNS', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (5, 'active-device-token-5', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
-- 삭제된 사용자들의 토큰 (대상에서 제외되어야 함)
       (6, 'deleted-device-token-1', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (7, 'deleted-device-token-2', 'APNS', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00');
