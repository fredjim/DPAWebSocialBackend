-- V16__simplify_comment_config.sql
-- Replace the comment_config lookup table with a simple boolean on post.
-- Two states only: comments enabled (true) or disabled (false).

ALTER TABLE public.post ADD COLUMN comments_enabled BOOLEAN NOT NULL DEFAULT TRUE;

-- Posts with RESTRICTED_COMMENTS are the only ones that become false.
-- FREE_COMMENTS and MODERATED_COMMENTS both map to true (already the default).
  UPDATE public.post p
  SET comments_enabled = FALSE
  FROM public.comment_config cc
  WHERE p.comment_config_id = cc.uuid
    AND cc.configuration_type = 'RESTRICTED_COMMENTS';

ALTER TABLE public.post DROP COLUMN comment_config_id;
DROP TABLE public.comment_config;
