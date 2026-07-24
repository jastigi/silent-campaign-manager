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

## Detection

- [x] Detection Phase
- [x] DetectionProbabilityCalculator
- [x] SubmarineDetectionModifier

## Contact Generation

- [x] DetectedContact
- [x] ContactTypeGenerator
- [x] NationGenerator
- [ ] ThreatLevelGenerator
- [ ] ConfidenceLevelGenerator
- [ ] DetectedContactFactory

## Mission Resolution

- [ ] Mission Outcome Resolver
- [ ] Mission Score Integration

## Reporting

- [ ] Simulation Report Builder
- [ ] Timeline Formatter
- [ ] Mission Debrief

## Persistence

- [ ] Optional Simulation Persistence
- [ ] Simulation History

## Testing

- [ ] Simulation Engine Unit Tests
- [ ] Detection Tests
- [ ] Contact Generation Tests
- [ ] REST Simulation Tests

## Documentation

- [x] architecture.md
- [x] domain-model.md
- [x] dto-guidelines.md
- [x] mapper-guidelines.md
- [x] repository-guidelines.md
- [x] simulation-engine.md
- [ ] Simulation Testing Guide

---

# Release 0.9 — Operational Simulation

## Weather Engine

- [ ] Weather Generator
- [ ] Sea State
- [ ] Visibility

## Sonar

- [ ] Passive Detection
- [ ] Active Sonar
- [ ] Acoustic Signature

## Contact Behaviour

- [ ] Contact Tracking
- [ ] Contact Classification
- [ ] Contact Loss

## Tactical Decisions

- [ ] Shadowing
- [ ] Intelligence Gathering
- [ ] Abort Mission

## Mission Evaluation

- [ ] Dynamic Mission Scoring
- [ ] Operational Success Calculation

---

# Release 1.0 — Campaign Simulator

## Campaign Engine

- [ ] Campaign Timeline
- [ ] Dynamic Patrol Generation
- [ ] AI Opponent

## Combat

- [ ] Weapon Launch
- [ ] Torpedo Resolution
- [ ] Damage System

## Logistics

- [ ] Fuel
- [ ] Supplies
- [ ] Maintenance

## Statistics

- [ ] Campaign Statistics
- [ ] Fleet Statistics
- [ ] Historical Reports

## User Experience

- [ ] Advanced Swagger Examples
- [ ] Import / Export
- [ ] Final Documentation

---

# Development Rules

Every implementation sprint must include:

- Implementation
- Validation
- Documentation
- Commit

No new abstractions should be introduced unless they are immediately used by the current sprint.

Simulation phases should orchestrate the workflow only.

Business rules belong to dedicated calculators, generators, modifiers or resolvers.
