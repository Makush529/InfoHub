package com.IH.model.dto.rest;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDate createdAt;
    private String authorName;
    private Long authorId;
    private int likesCount;
    private int dislikesCount;
    private int rating; // likes - dislikes
    private String status; // PENDING, APPROVED, REJECTED
    private List<String> tags;
    private boolean userLiked; // для текущего пользователя
    private boolean userDisliked; // для текущего пользователя

}
