package com.IH.controller;

import com.IH.model.dto.responce.CommentDto;
import com.IH.model.dto.responce.PostDto;
import com.IH.service.CommentService;
import com.IH.service.LogService;
import com.IH.service.PostService;
import com.IH.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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


    private final PostService postService;
    private final AuthUtil authUtil;
    private final CommentService commentService;
    private final LogService logService;

    @Autowired
    public AdminController(PostService postService, AuthUtil authUtil, CommentService commentService, LogService logService) {
        this.postService = postService;
        this.authUtil = authUtil;
        this.commentService = commentService;
        this.logService = logService;
    }

    @GetMapping("/posts/pending")
    @Operation(summary = "Get pending posts", description = "Returns all posts waiting for moderation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of pending posts retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = PostDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User does not have ADMIN or MODERATOR role",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getPendingPosts(HttpServletRequest request) {
        log.debug(">> Get Pending Posts");
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.error("<< User {} tried to access pending posts without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }
        List<PostDto> pendingPosts = postService.getPendingPosts();
        log.debug("<< Pending Posts request received, {}", pendingPosts.size());
        return ResponseEntity.status(HttpStatus.OK).body(pendingPosts);
    }

    @PostMapping("/posts/{id}/approve")
    @Operation(summary = "Approve a post", description = "Approves a pending post")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post approved successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Could not approve post (post not found or already approved)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User does not have ADMIN or MODERATOR role",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> approvePost(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> Approve a pending post");
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.error("<< User {} tried to approve pending post without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = postService.approvePost(id);
        if (success) {
            log.debug("<< Post {} approved by user {}", id, userId);
            logService.log(userId, "APPROVE_POST", "Post ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Post approved successfully");
        } else {
            log.error("<< Post {} approved by user {}", id, userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not approve post");
        }
    }

    @PostMapping("/posts/{id}/reject")
    @Operation(summary = "Reject a post", description = "Rejects a pending post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Could not reject post (post not found or already processed)"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied. Admin or Moderator role required.")
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> rejectPost(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> Reject a pending post");
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.error("<< User {} tried to reject pending post without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = postService.rejectPost(id);
        if (success) {
            log.debug("<< Post {} rejected by user {}", id, userId);
            logService.log(userId, "REJECT_POST", "Post ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Post rejected successfully");
        } else {
            log.error("<< Post {} rejected by user {}", id, userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not reject post");
        }
    }

    @GetMapping("/comments/pending")
    @Operation(summary = "Get pending comments", description = "Returns all comments waiting for moderation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of pending comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User does not have ADMIN or MODERATOR role",
                    content = @Content
            )
    })
    public ResponseEntity<?> getPendingComments(HttpServletRequest request) {
        log.debug(">> Get Pending Comments");
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.error("<< User {} tried to access pending comments without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        List<CommentDto> pendingComments = commentService.getPendingComments();
        log.debug("<< Pending Comments request received, {}", pendingComments.size());
        return ResponseEntity.status(HttpStatus.OK).body(pendingComments);
    }

    @PostMapping("/comments/{id}/approve")
    @Operation(summary = "Approve a comment", description = "Approves a pending comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment approved successfully"),
            @ApiResponse(responseCode = "400", description = "Could not approve comment (comment not found or already processed)"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied. Admin or Moderator role required.")
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> approveComment(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> Approve a comment");
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.error("<< User {} tried to approve a comment without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = commentService.approveComment(id);
        if (success) {
            log.debug("<< Comment {} approved by user {}", id, userId);
            logService.log(userId, "APPROVE_COMMENT", "Comment ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Comment approved successfully");
        } else {
            log.error("<< Comment {} approved by user {}", id, userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not approve comment");
        }
    }

    @PostMapping("/comments/{id}/reject")
    @Operation(summary = "Reject a comment", description = "Rejects a pending comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Could not reject comment (comment not found or already processed)"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied. Admin or Moderator role required.")
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> rejectComment(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> Reject a comment");
        Long userId = (Long) request.getAttribute("userId");

        if (!authUtil.isModerator(userId)) {
            log.error("<< User {} tried to reject a comment without permission", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Admin or Moderator role required.");
        }

        boolean success = commentService.rejectComment(id);
        if (success) {
            log.info("<< Comment {} rejected by user {}", id, userId);
            logService.log(userId, "REJECT_COMMENT", "Comment ID: " + id);
            return ResponseEntity.ok("Comment rejected successfully");
        } else {
            log.error("<< Comment {} rejected by user {}", id, userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not reject comment");
        }
    }
}