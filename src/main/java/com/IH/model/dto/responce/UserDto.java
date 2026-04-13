package com.IH.model.dto.responce;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String login;
    private LocalDate birthDate;
    private int rating;
    private String role;
}
