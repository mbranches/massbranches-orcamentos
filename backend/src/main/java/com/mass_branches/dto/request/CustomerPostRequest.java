package com.mass_branches.dto.request;

import com.mass_branches.model.CustomerTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerPostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name,
        @NotNull(message = "The field 'type' is required")
        CustomerTypeName type
) {}
