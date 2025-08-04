package com.mass_branches.dto.response;

import com.mass_branches.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserGetResponse(
        @Schema(example = "user-uuid-1", description = "user id")
        String id,
        @Schema(example = "admin", description = "user first name")
        String firstName,
        @Schema(example = "mass", description = "user last name")
        String lastName,
        @Schema(example = "admin@admin.com", description = "user id")
        String email
) {
    public static UserGetResponse by(User user) {
        return new UserGetResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
