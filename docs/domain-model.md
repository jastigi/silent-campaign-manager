# Domain Model

```
Campaign
│
├── Patrol
│     │
│     ├── Submarine
│     ├── MissionType
│     ├── PatrolResult
│     ├── PatrolEvent
│     └── Contact
│
└── Campaign Statistics
```

## Domain Overview

The Campaign entity represents the highest-level operational unit.

Each Campaign contains multiple Patrols.

A Patrol is assigned to one Submarine, generates PatrolEvents and detects Contacts during its execution.

Mission evaluation services analyze patrol outcomes using specialized business rule engines.

Campaign statistics aggregate operational information across all patrols.

### Patrol Lifecycle

A patrol does not receive a final result when it is created.

```text
CREATE PATROL
     │
     ▼
 result = null
     │
     ▼
   PENDING
     │
     ▼
PATCH /api/v1/campaigns/{campaignId}/patrols/{id}/close
     │
     ▼
Mission Evaluation
     │
     ├── SUCCESS
     ├── PARTIAL_SUCCESS
     └── FAILURE

PatrolResult represents the persisted final outcome of a completed patrol.

The result is therefore not part of the general patrol creation or update contract. Clients may update general patrol information without altering its operational outcome.

The dedicated close operation evaluates the mission using the mission evaluation domain services and persists the resulting PatrolResult.

Mission risk and scoring data are supporting operational metrics. They do not independently redefine a persisted patrol result.

This ensures that campaign views, patrol reports and mission evaluations use the persisted patrol outcome as the authoritative final result.

#### Patrol Lifecycle Constraints

While `result` is `null`, the patrol is considered pending and may be updated, deleted or closed.

Once a final `PatrolResult` has been persisted, the patrol is considered closed. Closed patrols are immutable through the patrol management API:

- update operations are rejected;
- delete operations are rejected;
- repeated close operations are rejected.

These invalid lifecycle operations return HTTP `409 Conflict`.

Campaign-scoped mutation operations validate both the campaign identifier and patrol identifier, preventing a patrol from being modified through a different campaign context.

### Campaign Lifecycle Constraints

Campaigns are mutable only while their status is `ACTIVE`.

General campaign data such as name, description and start date may be updated while active. Campaign status transitions are not performed through the general update operation.

Valid lifecycle transitions are handled explicitly through the campaign lifecycle service:

```text
ACTIVE
├── FINISHED
└── ABANDONED

FINISHED and ABANDONED campaigns are historical records and cannot be edited or deleted.

Deletion is permitted only for an ACTIVE campaign that has no patrols and no persisted campaign execution history. This protects operational and audit data from accidental removal.
