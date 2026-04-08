package com.IH.service;

import com.IH.model.dto.responce.PostResponse;
import com.IH.model.dto.PostStatus;
import com.IH.model.dto.request.CreatePostRequest;
import com.IH.model.dto.responce.PostDto;
import com.IH.repository.PostRepository;
import com.IH.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;

    }

    public Optional<Long> createPost(CreatePostRequest request, Long userId) {
        try {
            Long postId = postRepository.createPost(
                    request.getTitle(),
                    request.getContent(),
                    userId,
                    PostStatus.PENDING);

            if (postId != null && request.getTags() != null && !request.getTags().isEmpty()) {
                addTagsToPost(postId, request.getTags());
            }

            return Optional.ofNullable(postId);
        } catch (SQLException e) {
            System.out.println("SQLException in DB: " + e.getMessage());//TODO log
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void addTagsToPost(Long postId, List<String>tags){
        List<String>uniqueTags= tags.stream()
                .map(t->t.toLowerCase().trim())
                .distinct()
                .limit(5)
                .toList();
        for(String tagName:uniqueTags){
            try{
                Long tagId = tagRepository.getOrCreateTag(tagName);
                tagRepository.addTagToPost(postId, tagId);
                log.debug("Tag '{}' added to post {}", tagName, postId);
            }catch (SQLException e){
                log.warn("Failed to add tag '{}' to post {}", tagName, postId, e);
            }
        }
    }

    public List<PostDto> getAllPublishedPosts(Long userId) {
        try {
            List<PostDto> posts = postRepository.getAllPublishedPosts(userId);
            for(PostDto post:posts){
                List<String> tags = tagRepository.getTagsByPost(post.getId());
                post.setTags(tags);
            }
            return posts;
        } catch (SQLException e) {
            log.error("Error getting all published posts", e);
            return List.of();
        }
    }

    public Optional<PostDto> getPostById(Long postId, Long userId) {
        try {
            Optional<PostDto> posts = postRepository.getPostById(postId, userId);
            if(posts.isPresent()){
                PostDto post = posts.get();
                List<String> tags = tagRepository.getTagsByPost(postId);
                post.setTags(tags);
            }
            return posts;
        } catch (SQLException e) {
            log.error("Error getting post by id = {}", postId, e);
            return Optional.empty();
        }
    }

    public boolean addLike(Long postId, Long userId) {
        try {
            postRepository.removeDislike(postId, userId);
            if (postRepository.checkUserLike(postId, userId)) {
                return false;
            }
            return postRepository.addLike(postId, userId);
        } catch (SQLException e) {
            System.out.println("SQLException add like: " + e.getMessage());//TODO log
            e.printStackTrace();
            return false;
        }
    }

    public boolean addDislike(Long postId, Long userId) {
        try {
            postRepository.removeLike(postId, userId);
            if (postRepository.checkUserDilLike(postId, userId)) {
                return false;
            }
            return postRepository.addDislike(postId, userId);
        } catch (SQLException e) {
            System.out.println("SQLException add disLike: " + e.getMessage());//TODO log
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeReaction(Long postId, Long userId) {
        try {
            boolean removeLike = postRepository.removeLike(postId, userId);
            boolean removeDislike = postRepository.removeDislike(postId, userId);
            return removeLike || removeDislike;
        } catch (SQLException e) {
            System.out.println("SQLException removeReaction: " + e.getMessage());//TODO log
            e.printStackTrace();
            return false;
        }
    }

    public List<PostResponse> getFeed() throws SQLException {
        return postRepository.findAll();
    }
}
