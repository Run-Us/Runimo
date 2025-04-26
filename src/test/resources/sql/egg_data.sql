TRUNCATE TABLE item;
TRUNCATE TABLE egg_type;

INSERT INTO egg_type (id, name, code, required_distance_in_meters, level, created_at, updated_at)
VALUES (1, '마당', 'A100', 0L, 1, NOW(), NOW()),
       (2, '숲', 'A101', 30000L, 2, NOW(), NOW());

INSERT INTO item (id, name, item_code, description, item_type, img_url, dtype, egg_type_id,
                  hatch_require_amount, created_at, updated_at)
VALUES (1, '마당', 'A100', '기본 알', 'USABLE', 'https://example.com/images/egg1.png', 'EGG', 1,
        10, NOW(), NOW()),
       (2, '숲', 'A101', '두번째 단계 알', 'USABLE', 'https://example.com/images/egg2.png', 'EGG',
        2, 30, NOW(), NOW());
