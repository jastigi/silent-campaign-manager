# ADR-002 — Campaign progress is derived from persisted simulations

- Status: Accepted
- Date: 2026-07-30
- Release: 1.0
- Sprint: 42.2

## Context

Campaign execution requires a consistent way to report how many patrols
have been completed and how many remain pending.

The application already persists every completed patrol simulation in
`SimulationRecord`.

Persisting an additional mutable campaign-progress value would duplicate
information that can already be derived from existing data.

A patrol may also be simulated more than once, so counting simulation
records directly would produce an incorrect completed-patrol count.

## Decision

Campaign progress will be calculated from:

- the number of patrols belonging to the campaign;
- the number of distinct campaign patrols with at least one persisted
  simulation record.

The calculated values will be represented by the immutable
`CampaignProgress` object.

Campaign progress will not be persisted in a separate entity or table.

An empty campaign will have zero-percent progress and will not be
considered completed.

## Alternatives considered

### Persist progress on `Campaign`

Rejected because the value could become inconsistent with patrol and
simulation data.

### Count every simulation record

Rejected because repeated simulations of one patrol would incorrectly
increase campaign completion.

### Calculate progress only from the current execution result list

Rejected because this would ignore simulations completed in previous
executions and would not support future campaign resumption.

## Consequences

### Positive

- No duplicated persistence state.
- Progress always reflects persisted simulations.
- Repeated patrol simulations do not distort completion.
- Progress can represent execution across multiple application sessions.
- The model is ready for future campaign resumption.

### Negative

- Calculating progress requires database count queries.
- Progress depends on successful simulation persistence.
- A campaign with no patrols is not considered complete.
