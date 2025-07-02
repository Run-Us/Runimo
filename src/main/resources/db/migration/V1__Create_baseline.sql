-- V1__Create_baseline.sql
-- Production baseline - existing schema as of 2025-06-24
-- This migration represents the current state of the production database
-- All existing tables are already present, this is just a baseline marker

-- Flyway baseline marker for existing production database
-- No actual schema changes in this file as tables already exist

-- Existing tables:
-- - users
-- - user_token
-- - user_love_point
-- - oauth_account
-- - apple_user_token
-- - signup_token
-- - running_record
-- - item
-- - egg_type
-- - item_activity
-- - user_item
-- - incubating_egg
-- - runimo_definition
-- - runimo
-- - user_refresh_token

-- This baseline allows us to start versioned migrations from V2 onwards
SELECT 'Baseline migration - existing schema marked as V1' as message;
