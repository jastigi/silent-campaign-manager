# Simulation Engine

## Overview

The Simulation Engine is the core component responsible for executing submarine patrol simulations.

Unlike the REST API, which manages persistence and application workflows, the Simulation Engine focuses exclusively on simulating patrol execution.

Its design follows the following principles:

- Open/Closed Principle
- Strategy Pattern
- Pipeline Architecture
- Separation of Simulation and Persistence
- Shared Simulation Context

---

## Architecture

SimulationEngine

↓

SimulationContext

↓

Ordered Simulation Pipeline

↓

SimulationResult

Each simulation phase operates independently and modifies the shared simulation context.

---

## Current Pipeline

Current simulation phases:

1. Transit
2. Patrol Area
3. Detection
4. Return

Future phases may include:

- Weather
- Sonar
- Intelligence
- Weapon Engagement
- Damage Control
- Logistics

The engine automatically executes all registered phases using Spring dependency injection and `@Order`.

---

## Simulation Context

The SimulationContext stores the mutable state of the simulation.

Current responsibilities:

- Patrol
- Simulation Date
- Operational State
- Event Log
- Detected Contacts
- Contacts Detected
- Contacts Lost
- Incidents

---

## Simulation Events

Simulation events are represented by `SimulationEvent`.

Each event contains:

- Date
- Event Type
- Description

Using structured events enables:

- Timeline generation
- Mission reports
- Event filtering
- Future GUI visualization

---

## Detected Contacts

Detected contacts are transient simulation objects.

They are **not** persisted automatically.

A detected contact may later become a persistent `Contact` entity if the application decides to store the simulation outcome.

---

## Design Principles

The Simulation Engine never:

- accesses repositories directly;
- modifies the database;
- exposes REST endpoints;
- depends on DTOs.

Its only responsibility is to execute simulations and produce a `SimulationResult`.

Persistence is handled by the application services.

---

## Probability Model

Simulation phases do not contain hardcoded business probabilities.

Probability calculations are delegated to dedicated calculator components, allowing the simulation model to evolve independently from the execution pipeline.

Current probability calculators:

- DetectionProbabilityCalculator

Future calculators may include:

- SonarDetectionCalculator
- WeaponHitProbabilityCalculator
- DamageProbabilityCalculator
- CrewEfficiencyCalculator
- WeatherImpactCalculator

This approach keeps simulation phases focused on orchestration while encapsulating tactical rules into reusable, testable components.

---

### Probability Modifiers

After the base probability has been calculated, simulation modifiers may adjust the final value according to the operational context.

Current modifiers:

- SubmarineDetectionModifier

Future modifiers may include:

- WeatherModifier
- CrewExperienceModifier
- SonarModifier
- IntelligenceModifier
- DamageModifier

Modifiers are applied sequentially, allowing complex probability models to be built from small, independent components.

---

## Current Status

Implemented:

- Simulation Engine
- Ordered Pipeline
- Simulation Context
- Simulation Clock
- Structured Events
- Detection Phase
- Transient Detected Contacts

Planned:

- Weather Engine
- Sonar Detection
- Tactical Decisions
- Combat Resolution
- Campaign Timeline
- AI Behaviour

## Contact Generation

Once a contact has been detected, dedicated generator components determine its characteristics.

Current generators:

- ContactTypeGenerator
- ThreatLevelGenerator

Threat level generation depends on the detected contact type.

Future generators will also determine:

- Nation
- Threat Level
- Confidence Level
- Submarine Class

This keeps contact creation independent from the detection workflow and allows tactical rules to evolve separately.
