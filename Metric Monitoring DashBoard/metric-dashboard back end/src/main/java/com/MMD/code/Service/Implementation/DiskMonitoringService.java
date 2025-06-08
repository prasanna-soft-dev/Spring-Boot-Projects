package com.MMD.code.Service.Implementation;

import com.MMD.code.Entity.DiskUsage;
import com.MMD.code.Repository.DiskUsageRepository;
import com.MMD.code.Service.MonitoringService;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiskMonitoringService implements MonitoringService {

    private final DiskUsageRepository repository;
    private final FileSystem fileSystem;

    public DiskMonitoringService(DiskUsageRepository repository) {
        this.repository = repository;
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        this.fileSystem = systemInfo.getOperatingSystem().getFileSystem();
    }

    @Override
    public void collectMetrics() {
        List<OSFileStore> fileStores = fileSystem.getFileStores();

        LocalDateTime now = LocalDateTime.now();
        for (OSFileStore fs : fileStores) {
            long total = fs.getTotalSpace();
            long usable = fs.getUsableSpace();
            double usedPercent = ((double) (total - usable) / total) * 100.0;

            DiskUsage usage = new DiskUsage();
            usage.setDiskName(fs.getMount()); // You could also use getName()
            usage.setUsagePercent(usedPercent);
            usage.setTimestamp(now.withNano(0));

            repository.save(usage);
        }

        System.out.println("Collected disk usage at " + now);
    }
}
