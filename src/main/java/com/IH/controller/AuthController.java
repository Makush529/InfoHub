package com.IH.controller;

import com.IH.model.dto.UserRole;
import com.IH.model.dto.responce.UserResponse;
import com.IH.model.dto.request.LoginRequest;
import com.IH.model.dto.request.RegisterRequest;
import com.IH.model.dto.responce.UserDto;
import com.IH.service.JwtService;
import com.IH.service.SecurityService;
import com.IH.service.TokenBlacklistService;
import com.IH.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization", description = "API for registration, login, and session management")
public class AuthController {

    private final SecurityService securityService;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    @Autowired
    public AuthController(SecurityService securityService, UserService userService, JwtService jwtService, TokenBlacklistService blacklistService) {
        this.securityService = securityService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }

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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info(">> Register request for login: {}", request.getLogin());
        try {
            UserResponse userResponse = securityService.registration(request);
            UserDto userDto = new UserDto();

            userDto.setId(userResponse.getId());
            userDto.setLogin(userResponse.getLogin());
            userDto.setUsername(userResponse.getUsername());
            log.info("<< User registered: {}", userResponse.getLogin());
            return ResponseEntity.ok(userDto);
        } catch (SQLException e) {
            log.error("<< SQL error during registration: {}", request.getLogin());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("this login is already in use");

        } catch (Exception e) {
            log.error("<< Unexpected error during registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info(">> Login attempt for login: {}", request.getLogin());
        try {
            UserResponse userResponse = securityService.login(request);

            if (userResponse != null) {
                String token = jwtService.generateToken(userResponse.getLogin(), userResponse.getId());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", userResponse.getId());
                response.put("username", userResponse.getUsername());
                response.put("login", userResponse.getLogin());

                log.info("<< Login successful for user: {}", userResponse.getLogin());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                log.warn("<< Login failed: invalid credentials for login: {}", request.getLogin());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (SQLException e) {
            log.error("<< SQL error during login for login: {}", request.getLogin(), e);
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
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.addToBlacklist(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("No token provided");
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
    public ResponseEntity<UserDto> getCurrentUser(HttpServletRequest request) {
        log.debug(">>about profile");
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("<<user unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<UserDto> userOpt = securityService.findById(userId);
        if (userOpt.isPresent()) {
            UserDto user = userOpt.get();
            int rating = userService.getUserRating(userId);
            user.setRating(rating);
            log.debug("<<user found: {}", user);
            Optional<UserRole> roleOpt = securityService.getUserRole(userId);
            roleOpt.ifPresent(role -> user.setRole(role.name()));
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            log.warn("<<user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
