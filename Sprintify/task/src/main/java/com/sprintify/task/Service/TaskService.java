package com.sprintify.task.Service;

import com.sprintify.task.Entity.TaskEntity;

import java.util.List;

public interface TaskService {
    String addTask(TaskEntity task,String email);
    String deleteTask(Long taskId, String email);
    String updateTask(TaskEntity task,String email);
    List<TaskEntity> getTaskByUserId(Long userid,String email);
}
