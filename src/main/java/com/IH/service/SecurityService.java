package com.IH.service;

import com.IH.model.dto.UserResponse;
import com.IH.model.dto.UserRole;
import com.IH.model.dto.rest.LoginRequest;
import com.IH.model.dto.rest.RegisterRequest;
import com.IH.model.dto.rest.UserDto;
import com.IH.repository.SecurityRepository;
import com.IH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class SecurityService {

    private final SecurityRepository securityRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, UserRepository userRepository) {
        this.securityRepository = securityRepository;
    }

    public UserResponse registration(RegisterRequest request) throws SQLException {
        Long userId = securityRepository.registerUser(
                request.getLogin(),
                request.getPassword(),
                request.getUsername(),
                request.getBirthDate()
        );
        securityRepository.addUserRole(userId, UserRole.USER);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setLogin(request.getLogin());
        userResponse.setUsername(request.getUsername());
        return userResponse;
    }

    public UserResponse login(LoginRequest loginDTO) throws SQLException {
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
