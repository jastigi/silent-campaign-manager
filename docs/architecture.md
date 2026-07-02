# Architecture

Silent Campaign Manager follows a layered architecture that separates presentation, business logic and persistence.

```
                REST API

                    │

             Controllers

                    │

              Service Layer

        ┌─────────────────────┐

        ▼                     ▼

Risk Evaluation Engine   Mission Evaluation Engine

        ▼                     ▼

     Strategy Pattern    Strategy Pattern

        └─────────┬───────────┘

                  ▼

            Repository Layer

                  ▼

              PostgreSQL
```

## Layers

### Controllers

Expose REST endpoints.

### Services

Contain business logic.

### Repositories

Persistence using Spring Data JPA.

### Engines

Specialized business rule engines using the Strategy Pattern.
