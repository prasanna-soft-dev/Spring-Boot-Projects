package com.MMD.code.Scheduler;

import com.MMD.code.Service.MonitoringService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricScheduler {

    private final List<MonitoringService> services;

    public MetricScheduler(List<MonitoringService> services) {
        this.services = services;
    }

    @Scheduled(fixedRate = 300000)  // every minute
    public void collectAllMetrics() {
        services.forEach(MonitoringService::collectMetrics);
    }
}
