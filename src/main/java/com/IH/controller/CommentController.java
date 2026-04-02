package com.IH.controller;

import com.IH.model.dto.rest.CreateCommentRequest;
import com.IH.service.CommentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest request,
                                           HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to comment");
        }

        Optional<Long> commentId = commentService.createComment(request, userId);

        if (commentId.isPresent()) {
            log.debug("Comment created: {}", commentId.get());
            return ResponseEntity.ok("Comment submitted for moderation");
        } else {
            return ResponseEntity.badRequest().body("Could not create comment");
        }
    }
}
