package com.MMD.code.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "memory_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MemoryUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long totalMemory;

    private long usedMemory;

    private long freeMemory;

    private LocalDateTime timestamp;
}
