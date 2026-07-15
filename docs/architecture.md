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
          ▼
 Mission Evaluation Service
          │
          ▼
 Mission Strategy Resolver
          │
   ┌──────┼──────────────────────────────┐
   ▼      ▼              ▼               ▼
Surveillance   Hunt SSN   Intelligence   Special Operation
 Strategy        Strategy      Strategy        Strategy
          │
          ▼
 AbstractMissionStrategy
          │
          ▼
 Contact Assessment Calculator
          │
          ▼
 Contact Risk Calculator
          │
          ▼
 Repository Layer
          │
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
- Contact Assessment Engine
- Mission Doctrine Engine
- Mission Evaluation
- Campaign Statistics
