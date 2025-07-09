ALTER TABLE user_token
    ADD CONSTRAINT uq_user_token_device_token UNIQUE (device_token);