package com.IH.service;

import com.IH.model.dto.RequestRegistrationDTO;
import com.IH.model.dto.UserResponse;
import com.IH.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class SecurityService {

    private final SecurityRepository securityRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }
    public UserResponse registration(RequestRegistrationDTO requestRegistrationDTO) throws SQLException {
        securityRepository.registerUser(
                requestRegistrationDTO.getLogin(),
                requestRegistrationDTO.getPassword(),
                requestRegistrationDTO.getFirstname(),
                requestRegistrationDTO.getBirthDate()

        );

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
