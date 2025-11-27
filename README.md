# Expense Service

Spring Boot 3.5 service that records user expenses, exposes REST endpoints, and ingests expense events from Kafka into MySQL.

## Features
- REST API to add an expense and list expenses for a user
- Kafka consumer for expense events (`expense_service` by default)
- MySQL persistence via Spring Data JPA with auto DDL
- Lombok-based model/DTO mapping and default currency fallback (`inr`)

## Tech Stack
- Java 21, Gradle wrapper
- Spring Boot (web, data-jpa), Jackson
- MySQL (connector/j), HikariCP
- Apache Kafka (Spring Kafka)
- Lombok

## API
- `GET /expense/v1/getExpense?user_id={userId}`  
  Returns a JSON array of expenses for the user. Response fields: `external_id`, `amount`, `user_id`, `merchant`, `currency`, `created_at`.
- `POST /expense/v1/addExpense`  
  Headers: `X-User-Id: <user id>`  
  Body parameters (form or query-encoded): `amount` (required), `merchant` (optional), `currency` (optional; defaults to `inr`).  
  Returns `true` when the expense is persisted. (The controller currently binds request parameters rather than a JSON body.)

Server runs on port `9820` by default (`spring.application.name=expenseservice`).

## Kafka Ingestion
- Topic: `${spring.kafka.topic-json.name}` (default `expense_service`)
- Group: `${spring.kafka.consumer.group-id}` (default `expense-info-consumer-group`)
- Value deserializer: `com.example.expenseservice.consumer.ExpenseDeserializer`
- Expected payload (snake_case JSON):
  ```json
  {
    "external_id": "c0f4b4a2-1234-4a1b-8d5a-9cc9e6e91a12",
    "amount": 42.50,
    "user_id": "user-123",
    "merchant": "Coffee Shop",
    "currency": "usd",
    "created_at": "2024-01-01T12:00:00Z"
  }
  ```

## Configuration
Set via environment variables or edit `src/main/resources/application.properties`:
- `KAFKA_HOST` / `KAFKA_PORT` (default `localhost:9092`)
- `MYSQL_HOST` / `MYSQL_PORT` / `MYSQL_DB` (default `localhost:3306/expenseservice`)
- `MYSQL_USER` / `MYSQL_PASSWORD` (default `root` / `password`)
- `spring.jpa.hibernate.ddl-auto` is `create` with `hbm2ddl.auto=update`, so tables are auto-created.

## Run Locally
Prerequisites: JDK 21, MySQL running with the configured credentials, optional Kafka broker if consuming events.

```bash
# From repository root
./gradlew bootRun
```

Application is available at `http://localhost:9820`.

## Tests
```bash
./gradlew test
```
