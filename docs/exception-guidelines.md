# Exception Guidelines

## Exception hierarchy

RuntimeException

↓

ResourceNotFoundException

↓

CampaignNotFoundException

PatrolNotFoundException

...

## Rules

Controllers never catch business exceptions.

Services throw domain exceptions.

GlobalExceptionHandler converts exceptions into HTTP responses.
