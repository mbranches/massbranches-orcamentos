package com.mass_branches.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;

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
    private String id;
    @Column(nullable = false)
    private String name;
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_type_id")
    private CustomerType type;
    private Boolean active;
    @Column(name = "created_at", nullable = false)
    @CurrentTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
