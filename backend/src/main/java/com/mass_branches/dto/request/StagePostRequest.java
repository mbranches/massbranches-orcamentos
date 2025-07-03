package com.mass_branches.dto.request;

import jakarta.validation.constraints.NotBlank;

public record StagePostRequest(
        @NotBlank(message = "The field 'order' is required")
        String order,
        @NotBlank(message = "The field 'name' is required")
        String name
){}
