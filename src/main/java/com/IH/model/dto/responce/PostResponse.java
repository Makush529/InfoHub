package com.IH.model.dto.responce;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostResponse {
    private Long id;
    private String postTitle;
    private String text;
    private String authorName;
    private LocalDate postAge;
    private Integer rating;
}