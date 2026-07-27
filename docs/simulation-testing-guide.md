# Simulation Testing Guide

## Overview

This document describes how to validate the Silent Campaign Manager simulation engine.

The simulation test suite covers:

- Simulation pipeline execution
- Phase ordering
- Contact detection
- Contact generation
- Contact classification
- Mission outcome resolution
- Mission scoring
- Simulation reporting
- REST response mapping
- Endpoint security
- Full application regression

---

## Automated Test Execution

### Windows

```powershell
.\mvnw.cmd clean test
```

### Linux and macOS

```bash
./mvnw clean test
```

Expected result:

```text
BUILD SUCCESS
```

The Maven output must also report:

```text
Failures: 0
Errors: 0
```

---

## Test Categories

### Simulation Core

| Test class                 | Purpose                                                                                |
| -------------------------- | -------------------------------------------------------------------------------------- |
| `SimulationEngineTest`     | Verifies phase execution and conversion of `SimulationContext` into `SimulationResult` |
| `SimulationPhaseOrderTest` | Verifies the declared order of the simulation phases                                   |

### Detection

| Test class                               | Purpose                                              |
| ---------------------------------------- | ---------------------------------------------------- |
| `DetectionProbabilityCalculatorImplTest` | Verifies mission-based detection probabilities       |
| `SubmarineDetectionModifierImplTest`     | Verifies detection modifiers based on submarine role |
| `DetectionPhaseTest`                     | Verifies successful and failed contact detection     |

### Contact Generation

| Test class                       | Purpose                                           |
| -------------------------------- | ------------------------------------------------- |
| `ContactTypeGeneratorImplTest`   | Verifies contact type generation by mission       |
| `ContactAttributeGeneratorsTest` | Verifies nation, threat and confidence generation |
| `DetectedContactFactoryImplTest` | Verifies coordination of all contact generators   |

### Contact Classification

| Test class                         | Purpose                                               |
| ---------------------------------- | ----------------------------------------------------- |
| `ClassificationCalculatorImplTest` | Verifies classification probability                   |
| `ClassificationPhaseTest`          | Verifies successful and failed contact classification |

### Mission Resolution

| Test class                                 | Purpose                                                |
| ------------------------------------------ | ------------------------------------------------------ |
| `MissionSuccessStrategiesTest`             | Verifies the success rules for every supported mission |
| `MissionOutcomeResolverImplTest`           | Verifies mission strategy selection and delegation     |
| `SimulationMissionScoreCalculatorImplTest` | Verifies mission scoring and penalties                 |

### Reporting

| Test class                        | Purpose                                               |
| --------------------------------- | ----------------------------------------------------- |
| `SimulationReportBuilderTest`     | Verifies operational summary generation               |
| `SimulationTimelineFormatterTest` | Verifies timeline formatting                          |
| `SimulationDebriefBuilderTest`    | Verifies narrative mission debrief generation         |
| `SimulationMapperTest`            | Verifies mapping of resolved simulations to REST DTOs |

### REST and Security

| Test class                        | Purpose                                                                |
| --------------------------------- | ---------------------------------------------------------------------- |
| `SimulationControllerTest`        | Verifies the simulation REST response and missing-patrol handling      |
| `CampaignSecurityIntegrationTest` | Verifies authenticated and unauthenticated access to secured endpoints |

---

## Simulation Engine Tests

The engine tests verify:

- Every configured simulation phase is executed.
- Phases run in the expected order.
- `SimulationContext` is converted into `SimulationResult`.
- Event, contact and operational counters are preserved.
- Initial values remain valid when no phases are configured.

Expected pipeline:

1. Transit
2. Patrol Area
3. Detection
4. Classification
5. Return

Weather generation currently occurs inside `TransitPhase`.

---

## Detection Tests

Detection tests verify:

- Base detection probability depends on `MissionType`.
- Submarine role modifies detection probability.
- Failed detection records the correct event.
- Successful detection creates and stores a transient contact.
- Simulation time advances during detection.

---

## Contact Generation Tests

Contact generation tests verify:

- Contact type depends on the patrol mission.
- Nation generation delegates to `SimulationRandomService`.
- Threat level depends on contact type.
- Confidence level uses the correct range.
- `DetectedContactFactory` coordinates all contact generators.

---

## Contact Classification Tests

Classification tests verify:

- Classification probability uses contact confidence.
- Confidence values are limited to the valid probability range.
- Successful classification changes the contact status to `CLASSIFIED`.
- Failed classification leaves the contact as `UNCLASSIFIED`.
- Classification events are added to the simulation timeline.

