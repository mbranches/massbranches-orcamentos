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
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idcliente")
    private String id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType type;
    @Column(name = "created_at", nullable = false)
    @CurrentTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
