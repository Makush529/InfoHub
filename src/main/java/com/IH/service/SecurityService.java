package com.IH.service;

import com.IH.model.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SecurityService {
    public UserResponse registration(
            String login,
            String password,
            String firstname,
            LocalDate birthDate) {
        System.out.println("Login :" + login);
        System.out.println("Password :" + password);
        System.out.println("Firstname :" + firstname);
        System.out.println("Age :" +  birthDate);

        UserResponse userResponse = new UserResponse();
        userResponse.setLogin(login);
        userResponse.setFirstname(firstname);

        return userResponse;
    }
}
