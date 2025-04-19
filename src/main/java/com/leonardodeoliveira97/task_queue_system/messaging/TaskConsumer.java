package com.leonardodeoliveira97.task_queue_system.messaging;

import com.leonardodeoliveira97.task_queue_system.model.Task;
import com.leonardodeoliveira97.task_queue_system.model.TaskStatus;
import com.leonardodeoliveira97.task_queue_system.repository.TaskRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskConsumer {
    private final TaskRepository repository;

    public TaskConsumer(TaskRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receive(Task task) {
        try {
            task.setStatus(TaskStatus.PROCESSING);
            task.setProcessedAt(LocalDateTime.now());

            // Simulate processing time
            Thread.sleep(6000); // Simulate processing delay

            task.setStatus(TaskStatus.COMPLETED);
        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
        } finally {
            repository.save(task);
        }
    }

}
