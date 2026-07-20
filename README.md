<p align="center">
  <img src="docs/images/logo.png" alt="Silent Campaign Manager Logo" width="160">
</p>

![Silent Campaign Manager Banner](docs/images/banner.png)

# Silent Campaign Manager

> **Cold War Submarine Operations Simulator**

A backend application built with **Java 21** and **Spring Boot** for managing and simulating Cold War submarine operations.

The project combines a modern REST API with extensible simulation engines capable of evaluating tactical risk, mission success and submarine patrols using clean architecture and design patterns.

<p align="center">

<img src="https://img.shields.io/badge/Java-21-blue">

<img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F">

<img src="https://img.shields.io/badge/PostgreSQL-17-336791">

<img src="https://img.shields.io/badge/Docker-2496ED">

<img src="https://img.shields.io/badge/JUnit-5-success">

<img src="https://img.shields.io/badge/License-MIT-green">

</p>

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Simulation Engines](#simulation-engines)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [REST API Overview](#rest-api-overview)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Screenshots](#screenshots)
- [License](#license)

## Overview

Silent Campaign Manager is inspired by Cold War submarine operations and naval warfare simulations.

Unlike a traditional CRUD application, the project is evolving into a modular simulation platform capable of evaluating patrol missions, tactical situations and contact risk through extensible rule engines.

The architecture has been designed to be scalable, testable and easy to extend with future simulation mechanics.

---

## Features

### Domain Management

- Campaign management
- Patrol management
- Submarine management
- Contact tracking
- Patrol event management
- Patrol contact assignment
- Patrol contact retrieval endpoint
- Modular mission evaluation architecture prepared for automatic patrol assessment
- Automatic patrol closing with mission evaluation
- Mission evaluation report endpoint available through REST API
- Campaign statistics endpoint with patrol and contact operational metric

### Simulation

- Contact Risk Evaluation Engine
- Mission Evaluation Engine
- Mission Scoring Engine
- Strategy-based business rules
- Mission Doctrine Engine (Strategy Pattern)
- Strategy-based business rules

### Backend

- REST API
- Validation
- Exception handling
- DTO mapping
- Swagger documentation
- Dynamic filtering, pagination and sorting support for REST endpoints using Spring Data Specifications

---

## Technology Stack

| Technology        | Purpose               |
| ----------------- | --------------------- |
| Java 21           | Programming Language  |
| Spring Boot       | Backend Framework     |
| Spring Data JPA   | Persistence           |
| Hibernate         | ORM                   |
| PostgreSQL        | Database              |
| Docker Compose    | Local Infrastructure  |
| Maven             | Build Tool            |
| Lombok            | Boilerplate Reduction |
| Swagger / OpenAPI | API Documentation     |
| JUnit 5           | Testing               |
| Mockito           | Unit Testing          |

---

## Architecture

The project follows a layered architecture with a strong separation between business logic and infrastructure.

For more details see:

📄 **docs/architecture.md**

## Domain Model

The simulation is centered around patrols.

Each patrol belongs to a campaign and generates contacts and events that are later evaluated by specialized simulation engines.

See:

📄 **docs/domain-model.md**

---

## Simulation Engines

The project now includes the initial architecture of the simulation engine.

The engine will execute patrols through multiple simulation phases:

- Transit
- Patrol Area
- Detection
- Contact
- Mission Evaluation
- Return

The architecture follows the Open/Closed Principle, allowing new simulation phases to be added without modifying the engine itself.
Current implementation executes an ordered simulation pipeline composed of independent phases managed by Spring.
Simulation phases now generate a chronological event log stored in the simulation context.
The Simulation engine now tracks the operational state of a patrol throughout its lifecycle.
The simulation engine now performs its first probabilistic decision by determining whether a patrol detects an enemy contact.
The simulation engine now creates transient DetectedContact objects representing contacts generated during the simulation. These objects are independent from the persistent Contact entity.
The simulation engine now tracks an internal simulation date that advances as patrol phases are executed.
Simulation events are now represented as structured objects instead of plain strings, enabling richer reporting and future timeline visualization.
Simulation events are now categorized using SimulationEventType, enabling structured reporting and future timeline filtering.

### Contact Risk Evaluation Engine

Calculates the tactical risk associated with detected contacts using configurable Strategy Pattern implementations.

### Mission Evaluation Engine

Evaluates patrol success according to the assigned mission and the contacts generated during the patrol.

Both engines have been designed to be easily extensible without modifying existing business logic.

## Project Structure

````text
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
│   │               │   │   └── statistics
│   │               │   │       └── impl
│   │               │   ├── contact
│   │               │   ├── missions
│   │               │   │   ├── constants
│   │               │   │   ├── impl
│   │               │   │   ├── model
│   │               │   │   └── strategy
│   │               │   ├── patrol
│   │               │   ├── submarine
│   │               │   └── simulation
│   │               └── SilentCampaignManagerApplication.java
│   └── resources
│       ├── application.properties
│       └── ...
│
└── test
    └── java
        └── com
            └── jastigi
                └── silentcampaignmanager
                    ├── controller
                    ├── repository
                    ├── service
                    └── ...
│
docs
├── architecture.md
├── domain-model.md
├── decisions
├── images
└── LICENSE
````

The project is organized following a layered architecture that clearly separates presentation, business logic, persistence and simulation engines.

## Getting Started

### Prerequisites

Before running the project, make sure you have installed:

- Java 21
- Docker Desktop
- Git

The project includes the Maven Wrapper, so no Maven installation is required.

---

### Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/silent-campaign-manager.git

cd silent-campaign-manager
```

---

### Start PostgreSQL

```bash
docker compose up -d
```

Verify that the PostgreSQL container is running.

---

### Run the application

Windows

```bash
mvnw.cmd spring-boot:run
```

Linux / macOS

```bash
./mvnw spring-boot:run
```

The application will be available at:

```
http://localhost:8080
```

## API Documentation

Once the application is running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI documentation is generated automatically.

![Swagger](docs/images/swagger.png)

---

# REST API Overview

The following endpoints are currently available.

## Campaigns

| Method | Endpoint                            | Description                                    |
| ------ | ----------------------------------- | ---------------------------------------------- |
| GET    | `/api/v1/campaigns`                 | Retrieve all campaigns                         |
| GET    | `/api/v1/campaigns/paged`           | Retrieve campaigns with pagination             |
| GET    | `/api/v1/campaigns/search`          | Search campaigns using dynamic filters         |
| GET    | `/api/v1/campaigns/{id}`            | Retrieve a campaign by ID                      |
| POST   | `/api/v1/campaigns`                 | Create a new campaign                          |
| PUT    | `/api/v1/campaigns/{id}`            | Update an existing campaign                    |
| DELETE | `/api/v1/campaigns/{id}`            | Delete a campaign                              |
| GET    | `/api/v1/campaigns/{id}/statistics` | Retrieve operational statistics for a campaign |

---

## Patrols

| Method | Endpoint                                                 | Description                                   |
| ------ | -------------------------------------------------------- | --------------------------------------------- |
| GET    | `/api/v1/campaigns/{campaignId}/patrols`                 | Retrieve campaign patrols                     |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/paged`           | Retrieve campaign patrols with pagination     |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/search`          | Search patrols by mission result              |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/{id}`            | Retrieve patrol details                       |
| POST   | `/api/v1/campaigns/{campaignId}/patrols`                 | Create a patrol                               |
| PUT    | `/api/v1/campaigns/{campaignId}/patrols/{id}`            | Update a patrol                               |
| DELETE | `/api/v1/campaigns/{campaignId}/patrols/{id}`            | Delete a patrol                               |
| PATCH  | `/api/v1/campaigns/{campaignId}/patrols/{id}/close`      | Close a patrol and execute mission evaluation |
| GET    | `/api/v1/campaigns/{campaignId}/patrols/{id}/evaluation` | Retrieve mission evaluation report            |

---

## Contacts

| Method | Endpoint                              | Description                            |
| ------ | ------------------------------------- | -------------------------------------- |
| GET    | `/api/v1/patrols/{patrolId}/contacts` | Retrieve contacts assigned to a patrol |
| POST   | `/api/v1/patrols/{patrolId}/contacts` | Assign a contact to a patrol           |

---

The API is fully documented using OpenAPI and can be explored interactively through Swagger UI.

## Testing

The project includes automated tests covering the most important business components.

Current test coverage includes:

- Service layer
- REST controllers
- Simulation engines
- Risk calculation
- Mission evaluation

Run all tests:

```bash
mvnw test
```

Expected output:

```text
BUILD SUCCESS

Tests run: XXX

Failures: 0

Skipped: 0
```

![Tests](docs/images/tests.png)

## Current Status

### Implemented

- Campaign Management
- Patrol Management
- Contact Management
- Patrol Events
- Submarine Management
- Contact Risk Evaluation Engine
- Mission Evaluation Engine
- Mission Scoring Engine
- PostgreSQL Persistence
- Docker Support
- REST API
- Swagger Documentation
- Patrol Contact Assignment
- Patrol Contact REST endpoint
- Strategy Pattern for mission evaluation
- Mission evaluation is executed through a dedicated business service when a patrol is closed
- Campaign pagination endpoint
- Patrol pagination endpoint
- Patrol filtering endpoint with pagination and sorting

### Under Development

- Campaign Simulation
- Dynamic Mission Scoring
- Authentication & Authorization
- Tactical Combat

## Roadmap

### Version 0.8

- Mission Evaluation Engine
- Strategy Pattern refactoring
- Patrol contacts

### Version 0.9

- Campaign Simulation Engine
- Dynamic Patrol Resolution
- Tactical Events

### Version 1.0

- Cold War Campaign Simulator
- NATO vs Warsaw Pact
- Advanced Statistics
- AI-assisted Mission Evaluation

## Screenshots

### Swagger UI

![Swagger](docs/images/swagger.png)

---

### Docker

![Docker](docs/images/docker.png)

---

### Architecture

![Architecture](docs/images/architecture.png)

## Future Vision

Silent Campaign Manager aims to evolve into a modular simulation platform capable of reproducing Cold War submarine operations through extensible rule-based engines.

Future versions will introduce:

- Dynamic sonar detection
- Weapon engagement
- Strategic campaign progression
- NATO and Warsaw Pact doctrine
- AI-assisted mission planning
- Advanced operational statistics

## About the Author

**Jorge Martínez Juan**

Backend Developer passionate about Java, software architecture and naval warfare simulations.

GitHub

https://github.com/jastigi

LinkedIn

https://www.linkedin.com/in/jorgemartinezjuan

## Contributing

Contributions, suggestions and constructive feedback are always welcome.

If you would like to discuss new features or improvements, feel free to open an issue.

## License

This project is licensed under the MIT License.

See the LICENSE file for more information.
