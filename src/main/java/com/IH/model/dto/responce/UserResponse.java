package com.IH.model.dto.responce;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String login;
    private String username;
    private String role;
}
