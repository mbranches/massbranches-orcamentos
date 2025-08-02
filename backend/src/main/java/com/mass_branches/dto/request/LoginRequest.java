package com.mass_branches.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @Schema(example = "admin@admin.com", description = "your account email")
        @NotBlank(message = "The field 'email' is required")
        String email,
        @Schema(example = "123", description = "your account password")
        @NotBlank(message = "The field 'password' is required")
        String password
) {}
