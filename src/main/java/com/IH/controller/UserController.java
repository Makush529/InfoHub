package com.IH.controller;

import com.IH.model.dto.responce.UserDto;
import com.IH.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "API for user management")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id",
            description = "Returns public information about a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"
                    , description = "User found"
                    , content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug(">> getUserById: {}", id);
        Optional<UserDto> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            UserDto user = userOpt.get();
            int rating = userService.getUserRating(id);
            user.setRating(rating);
            log.debug("<< User found: {}, rating: {}", user.getUsername(), rating);
            return ResponseEntity.ok(user);
        }
        log.warn("<< User not found: {}", id);
        return ResponseEntity.notFound().build();
    }
}
