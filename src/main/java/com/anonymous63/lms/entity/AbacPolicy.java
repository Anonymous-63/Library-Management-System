package com.anonymous63.lms.entity;

import com.anonymous63.lms.config.RawJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

    @Column(nullable = false)
    private String name;

    private String description;

    // resourceType and action used for lookup
    @Column(nullable = false)
    private String resourceType;

    @Column(nullable = false)
    private String action;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JsonDeserialize(using = RawJsonDeserializer.class) // 👈 handles array/object → JSON text
    @JsonRawValue // ensures it remains valid JSON when returned
    private String conditions; // JSON array of Condition objects

    @Column(nullable = false)
    private String effect; // ALLOW or DENY

    @Column
    private Integer priority; // higher priority evaluated first (optional)

    // metadata, enabled flag etc
    @Column(nullable = false)
    private boolean enabled = true;
}
