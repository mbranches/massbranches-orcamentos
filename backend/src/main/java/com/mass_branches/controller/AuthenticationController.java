package com.mass_branches.controller;

import com.mass_branches.dto.request.LoginRequest;
import com.mass_branches.dto.response.LoginResponse;
import com.mass_branches.exception.DefaultErrorMessage;
import com.mass_branches.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication operations")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final UserService service;

    @Operation(
            summary = "Authenticate user",
            responses = {
                    @ApiResponse(
                            description = "user successfully authenticated",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            description = "invalid credentials given",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    ),
                    @ApiResponse(
                            description = "required field not given",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
