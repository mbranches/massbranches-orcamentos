package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrole")
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleType name;
    private String description;
}
