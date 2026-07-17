# Configuration Guidelines

## Responsibilities

Configuration classes configure infrastructure only.

Allowed:

- Bean definitions
- Framework configuration
- External integrations

Forbidden:

- Business logic
- Repository access
- Service invocation

## Naming

<Framework>Config

Examples:

SecurityConfig

OpenApiConfig

JacksonConfig
