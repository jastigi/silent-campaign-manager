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
- [x] SubmarineDetectionModifier

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
- [ ] REST Simulation Tests
- [ ] Full Regression Test Suite

---

## Documentation

- [x] architecture.md
- [x] domain-model.md
- [x] dto-guidelines.md
- [x] mapper-guidelines.md
- [x] repository-guidelines.md
- [x] simulation-engine.md
- [ ] Simulation Testing Guide
- [ ] Release 0.8 Final Review

---

# Release 0.9 — Operational Simulation

## Weather Engine

- [x] Weather Model
- [x] Weather Generator
- [x] Weather Context Integration
- [ ] Sea State Detection Modifier
- [ ] Weather Classification Modifier
- [ ] Visibility

---

## Contact Resolution

- [ ] Contact Tracking
- [ ] Contact Loss
- [ ] Intelligence Gathering

---

## Simulation Persistence

- [ ] Optional Simulation Persistence
- [ ] Simulation History

---

## Sonar Engine

- [ ] Passive Detection
- [ ] Active Sonar
- [ ] Acoustic Signature

---

## Tactical AI

- [ ] Contact Behaviour
- [ ] Contact Classification AI
- [ ] Shadowing
- [ ] Contact Evasion

---

## Mission Evaluation

- [ ] Dynamic Mission Scoring
- [ ] Operational Success Calculation

---

# Release 1.0 — Campaign Simulator

## Campaign Engine

- [ ] Campaign Timeline
- [ ] Dynamic Patrol Generation
- [ ] AI Opponent

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
