# ADR-004 — Campaign executions are persisted independently

- Status: Accepted
- Date: 2026-08-04
- Release: 1.0
- Sprint: 43.5

## Context

Campaign simulation coordinates multiple tactical patrol simulations.

Each patrol result is already persisted independently as a
`SimulationRecord`.

However, the application cannot currently identify:

- when a campaign execution started;
- whether the complete orchestration succeeded;
- how many patrols were processed before a failure;
- when an execution finished;
- why an execution failed.

These values describe the strategic orchestration attempt rather than an
individual tactical simulation.

## Decision

Each campaign simulation attempt will create a persistent
`CampaignExecution`.

The execution uses the following states:

- `RUNNING`;
- `COMPLETED`;
- `FAILED`.

`CampaignExecution` stores only orchestration metadata:

- campaign;
- execution status;
- total patrol count;
- completed patrol count;
- start timestamp;
- completion timestamp;
- failure message.

Tactical outcomes, scores, contacts, intelligence and debriefs remain in
`SimulationRecord`.

Campaign execution persistence will use a dedicated service whose
operations run in their own transactions.

## Alternatives considered

### Store execution metadata on `Campaign`

Rejected because a campaign may be executed more than once and historical
attempts must remain available.

### Extend `SimulationRecord`

Rejected because a simulation record represents one patrol, while a
campaign execution represents orchestration of multiple patrols.

### Persist only successful executions

Rejected because failed and partial attempts are operationally valuable
for diagnostics and future resumption.

### Wrap the entire campaign execution in one transaction

Rejected because a later patrol failure would roll back previously
persisted tactical simulations and the execution audit record.

## Consequences

### Positive

- Campaign execution attempts become auditable.
- Failed executions retain diagnostic information.
- Partial campaign execution can be identified.
- Tactical and strategic persistence remain separated.
- Future execution history and resumption APIs have a stable foundation.

### Negative

- Campaign execution and patrol simulations are not committed atomically.
- Failure persistence is best-effort if the database itself is unavailable.
- Execution records are not yet linked directly to individual
  `SimulationRecord` rows.

The lack of direct record linkage is accepted for this foundation sprint
and can be revisited if execution replay requires it.
