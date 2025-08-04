package com.mass_branches.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record StagePostRequest(
        @Schema(example = "1.0", description = "order of the stage in the budget")
        @NotBlank(message = "The field 'order' is required")
        String order,
        @Schema(example = "ETAPA PRELIMINAR", description = "stage name")
        @NotBlank(message = "The field 'name' is required")
        String name
){}
