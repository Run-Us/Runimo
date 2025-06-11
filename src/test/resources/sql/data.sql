TRUNCATE TABLE item;

INSERT INTO item (name, item_code, description, item_type, img_url, dtype, egg_type_id,
                  hatch_require_amount, created_at,
                  updated_at)
VALUES ('마당알', 'A100', '마당알: 기본 알', 'USABLE',
        'https://runimo.s3.ap-northeast-2.amazonaws.com/R-100_Egg.png', 'EGG', 1, 10, NOW(),
        NOW());

INSERT INTO item (name, item_code, description, item_type, img_url, dtype, egg_type_id,
                  hatch_require_amount, created_at,
                  updated_at)
VALUES ('숲알', 'A101', '숲알: 기본 알', 'USABLE',
        'https://runimo.s3.ap-northeast-2.amazonaws.com/R-110_Egg.png', 'EGG', 2, 20, NOW(),
        NOW());
