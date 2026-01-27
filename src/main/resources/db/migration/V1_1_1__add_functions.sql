CREATE OR REPLACE FUNCTION toggle_like_dislike(post_id BIGINT, user_id BIGINT)
    RETURNS VOID AS
$$
BEGIN
    IF EXISTS (SELECT 1
               FROM user_likes
               WHERE post_id = toggle_like_dislike.post_id
                 AND user_id = toggle_like_dislike.user_id) THEN
DELETE FROM user_likes WHERE post_id = toggle_like_dislike.post_id AND user_id = toggle_like_dislike.user_id;
INSERT INTO user_dislikes (post_id, user_id) VALUES (toggle_like_dislike.post_id, toggle_like_dislike.user_id);
ELSE
        IF EXISTS (SELECT 1
                   FROM user_dislikes
                   WHERE post_id = toggle_like_dislike.post_id
                     AND user_id = toggle_like_dislike.user_id) THEN

DELETE
FROM user_dislikes
WHERE post_id = toggle_like_dislike.post_id
  AND user_id = toggle_like_dislike.user_id;
INSERT INTO user_likes (post_id, user_id) VALUES (toggle_like_dislike.post_id, toggle_like_dislike.user_id);
ELSE
            INSERT INTO user_likes (post_id, user_id) VALUES (toggle_like_dislike.post_id, toggle_like_dislike.user_id);
END IF;
END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_rating_for_post_with_likes(post_id BIGINT)
    RETURNS INTEGER AS $$
BEGIN
    -- Получаем количество лайков и дизлайков для поста
RETURN (
    SELECT
        (SELECT COUNT(*) FROM user_likes WHERE post_id = get_rating_for_post_with_likes.post_id) -
        (SELECT COUNT(*) FROM user_dislikes WHERE post_id = get_rating_for_post_with_likes.post_id)
);
END;
$$ LANGUAGE plpgsql;