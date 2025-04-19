package com.leonardodeoliveira97.task_queue_system.controller;

import com.leonardodeoliveira97.task_queue_system.messaging.TaskProducer;
import com.leonardodeoliveira97.task_queue_system.model.Task;
import com.leonardodeoliveira97.task_queue_system.model.TaskStatus;
import com.leonardodeoliveira97.task_queue_system.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository repository;
    private final TaskProducer producer;

    public TaskController(TaskRepository repository, TaskProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        Task saved = repository.save(task);

        producer.send(saved);

        return saved;
    }

    @GetMapping
    public List<Task> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }
}
