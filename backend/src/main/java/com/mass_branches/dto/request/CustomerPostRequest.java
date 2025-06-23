package com.mass_branches.dto.request;

public record CustomerPostRequest(
        String name,
        String description,
        String type
) {}
