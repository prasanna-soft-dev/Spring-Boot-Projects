package com.MMD.code.Repository;

import com.MMD.code.Entity.DiskUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiskUsageRepository extends JpaRepository<DiskUsage, Long> {
}

