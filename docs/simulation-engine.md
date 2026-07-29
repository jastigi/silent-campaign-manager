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

- PassiveSonarDetectionModifier

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
- NationGenerator
- ThreatLevelGenerator
- ConfidenceLevelGenerator

Threat and confidence values are generated according to the detected contact type.

Future generators will also determine:

- Nation
- Threat Level
- Confidence Level
- Submarine Class

This keeps contact creation independent from the detection workflow and allows tactical rules to evolve separately.

### Detected Contact Factory

`DetectedContactFactory` coordinates the contact generators and creates the complete transient contact model.

The detection phase no longer knows how individual contact attributes are generated.

Current generation flow:

1. ContactTypeGenerator
2. NationGenerator
3. ThreatLevelGenerator
4. ConfidenceLevelGenerator
5. DetectedContactFactory

## Contact Classification

Detected contacts are processed by an independent classification phase.

Classification probability is currently based on the contact confidence level.

A successfully processed contact changes from `UNCLASSIFIED` to `CLASSIFIED`.

Current flow:

1. Contact detection
2. Contact generation
3. Contact classification
4. Return phase

## Mission Outcome Resolution

The simulation engine includes a `MissionOutcomeResolver` that derives a tactical mission outcome from the completed `SimulationResult`.

Current outcomes:

- `SUCCESS`
- `PARTIAL_SUCCESS`
- `FAILURE`

The initial resolution rules are:

- No detected contacts produce `FAILURE`.
- Detected contacts without successful classification produce `PARTIAL_SUCCESS`.
- At least one classified contact produces `SUCCESS`.

The resolver does not modify or persist the simulation result. It derives the outcome from the data already generated by the simulation pipeline.

These initial generic rules will later be refined through mission-specific success strategies based on `MissionType`.

Current mission success strategies:

- Deterrence Patrol
- Follow SSBN
- Hunt SSN
- Surveillance
- Intelligence
- Special Operation
- Escort
- Training

Every supported `MissionType` now has its own mission success rules.

### Mission Resolution

- Integrated mission outcome resolution into the simulation application service.
- Simulation REST responses now expose the resolved mission outcome.
- Kept the simulation engine independent from REST DTOs and persistence.

## Mission Scoring

`SimulationMissionScoreCalculator` converts the resolved mission outcome into a numeric score.

Current base scores:

- `SUCCESS`: 100
- `PARTIAL_SUCCESS`: 70
- `FAILURE`: 30

The score is reduced by simulation incidents and lost contacts and is always limited to the range from 0 to 100.

This calculator operates on transient simulation results and remains separate from the existing patrol persistence scoring service.
The calculated mission score is included in `ResolvedSimulationResult` and exposed through the simulation REST response.

The application service coordinates:

1. Simulation execution
2. Mission outcome resolution
3. Mission score calculation
4. REST DTO mapping

## Simulation Reporting

`SimulationReportBuilder` creates an operational summary after mission outcome resolution and scoring.

The report currently includes:

- Mission type
- Mission outcome
- Mission score
- Detected contacts
- Classified contacts
- Incidents

Report generation belongs to the application reporting layer and does not modify the internal simulation result.

### Mission Debrief

`SimulationDebriefBuilder` produces a narrative operational assessment of the completed patrol.

The current debrief includes:

- Mission outcome
- Detected contacts
- Classified contacts
- Hostile contacts
- Incidents
- Lost contacts

The debrief is exposed through the simulation REST response while remaining independent from the simulation engine.

### Timeline Formatting

`SimulationTimelineFormatter` converts structured simulation events into ordered human-readable timeline entries.

The formatter centralizes presentation formatting and keeps the REST mapper free from reporting rules.

Current timeline format:

`date | event type | description`

## Environmental Model

The simulation includes an environmental model generated at the beginning of each patrol.

Current environmental components:

- `WeatherCondition`
- `SeaState`
- `WeatherReport`
- `WeatherGenerator`
- `RandomWeatherGenerator`

The generated `WeatherReport` is stored in `SimulationContext` and remains available to every subsequent simulation phase.

Current weather conditions:

- `CALM`
- `MODERATE`
- `ROUGH`
- `STORM`

Current sea states:

