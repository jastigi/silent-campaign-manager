# Campaign Lifecycle

## Overview

Release 1.0 introduces an explicit lifecycle for campaign execution.

The campaign lifecycle defines:

- which campaign states exist;
- which transitions are valid;
- which application operations trigger those transitions;
- what conditions must be satisfied before a transition;
- which transitions are terminal.

Campaign lifecycle management belongs to the campaign layer.

The tactical simulation engine remains unaware of campaign state.

---

## Current Campaign Status Model

Campaign status is represented by the existing `CampaignStatus` enum:

```text
ACTIVE
FINISHED
ABANDONED
```

The current model does not include a separate `PLANNED` state.

A newly created campaign is expected to enter the system as `ACTIVE`.

This preserves the existing REST contract and database representation.

---

## State Definitions

### ACTIVE

The campaign is available for operational activity.

An active campaign may:

- contain patrols;
- execute patrol simulations;
- calculate campaign progression;
- calculate campaign statistics;
- transition to `FINISHED`;
- transition to `ABANDONED`.

An active campaign is the only campaign state that may execute new campaign simulations.

---

### FINISHED

The campaign has completed its operational lifecycle.

A campaign may transition from `ACTIVE` to `FINISHED` only when:

- the campaign contains at least one patrol;
- every campaign patrol has at least one persisted simulation;
- campaign progression reports `completed = true`.

A finished campaign:

- remains available for historical queries;
- retains its patrols and simulation records;
- retains its statistics;
- cannot execute new campaign simulations;
- cannot return to `ACTIVE`.

`FINISHED` is a terminal state.

---

### ABANDONED

The campaign was intentionally terminated before normal completion.

An active campaign may transition to `ABANDONED` regardless of its current progression.

An abandoned campaign:

- retains all existing patrols and simulation history;
- remains available for reports and statistics;
- cannot execute new campaign simulations;
- cannot transition to `FINISHED`;
- cannot return to `ACTIVE`.

`ABANDONED` is a terminal state.

---

## State Diagram

```text
                    all patrols simulated
              +-------------------------------+
              |                               v
        +-----------+                    +----------+
        |  ACTIVE   | -----------------> | FINISHED |
        +-----------+                    +----------+
              |
              | explicit abandonment
              v
        +-----------+
        | ABANDONED |
        +-----------+
```

There are no transitions out of:

```text
FINISHED
ABANDONED
```

---

## Transition Matrix

| Current state | Target state | Allowed | Required condition               |
| ------------- | ------------ | ------: | -------------------------------- |
| `ACTIVE`      | `FINISHED`   |     Yes | Campaign progression is complete |
| `ACTIVE`      | `ABANDONED`  |     Yes | Explicit abandonment request     |
| `ACTIVE`      | `ACTIVE`     |      No | No state change is required      |
| `FINISHED`    | `ACTIVE`     |      No | Terminal state                   |
| `FINISHED`    | `ABANDONED`  |      No | Terminal state                   |
| `ABANDONED`   | `ACTIVE`     |      No | Terminal state                   |
| `ABANDONED`   | `FINISHED`   |      No | Terminal state                   |

---

## Transition Ownership

Campaign state transitions will be coordinated by a dedicated campaign
lifecycle service.

Planned responsibility:

```text
CampaignLifecycleService
        |
        +--> CampaignRepository
        |
        +--> CampaignProgressService
```

The lifecycle service will:

- load the campaign;
- validate its current state;
- validate the requested transition;
- verify campaign progression when finishing;
- update the campaign status;
- persist the updated campaign.

The lifecycle service will not:

- execute patrol simulations;
- calculate tactical outcomes;
- persist simulation records;
- calculate campaign statistics;
- delete campaign history.

---

## Campaign Simulation Rules

`CampaignSimulationService` must only execute campaigns whose status is:

```text
ACTIVE
```

Attempts to execute a campaign with status:

```text
FINISHED
ABANDONED
```

must be rejected before any patrol simulation begins.

This validation will be implemented in the campaign lifecycle integration
sprint.

---

## Completion Rules

Campaign completion is derived from `CampaignProgress`.

A campaign can be finished when:

```text
totalPatrols > 0
completedPatrols == totalPatrols
pendingPatrols == 0
completionPercentage == 100.0
completed == true
```

The persisted campaign status is not itself the source of progression.

The distinction is:

```text
CampaignProgress
    describes operational completion derived from simulations

CampaignStatus
    describes the persisted lifecycle decision
```

A campaign may therefore temporarily have:

```text
CampaignProgress.completed = true
CampaignStatus = ACTIVE
```

until the lifecycle transition to `FINISHED` is performed.

---

## Empty Campaigns

A campaign without patrols cannot transition to `FINISHED`.

The existing campaign progression rule defines an empty campaign as:

```text
totalPatrols = 0
completionPercentage = 0.0
completed = false
```

An empty active campaign may still be abandoned.

---

## Failure Behaviour

Lifecycle transitions use fail-fast validation.

Invalid transitions must:

- throw a domain exception;
- leave the campaign status unchanged;
- avoid partial persistence;
- avoid executing any patrol simulation.

The precise exception model will be implemented with the lifecycle
service.

---

## Persistence

Campaign lifecycle state remains persisted in the existing column:

```text
campaigns.status
```

No new table or column is required for the initial lifecycle model.

Campaign simulation records remain independent and are not deleted when a
campaign becomes terminal.

---

## Future Evolution

Future releases may introduce:

```text
PLANNED
PAUSED
SUSPENDED
FAILED
```

These states are deliberately excluded from Release 1.0 because the
current product does not yet support:

- asynchronous campaign execution;
- execution checkpoints;
- administrative suspension;
- automatic retries;
- resumable failed executions.

If those behaviours are introduced, the lifecycle model and the possible
use of State Pattern should be reviewed through a new ADR.

---

## Acceptance Criteria

The campaign lifecycle implementation will be complete when:

- only active campaigns can execute simulations;
- active campaigns can be finished when progression is complete;
- active campaigns can be abandoned explicitly;
- finished and abandoned campaigns are terminal;
- invalid transitions are rejected;
- lifecycle transitions are covered by unit and integration tests;
- campaign history remains available after terminal transitions.
