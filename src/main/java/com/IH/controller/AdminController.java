package com.IH.controller;

import com.IH.model.dto.responce.CommentDto;
import com.IH.model.dto.responce.PostDto;
import com.IH.service.CommentService;
import com.IH.service.PostService;
import com.IH.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Administrative endpoints")
public class AdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CommentService commentService;

    @GetMapping("/posts/pending")
    @Operation(summary = "Get pending posts", description = "Returns all posts waiting for moderation")
    public ResponseEntity<?> getPendingPosts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.warn("User {} tried to access pending posts without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        List<PostDto> pendingPosts = postService.getPendingPosts();
        return ResponseEntity.ok(pendingPosts);
    }

    @PostMapping("/posts/{id}/approve")
    @Operation(summary = "Approve a post", description = "Approves a pending post")
    public ResponseEntity<?> approvePost(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = postService.approvePost(id);
        if (success) {
            log.info("Post {} approved by user {}", id, userId);
            return ResponseEntity.ok("Post approved successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not approve post");
        }
    }

    @PostMapping("/posts/{id}/reject")
    @Operation(summary = "Reject a post", description = "Rejects a pending post")
    public ResponseEntity<?> rejectPost(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = postService.rejectPost(id);
        if (success) {
            log.info("Post {} rejected by user {}", id, userId);
            return ResponseEntity.ok("Post rejected successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not reject post");
        }
    }

    @GetMapping("/comments/pending")
    @Operation(summary = "Get pending comments", description = "Returns all comments waiting for moderation")
    public ResponseEntity<?> getPendingComments(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        List<CommentDto> pendingComments = commentService.getPendingComments();
        return ResponseEntity.ok(pendingComments);
    }

    // Одобрить комментарий
    @PostMapping("/comments/{id}/approve")
    @Operation(summary = "Approve a comment", description = "Approves a pending comment")
    public ResponseEntity<?> approveComment(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = commentService.approveComment(id);
        if (success) {
            log.info("Comment {} approved by user {}", id, userId);
            return ResponseEntity.ok("Comment approved successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not approve comment");
        }
    }

    // Отклонить комментарий
    @PostMapping("/comments/{id}/reject")
    @Operation(summary = "Reject a comment", description = "Rejects a pending comment")
    public ResponseEntity<?> rejectComment(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = commentService.rejectComment(id);
        if (success) {
            log.info("Comment {} rejected by user {}", id, userId);
            return ResponseEntity.ok("Comment rejected successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not reject comment");
        }
    }
}