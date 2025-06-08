package com.MMD.code.Service.Implementation;

import com.MMD.code.DTO.MetricAverageResponse;
import com.MMD.code.Entity.AverageType;
import com.MMD.code.Entity.ComponentType;
import com.MMD.code.Entity.TimeRange;
import com.MMD.code.Repository.AggregatedMetricRepository;
import com.MMD.code.Service.MetricQueryService;
import com.MMD.code.Util.TimeRangeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MetricQueryServiceImpl implements MetricQueryService {

    private final AggregatedMetricRepository aggregatedMetricRepository;

    public MetricQueryServiceImpl(AggregatedMetricRepository aggregatedMetricRepository) {
        this.aggregatedMetricRepository = aggregatedMetricRepository;
    }

    @Override
    public List<MetricAverageResponse> getAverage(ComponentType componentType, AverageType averageType, TimeRange timeRange) {
        List<MetricAverageResponse> response = new ArrayList<>();

        LocalDateTime start = TimeRangeUtil.getStartTime(timeRange);
        LocalDateTime end = TimeRangeUtil.getEndTime(timeRange);

        switch (componentType) {
            case OVERALL_CPU_CORE: {
                // Step 1: Fetch per-core per-timestamp values
                List<Object[]> results = aggregatedMetricRepository.findAveragePerCoreOverTime(ComponentType.CPU_CORE, averageType, start, end);

                // Step 2: Aggregate by timestamp: Map<timestamp, List<Double>> → average each
                Map<LocalDateTime, List<Double>> grouped = new TreeMap<>();
                for (Object[] row : results) {
                    LocalDateTime timestamp = (LocalDateTime) row[0];
                    Double value = (Double) row[2];
                    grouped.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(value);
                }

                // Step 3: Build response
                for (Map.Entry<LocalDateTime, List<Double>> entry : grouped.entrySet()) {
                    LocalDateTime timestamp = entry.getKey();
                    List<Double> values = entry.getValue();
                    double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    response.add(new MetricAverageResponse(timestamp, "Overall CPU Core", average));
                }
                break;
            }

            case INDIVIDUAL_CPU_CORES: {
                List<Object[]> results = aggregatedMetricRepository.findAverageGroupedByComponentId(ComponentType.CPU_CORE, averageType, start, end);
                for (Object[] row : results) {
                    String core = (String) row[0];
                    Double avg = (Double) row[1];
                    response.add(new MetricAverageResponse(null, "Core " + core, avg != null ? avg : 0.0));
                }
                break;
            }

            case MEMORY: {
                Double used = aggregatedMetricRepository.findOverallAverage(ComponentType.MEMORY_USED, averageType, start, end);
                Double unused = aggregatedMetricRepository.findOverallAverage(ComponentType.MEMORY_UNUSED, averageType, start, end);
                response.add(new MetricAverageResponse(null, "Used", used != null ? used : 0.0));
                response.add(new MetricAverageResponse(null, "Unused", unused != null ? unused : 0.0));
                break;
            }

            case DISK: {
                List<Object[]> results = aggregatedMetricRepository.findAverageGroupedByComponentId(ComponentType.DISK, averageType, start, end);
                for (Object[] row : results) {
                    String partition = (String) row[0];
                    Double avg = (Double) row[1];
                    response.add(new MetricAverageResponse(null, partition, avg != null ? avg : 0.0));
                }
                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported component type: " + componentType);
        }

        return response;
    }


    @Override
    public List<MetricAverageResponse> getCoreTimeSeries(AverageType averageType, TimeRange timeRange) {
        List<MetricAverageResponse> response = new ArrayList<>();
        LocalDateTime start = TimeRangeUtil.getStartTime(timeRange);
        LocalDateTime end = TimeRangeUtil.getEndTime(timeRange);

        List<Object[]> results = aggregatedMetricRepository.findAveragePerCoreOverTime(ComponentType.CPU_CORE, averageType, start, end);

        for (Object[] row : results) {
            LocalDateTime timestamp = (LocalDateTime) row[0];
            String coreId = (String) row[1];
            Double value = (Double) row[2];
            response.add(new MetricAverageResponse(timestamp, "Core " + coreId, value != null ? value : 0.0));
        }

        return response;
    }
}
