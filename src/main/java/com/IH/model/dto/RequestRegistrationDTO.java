package com.IH.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Scope("prototype")
@Data
public class RequestRegistrationDTO {
    @Size(min = 3, max = 50)
    String login;
    String password;
    String firstname;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    LocalDate birthDate;
}
