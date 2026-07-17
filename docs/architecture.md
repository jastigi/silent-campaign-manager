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

### Controller Layer

Controllers expose REST endpoints and delegate all business logic to the service layer.

Responsibilities:

- Receive HTTP requests
- Validate input DTOs
- Invoke application services
- Return ResponseEntity with DTOs

Controllers must never:

- Access repositories directly
- Contain business rules
- Return JPA entities
- Execute simulation logic

## DTO Layer

DTOs define the public contract of the REST API.

Rules:

- Separate request and response models.
- Never expose JPA entities.
- Keep DTOs free of business logic.
- Validation belongs only to Request DTOs.

## Mapper Layer

Mappers isolate the REST API from the persistence model.

Responsibilities:

- Convert RequestDTO to Entity
- Convert Entity to ResponseDTO
- Convert business objects to REST DTOs

Mappers must never:

- Access repositories
- Invoke services
- Execute business rules
- Perform persistence operations

## Service Layer

The service layer contains all business logic.

Services are organized by business domain rather than technical role.

Each module may contain:

- Service interfaces
- Service implementations
- Strategy implementations
- Engines
- Calculators
- Domain-specific models

## Repository Layer

Repositories encapsulate persistence operations using Spring Data JPA.

Responsibilities:

- CRUD operations
- Derived query methods
- Pagination
- Sorting
- Specifications

Repositories must never:

- Execute business logic
- Invoke services
- Return DTOs
- Calculate statistics
- Perform mission evaluation

## Entity Layer

Entities represent the core domain model.

Rules:

- Prefer @Getter and @Setter over @Data.
- Always provide a no-argument constructor.
- Use EnumType.STRING for enumerations.
- Prefer LAZY loading for associations.
- Avoid automatic equals(), hashCode() and toString() generation.
- Never expose entities directly through the REST API.

Responsibilities:

- Model business concepts
- Persist domain state
- Define relationships

Entities should avoid:

- Business services
- DTO responsibilities
- REST concerns
- Automatic equals/hashCode generation

## Configuration Layer

Configuration classes define framework behaviour only.

Responsibilities:

- Bean creation
- Framework configuration
- External integrations

Configuration classes must never:

- Contain business logic
- Access repositories
- Execute services

## Exception Layer

Exceptions centralize API error handling.

Responsibilities:

- Represent application failures
- Provide meaningful HTTP responses
- Keep controllers free from try/catch blocks

Rules:

- Use @ControllerAdvice
- Prefer specific exceptions
- Reuse common base exceptions
- Never expose stack traces

## Specification Layer

Specifications provide dynamic query composition using Spring Data JPA.

Responsibilities:

- Dynamic filtering
- Search criteria composition
- Complex query building

Specifications must never:

- Execute business logic
- Replace repositories
- Return DTOs

## Security Layer

The security layer is responsible for authentication and authorization.

Responsibilities:

- JWT validation
- Authentication filters
- Token generation
- Security context initialization

Security components must never:

- Contain business logic
- Access controllers
- Execute persistence directly
