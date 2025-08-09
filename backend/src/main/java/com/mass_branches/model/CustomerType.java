package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Builder
@Entity(name = "customer_type")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcustomer_type")
    @EqualsAndHashCode.Include
    private Long id;
    @Enumerated(EnumType.STRING)
    private CustomerTypeName name;
}
