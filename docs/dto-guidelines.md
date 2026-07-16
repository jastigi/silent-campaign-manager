# DTO Guidelines

## Request DTO

- Used for incoming HTTP requests
- Contains validation annotations
- Never contains business logic

## Response DTO

- Used for outgoing HTTP responses
- Never contains validation annotations
- Never exposes JPA entities
- May use Builder pattern

## Naming

<Entity>RequestDTO

<Entity>ResponseDTO
