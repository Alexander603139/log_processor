package io.github.bugrov.log_processor.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "condition_type", nullable = false, length = 50)
    private String conditionType;

    @Column(name = "condition_config", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String conditionConfig; // храним как JSON-строку

    @Column(nullable = false, length = 50)
    private String action;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at")
    private Instant createdAt;
}