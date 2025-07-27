package com.mass_branches.controller;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerGetResponse;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;
import com.mass_branches.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<CustomerTypeName> type
            ) {

        List<CustomerGetResponse> response = service.listAll(name, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<CustomerGetResponse>> listMyAll(
            Authentication authentication,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<CustomerTypeName> type
    ) {
        User user = (User) authentication.getPrincipal();

        List<CustomerGetResponse> response = service.listMyAll(user, name, type);
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
