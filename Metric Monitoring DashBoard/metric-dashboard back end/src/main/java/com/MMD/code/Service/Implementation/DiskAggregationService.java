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
public class DiskAggregationService {

    private final AggregatedMetricRepository aggregatedMetricRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DiskAggregationService(AggregatedMetricRepository aggregatedMetricRepository) {
        this.aggregatedMetricRepository = aggregatedMetricRepository;
    }

    // ⏰ Run every hour at minute 1 (e.g., 01:01, 02:01)
    @Scheduled(cron = "0 0 * * * *")
    public void calculateHourlyDiskAverages() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start = now.minusHours(1);

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT disk_name, AVG(usage_percent) " +
                                "FROM disk_usage " +
                                "WHERE timestamp >= ?1 AND timestamp < ?2 " +
                                "GROUP BY disk_name"
                )
                .setParameter(1, start)
                .setParameter(2, now)
                .getResultList();

        for (Object[] row : results) {
            String diskName = (String) row[0];
            double avg = ((Number) row[1]).doubleValue();

            AggregatedMetric metric = new AggregatedMetric();
            metric.setComponent(ComponentType.DISK);
            metric.setComponentId(diskName);
            metric.setTimestamp(start);
            metric.setAverageType(AverageType.HOUR);
            metric.setAverageValue(avg);

            aggregatedMetricRepository.save(metric);
        }

        System.out.println("✅ Hourly disk averages saved for " + start);
    }

    // ⏰ Run daily at 00:05 AM
    @Scheduled(cron = "0 0 0 * * *")
    public void calculateDailyDiskAverages() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime start = now.minusDays(1);

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT disk_name, AVG(usage_percent) " +
                                "FROM disk_usage " +
                                "WHERE timestamp >= ?1 AND timestamp < ?2 " +
                                "GROUP BY disk_name"
                )
                .setParameter(1, start)
                .setParameter(2, now)
                .getResultList();

        for (Object[] row : results) {
            String diskName = (String) row[0];
            double avg = ((Number) row[1]).doubleValue();

            AggregatedMetric metric = new AggregatedMetric();
            metric.setComponent(ComponentType.DISK);
            metric.setComponentId(diskName);
            metric.setTimestamp(start);
            metric.setAverageType(AverageType.DAY);
            metric.setAverageValue(avg);

            aggregatedMetricRepository.save(metric);
        }

        System.out.println("✅ Daily disk averages saved for " + start);
    }
}
