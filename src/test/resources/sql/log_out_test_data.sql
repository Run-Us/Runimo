-- 사용자
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE users;
INSERT INTO users (id, public_id, nickname, img_url, total_distance_in_meters, total_time_in_seconds, created_at,
                   updated_at)
VALUES (1, 'test-user-uuid-1', 'Daniel', 'https://example.com/images/user1.png', 10000, 3600, NOW(), NOW()),
       (2, 'test-user-uuid-2', 'Daniel2', 'https://example.com/images/user2.png', 20000, 2600, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;

TRUNCATE TABLE oauth_account;
INSERT INTO oauth_account (id, user_id, provider, provider_id, created_at, updated_at)
VALUES (1, 1, 'KAKAO', 'test-oidc-token-1', NOW(), NOW()),
       (2, 2, 'APPLE', 'test-oidc-token-2', NOW(), NOW());