package com.sprintify.task.Repository;

import com.sprintify.task.Entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserId(Long userid);

    List<TaskEntity> findByReminderTimeBetween(LocalDateTime start, LocalDateTime end);
}
