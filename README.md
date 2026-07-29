<p align="center">
  <img src="docs/images/logo.png" alt="Silent Campaign Manager Logo" width="160">
</p>

![Silent Campaign Manager Banner](docs/images/banner.png)

# Silent Campaign Manager

> **Cold War Submarine Operations Simulator**

[![Release](https://img.shields.io/badge/release-v0.9.0-blue)](https://github.com/jastigi/silent-campaign-manager/releases)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791)
![Docker](https://img.shields.io/badge/Docker-2496ED)
![JUnit](https://img.shields.io/badge/JUnit-5-success)
![License](https://img.shields.io/badge/License-MIT-green)

A backend application built with **Java 21** and **Spring Boot 3.5** for managing and simulating Cold War submarine patrol operations.

The project combines a secured REST API, PostgreSQL persistence and an extensible tactical simulation engine capable of resolving submarine patrols through detection, classification, tracking, contact loss, intelligence gathering and mission evaluation.

## Table of Contents

- [Overview](#overview)
- [Release Status](#release-status)
- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Simulation Engine](#simulation-engine)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [API Documentation](#api-documentation)
- [REST API Overview](#rest-api-overview)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Screenshots](#screenshots)
- [Future Vision](#future-vision)
- [License](#license)

## Overview

Silent Campaign Manager is inspired by Cold War submarine operations and naval warfare simulations.

Unlike a traditional CRUD application, it provides a modular simulation platform capable of:

- managing campaigns, submarines, patrols, contacts and patrol events;
- evaluating patrol missions through mission-specific strategies;
- executing complete tactical patrol simulations;
- modelling environmental and sonar conditions;
- persisting completed simulation results;
- consulting a paginated operational history;
- protecting the REST API with JWT authentication.

The architecture is designed to remain testable and extensible as new tactical mechanics are introduced.

## Release Status

The current stable release is:

```text
v0.9.0
```

Release 0.9 completes the tactical simulation and persistence foundations.

It includes:

- complete patrol simulation pipeline;
- passive and active sonar;
- environmental weather modelling;
- contact detection and classification;
- contact tactical behaviour;
- shadowing and tracking;
- contact-loss resolution;
- intelligence gathering;
- tactical mission evaluation;
- mission scoring and debriefing;
- automatic persistence of completed simulations;
- paginated simulation history;
- simulation history filtered by patrol.

Detailed release information is available in:

```text
docs/release-notes/0.9.md
```

## Features

### Domain Management

- Campaign management
- Patrol management
- Submarine management
- Contact management
- Patrol event management
- Patrol contact assignment
- Campaign operational statistics
- Patrol reporting
- Automatic patrol closing
- Mission evaluation reports

### Tactical Simulation

- Ordered patrol simulation pipeline
- Transit phase
- Patrol-area phase
- Contact detection
- Contact classification
- Contact tactical behaviour
- Shadowing decisions
- Contact tracking
- Contact-loss resolution
- Intelligence gathering
- Return phase
- Tactical mission evaluation
- Mission-specific outcomes
- Numeric mission scoring
- Operational reports
- Formatted event timelines
- Narrative mission debriefing

### Environmental and Sonar Modelling

- Weather generation
- Sea-state modelling
- Visibility modelling
- Environmental detection modifiers
- Environmental classification modifiers
- Passive sonar detection
- Active sonar sweeps
- Submarine acoustic signatures
- Mission-dependent sonar behaviour

### Simulation Persistence

- Automatic persistence of completed simulations
- Stable operational simulation records
- Global paginated simulation history
- Simulation history filtered by patrol
- Default ordering by most recent execution
- DTO-based history responses
- JPA entity graph for patrol history data

### Security

- JWT authentication
- Stateless Spring Security configuration
- Protected application endpoints
- Public authentication endpoint
- Public Swagger and OpenAPI endpoints
- Password encoding
- Persistent application users

### Backend Platform

- REST API
- Request validation
- Global exception handling
- DTO mapping
- Pagination and sorting
- Dynamic filtering through Spring Data Specifications
- Swagger/OpenAPI documentation
- PostgreSQL persistence
- Docker Compose support
- Automated unit, repository, controller and integration tests

## Architecture

The application follows a layered architecture with a clear separation between presentation, business logic, simulation logic and persistence.

```text
REST Controllers
        │
        ▼
Application Services
        │
        ├── Domain Services
        │
        ├── Mission Evaluation
        │
        └── Simulation Services
                │
                ▼
        Tactical Simulation Engine
                │
                ▼
        Tactical Mission Evaluator
                │
                ▼
        Simulation Persistence
                │
                ▼
        Spring Data JPA / PostgreSQL
```

The main architectural principles are:

- separation of concerns;
- dependency injection;
- strategy-based mission evaluation;
- ordered and independently testable simulation phases;
- DTO isolation between REST and persistence layers;
- Open/Closed Principle for tactical mechanics;
- stateless JWT security.

Further documentation:

```text
docs/architecture.md
docs/domain-model.md
docs/simulation-engine.md
docs/development-roadmap.md
```

## Technology Stack

| Technology        | Purpose                          |
| ----------------- | -------------------------------- |
| Java 21           | Programming language             |
| Spring Boot 3.5   | Backend framework                |
| Spring Web        | REST API                         |
| Spring Security   | Authentication and authorization |
| JWT               | Stateless access tokens          |
| Spring Data JPA   | Persistence layer                |
| Hibernate         | ORM                              |
| PostgreSQL 17     | Relational database              |
| Docker Compose    | Local infrastructure             |
| Maven             | Build and dependency management  |
| Lombok            | Boilerplate reduction            |
| Springdoc OpenAPI | Swagger/OpenAPI documentation    |
| JUnit 5           | Automated testing                |
| Mockito           | Unit testing                     |
| MockMvc           | Controller testing               |
| H2                | Repository and integration tests |

## Simulation Engine

The simulation engine executes a patrol through an ordered tactical pipeline.

```text
Transit
  │
  ▼
Patrol Area
  │
  ▼
Detection
  │
  ▼
Classification
  │
  ▼
Contact Behaviour
  │
  ▼
Tracking
  │
  ▼
Contact Loss
  │
  ▼
Intelligence Gathering
  │
  ▼
Return
```

After the simulation phases finish, the result follows this process:

```text
SimulationEngine
        │
        ▼
SimulationResult
        │
        ▼
TacticalMissionEvaluator
        │
        ▼
ResolvedSimulationResult
        │
        ▼
SimulationPersistenceService
        │
        ▼
SimulationRecordRepository
        │
        ▼
PostgreSQL
```

### Core simulation characteristics

- ordered simulation phases;
- shared simulation context;
- structured tactical events;
- probabilistic decision-making;
- mission-dependent behaviour;
- weather and sea-state influence;
- passive and active sonar;
- contact classification confidence;
- threat-based contact behaviour;
- shadowing and tracking decisions;
- contact-loss probabilities;
- intelligence collection;
- mission-specific success strategies;
- tactical scoring and debriefing.

### Simulation history

Completed simulations are persisted automatically and can be queried through the REST API.

```http
GET /api/v1/simulations/history
GET /api/v1/patrols/{patrolId}/simulations
```

Both endpoints support pagination and use `recordedAt DESC` as their default ordering.

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com
│   │       └── jastigi
│   │           └── silentcampaignmanager
│   │               ├── config
│   │               ├── controller
│   │               ├── dto
│   │               ├── entity
│   │               ├── exception
│   │               ├── mapper
│   │               ├── repository
│   │               ├── security
│   │               ├── service
│   │               │   ├── auth
│   │               │   ├── campaign
│   │               │   ├── contact
│   │               │   ├── missions
│   │               │   ├── patrol
│   │               │   ├── simulation
│   │               │   │   ├── calculator
│   │               │   │   ├── context
│   │               │   │   ├── engine
│   │               │   │   ├── evaluation
│   │               │   │   ├── generator
│   │               │   │   ├── history
│   │               │   │   ├── modifier
│   │               │   │   ├── persistence
│   │               │   │   ├── phase
│   │               │   │   ├── resolver
│   │               │   │   └── result
│   │               │   └── submarine
│   │               └── SilentCampaignManagerApplication.java
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com
            └── jastigi
                └── silentcampaignmanager
                    ├── controller
                    ├── mapper
                    ├── repository
                    ├── security
                    └── service

docs
├── architecture.md
├── development-roadmap.md
├── domain-model.md
├── simulation-engine.md
├── decisions
├── images
└── release-notes
```

## Getting Started

### Prerequisites

Install the following tools:

- Java 21
- Docker Desktop
- Git

The project includes Maven Wrapper, so a separate Maven installation is not required.

### Clone the repository

```bash
git clone https://github.com/jastigi/silent-campaign-manager.git
cd silent-campaign-manager
```

### Start PostgreSQL

```bash
docker compose up -d
```

Verify that the PostgreSQL container is running:

```bash
docker compose ps
```

### Run the application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux or macOS:

```bash
./mvnw spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

### Stop the infrastructure

```bash
docker compose down
```

## Authentication

The application uses JWT-based authentication.

Most application endpoints require a valid bearer token.

### Obtain an access token

```http
POST /api/v1/auth/login
Content-Type: application/json
```

Example request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Example response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Authorize Swagger

1. Open Swagger UI.
2. Execute `POST /api/v1/auth/login`.
3. Copy the returned token.
4. Select **Authorize**.
5. Enter the JWT token.
6. Execute the protected endpoints.

The public routes are:

```text
/api/v1/auth/**
/swagger-ui/**
/v3/api-docs/**
```

All remaining endpoints require authentication.

> Development credentials must be replaced with environment-specific credentials before production deployment.

## API Documentation

Swagger UI is available after starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

![Swagger](docs/images/swagger.png)

## REST API Overview

### Authentication

| Method | Endpoint             | Description                          |
| ------ | -------------------- | ------------------------------------ |
| POST   | `/api/v1/auth/login` | Authenticate a user and return a JWT |

### Campaigns

| Method | Endpoint                            | Description                              |
| ------ | ----------------------------------- | ---------------------------------------- |
| POST   | `/api/v1/campaigns`                 | Create a campaign                        |
| GET    | `/api/v1/campaigns`                 | Retrieve campaigns                       |
| GET    | `/api/v1/campaigns/paged`           | Retrieve campaigns with pagination       |
| GET    | `/api/v1/campaigns/search`          | Search campaigns with dynamic filters    |
| GET    | `/api/v1/campaigns/{id}`            | Retrieve a campaign                      |
| GET    | `/api/v1/campaigns/{id}/details`    | Retrieve detailed campaign information   |
| GET    | `/api/v1/campaigns/status/{status}` | Retrieve campaigns by status             |
| PUT    | `/api/v1/campaigns/{id}`            | Update a campaign                        |
| DELETE | `/api/v1/campaigns/{id}`            | Delete a campaign                        |
| GET    | `/api/v1/campaigns/{id}/statistics` | Retrieve campaign operational statistics |

### Patrols

| Method | Endpoint                                                 | Description                                           |
| ------ | -------------------------------------------------------- | ----------------------------------------------------- |
| POST   | `/api/v1/campaigns/{campaignId}/patrols`                 | Create a patrol                                       |
| GET    | `/api/v1/campaigns/{campaignId}/patrols`                 | Retrieve campaign patrols                             |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/paged`           | Retrieve patrols with pagination                      |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/search`          | Search patrols with filtering, pagination and sorting |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/{id}/report`     | Retrieve a patrol report                              |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/{id}/contacts`   | Retrieve contacts assigned to a patrol                |
| PATCH  | `/api/v1/campaigns/{campaignId}/patrols/{id}/close`      | Close and evaluate a patrol                           |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/{id}/evaluation` | Retrieve the mission evaluation                       |
| GET    | `/api/v1/patrols/{id}`                                   | Retrieve a patrol directly                            |
| PUT    | `/api/v1/patrols/{id}`                                   | Update a patrol directly                              |
| DELETE | `/api/v1/patrols/{id}`                                   | Delete a patrol directly                              |

### Contacts

| Method | Endpoint                              | Description                  |
| ------ | ------------------------------------- | ---------------------------- |
| POST   | `/api/v1/patrols/{patrolId}/contacts` | Assign a contact to a patrol |
| GET    | `/api/v1/patrols/{patrolId}/contacts` | Retrieve patrol contacts     |
| GET    | `/api/v1/contacts/{id}`               | Retrieve a contact           |
| PUT    | `/api/v1/contacts/{id}`               | Update a contact             |
| DELETE | `/api/v1/contacts/{id}`               | Delete a contact             |

### Patrol events

| Method | Endpoint                            | Description             |
| ------ | ----------------------------------- | ----------------------- |
| POST   | `/api/v1/patrols/{patrolId}/events` | Create a patrol event   |
| GET    | `/api/v1/patrols/{patrolId}/events` | Retrieve patrol events  |
| GET    | `/api/v1/patrol-events/{id}`        | Retrieve a patrol event |
| PUT    | `/api/v1/patrol-events/{id}`        | Update a patrol event   |
| DELETE | `/api/v1/patrol-events/{id}`        | Delete a patrol event   |

### Submarines

| Method | Endpoint                  | Description          |
| ------ | ------------------------- | -------------------- |
| POST   | `/api/v1/submarines`      | Create a submarine   |
| GET    | `/api/v1/submarines`      | Retrieve submarines  |
| GET    | `/api/v1/submarines/{id}` | Retrieve a submarine |
| PUT    | `/api/v1/submarines/{id}` | Update a submarine   |
| DELETE | `/api/v1/submarines/{id}` | Delete a submarine   |

### Simulations

| Method | Endpoint                                 | Description                                        |
| ------ | ---------------------------------------- | -------------------------------------------------- |
| POST   | `/api/v1/patrols/{id}/simulate`          | Execute and persist a complete patrol simulation   |
| GET    | `/api/v1/simulations/history`            | Retrieve the complete paginated simulation history |
| GET    | `/api/v1/patrols/{patrolId}/simulations` | Retrieve paginated simulation history for a patrol |

### Pagination examples

Global simulation history:

```http
GET /api/v1/simulations/history?page=0&size=10
```

Patrol simulation history:

```http
GET /api/v1/patrols/1/simulations?page=0&size=10
```

The default simulation-history ordering is:

```text
recordedAt,DESC
```

## Testing

The project includes automated tests covering:

- domain services;
- REST controllers;
- DTO mappers;
- JPA repositories;
- JWT security;
- mission evaluation strategies;
- simulation calculators;
- simulation generators;
- environmental modifiers;
- tactical resolvers;
- simulation phases;
- complete simulation execution;
- tactical mission evaluation;
- simulation persistence;
- simulation history.

Run the complete test suite on Windows:

```powershell
.\mvnw.cmd clean test
```

Linux or macOS:

```bash
./mvnw clean test
```

Expected result:

```text
BUILD SUCCESS
Failures: 0
Errors: 0
Skipped: 0
```

![Tests](docs/images/tests.png)

## Roadmap

### Version 0.8 — Simulation Engine Foundation

Completed:

- simulation engine foundation;
- ordered simulation phases;
- shared simulation context;
- structured simulation events;
- mission-specific outcomes;
- mission scoring;
- operational reporting and debriefing.

### Version 0.9 — Tactical Simulation and Persistence

Completed:

- contact detection;
- contact classification;
- environmental weather engine;
- passive and active sonar;
- contact tactical behaviour;
- shadowing;
- tracking;
- contact loss;
- intelligence gathering;
- tactical mission evaluation;
- automatic simulation persistence;
- paginated simulation history;
- simulation history filtered by patrol.

### Version 1.0 — Campaign Simulation

Planned:

- campaign-level simulation;
- strategic campaign progression;
- historical simulation analytics;
- advanced operational statistics;
- NATO and Warsaw Pact doctrine;
- tactical combat and weapon engagement.

The detailed roadmap is available in:

```text
docs/development-roadmap.md
```

## Screenshots

### Swagger UI

![Swagger](docs/images/swagger.png)

### Docker

![Docker](docs/images/docker.png)

### Architecture

![Architecture](docs/images/architecture.png)

## Future Vision

Silent Campaign Manager aims to evolve into a modular campaign platform capable of reproducing Cold War submarine operations through extensible rule-based engines.

Future versions are expected to introduce:

- campaign-level progression;
- weapon engagement;
- strategic consequences;
- NATO and Warsaw Pact doctrine;
- historical operational analytics;
- advanced statistics;
- AI-assisted mission planning.

## About the Author

**Jorge Martínez Juan**

Backend developer interested in Java, software architecture and naval warfare simulations.

GitHub:

```text
https://github.com/jastigi
```

LinkedIn:

```text
https://www.linkedin.com/in/jorgemartinezjuan
```

## Contributing

Contributions, suggestions and constructive feedback are welcome.

Open an issue to report a problem, propose a feature or discuss an architectural improvement.

## License

This project is licensed under the MIT License.

See the `LICENSE` file for more information.
