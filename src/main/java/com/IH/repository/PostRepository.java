package com.IH.repository;

import com.IH.model.dto.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.IH.SQLCommandsPosts.CREATE_POST;
import static com.IH.SQLCommandsPosts.GET_ALL_POSTS;

@Repository
public class PostRepository {
    private final Connection connection;

    @Autowired
    public PostRepository(Connection connection) {
        this.connection = connection;
    }

    public Long createPost(String title, String content, Long user_id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_POST, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setLong(3, user_id);
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        }
        return null;
    }

    public void savePost(String postName, String text, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_POST)) {
            statement.setString(1, postName);
            statement.setString(2, text);
            statement.setLong(3, userId);
            statement.executeUpdate();
        }
    }

    public List<PostResponse> findAll() throws SQLException {
        List<PostResponse> posts = new ArrayList<>();//список постов
        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_POSTS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PostResponse post = new PostResponse();//один пост
                post.setId(rs.getLong("id"));
                post.setPostTitle(rs.getString("post_title"));
                post.setText(rs.getString("text"));
                post.setAuthorName(rs.getString("username"));
                java.sql.Date dbDate = rs.getDate("post_age");
                if (dbDate != null) {
                    post.setPostAge(dbDate.toLocalDate());
                } else {
                    post.setPostAge(null); // или LocalDate.now(), если хочешь дату по умолчанию
                }
                posts.add(post);
            }
        }
        return posts;
    }
}

