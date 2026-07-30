# ADR-001 — Campaign simulation delegates tactical execution

- Status: Accepted
- Date: 2026-07-29
- Release: 1.0
- Sprint: 42.1

## Context

Release 0.9 introduced a complete tactical simulation workflow for
individual patrols.

Release 1.0 requires campaigns to coordinate the execution of multiple
patrols.

The campaign layer must not duplicate tactical rules already implemented
by the patrol simulation engine.

## Decision

A dedicated `CampaignSimulationService` will coordinate campaign
execution.

The service will:

1. Load the requested campaign.
2. Load its patrols in chronological and deterministic order.
3. Delegate each patrol execution to `SimulationService`.
4. Collect each `ResolvedSimulationResult`.
5. Return a `CampaignSimulationResult`.

`CampaignSimulationService` will not:

- execute simulation phases;
- calculate detection or classification;
- evaluate tactical mission outcomes;
- persist simulation records directly;
- calculate campaign statistics.

Those responsibilities remain in their existing components or will be
introduced by later campaign-layer services.

## Alternatives considered

### Duplicate tactical execution in the campaign service

Rejected because it would couple the campaign layer to tactical rules and
duplicate logic already owned by `SimulationService`.

### Invoke `SimulationEngine` directly

Rejected because this would bypass patrol loading, tactical mission
evaluation and automatic simulation persistence performed by
`SimulationService`.

### Return only a list of patrol results

Rejected because a dedicated campaign result provides a stable contract
that can later include progression and statistics without replacing the
service return type.

## Consequences

### Positive

- Existing tactical simulation logic is fully reused.
- Campaign and patrol responsibilities remain separated.
- Tactical engine changes remain isolated from campaign orchestration.
- The campaign result can evolve without changing the service contract.
- Patrol simulation persistence continues to work unchanged.

### Negative

- Campaign execution is currently synchronous.
- A patrol failure stops the remaining campaign execution.
- No campaign-level execution state is persisted yet.
- Partially completed campaigns are not yet resumed automatically.

These limitations are accepted for the initial campaign simulation
foundation and will be addressed in later Release 1.0 sprints.
