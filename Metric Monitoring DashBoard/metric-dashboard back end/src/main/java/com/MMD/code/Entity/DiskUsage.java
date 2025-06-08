package com.MMD.code.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disk_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class DiskUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diskName;

    private double usagePercent;

    private LocalDateTime timestamp;
}
