package com.IH.controller;

import com.IH.model.dto.RequestLoginDTO;
import com.IH.model.dto.RequestRegistrationDTO;
import com.IH.model.dto.UserResponse;
import com.IH.model.dto.rest.LoginRequest;
import com.IH.model.dto.rest.RegisterRequest;
import com.IH.model.dto.rest.UserDto;
import com.IH.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authorization", description = "API for registration, login, and session management")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private SecurityService securityService;

    @PostMapping("/register")
    @Operation(summary = "registration new user", description = "create new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "user was created"
                    , content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400"
                    , description = "Data validation error or user already exists"
                    , content = @Content),
            @ApiResponse(responseCode = "500"
                    , description = "Internal Server Error"
                    , content = @Content)
    })
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("IN:");
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
            logger.info("OUT: " + userResponse.toString());
            return ResponseEntity.ok(userDto);//проверить 200/203!!!!
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
    @Operation(summary = "login in the system", description = "User authentication using login and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "Successful login"
                    , content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401"
                    , description = "Incorrect login or password"
                    , content = @Content),
            @ApiResponse(responseCode = "400"
                    , description = "Validation error"
                    , content = @Content),
    })
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
                return ResponseEntity.ok(userDto);//проверить 200/201!!!
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout"
            , description = "Ends the user session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "Successful exit"),
            @ApiResponse(responseCode = "400"
                    , description = "Error when exiting")
    })
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Information about the current user"
            , description = "Returns the authenticated user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "User data"
                    , content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401"
                    , description = "The user is not authorized"
                    , content = @Content),
            @ApiResponse(responseCode = "404"
                    , description = "User not found"
                    , content = @Content)
    })
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
