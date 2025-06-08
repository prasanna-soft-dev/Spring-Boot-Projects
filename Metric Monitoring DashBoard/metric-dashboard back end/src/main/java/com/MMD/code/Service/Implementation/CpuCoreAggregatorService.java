package com.MMD.code.Service.Implementation;

import com.MMD.code.Entity.AggregatedMetric;
import com.MMD.code.Entity.AverageType;
import com.MMD.code.Entity.ComponentType;
import com.MMD.code.Repository.AggregatedMetricRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CpuCoreAggregatorService {

    private final AggregatedMetricRepository metricRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CpuCoreAggregatorService(AggregatedMetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    // Runs every hour at minute 1 (e.g., 12:01)
    @Scheduled(cron = "0 0 * * * *")
    public void calculateHourlyCpuAverages() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start = now.minusHours(1);

        // Native query
        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT core_index, AVG(usage_percent) " +
                                "FROM cpu_core_usage " +
                                "WHERE timestamp >= ?1 AND timestamp < ?2 " +
                                "GROUP BY core_index"
                )
                .setParameter(1, start)
                .setParameter(2, now)
                .getResultList();

        for (Object[] row : results) {
            int coreIndex = ((Number) row[0]).intValue();
            double avg = ((Number) row[1]).doubleValue();

            AggregatedMetric metric = new AggregatedMetric();
            metric.setComponent(ComponentType.CPU_CORE);
            metric.setComponentId(String.valueOf(coreIndex));
            metric.setTimestamp(start);
            metric.setAverageType(AverageType.HOUR);
            metric.setAverageValue(avg);

            metricRepository.save(metric);
        }

        System.out.println("✅ Hourly CPU core averages saved for " + start);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void calculateDailyCpuAverages() {
        // Get start of "yesterday"
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime start = now.minusDays(1);

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT core_index, AVG(usage_percent) " +
                                "FROM cpu_core_usage " +
                                "WHERE timestamp >= ?1 AND timestamp < ?2 " +
                                "GROUP BY core_index"
                )
                .setParameter(1, start)
                .setParameter(2, now)
                .getResultList();

        for (Object[] row : results) {
            int coreIndex = ((Number) row[0]).intValue();
            double avg = ((Number) row[1]).doubleValue();

            AggregatedMetric metric = new AggregatedMetric();
            metric.setComponent(ComponentType.CPU_CORE);
            metric.setComponentId(String.valueOf(coreIndex));
            metric.setTimestamp(start); // Timestamp = start of day
            metric.setAverageType(AverageType.DAY);
            metric.setAverageValue(avg);

            metricRepository.save(metric);
        }

        System.out.println("✅ Daily CPU core averages saved for " + start);
    }

}
