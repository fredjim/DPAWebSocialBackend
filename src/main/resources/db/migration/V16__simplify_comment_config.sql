-- V16__simplify_comment_config.sql
-- Replace the comment_config lookup table with a simple boolean on post.
-- Two states only: comments enabled (true) or disabled (false).

ALTER TABLE public.post ADD COLUMN comments_enabled BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE public.post DROP COLUMN comment_config_id;
DROP TABLE public.comment_config;
