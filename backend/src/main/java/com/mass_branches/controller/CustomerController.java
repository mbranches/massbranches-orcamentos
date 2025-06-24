package com.mass_branches.controller;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerGetResponse;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.model.User;
import com.mass_branches.service.CustomerService;
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
    public ResponseEntity<CustomerPostResponse> post(Authentication authentication, @RequestBody CustomerPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        CustomerPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerGetResponse>> listAllMy(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<CustomerGetResponse> response = service.listAllMy(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerGetResponse> findMyById(Authentication authentication, @PathVariable String id) {
        User user = (User) authentication.getPrincipal();

        CustomerGetResponse response = service.findMyById(user, id);

        return ResponseEntity.ok(response);
    }
}
