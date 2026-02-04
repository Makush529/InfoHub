package com.IH.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostResponse {
    private Long id;
    private String postTitle; // Заголовок
    private String text;    // Текст
    private String authorName; // Имя автора (получим через JOIN)
    private LocalDate postAge; // Дата
    private Integer rating;    // Будем вычислять позже
}