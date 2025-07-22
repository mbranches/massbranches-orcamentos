package com.mass_branches.model;

import com.mass_branches.converter.BudgetStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@With
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idbudget")
    @EqualsAndHashCode.Include
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String proposalNumber;
    @Column(precision = 10, scale = 2)
    private BigDecimal bdi;
    @Column(name = "total_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;
    @Column(name = "total_with_bdi", precision = 10, scale = 2)
    private BigDecimal totalWithBdi;
    @Convert(converter = BudgetStatusConverter.class)
    private BudgetStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_at")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean active;
}
