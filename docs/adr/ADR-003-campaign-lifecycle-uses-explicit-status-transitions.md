# ADR-003 — Campaign lifecycle uses explicit status transitions

- Status: Accepted
- Date: 2026-08-03
- Release: 1.0
- Sprint: 43.0

## Context

The campaign layer now supports orchestration, progression and historical
statistics.

Release 1.0 requires campaigns to have an explicit operational lifecycle.

The existing domain already stores campaign state through
`CampaignStatus`:

- `ACTIVE`;
- `FINISHED`;
- `ABANDONED`.

A State Pattern was considered to encapsulate behaviour in one class per
campaign state.

However, the current states define transition rules but do not yet own
enough distinct behaviour to justify a hierarchy of state objects.

## Decision

Campaign lifecycle will continue to use the existing persisted
`CampaignStatus` enum.

All transition rules will be centralized in a dedicated
`CampaignLifecycleService`.

The service will validate:

- current status;
- target status;
- campaign progression;
- terminal-state restrictions.

Campaign entities, controllers and simulation services must not implement
transition rules independently.

State Pattern will not be introduced in Release 1.0.

## Alternatives considered

### Implement State Pattern immediately

Rejected because the current lifecycle has only three states and two
valid transitions.

Introducing state classes now would add interfaces, implementations and
dependency wiring without reducing meaningful conditional complexity.

### Allow services to update `Campaign.status` directly

Rejected because transition rules would become distributed across
controllers and services.

This could allow inconsistent or invalid state changes.

### Derive status entirely from campaign progression

Rejected because progression and lifecycle status represent different
concepts.

A campaign with complete progression may remain active until the explicit
finish transition occurs.

A partially completed campaign may be explicitly abandoned.

## Consequences

### Positive

- Reuses the existing database model.
- Avoids unnecessary abstractions.
- Keeps transition rules in one application service.
- Makes invalid transitions testable.
- Preserves a clear distinction between progression and lifecycle state.
- Allows later migration to State Pattern if behaviour becomes complex.

### Negative

- The lifecycle service will contain explicit status checks.
- Adding many future states may increase transition complexity.
- Existing setters technically still permit direct status mutation inside
  the codebase.

These limitations are acceptable for Release 1.0.

A new ADR must reconsider the design if paused, suspended, resumable or
failed campaign execution is introduced.
