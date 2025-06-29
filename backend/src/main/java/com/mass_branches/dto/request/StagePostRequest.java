package com.mass_branches.dto.request;

import com.mass_branches.model.Stage;

public record StagePostRequest(String order, String name) {
    public static StagePostRequest by(Stage stage) {
        return new StagePostRequest(stage.getOrderIndex(), stage.getName());
    }
}
