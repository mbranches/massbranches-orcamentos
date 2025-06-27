package com.mass_branches.controller;

import com.mass_branches.dto.response.UserGetResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService service;

    @GetMapping("/me")
    public ResponseEntity<UserGetResponse> get(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        UserGetResponse response = service.getMe(user);
        return ResponseEntity.ok(response);
    }
}
