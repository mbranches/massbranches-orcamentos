package com.mass_branches.controller;

import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.response.BudgetGetResponse;
import com.mass_branches.dto.response.BudgetPostResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
@RestController
public class BudgetController {
    private final BudgetService service;

    @PostMapping
    public ResponseEntity<BudgetPostResponse> create(Authentication authentication, @RequestBody BudgetPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        BudgetPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetGetResponse>> listAllMy(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<BudgetGetResponse> response = service.listAllMy(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetGetResponse> findMyById(Authentication authentication, @PathVariable String id) {
        User user = (User) authentication.getPrincipal();

        BudgetGetResponse response = service.findMyById(user, id);

        return ResponseEntity.ok(response);
    }
}
