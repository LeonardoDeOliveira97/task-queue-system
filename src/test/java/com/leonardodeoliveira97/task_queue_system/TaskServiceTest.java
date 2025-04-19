package com.leonardodeoliveira97.task_queue_system;

import com.leonardodeoliveira97.task_queue_system.exception.NotFoundException;
import com.leonardodeoliveira97.task_queue_system.messaging.TaskProducer;
import com.leonardodeoliveira97.task_queue_system.model.Task;
import com.leonardodeoliveira97.task_queue_system.model.TaskStatus;
import com.leonardodeoliveira97.task_queue_system.repository.TaskRepository;
import com.leonardodeoliveira97.task_queue_system.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskProducer taskProducer;

    @InjectMocks
    private TaskService taskService;

    private Task mockTask;

    @BeforeEach
    void setup() {
        mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setDescription("Test Task");
        mockTask.setStatus(TaskStatus.PENDING);
    }

    @Test
    void shouldCreateTaskAndSendToQueue() {
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task result = taskService.createTask(mockTask);

        assertNotNull(result);
        assertEquals("Test Task", result.getDescription());
        verify(taskRepository).save(mockTask);
        verify(taskProducer).send(mockTask);
    }

    @Test
    void shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(mockTask));

        List<Task> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getDescription());
    }

    @Test
    void shouldReturnTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldMarkTaskAsCompleted() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        taskService.markTaskAsCompleted(1L);

        assertEquals(TaskStatus.COMPLETED, mockTask.getStatus());
        verify(taskRepository).save(mockTask);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.getTaskById(99L));

        assertEquals("Task with id " + 99L + " not found", exception.getMessage());
    }
}
