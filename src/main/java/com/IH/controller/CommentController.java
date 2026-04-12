package com.IH.controller;

import com.IH.model.dto.request.CreateCommentRequest;
import com.IH.model.dto.responce.CommentDto;
import com.IH.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/comments")
@Tag(name = "CommentController", description = "Comments endpoints")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Operation(
            summary = "Create a comment",
            description = "Adds a comment to a specific post. " +
                    "The comment will be submitted for moderation before becoming visible. " +
                    "Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment submitted for moderation successfully"),
            @ApiResponse(responseCode = "400", description = "Could not create comment (invalid data)"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest commentRequest,
                                           HttpServletRequest request) throws SQLException {
        log.debug(">> Adding a comment to a post with Id = {} ", commentRequest.getPostId());
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to comment");
        }

        Optional<Long> commentId = commentService.createComment(commentRequest, userId);

        if (commentId.isPresent()) {
            log.debug("<< OK:Comment created: {}", commentId.get());
            return ResponseEntity.ok("Comment submitted for moderation");
        } else {
            log.warn("<< BAD_REQUEST: Could not create comment");
            return ResponseEntity.badRequest().body("Could not create comment");
        }
    }

    @GetMapping("/post/{postId}")
    @Operation(
            summary = "Get comments by post ID",
            description = "Returns all approved comments for a specific post. " +
                    "Authentication is optional - returns user's likes/dislikes status if authenticated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated (still returns comments, without personal flags)")
    })
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId, HttpServletRequest request) {
        log.debug(">> Getting all comments for a post with id = {}", postId);
        Long userId = (Long) request.getAttribute("userId");
        List<CommentDto> comments = commentService.getCommentsByPost(postId, userId);
        log.debug("<< OK:Comments found: {}", comments.size());
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment by ID. " +
                    "Only the author of the comment or a user with ADMIN/MODERATOR role can delete it. " +
                    "Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Could not delete comment (not found or not authorized)"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Access denied (not the author and not moderator/admin)")
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        log.debug(">> deleting a comment with index id = {} ", id);
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<< UNAUTHORIZED: user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged");
        }
        boolean deleted = commentService.deleteComment(id, userId);
        if (deleted) {
            log.debug("<< OK:Comment deleted: {}", id);
            return ResponseEntity.ok("Comment deleted");
        } else {
            log.warn("<< BAD_REQUEST: Could not delete comment");
            return ResponseEntity.badRequest().body("Could not delete comment");
        }
    }
}
