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
            log.warn("SQLException in DB: " + e.getMessage());
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

    public List<PostDto> getAllPostsByRating(Long currentUserId) {
        try {
            return postRepository.getAllPostsByRating(currentUserId);
        } catch (SQLException e) {
            log.error("Error getting posts by rating", e);
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
            log.error("SQLException add like: " + e.getMessage());
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
            log.error("SQLException add disLike: " + e.getMessage());
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
            log.error("SQLException removeReaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePost(Long postId) {
        try {
            return postRepository.deletePostById(postId);
        } catch (SQLException e) {
            log.error("Error deleting post: {}", postId, e);
            return false;
        }
    }

    public Optional<Long> getPostAuthorId(Long postId) {
        try {
            return postRepository.getPostAuthorId(postId);
        } catch (SQLException e) {
            log.error("Error getting post author: {}", postId, e);
            return Optional.empty();
        }
    }

    public List<PostDto> getPendingPosts() {
        try {
            return postRepository.getPendingPosts();
        } catch (SQLException e) {
            log.error("Error getting pending posts", e);
            return List.of();
        }
    }

    public boolean approvePost(Long postId) {
        try {
            return postRepository.updatePostStatus(postId, "APPROVED");
        } catch (SQLException e) {
            log.error("Error approving post: {}", postId, e);
            return false;
        }
    }

    public boolean rejectPost(Long postId) {
        try {
            return postRepository.updatePostStatus(postId, "REJECTED");
        } catch (SQLException e) {
            log.error("Error rejecting post: {}", postId, e);
            return false;
        }
    }
}
