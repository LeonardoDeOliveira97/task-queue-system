package com.leonardodeoliveira97.task_queue_system.repository;

import com.leonardodeoliveira97.task_queue_system.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
