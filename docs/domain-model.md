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
