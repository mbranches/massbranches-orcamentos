package com.mass_branches.infra.config;

import com.mass_branches.model.Role;
import com.mass_branches.model.RoleType;
import com.mass_branches.model.User;
import com.mass_branches.model.UserRole;
import com.mass_branches.repository.UserRepository;
import com.mass_branches.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserAdminConfig implements CommandLineRunner {
    private static final Logger log = LogManager.getLogger(UserAdminConfig.class);
    private final UserRepository repository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@admin.com";
        if (repository.findByEmail(adminEmail).isPresent()) {
            log.info("Admin já cadastrado");

            return;
        }

        Role adminRole = roleService.findByName(RoleType.ADMIN);

        User user = User.builder()
                .firstName("admin")
                .lastName("admin")
                .email(adminEmail)
                .password(passwordEncoder.encode("123"))
                .active(true).build();
        UserRole userRole = UserRole.builder().user(user).role(adminRole).build();

        user.setRoles(List.of(userRole));

        repository.save(user);
    }
}
