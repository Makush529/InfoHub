package com.IH.model.dto.rest;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String login;
    private LocalDate birthDate;
    private int rating; // рейтинг пользователя
    private String role; // роль
}
