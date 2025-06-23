package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser_role")
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
