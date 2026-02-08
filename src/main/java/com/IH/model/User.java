package com.IH.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private long id;
    private String username;
    private String password;

}
