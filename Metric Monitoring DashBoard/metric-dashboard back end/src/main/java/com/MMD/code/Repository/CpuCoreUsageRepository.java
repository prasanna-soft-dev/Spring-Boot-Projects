package com.MMD.code.Repository;



import com.MMD.code.Entity.CpuCoreUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CpuCoreUsageRepository extends JpaRepository<CpuCoreUsage, Long> {
    List<CpuCoreUsage> findByCoreIndexAndTimestampBetween(int coreIndex, LocalDateTime start, LocalDateTime end);
}
