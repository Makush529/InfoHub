package com.IH.service;

import com.IH.model.dto.RequestRegistrationDTO;
import com.IH.model.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SecurityService {
    public UserResponse registration(
            RequestRegistrationDTO requestRegistrationDTO) {
        System.out.println(requestRegistrationDTO.getLogin());
        System.out.println(requestRegistrationDTO.getPassword());
        System.out.println(requestRegistrationDTO.getFirstname());
        System.out.println(requestRegistrationDTO.getBirthDate());

        UserResponse userResponse = new UserResponse();
        userResponse.setLogin(requestRegistrationDTO.getLogin());
        userResponse.setFirstname(requestRegistrationDTO.getFirstname());

        return userResponse;
    }
}
