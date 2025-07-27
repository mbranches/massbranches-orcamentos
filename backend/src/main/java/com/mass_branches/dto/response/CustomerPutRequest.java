package com.mass_branches.dto.response;

import com.mass_branches.model.CustomerTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerPutRequest(
        @NotBlank(message = "The field 'id' is required")
        String id,
        @NotBlank(message = "The field 'name' is required")
        String name,
        @NotNull(message = "The field 'type' is required")
        CustomerTypeName type
) {
}
