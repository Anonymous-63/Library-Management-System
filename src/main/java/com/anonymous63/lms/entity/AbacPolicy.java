package com.anonymous63.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "abac_policies")
public class AbacPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String resourceType;
    private String action;
    private String effect; // ALLOW or DENY

    @Column(columnDefinition = "TEXT")
    private String conditions; // store as JSON string

    public AbacPolicy(String resourceType, String action, String effect, String conditions) {
        this.resourceType = resourceType;
        this.action = action;
        this.effect = effect;
        this.conditions = conditions;
    }
}
