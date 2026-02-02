package com.IH.repository;

import com.IH.SQLCommands;
import com.IH.model.dto.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.IH.SQLCommands.CREATE_POST;

@Repository
public class PostRepository {
    private final Connection connection;

    @Autowired
    public PostRepository(Connection connection) {
        this.connection = connection;
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
        try (PreparedStatement ps = connection.prepareStatement(SQLCommands.GET_ALL_POSTS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PostResponse post = new PostResponse();//один пост
                post.setId(rs.getLong("id"));
                post.setPostName(rs.getString("post_name"));
                post.setTitle(rs.getString("title"));
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

