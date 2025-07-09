-- INACTIVE_USERS 조건 테스트를 위한 데이터
-- 1주일 내 달리기 기록이 없는 사용자 = 비활성 사용자

-- 사용자 데이터 (5명)
INSERT INTO users (id, public_id, nickname, created_at, updated_at)
VALUES (1, 'active-user-1', '활성사용자1', '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (2, 'active-user-2', '활성사용자2', '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (3, 'active-user-3', '활성사용자3', '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (4, 'inactive-user-1', '비활성사용자1', '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (5, 'inactive-user-2', '비활성사용자2', '2025-01-01 00:00:00', '2025-01-01 00:00:00');

INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters,
                   total_time_in_seconds, created_at,
                   updated_at, role)
VALUES (11, 'admin-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600,
        NOW(),
        NOW(), 'ADMIN');


-- 사용자별 device token (알림 허용된 사용자들만)
INSERT INTO user_token (user_id, device_token, platform, notification_allowed, created_at,
                        updated_at)
VALUES (1, 'active-device-token-1', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (2, 'active-device-token-2', 'APNS', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (3, 'active-device-token-3', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (4, 'inactive-device-token-1', 'FCM', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00'),
       (5, 'inactive-device-token-2', 'APNS', true, '2025-01-01 00:00:00', '2025-01-01 00:00:00');


