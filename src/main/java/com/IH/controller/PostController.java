package com.IH.controller;

import com.IH.model.dto.PostResponse;
import com.IH.model.dto.RequestPostDTO;
import com.IH.model.dto.rest.CreatePostRequest;
import com.IH.model.dto.rest.PostDto;
import com.IH.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Managing posts: creating, viewing, liking")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    @Operation(summary = "Create Post"
            , description = "Creates a post and submits it for moderation. Authorization required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "The post has been successfully created and sent for moderation."),
            @ApiResponse(responseCode = "401"
                    , description = "The user is not authorized"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest postRequest, HttpSession session) {
        log.info("IN:" + postRequest.getTitle());
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            log.warn("UNAUTHORIZED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }
        Optional<Long> postId = postService.createPost(postRequest, userId);
        if (postId.isPresent()) {
            log.info("Post has been successfully created and sent for moderation.");
            return ResponseEntity.status(HttpStatus.OK).body("Post created, id: " + postId);
        } else {
            log.warn("Post could not be created");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post could not be created");
        }
    }

    @GetMapping()
    @Operation(summary = "Get all published posts",
            description = "Returns a list of posts with the APPROVED status. " +
                    "For authorized users, it also shows whether the user has liked or disliked the post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of posts has been successfully retrieved.")
    })
    public ResponseEntity<List<PostDto>> getAllPosts(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        List<PostDto> posts = postService.getAllPublishedPosts(userId);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find post by id"
            , description = "Returns detailed information about a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        Optional<PostDto> post = postService.getPostById(id, userId);
        if (post.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(post.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Like"
            , description = "Adds a like to the post. If the user already disliked it,"
            + " it is replaced with a like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like successfully placed"),
            @ApiResponse(responseCode = "401", description = "Authorization required"),
            @ApiResponse(responseCode = "400", description = "Error placing like")
    })
    public ResponseEntity<?> addLike(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.addLike(id, userId);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }

    @PostMapping("/{id}/dislike")
    @Operation(summary = "Dislike"
            , description = "Adds a Dislike to the post. If the user already liked it,"
            + " it is replaced with a Dislike")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dislike successfully placed"),
            @ApiResponse(responseCode = "401", description = "Authorization required"),
            @ApiResponse(responseCode = "400", description = "Error placing like")
    })
    public ResponseEntity<?> removeLike(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.addDislike(id, userId);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
    }

    @PostMapping("/{id}/reaction")
    @Operation(summary = "Deleted reaction", description = "Removes a user's like or dislike from a post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reaction deleted"),
            @ApiResponse(responseCode = "401", description = "Authorization required"),
            @ApiResponse(responseCode = "400", description = "No reaction found to remove")
    })
    public ResponseEntity<?> removeReaction(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        boolean success = postService.removeReaction(id, userId);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);//проверить, может 404
        }
    }

    //Дописать
}
