# Domain Model

```
Campaign
    │
    ├──────── Patrol
    │              │
    │              ├── MissionType
    │              ├── PatrolEvent
    │              ├── Contact
    │              └── Assigned Submarine
    │
    └──────── Fleet
```

The Patrol entity represents the central element of the simulation.

Each patrol belongs to a campaign and generates contacts and events that are later evaluated by the simulation engines.