---

## Mission Resolution Tests

Mission resolution tests verify:

- Every supported `MissionType` has a success strategy.
- `MissionOutcomeResolver` selects the correct strategy.
- Mission outcomes may be:
  - `SUCCESS`
  - `PARTIAL_SUCCESS`
  - `FAILURE`
- Mission scoring applies the correct base score.
- Incident and lost-contact penalties are applied.
- Scores always remain between `0` and `100`.

---

## Reporting Tests

Reporting tests verify:

- Operational summaries contain mission metrics.
- Mission debriefs describe the simulation result.
- Timeline entries use the standard format.
- `SimulationMapper` exposes the complete REST response.

Timeline format:

```text
date | event type | description
```

---

## REST Simulation Tests

The simulation endpoint is:

```http
POST /api/v1/patrols/{id}/simulate
```

Automated REST tests verify:

- Successful responses return HTTP `200`.
- Missing patrols return HTTP `404`.
- Requests without authentication return HTTP `401`.
- Authenticated requests pass the security layer.
- Responses expose:
  - `summary`
  - `missionDebrief`
  - `missionOutcome`
  - `missionScore`
  - `finalState`
  - `completionDate`
  - `contactsDetected`
  - `contactsLost`
  - `incidents`
  - `timeline`

---

## Manual Swagger Validation

Start PostgreSQL:

```powershell
docker compose up -d
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

Open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

### Validation sequence

1. Authenticate through:

   ```http
   POST /api/v1/auth/login
   ```

2. Copy the generated JWT token.

3. Authorize Swagger using:

   ```text
   Bearer <token>
   ```

4. Create or select an existing patrol.

5. Execute:

   ```http
   POST /api/v1/patrols/{id}/simulate
   ```

6. Verify:
   - HTTP status is `200`.
   - `finalState` is `COMPLETED`.
   - `missionOutcome` matches the patrol mission.
   - `missionScore` is between `0` and `100`.
   - `summary` is populated.
   - `missionDebrief` is populated.
   - Timeline entries appear in chronological order.

Because detection and contact generation are probabilistic, repeated executions may produce different contacts, outcomes and scores.

---

## Missing Patrol Validation

Execute the simulation endpoint using an ID that does not exist:

```http
POST /api/v1/patrols/999999/simulate
```

Expected result:

```text
HTTP 404 Not Found
```

The response must contain a meaningful error message identifying the missing patrol.

---

## Security Validation

Execute the simulation endpoint without a JWT token:

```http
POST /api/v1/patrols/{id}/simulate
```

Expected result:

```text
HTTP 401 Unauthorized
```

Execute the same request with a valid JWT token.

Expected result:

- HTTP `200` when the patrol exists.
- HTTP `404` when the patrol does not exist.
- It must not return HTTP `401` or `403` for an authorized user.

---

## Full Regression Test

Run the complete Maven test suite:

### Windows

```powershell
.\mvnw.cmd clean test
```

### Linux and macOS

```bash
./mvnw clean test
```

Expected result:

```text
BUILD SUCCESS
```

Also verify:

```text
Failures: 0
Errors: 0
```

After the automated tests pass:

1. Start PostgreSQL using Docker Compose.
2. Start the Spring Boot application.
3. Authenticate successfully.
4. Execute a patrol simulation through Swagger.
5. Verify:
   - `missionOutcome`
   - `missionScore`
   - `summary`
   - `missionDebrief`
   - `timeline`
6. Execute the endpoint with an unknown patrol ID.
7. Confirm that the API returns HTTP `404`.
8. Execute the endpoint without authentication.
9. Confirm that the API returns HTTP `401`.

---

## Release 0.8 Regression Checklist

Before closing Release 0.8:

- [ ] Run the complete Maven test suite.
- [ ] Confirm `BUILD SUCCESS`.
- [ ] Confirm zero failures and errors.
- [ ] Start PostgreSQL through Docker Compose.
- [ ] Start the application.
- [ ] Authenticate successfully.
- [ ] Execute a patrol simulation through Swagger.
- [ ] Verify mission outcome and score.
- [ ] Verify summary, timeline and debrief.
- [ ] Verify an unknown patrol returns HTTP `404`.
- [ ] Verify an unauthenticated request returns HTTP `401`.
- [ ] Review `README.md`.
- [ ] Review `docs/simulation-engine.md`.
- [ ] Review `docs/release-notes/0.8.md`.
- [ ] Review `docs/development-roadmap.md`.
