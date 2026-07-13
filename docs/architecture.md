# Architecture

Silent Campaign Manager follows a layered architecture that separates presentation, business logic and persistence.

```
                                    REST API
                       │
         Spring MVC Controllers
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
 Application Services      Statistics Services
          │                         │
          ▼                         ▼
 Mission Evaluation        Campaign Analytics
          │
          ▼
 Mission Scoring Engine
          │
 ┌────────┴─────────┐
 ▼                  ▼
Contact Risk Engine Mission Engine
          │
          ▼
 Strategy Pattern Components
          │
          ▼
    Repository Layer
          │
          ▼
 PostgreSQL Database
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

## Design Principles

The application follows several architectural principles:

- Layered Architecture
- Separation of Concerns
- Dependency Injection
- Strategy Pattern
- Repository Pattern
- DTO-based REST API
- Service-oriented business logic

## Current Modules

- Campaign Management
- Patrol Management
- Contact Management
- Patrol Events
- Mission Evaluation
- Campaign Statistics
