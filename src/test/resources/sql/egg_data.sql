TRUNCATE TABLE item;

INSERT INTO item (name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at,
                   updated_at)
VALUES ('마당알', 'A100', '마당알: 기본 알', 'USABLE', 'example.url', 'EGG', 'MADANG', 10, NOW(), NOW());

INSERT INTO item (name, item_code, description, item_type, img_url, dtype, egg_type, hatch_require_amount, created_at,
                   updated_at)
VALUES ('숲알', 'A101', '숲알: 기본 알', 'USABLE', 'example1.url', 'EGG', 'FOREST', 20, NOW(), NOW());