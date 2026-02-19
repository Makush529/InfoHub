package com.IH.model.dto.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Scope("prototype")
@Data
public class RegisterRequest {
    @Size(min = 3, max = 50)
    String login;
    @NotBlank
    @Size(min = 6)
    String password;
    @NotBlank @Size(min = 2, max = 15)
    String username;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    LocalDate birthDate;
}
