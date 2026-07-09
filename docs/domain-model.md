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
