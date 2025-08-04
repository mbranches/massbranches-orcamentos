package com.mass_branches.dto.response;

import com.mass_branches.model.Stage;
import io.swagger.v3.oas.annotations.media.Schema;

public record StagePostResponse(
        @Schema(example = "1", type = "number", description = "stage id")
        Long id,
        @Schema(example = "1.0", description = "order of the stage in the budget")
        String orderIndex,
        @Schema(example = "ETAPA PRELIMINAR", description = "stage name")
        String name
) {
    public static StagePostResponse by(Stage stage) {
        return new StagePostResponse(stage.getId(), stage.getOrderIndex(), stage.getName());
    }
}
