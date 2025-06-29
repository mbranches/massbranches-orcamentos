package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrole")
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleType name;
    private String description;
}
