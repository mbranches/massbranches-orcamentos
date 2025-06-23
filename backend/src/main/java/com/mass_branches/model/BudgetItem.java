package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@With
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "budget_item")
public class BudgetItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbudget_item")
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "budget_id")
    private Budget budget;
    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(nullable = false)
    private String order;
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 4)
    private BigDecimal unitPrice;
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal quantity;
    @Column(name = "total_value", nullable = false)
    private Double totalValue;
    @Column(name = "total_with_bdi")
    private Double totalWithBdi;
}
