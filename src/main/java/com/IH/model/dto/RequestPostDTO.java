package com.IH.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestPostDTO {
    @Size(min = 5, max = 100)
    private String postTitle;

    @NotBlank
    private String text;
}
