package com.mass_branches.utils;

import com.mass_branches.model.Role;
import com.mass_branches.model.User;
import com.mass_branches.model.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    public static List<User> newUserList() {
        Role adminRole = RoleUtils.newRoleList().getFirst();
        Role clientRole = RoleUtils.newRoleList().getLast();

        LocalDateTime createdAt1 = LocalDateTime.of(2025, 2, 2, 20, 22, 22);
        User user1 = User.builder().id("id-uui-1").firstName("Admin").lastName("One").email("admin@branches.dev").createdAt(createdAt1).updatedAt(createdAt1).active(true).build();
        UserRole userRole1 = UserRole.builder().id(1L).user(user1).role(adminRole).build();
        user1.setRoles(List.of(userRole1));

        LocalDateTime createdAt2 = LocalDateTime.of(2025, 1, 2, 20, 12, 11);
        User user2 = User.builder().id("id-uui-2").firstName("Marcus").lastName("Branches").email("branches@branches.dev").createdAt(createdAt2).updatedAt(createdAt2).active(false).build();
        UserRole userRole2 = UserRole.builder().id(2L).user(user2).role(clientRole).build();
        user2.setRoles(List.of(userRole2));

        LocalDateTime createdAt3 = LocalDateTime.of(2025, 1, 1, 20, 12, 11);
        User user3 = User.builder().id("id-uui-3").firstName("Vinicius").lastName("Silva").email("v.silva@branches.dev").createdAt(createdAt3).updatedAt(createdAt3).active(true).build();
        UserRole userRole3 = UserRole.builder().id(3L).user(user3).role(clientRole).build();
        user3.setRoles(List.of(userRole3));

        return new ArrayList<>(List.of(user1, user2, user3));
    }
}
