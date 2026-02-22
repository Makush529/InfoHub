package com.IH.service;

import com.IH.model.dto.PostResponse;
import com.IH.model.dto.RequestPostDTO;
import com.IH.model.dto.rest.CreatePostRequest;
import com.IH.model.dto.rest.PostDto;
import com.IH.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;

    }

    public Optional<Long> createPost(CreatePostRequest request, Long userId) {
        try {
            Long postId = postRepository.createPost(
                    request.getTitle(),
                    request.getContent(),
                    userId);
            return Optional.ofNullable(postId);
        } catch (SQLException e) {
            System.out.println("SQLException in DB: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<PostDto> getAllPublishedPosts(Long userId) {
        try {
            return postRepository.getAllPublishedPosts(userId);
        } catch (SQLException e) {
            System.out.println("SQLException in DB: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<PostDto> getPostById(Long postId, Long userId) {
        try {
            return postRepository.getPostById(postId, userId);
        } catch (SQLException e) {
            System.out.println("SQLException in DB: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Transactional
    public boolean addLike(Long postId, Long userId) {
        try {
            postRepository.removeLike(postId, userId);
            return postRepository.addLike(postId, userId);
        } catch (SQLException e) {
            System.out.println("SQLException add like: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean addDislike(Long postId, Long userId) {
        try {
            postRepository.removeLike(postId, userId);
            return postRepository.addDislike(postId, userId);
        } catch (SQLException e) {
            System.out.println("SQLException add disLike: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean removeReaction(Long postId, Long userId){
        try {
            boolean removeLike = postRepository.removeLike(postId, userId);
            boolean removeDislike = postRepository.removeDislike(postId, userId);
            return removeLike || removeDislike;
        } catch (SQLException e) {
            System.out.println("SQLException removeReaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void createPostOld(RequestPostDTO dto, Long userId) throws SQLException {
        //проверка текста на мат или запрещенку
        postRepository.savePost(dto.getPostTitle(), dto.getText(), userId);
    }

    public List<PostResponse> getFeed() throws SQLException {
        return postRepository.findAll();
    }
}
