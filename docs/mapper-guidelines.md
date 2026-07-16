# Mapper Guidelines

## Responsibilities

A mapper converts objects between layers.

Allowed conversions:

- RequestDTO → Entity
- Entity → ResponseDTO
- Business Object → ResponseDTO

## Forbidden

A mapper must never:

- Inject repositories
- Inject services
- Execute business logic
- Perform validations
- Access the database

## Naming

<Entity>Mapper

Examples:

CampaignMapper

PatrolMapper

MissionEvaluationMapper
