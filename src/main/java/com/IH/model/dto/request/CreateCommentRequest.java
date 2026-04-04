package com.IH.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotBlank(message = "Comment content cannot be empty")
    private String content;
}
