package com.mass_branches.dto.request;

import com.mass_branches.model.CustomerTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerPostRequest(
        @Schema(example = "Gerdau", description = "customer's name")
        @NotBlank(message = "The field 'name' is required")
        String name,
        @Schema(example = "PESSOA_FISICA", description = "customer's type")
        @NotNull(message = "The field 'type' is required")
        CustomerTypeName type
) {}
