package com.leonardodeoliveira97.task_queue_system.service;

import com.leonardodeoliveira97.task_queue_system.exception.NotFoundException;
import com.leonardodeoliveira97.task_queue_system.messaging.TaskProducer;
import com.leonardodeoliveira97.task_queue_system.model.Task;
import com.leonardodeoliveira97.task_queue_system.model.TaskStatus;
import com.leonardodeoliveira97.task_queue_system.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProducer taskProducer;

    public TaskService(TaskRepository taskRepository, TaskProducer taskProducer) {
        this.taskRepository = taskRepository;
        this.taskProducer = taskProducer;
    }

    public Task createTask(Task task) {
        task.setStatus(TaskStatus.PENDING);
        Task savedTask = taskRepository.save(task);
        taskProducer.send(savedTask);
        return savedTask;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id " + id + " not found"));
    }

    public void markTaskAsCompleted(Long id) {
        Task task = getTaskById(id);
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
    }
}
