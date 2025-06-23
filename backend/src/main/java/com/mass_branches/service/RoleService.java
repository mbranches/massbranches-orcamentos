package com.mass_branches.service;

import com.mass_branches.model.Role;
import com.mass_branches.model.RoleType;
import com.mass_branches.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository repository;

    public Role findByName(RoleType name) {
        return repository.findByName(name);
    }
}
