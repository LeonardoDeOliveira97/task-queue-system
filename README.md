# Task Queue System

A simple Java + Spring Boot project demonstrating asynchronous task processing using RabbitMQ.

## Features
- Task creation via REST API
- Asynchronous processing using RabbitMQ
- RabbitMQ Management integration
- Status tracking (`PENDING`, `PROCESSING`, `COMPLETED`)

## Tech Stack
- Java 21
- Spring Boot
- RabbitMQ
- Spring Data JPA + H2
- Lombok

## How It Works
1. A task is submitted through the API.
2. It is pushed to a RabbitMQ queue.
3. A listener consumes the task, simulates background processing, then updates the task status.

## Running Locally

### Prerequisites
- Java 21+
- Docker (for RabbitMQ)
```bash
docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
