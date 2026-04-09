package com.IH;

public interface SQLCommandsTags {
    String GET_OR_CREATE_TAG =
            "WITH ins AS (" +
                    "    INSERT INTO tags (tag_name) VALUES (?) " +
                    "    ON CONFLICT (tag_name) DO NOTHING " +
                    "    RETURNING id" +
                    ") " +
                    "SELECT id FROM ins " +
                    "UNION ALL " +
                    "SELECT id FROM tags WHERE tag_name = ?";

    String ADD_TAG_TO_POST =
            "INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?) " +
                    "ON CONFLICT (post_id, tag_id) DO NOTHING";

    String GET_TAGS_BY_POST =
            "SELECT t.id, t.tag_name " +
                    "FROM tags t " +
                    "JOIN post_tags pt ON t.id = pt.tag_id " +
                    "WHERE pt.post_id = ?";

    String GET_ALL_TAGS =
            "SELECT t.id, t.tag_name, COUNT(pt.post_id) as posts_count " +
                    "FROM tags t " +
                    "LEFT JOIN post_tags pt ON t.id = pt.tag_id " +
                    "GROUP BY t.id " +
                    "ORDER BY posts_count DESC, t.tag_name";

    String GET_POST_IDS_BY_TAG =
            "SELECT p.id " +
                    "FROM posts p " +
                    "JOIN post_tags pt ON p.id = pt.post_id " +
                    "JOIN tags t ON pt.tag_id = t.id " +
                    "WHERE t.tag_name = ? AND p.status = 'APPROVED'";
}
