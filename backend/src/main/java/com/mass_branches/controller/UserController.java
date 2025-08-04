package com.mass_branches.controller;

import com.mass_branches.dto.response.UserGetResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "Operations with users")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService service;

    @Operation(
            summary = "Get the requesting user data",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully get the requesting user data",
                            content = @Content(schema = @Schema(implementation = UserGetResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticate",
                            content = @Content
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserGetResponse> getMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        UserGetResponse response = service.getMe(user);
        return ResponseEntity.ok(response);
    }
}
