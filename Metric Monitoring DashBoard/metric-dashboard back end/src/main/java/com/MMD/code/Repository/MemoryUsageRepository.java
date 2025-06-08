package com.MMD.code.Repository;

import com.MMD.code.Entity.MemoryUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryUsageRepository extends JpaRepository<MemoryUsage, Long> {
}
