package com.mass_branches.dto.response;

import com.mass_branches.model.User;

public record UserGetResponse(String id, String firstName, String lastName, String email) {
    public static UserGetResponse by(User user) {
        return new UserGetResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
