package com.mass_branches.utils;

import com.mass_branches.model.Role;
import com.mass_branches.model.RoleType;

import java.util.ArrayList;
import java.util.List;

public class RoleUtils {
    public static List<Role> newRoleList() {
        Role adminRole = Role.builder().id(1L).name(RoleType.ADMIN).description("has all system permissions").build();
        Role clientRole = Role.builder().id(2L).name(RoleType.CLIENT).description("has limited system permissions").build();

        return new ArrayList<>(List.of(adminRole, clientRole));
    }
}
