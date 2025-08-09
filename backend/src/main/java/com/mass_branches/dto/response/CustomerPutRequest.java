package com.mass_branches.dto.response;

import com.mass_branches.model.CustomerTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.With;

@With
public record CustomerPutRequest(
        @Schema(example = "id-uuid-1", description = "customer's id")
        @NotBlank(message = "The field 'id' is required")
        String id,
        @Schema(example = "New name", description = "customer's name")
        @NotBlank(message = "The field 'name' is required")
        String name,
        @Schema(example = "PESSOA_FISICA", description = "customer's type")
        @NotNull(message = "The field 'type' is required")
        CustomerTypeName type
) {
}
