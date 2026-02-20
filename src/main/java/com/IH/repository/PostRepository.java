package com.IH.repository;

import com.IH.model.dto.PostResponse;
import com.IH.model.dto.rest.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.IH.SQLCommandsPosts.*;

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

    public List<PostDto> getAllPublishedPosts(Long currentUserId) throws SQLException {
        List<PostDto> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_PUBLISHED_POSTS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                PostDto post = mapResultSetToPostDto(resultSet);
                if (currentUserId != null) {
                    post.setUserLiked(checkUserLike(post.getId(), currentUserId));
                    post.setUserDisliked(checkUserDilLike(post.getId(), currentUserId));
                }
                posts.add(post);
            }
        }
        return posts;
    }

    public Optional<PostDto> getPostById(Long id, Long CurrentUserId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(GET_POST_BY_ID)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PostDto post = mapResultSetToPostDto(resultSet);
                    if (CurrentUserId != null) {
                        post.setUserLiked(checkUserLike(post.getId(), id));
                        post.setUserDisliked(checkUserDilLike(post.getId(), id));
                    }
                    return Optional.of(post);
                }
            }
        }return Optional.empty();
    }

    public boolean addLike(Long postId, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ADD_LIKE)) {
            statement.setLong(1, postId);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean addDislike(Long postId, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ADD_DISLIKE)) {
            statement.setLong(1, postId);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean removeLike(Long postId, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_LIKE)) {
            statement.setLong(1, postId);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean removeDislike(Long postId, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_DISLIKE)) {
            statement.setLong(1, postId);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean checkUserLike(Long postId, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CHECK_USER_LIKE)) {
            statement.setLong(1, postId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean checkUserDilLike(Long postId, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CHECK_USER_DISLIKE)) {
            statement.setLong(1, postId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
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

    private PostDto mapResultSetToPostDto(ResultSet resultSet) throws SQLException {
        PostDto post = new PostDto();
        post.setId(resultSet.getLong("id"));
        post.setTitle(resultSet.getString("post_title"));
        post.setContent(resultSet.getString("text"));
        post.setCreatedAt(resultSet.getDate("post_age").toLocalDate());
        post.setStatus(resultSet.getString("status"));
        post.setAuthorId(resultSet.getLong("author_id"));
        post.setAuthorName(resultSet.getString("author_name"));
        post.setLikesCount(resultSet.getInt("likes_count"));
        post.setDislikesCount(resultSet.getInt("dislikes_count"));
        post.setRating(resultSet.getInt("likes_count") - resultSet.getInt("dislikes_count"));
        return post;
    }
}


