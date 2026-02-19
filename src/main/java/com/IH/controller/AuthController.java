package com.IH.controller;

import com.IH.model.dto.RequestLoginDTO;
import com.IH.model.dto.RequestRegistrationDTO;
import com.IH.model.dto.UserResponse;
import com.IH.model.dto.rest.LoginRequest;
import com.IH.model.dto.rest.RegisterRequest;
import com.IH.model.dto.rest.UserDto;
import com.IH.service.SecurityService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SecurityService securityService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        try {
            RequestRegistrationDTO oldDTO = new RequestRegistrationDTO();
            oldDTO.setLogin(request.getLogin());
            oldDTO.setPassword(request.getPassword());
            oldDTO.setUsername(request.getUsername());
            oldDTO.setBirthDate(request.getBirthDate());

            UserResponse userResponse = securityService.registration(oldDTO);
            UserDto userDto = new UserDto();

            userDto.setId(userResponse.getId());
            userDto.setLogin(userResponse.getLogin());
            userDto.setUsername(userResponse.getUsername());
            return ResponseEntity.ok(userDto);
        } catch (SQLException e) {
            System.err.println("!!! SQL ОШИБКА !!!");
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Database error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("!!! НЕИЗВЕСТНАЯ ОШИБКА !!!");
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        try {
            RequestLoginDTO oldDTO = new RequestLoginDTO();
            oldDTO.setLogin(request.getLogin());
            oldDTO.setPassword(request.getPassword());

            UserResponse userResponse = securityService.login(oldDTO);

            if (userResponse != null) {
                session.setAttribute("username", userResponse.getUsername());
                session.setAttribute("id", userResponse.getId());

                UserDto userDto = new UserDto();
                userDto.setId(userResponse.getId());
                userDto.setLogin(userResponse.getLogin());
                userDto.setUsername(userResponse.getUsername());
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(HttpSession session) {
        Long id = (Long) session.getAttribute("id");

        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<UserDto> userOpt = securityService.findById(id);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
