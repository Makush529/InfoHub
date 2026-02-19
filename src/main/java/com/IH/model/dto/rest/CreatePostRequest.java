package com.IH.model.dto.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {

    @Size(min = 3, max = 255, message = "the title cannot be empty")
    private String title;

    @NotBlank(message = "content cannot be empty")
    private String content;

    @Size(max = 5, message = "maximum 5 tags")
    private List<String> tags;
}
