package com.MMD.code.Service.Implementation;

import com.MMD.code.Entity.CpuCoreUsage;
import com.MMD.code.Repository.CpuCoreUsageRepository;
import com.MMD.code.Service.MonitoringService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CpuMonitoringService implements MonitoringService {

    private final CpuCoreUsageRepository repository;
    private final CentralProcessor processor;

    public CpuMonitoringService(CpuCoreUsageRepository repository) {
        this.repository = repository;
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        this.processor = hal.getProcessor();
    }

    @Override
    public void collectMetrics() {
        // Step 1: Capture previous CPU tick counts
        long[][] prevTicks = processor.getProcessorCpuLoadTicks();

        // Step 2: Wait 1 second to measure usage over time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // Step 3: Measure CPU load for each core
        double[] load = processor.getProcessorCpuLoadBetweenTicks(prevTicks);

        // Step 4: Strip nanoseconds for clean timestamp
        LocalDateTime now = LocalDateTime.now().withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Step 5: Store each core's usage in DB
        for (int i = 0; i < load.length; i++) {
            CpuCoreUsage usage = new CpuCoreUsage();
            usage.setCoreIndex(i);
            usage.setUsagePercent(load[i] * 100); // Convert to percentage
            usage.setTimestamp(now);
            repository.save(usage);
        }

        // Step 6: Log the event
        System.out.println("✅ Collected CPU core usage at " + now.format(formatter));
    }
}
