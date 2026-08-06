# Silent Campaign Manager Development Roadmap

This document tracks the technical progress of the project and defines the implementation roadmap towards version 1.0.

Unlike the public README, this document is intended for development planning.

---

# Release 0.8 — Simulation Engine Foundation

## Simulation Core

- [x] Simulation Engine
- [x] Ordered Simulation Pipeline
- [x] Simulation Context
- [x] Simulation Clock
- [x] Simulation Events
- [x] Simulation Event Types
- [x] Simulation Result
- [x] Simulation REST Endpoint

---

## Detection Engine

### Detection

- [x] Detection Phase
- [x] DetectionProbabilityCalculator
- [x] Passive Sonar Detection Modifier

---

## Contact Generation

- [x] DetectedContact
- [x] ContactTypeGenerator
- [x] NationGenerator
- [x] ThreatLevelGenerator
- [x] ConfidenceLevelGenerator
- [x] DetectedContactFactory

---

## Contact Resolution

- [x] Contact Classification

Further contact-resolution mechanics are planned for Release 0.9:

- Contact Tracking
- Contact Loss
- Intelligence Gathering

---

## Mission Resolution

- [x] Mission Success Strategy
  - [x] Deterrence Patrol Strategy
  - [x] Follow SSBN Strategy
  - [x] Hunt SSN Strategy
  - [x] Surveillance Strategy
  - [x] Intelligence Strategy
  - [x] Special Operation Strategy
  - [x] Escort Strategy
  - [x] Training Strategy
- [x] Mission Outcome Resolver
- [x] Mission Success Calculator
- [x] Mission Score Integration

---

## Reporting

- [x] Simulation Report Builder
- [x] Timeline Formatter
- [x] Mission Debrief Report

---

## Persistence

Simulation persistence is deferred until the transient simulation model is stable.

Planned for a later release:

- Optional Simulation Persistence
- Simulation History

---

## Testing

- [x] Simulation Engine Unit Tests
- [x] Detection Tests
- [x] Contact Generation Tests
- [x] Mission Resolution Tests
- [x] Reporting Tests
- [x] REST Simulation Tests
- [x] Full Regression Test Suite

---

## Documentation

- [x] architecture.md
- [x] domain-model.md
- [x] dto-guidelines.md
- [x] mapper-guidelines.md
- [x] repository-guidelines.md
- [x] simulation-engine.md
- [x] Simulation Testing Guide
- [x] Release 0.8 Final Review

---

# Release 0.9 — Operational Simulation

## Weather Engine

- [x] Weather Model
- [x] Weather Generator
- [x] Weather Context Integration
- [x] Weather Detection Modifier
- [x] Sea State Detection Modifier
- [x] Weather Classification Modifier
- [x] Visibility

---

## Contact Resolution

- [x] Contact Tracking
- [x] Contact Loss
- [x] Intelligence Gathering

---

## Simulation Persistence

- [x] Simulation Persistence Foundation
- [x] Automatic Simulation Result Persistence
- [x] Simulation History Service
- [x] Simulation History REST API
- [x] Simulation History Pagination
- [x] Simulation History Filtering by Patrol

---

## Sonar Engine

- [x] Passive Detection
- [x] Active Sonar
- [x] Acoustic Signature

---

## Tactical AI

- [x] Contact Behaviour
- [x] Shadowing Decision
- [x] Contact Tracking
- [x] Contact Loss
- [x] Intelligence Gathering

---

## Mission Evaluation

- [x] Mission Outcome Strategies
- [x] Mission Score Calculation
- [x] Mission Report Generation
- [x] Mission Debrief Generation
- [x] Tactical Mission Evaluation Orchestration

---

# Release 1.0 — Campaign Simulator

## Campaign Engine

- [x] Campaign Simulation Architecture
- [x] Campaign Simulation Foundation
- [x] Ordered Patrol Orchestration
- [x] Campaign Simulation Result
- [x] Campaign Simulation REST API
- [x] Campaign Simulation Response DTO
- [x] Campaign Simulation REST Tests
- [x] Campaign Progression
- [x] Derived Campaign Progress
- [x] Campaign Statistics
- [x] Statistics from Simulation History
- [x] Campaign Execution Persistence
- [x] Campaign Execution Status
- [x] Failed Campaign Execution Recording
- [x] Campaign Execution History API
- [x] Paginated Campaign Execution History
- [x] Campaign Execution History REST Tests
- [x] Campaign Timeline
- [x] Dynamic Patrol Generation
- [x] Active Submarine Patrol Generation
- [x] Duplicate Patrol Prevention
- [x] Dynamic Patrol Generation REST API
- [x] Dynamic Patrol Generation Tests
- [x] AI Opponent
- [x] Deterministic Opponent Decision Rules
- [x] Threat-Aware Contact Behaviour
- [x] Opponent Decision Behaviour Mapping
- [x] Opponent Pipeline Integration Tests

---

## Campaign Lifecycle

- [x] Campaign Lifecycle Design
- [x] Campaign State Diagram
- [x] Campaign Transition Rules
- [x] ADR-003 Lifecycle Decision
- [x] Campaign Lifecycle Service
- [x] Finish Campaign Transition
- [x] Abandon Campaign Transition
- [x] Campaign Execution Status Validation
- [x] Campaign Lifecycle REST API
- [x] Campaign Lifecycle Unit Tests
- [x] Campaign Lifecycle Integration Tests

---

## Combat

- [ ] Weapon Launch
- [ ] Torpedo Resolution
- [ ] Damage System

---

## Logistics

- [ ] Fuel Consumption
- [ ] Crew Fatigue
- [ ] Maintenance

---

## Statistics

- [ ] Campaign Statistics
- [ ] Fleet Statistics
- [ ] Historical Reports

---

## User Experience

- [ ] Swagger Examples
- [ ] Import / Export
- [ ] Final Documentation

---

# Development Rules

Every implementation sprint must include:

- Implementation
- Validation
- Documentation
- Commit

Simulation phases orchestrate the workflow only.

Business rules belong to dedicated:

- Calculators
- Generators
- Modifiers
- Factories
- Resolvers
