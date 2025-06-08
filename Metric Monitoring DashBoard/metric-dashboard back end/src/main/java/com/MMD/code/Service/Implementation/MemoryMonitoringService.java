package com.MMD.code.Service.Implementation;

import com.MMD.code.Entity.MemoryUsage;
import com.MMD.code.Repository.MemoryUsageRepository;
import com.MMD.code.Service.MonitoringService;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemoryMonitoringService implements MonitoringService {

    private final MemoryUsageRepository repository;
    private final HardwareAbstractionLayer hal;

    public MemoryMonitoringService(MemoryUsageRepository repository) {
        this.repository = repository;
        SystemInfo systemInfo = new SystemInfo();
        this.hal = systemInfo.getHardware();
    }

    @Override
    public void collectMetrics() {
        long totalBytes = hal.getMemory().getTotal();
        long availableBytes = hal.getMemory().getAvailable();
        long usedBytes = totalBytes - availableBytes;

        // Convert bytes to GB and round down
        long totalGB = totalBytes / (1024 * 1024 * 1024);
        long usedGB = usedBytes / (1024 * 1024 * 1024);
        long freeGB = availableBytes / (1024 * 1024 * 1024);

        MemoryUsage usage = new MemoryUsage();
        usage.setTotalMemory(totalGB);
        usage.setUsedMemory(usedGB);
        usage.setFreeMemory(freeGB);
        usage.setTimestamp(LocalDateTime.now().withNano(0));

        repository.save(usage);

        System.out.printf("Memory (GB): Total=%d, Used=%d, Free=%d%n", totalGB, usedGB, freeGB);
    }
}

