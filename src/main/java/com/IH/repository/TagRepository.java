package com.IH.repository;

import com.IH.SQLCommandsComments;
import com.IH.model.dto.responce.TagDto;
import com.IH.service.SQLCommandsTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagRepository {
    private final Connection connection;

    @Autowired
    public TagRepository(Connection connection) {
        this.connection = connection;
    }

    public Long getOrCreateTag(String tagName) throws SQLException {
        String normalized = tagName.toLowerCase().trim();

        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsTags.GET_OR_CREATE_TAG)) {
            ps.setString(1, normalized);
            ps.setString(2, normalized);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to get or create tag: " + tagName);
    }

    public void addTagToPost(Long id, Long postId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsTags.ADD_TAG_TO_POST)) {
            ps.setLong(1, id);
            ps.setLong(2, postId);
            ps.executeUpdate();
        }
    }

    public List<String> getTagsByPost(Long postId) throws SQLException {
        List<String> tags = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsTags.GET_TAGS_BY_POST)) {
            ps.setLong(1, postId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tags.add(rs.getString("tag_name"));
                }
            }
        }
        return tags;
    }

    public List<TagDto> getAllTags() throws SQLException {
        List<TagDto> tags = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsTags.GET_ALL_TAGS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TagDto tag = new TagDto();
                tag.setName(rs.getString("tag_name"));
                tag.setId(rs.getLong("tag_id"));
                tag.setPostsCount(rs.getInt("posts_count"));
                tags.add(tag);

            }
        }
        return tags;
    }

    public List<Long> getPostIdsByTagId(String tagName) throws SQLException {
        List<Long> postIds = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(SQLCommandsTags.GET_POST_IDS_BY_TAG)) {
            ps.setString(1, tagName.toLowerCase().trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    postIds.add(rs.getLong("post_id"));
                }
            }
        }
        return postIds;
    }
}
