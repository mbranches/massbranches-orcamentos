package com.mass_branches.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(example = "your-jwt-token", description = "your jwt access token")
        String accessToken
) {}
