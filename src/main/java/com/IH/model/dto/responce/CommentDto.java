package com.IH.model.dto.responce;

import com.IH.model.dto.CommentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private LocalDate createdAt;
    private String authorName;
    private Long authorId;
    private Long postId;
    private CommentStatus status;
    private boolean canEdit;
    private boolean canDelete;
}
