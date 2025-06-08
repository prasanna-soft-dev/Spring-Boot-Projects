package com.MMD.code.Service.Implementation;

import com.MMD.code.Entity.AggregatedMetric;
import com.MMD.code.Entity.AverageType;
import com.MMD.code.Entity.ComponentType;
import com.MMD.code.Repository.AggregatedMetricRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MemoryAggregationService {

    private final AggregatedMetricRepository aggregatedMetricRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MemoryAggregationService(AggregatedMetricRepository aggregatedMetricRepository) {
        this.aggregatedMetricRepository = aggregatedMetricRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void calculateHourlyMemoryAverages() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start = now.minusHours(1);

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT AVG(used_memory), AVG(total_memory) " +
                                "FROM memory_usage " +
                                "WHERE timestamp >= ?1 AND timestamp < ?2"
                )
                .setParameter(1, start)
                .setParameter(2, now)
                .getResultList();

        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            double avgUsed = ((Number) row[0]).doubleValue();
            double avgTotal = ((Number) row[1]).doubleValue();
            double avgUnused = avgTotal - avgUsed;

            AggregatedMetric usedMetric = new AggregatedMetric();
            usedMetric.setComponent(ComponentType.MEMORY_USED);
            usedMetric.setComponentId("MEMORY");
            usedMetric.setTimestamp(start);
            usedMetric.setAverageType(AverageType.HOUR);
            usedMetric.setAverageValue(avgUsed);
            aggregatedMetricRepository.save(usedMetric);

            AggregatedMetric unusedMetric = new AggregatedMetric();
            unusedMetric.setComponent(ComponentType.MEMORY_UNUSED);
            unusedMetric.setComponentId("MEMORY");
            unusedMetric.setTimestamp(start);
            unusedMetric.setAverageType(AverageType.HOUR);
            unusedMetric.setAverageValue(avgUnused);
            aggregatedMetricRepository.save(unusedMetric);

            System.out.println("✅ Hourly memory averages saved for " + start);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void calculateDailyMemoryAverages() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime start = now.minusDays(1);

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT AVG(used_memory), AVG(total_memory) " +
                                "FROM memory_usage " +
                                "WHERE timestamp >= ?1 AND timestamp < ?2"
                )
                .setParameter(1, start)
                .setParameter(2, now)
                .getResultList();

        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            double avgUsed = ((Number) row[0]).doubleValue();
            double avgTotal = ((Number) row[1]).doubleValue();
            double avgUnused = avgTotal - avgUsed;

            AggregatedMetric usedMetric = new AggregatedMetric();
            usedMetric.setComponent(ComponentType.MEMORY_USED);
            usedMetric.setComponentId("MEMORY");
            usedMetric.setTimestamp(start);
            usedMetric.setAverageType(AverageType.DAY);
            usedMetric.setAverageValue(avgUsed);
            aggregatedMetricRepository.save(usedMetric);

            AggregatedMetric unusedMetric = new AggregatedMetric();
            unusedMetric.setComponent(ComponentType.MEMORY_UNUSED);
            unusedMetric.setComponentId("MEMORY");
            unusedMetric.setTimestamp(start);
            unusedMetric.setAverageType(AverageType.DAY);
            unusedMetric.setAverageValue(avgUnused);
            aggregatedMetricRepository.save(unusedMetric);

            System.out.println("✅ Daily memory averages saved for " + start);
        }
    }
}