- `SEA_STATE_1`
- `SEA_STATE_2`
- `SEA_STATE_3`
- `SEA_STATE_4`
- `SEA_STATE_5`

Weather generation is recorded as a structured simulation event.

Weather conditions are generated at the beginning of every simulation and stored in `SimulationContext`.

Weather also modifies contact classification probability:

- `CALM`: +10 percentage points
- `MODERATE`: no modifier
- `ROUGH`: -10 percentage points
- `STORM`: -20 percentage points

The classification modifier is applied to the contact confidence level before the probabilistic classification roll.

Sea state and weather effects on classification remain planned.

Sea state also modifies contact detection:

- `SEA_STATE_1`: +10 percentage points
- `SEA_STATE_2`: +5 percentage points
- `SEA_STATE_3`: no modifier
- `SEA_STATE_4`: -10 percentage points
- `SEA_STATE_5`: -20 percentage points

Weather condition and sea state modifiers are applied sequentially before resolving contact detection.

### Visibility

Each generated weather report includes an operational visibility level:

- `EXCELLENT`
- `GOOD`
- `POOR`
- `ZERO`

Visibility modifies contact classification probability:

- `EXCELLENT`: +10 percentage points
- `GOOD`: +5 percentage points
- `POOR`: -10 percentage points
- `ZERO`: -20 percentage points

Weather condition and visibility modifiers are combined before the classification roll.

## Passive Sonar

`PassiveSonarDetectionModifier` represents the passive-search capability of the assigned submarine.

Current role modifiers:

- `SSN`: +15 percentage points
- `SSBN`: -5 percentage points

The passive sonar modifier is applied before environmental detection modifiers.

The previous generic submarine-role modifier has been replaced to avoid applying duplicate platform bonuses.

### Acoustic Signature

Each `SubmarineClass` defines an acoustic signature:

- `ULTRA_QUIET`
- `QUIET`
- `MODERATE`
- `LOUD`

The assigned submarine acoustic signature modifies passive sonar effectiveness:

- `ULTRA_QUIET`: +10 percentage points
- `QUIET`: +5 percentage points
- `MODERATE`: no modifier
- `LOUD`: -10 percentage points

The acoustic modifier represents platform self-noise and its effect on passive listening performance.

Role and acoustic-signature modifiers are combined inside `PassiveSonarDetectionModifier`.

### Active Sonar

Active sonar is used as a secondary detection attempt when passive detection fails.

Current supported missions:

- `HUNT_SSN`
- `ESCORT`
- `TRAINING`

Active sonar adds 20 percentage points to the final passive detection probability, limited to a maximum of 100.

Silent or covert missions do not use active sonar automatically.

Both successful and failed active-sonar sweeps are recorded in the simulation timeline.

## Tactical Contact Behaviour

Detected contacts receive an operational behaviour after classification.

Current behaviours:

- `UNAWARE`
- `EVASIVE`
- `SHADOWING`
- `AGGRESSIVE`

Behaviour selection currently depends on `ThreatLevel` and a probabilistic tactical decision.

The behaviour is stored in the transient `DetectedContact` and recorded in the simulation timeline.

The contact-behaviour phase executes after classification and before the return phase.

### Shadowing Decision

After contact behaviour has been resolved, the simulation evaluates whether the patrol should begin tracking the detected contact.

Only classified contacts may be shadowed.

Current missions eligible for shadowing:

- FOLLOW_SSBN
- HUNT_SSN
- SURVEILLANCE

The actual tracking process is implemented separately.

### Contact Tracking

After a patrol decides to shadow a classified contact, the simulation determines whether tracking can be maintained.

Tracking probability depends on the detected contact behaviour.

A successful tracking attempt does not resolve the mission by itself but enables future tactical decisions.

### Contact Loss

After the tracking phase, the simulation determines whether a shadowed contact has been lost.

A contact is considered lost when:

- the patrol decided to shadow the contact;
- tracking could not be established or maintained.

Contacts that were never selected for shadowing are not treated as lost.

Lost contacts remain in the simulation contact collection so that reporting and mission evaluation can inspect their complete tactical history.

When a contact is lost:

- the contact `lost` state is set to `true`;
- the `contactsLost` counter in `SimulationContext` is incremented;
- a `CONTACT_LOST` event is added to the simulation timeline.
