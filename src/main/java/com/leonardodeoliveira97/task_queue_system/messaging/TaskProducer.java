package com.leonardodeoliveira97.task_queue_system.messaging;

import com.leonardodeoliveira97.task_queue_system.model.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value(("${rabbitmq.exchange}"))
    private String exchange;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    public TaskProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Task task) {
        rabbitTemplate.convertAndSend(exchange, routingKey, task);
    }
}
