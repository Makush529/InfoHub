package com.IH.model.dto.rest;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
