package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "customer_type")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcustomer_type")
    private Long id;
    @Enumerated(EnumType.STRING)
    private CustomerTypeName name;
}
