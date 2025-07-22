package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstage")
    private Long id;
    @Column(name = "order_index", nullable = false)
    private String orderIndex;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;
    @ManyToOne(optional = false)
    @JoinColumn(name = "budget_id")
    private Budget budget;
}
