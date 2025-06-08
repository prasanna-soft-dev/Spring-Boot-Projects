package com.MMD.code.Service;

import com.MMD.code.DTO.MetricAverageResponse;
import com.MMD.code.Entity.AverageType;
import com.MMD.code.Entity.ComponentType;
import com.MMD.code.Entity.TimeRange;

import java.util.List;

public interface MetricQueryService {
    List<MetricAverageResponse> getAverage(ComponentType componentType, AverageType averageType, TimeRange timeRange);
    List<MetricAverageResponse> getCoreTimeSeries(AverageType averageType, TimeRange timeRange);
}
