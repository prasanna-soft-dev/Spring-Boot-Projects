package com.MMD.code.Repository;

import com.MMD.code.Entity.AggregatedMetric;
import com.MMD.code.Entity.AverageType;
import com.MMD.code.Entity.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AggregatedMetricRepository extends JpaRepository<AggregatedMetric, Long> {

    // Overall average for a component type (e.g., OVERALL_CPU_CORE, MEMORY, DISK)
    @Query("SELECT AVG(a.averageValue) FROM AggregatedMetric a " +
            "WHERE a.component = :component " +
            "AND a.averageType = :averageType " +
            "AND a.timestamp BETWEEN :start AND :end")
    Double findOverallAverage(
            @Param("component") ComponentType component,
            @Param("averageType") AverageType averageType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Average per componentId for a component type (e.g., INDIVIDUAL_CPU_CORES or DISK partitions)
    @Query("SELECT a.componentId, AVG(a.averageValue) " +
            "FROM AggregatedMetric a " +
            "WHERE a.component = :component " +
            "AND a.averageType = :averageType " +
            "AND a.timestamp BETWEEN :start AND :end " +
            "GROUP BY a.componentId")
    List<Object[]> findAverageGroupedByComponentId(
            @Param("component") ComponentType component,
            @Param("averageType") AverageType averageType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // ✅ New: Time series average per componentId (e.g., for individual CPU cores across time)
    @Query("SELECT a.timestamp, a.componentId, a.averageValue " +
            "FROM AggregatedMetric a " +
            "WHERE a.component = :component " +
            "AND a.averageType = :averageType " +
            "AND a.timestamp BETWEEN :start AND :end " +
            "ORDER BY a.timestamp ASC")
    List<Object[]> findAveragePerCoreOverTime(
            @Param("component") ComponentType component,
            @Param("averageType") AverageType averageType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
