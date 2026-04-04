package com.IH.service;

import com.IH.model.dto.responce.UserResponse;
import com.IH.model.dto.request.LoginRequest;
import com.IH.model.dto.request.RegisterRequest;
import com.IH.model.dto.responce.UserDto;
import com.IH.repository.SecurityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class SecurityService {

    private final SecurityRepository securityRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public UserResponse registration(RegisterRequest request) throws SQLException {
        securityRepository.registerUser(
                request.getLogin(),
                request.getPassword(),
                request.getUsername(),
                request.getBirthDate()
        );

        UserResponse userResponse = new UserResponse();
        userResponse.setLogin(request.getLogin());
        userResponse.setUsername(request.getUsername());

        return userResponse;
    }

    public UserResponse login(@Valid LoginRequest loginDTO) throws SQLException {
        return securityRepository.getUserByCredentials(loginDTO.getLogin(), loginDTO.getPassword());
    }

    public Optional<UserDto> findById(long id) {
        try {
            Optional<UserDto> user = securityRepository.getUserById(id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
