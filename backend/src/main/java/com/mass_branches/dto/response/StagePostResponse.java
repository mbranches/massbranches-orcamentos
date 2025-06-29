package com.mass_branches.dto.response;

import com.mass_branches.model.Stage;

public record StagePostResponse(Long id, String orderIndex, String name) {
    public static StagePostResponse by(Stage stage) {
        return new StagePostResponse(stage.getId(), stage.getOrderIndex(), stage.getName());
    }
}
