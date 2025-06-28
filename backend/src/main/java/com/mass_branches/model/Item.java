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
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iditem")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String unitMeasurement;
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 4)
    private BigDecimal unitPrice;
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean active;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
