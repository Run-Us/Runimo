-- 테스트용 기본 유저 (기존에 있다고 가정)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters,
                   total_time_in_seconds, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, NOW(),
        NOW());
SET FOREIGN_KEY_CHECKS = 1;

TRUNCATE TABLE oauth_account;
INSERT INTO oauth_account (id, created_at, deleted_at, updated_at, provider, provider_id, user_id)
VALUES (1, NOW(), null, NOW(), 'KAKAO', 1234, 1);

-- 기존 디바이스 토큰 데이터
INSERT INTO user_token (id, user_id, device_token, platform, notification_allowed, created_at,
                        updated_at)
VALUES (1, 1, 'existing_device_token_12345', 'FCM', true, NOW(), NOW());

-- 보유 애정
INSERT INTO user_love_point (id, user_id, amount, created_at, updated_at)
VALUES (1, 1, 0, NOW(), NOW());

-- 보유 아이템
INSERT INTO user_item (id, user_id, item_id, quantity, created_at, updated_at)
VALUES (1001, 1, 1, 0, NOW(), NOW()),
       (1002, 1, 2, 0, NOW(), NOW());




