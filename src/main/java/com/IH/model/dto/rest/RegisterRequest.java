package com.IH.model.dto.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Scope("prototype")
@Data
@Schema(description = "New user registration request")
public class RegisterRequest {
    @Schema(description = "Login",
            example = "Tony",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, maxLength = 50)
    @Size(min = 3, max = 50)
    String login;
    @Schema(description = "Password",
            example = "Qwerty123321",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, maxLength = 50)
    @NotBlank
    @Size(min = 6, max = 50)
    String password;
    @Schema(description = "Username",
            example = "Tony Soprano",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2, maxLength = 15)
    @NotBlank
    @Size(min = 2, max = 15)
    String username;
    @Schema(description = "Birthday",
            example = "1995-05-05",
            format = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthDate;
}
