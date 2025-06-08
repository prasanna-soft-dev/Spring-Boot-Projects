package com.MMD.code.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

@Table(name = "aggregated_metrics")
public class AggregatedMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComponentType component;

    @Column(name = "component_id", nullable = false)
    private String componentId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "average_type", nullable = false)
    private AverageType averageType;

    @Column(name = "average_value", nullable = false)
    private double averageValue;

    // Getters and Setters
}
