package com.mass_branches.controller;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.request.ItemPutRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@RestController
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ResponseEntity<ItemPostResponse> create(Authentication authentication, @Valid @RequestBody ItemPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        ItemPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemGetResponse>> listAll(@RequestParam(required = false) Optional<String> name) {
        List<ItemGetResponse> response = service.listAll(name);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ItemGetResponse>> listAllMy(
            Authentication authentication,
            @RequestParam(required = false) Optional<String> name
    ) {
        User user = (User) authentication.getPrincipal();

        List<ItemGetResponse> response = service.listAllMy(user, name);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemGetResponse> findById(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();

        ItemGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ItemPutRequest request) {
        User user = (User) authentication.getPrincipal();

        service.update(user, id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        service.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}
