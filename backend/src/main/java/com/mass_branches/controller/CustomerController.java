package com.mass_branches.controller;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerGetResponse;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerPostResponse> post(Authentication authentication, @Valid @RequestBody CustomerPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        CustomerPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerGetResponse>> listAll(
            Authentication authentication,
            @RequestParam(required = false) Boolean personal
    ) {
        User user = (User) authentication.getPrincipal();

        List<CustomerGetResponse> response = service.listAll(user, personal);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerGetResponse> findById(Authentication authentication, @PathVariable String id) {
        User user = (User) authentication.getPrincipal();

        CustomerGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> numberOfCustomers(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Integer response = service.numberOfCustomers(user);

        return ResponseEntity.ok(response);
    }
}
