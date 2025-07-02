package com.mass_branches.controller;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@RestController
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ResponseEntity<ItemPostResponse> create(Authentication authentication, @RequestBody ItemPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        ItemPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemGetResponse>> listAll(
            Authentication authentication,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean personal
    ) {
        User user = (User) authentication.getPrincipal();

        List<ItemGetResponse> response = service.listAll(user, name, personal);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemGetResponse> findById(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();

        ItemGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }
}
