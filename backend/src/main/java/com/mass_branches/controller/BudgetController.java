package com.mass_branches.controller;

import com.mass_branches.dto.request.BudgetPutRequest;
import com.mass_branches.dto.response.BudgetElementGetResponse;
import com.mass_branches.dto.request.BudgetItemPostRequest;
import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.request.StagePostRequest;
import com.mass_branches.dto.response.BudgetGetResponse;
import com.mass_branches.dto.response.BudgetItemPostResponse;
import com.mass_branches.dto.response.BudgetPostResponse;
import com.mass_branches.dto.response.StagePostResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.BudgetService;
import jakarta.validation.Valid;
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
    public ResponseEntity<BudgetPostResponse> create(Authentication authentication, @Valid @RequestBody BudgetPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        BudgetPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetGetResponse>> listAll(
            Authentication authentication,
            @RequestParam(required = false) Boolean personal
    ) {
        User user = (User) authentication.getPrincipal();

        List<BudgetGetResponse> response = service.listAll(user, personal);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody BudgetPutRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        service.update(id, request, user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        service.delete(id, user);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> numberOfBudgets(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Integer response = service.numberOfBudgets(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetGetResponse> findById(Authentication authentication, @PathVariable String id) throws InterruptedException {
        User user = (User) authentication.getPrincipal();

        BudgetGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/items")
    public ResponseEntity<BudgetItemPostResponse> addItem(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody BudgetItemPostRequest postRequest
    ) {
        User user = (User) authentication.getPrincipal();

        BudgetItemPostResponse response = service.addItem(user, id, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}/items/{budgetItemId}")
    public ResponseEntity<Void> removeItem(
            Authentication authentication,
            @PathVariable String id,
            @PathVariable Long budgetItemId
    ) {
        User user = (User) authentication.getPrincipal();

        service.removeItem(user, id, budgetItemId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stages")
    public ResponseEntity<StagePostResponse> addStage(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody StagePostRequest postRequest
    ) {
        User user = (User) authentication.getPrincipal();

        StagePostResponse response = service.addStage(user, id, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}/stages/{stageId}")
    public ResponseEntity<Void> removeStage(
            Authentication authentication,
            @PathVariable String id,
            @PathVariable Long stageId
    ) {
        User user = (User) authentication.getPrincipal();

        service.removeStage(user, id, stageId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/elements")
    public ResponseEntity<List<BudgetElementGetResponse>> listAllElements(
            Authentication authentication,
            @PathVariable String id
    ) {
        User user = (User) authentication.getPrincipal();

        List<BudgetElementGetResponse> response = service.listAllElements(user, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
