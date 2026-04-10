package com.IH.service;

import com.IH.model.dto.UserRole;
import com.IH.model.dto.responce.UserResponse;
import com.IH.model.dto.request.LoginRequest;
import com.IH.model.dto.request.RegisterRequest;
import com.IH.model.dto.responce.UserDto;
import com.IH.repository.SecurityRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@Service
public class SecurityService {

    private final SecurityRepository securityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, PasswordEncoder passwordEncoder) {
        this.securityRepository = securityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registration(RegisterRequest request) throws SQLException {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Long userId = securityRepository.registerUser(
                request.getLogin(),
                hashedPassword,
                request.getUsername(),
                request.getBirthDate()
        );

        securityRepository.addUserRole(userId, UserRole.USER);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setLogin(request.getLogin());
        userResponse.setUsername(request.getUsername());

        log.debug("User registered: {}", request.getLogin());
        return userResponse;
    }

    public UserResponse login(@Valid LoginRequest loginRequest) throws SQLException {
        UserResponse user = securityRepository.getUserByLogin(loginRequest.getLogin());

        if (user == null) {
            log.warn("Login failed: user not found - {}", loginRequest.getLogin());
            return null;
        }

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPasswordHash()
        );
        if (!passwordMatches) {
            log.warn("Login failed: wrong password for - {}", loginRequest.getLogin());
            return null;
        }
        log.debug("User logged in: {}", loginRequest.getLogin());
        return user;
    }

    public Optional<UserDto> findById(Long userId) {
        try {
            return securityRepository.getUserById(userId);
        } catch (Exception e) {
            log.error("Error getting user role for userId: {}", userId, e);
            return Optional.empty();
        }
    }
}
