package com.leonardodeoliveira97.task_queue_system.controller;

import com.leonardodeoliveira97.task_queue_system.messaging.TaskProducer;
import com.leonardodeoliveira97.task_queue_system.model.Task;
import com.leonardodeoliveira97.task_queue_system.model.TaskStatus;
import com.leonardodeoliveira97.task_queue_system.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskProducer producer;

    public TaskController(TaskService service, TaskProducer producer) {
        this.taskService = service;
        this.producer = producer;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        Task saved = taskService.createTask(task);

        producer.send(saved);

        return saved;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
}
