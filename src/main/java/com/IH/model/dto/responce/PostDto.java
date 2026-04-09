package com.IH.model.dto.responce;

import com.IH.model.dto.PostStatus;
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
    private int rating; //TODO likes - dislikes
    private PostStatus status;
    private List<String> tags;
    private boolean userLiked; //TODO проверить для текущего пользователя
    private boolean userDisliked; //TODO проверить для текущего пользователя

}
