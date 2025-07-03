package com.mass_branches.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerPostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name,
        String description,
        @NotBlank(message = "The field 'type' is required")
        String type
) {}
