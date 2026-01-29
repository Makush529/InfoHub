package com.IH;

import com.IH.model.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Service
public class SecurityService {
    public UserResponse registration(
            String login,
            String password,
            String firstname) {
        System.out.println("Login :" + login);
        System.out.println("Password :" + password);
        System.out.println("Firstname :" + firstname);
        System.out.println("Age :" );

        UserResponse userResponse = new UserResponse();
        userResponse.setLogin(login);
        userResponse.setFirstname(firstname);

        return userResponse;
    }
}
