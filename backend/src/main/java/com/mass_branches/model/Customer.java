package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@With
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idcustomer")
    @EqualsAndHashCode.Include
    private String id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_type_id")
    private CustomerType type;
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean active;
    @Column(name = "created_at", nullable = false)
    @CurrentTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
