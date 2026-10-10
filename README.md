# Classroom Availability System

A Java Application project that is designed to check for room availability, providing alternative room suggestions, search reference for schedules, as well as viewing entire school schedule. This is a project aimed to fulfill the proposed project in Computer Programming by Sir Ace.

## Features

- Check whether a room is available during a requested time range.
- Show conflicting schedules and available alternative rooms.
- View the schedule for a selected room and day.
- Search schedules by subject, section, or professor.
- View all rooms that have schedules on a selected day.
- Load and validate schedule data from a CSV resource.
- Run automated tests for repository, service, CLI, and formatter behavior.

## Architecture

The application uses a lightweight layered architecture:

```text
JavaFX UI / CLI
        |
        v
ScheduleService
        |
        v
ScheduleRepository + RoomRepository
        |
        v
models and schedules.csv
```

### Model

The `model` package contains the application's data objects:

- `Schedule` represents one scheduled class.
- `Room` represents the room number and its building location.
- `AvailabilityResult` contains the availability status, conflicts, and alternatives.

### Repository

The `repository` package handles data access:

- `ScheduleRepository` loads and validates `schedules.csv` records.
- `RoomRepository` provides the known rooms and their locations.

> Note: Repositories does not handle any alteration or modification of the data but rather the access point of the records to be given to the service.

### Service

`ScheduleService` contains the scheduling rules used by both interfaces. It
handles room lookup, conflict detection, availability checks, alternative-room
searches, grouping, and schedule searches.

### User interfaces

- `JavaFxApp` creates the graphical screens and sends user actions to
  `ScheduleService`.
- `ConsoleUI` provides the command-line menu (Mostly used for manual testing & as a fallback option).
- `JavaFxFormatter` and the CLI formatting methods turn results into readable
  output.

The interfaces share the service and repository classes instead of having to rewrite a duplicated logic.

## Program flow

The JavaFX interface is the primary user interface. The CLI is intended mainly
for developer use, manual testing, and as a manually launched fallback when
the JavaFX interface cannot be loaded. The two launchers create their own UI
objects and service instances, but both use the same repository and service
logic:

```mermaid
flowchart TD
    A([Program starts]) --> B{Launcher}

    B -->|Primary| C[JavaFxApp]
    B -->|Developer / manual fallback| D[Main]

    subgraph FX[JavaFX application]
        C --> E[Create JavaFX service and repositories]
        E --> F[start Stage and show dashboard]
        F --> G{Dashboard action}

        G -->|Check availability| H[Show availability form]
        H --> I[Read day, room, start time, and end time]
        I --> J{Valid input and end after start?}
        J -->|No| K[Show validation message on form]
        K --> H
        J -->|Yes| L[ScheduleService.checkAvailability]
        L --> M{Overlapping schedule exists?}
        M -->|No| N[Format AVAILABLE result]
        M -->|Yes| O[Find conflicts and same-area alternatives]
        O --> P[Format OCCUPIED result]
        N --> Q[Display result]
        P --> Q
        Q --> H
        H -->|Back| F

        G -->|View room schedule| R[Show room/day form]
        R --> S[Find schedules for selected room and day]
        S --> T[Format and display room schedule]
        T --> R
        R -->|Back| F

        G -->|Search schedules| U[Show search form]
        U --> V[Choose subject, section, or professor and enter query]
        V --> W[Run matching ScheduleService search]
        W --> X[Format and display results]
        X --> U
        U -->|Back| F

        G -->|View all room schedules| Y[Show day selector]
        Y --> Z[Group schedules by room for selected day]
        Z --> AA[Format and display day schedules]
        AA --> Y
        Y -->|Back| F

        G -->|Close window| AB([JavaFX program ends])
    end

    subgraph CLI[CLI application - developer and fallback only]
        D --> AC[Create ScheduleRepository, RoomRepository, ScheduleService, and ConsoleUI]
        AC --> AD[Start ConsoleUI menu loop]
        AD --> AE{Menu choice}

        AE -->|1. Check availability| AF[Read day, room, start time, and end time]
        AE -->|2. View room schedule| AG[Read day and room]
        AE -->|3. Search schedules| AH[Read search field and query]
        AE -->|4. View all room schedules| AI[Read day]

        AF --> AJ{Valid input and end after start?}
        AJ -->|No| AK[Print validation error]
        AJ -->|Yes| AL[ScheduleService.checkAvailability]
        AL --> AM{Overlapping schedule exists?}
        AM -->|No| AN[Print AVAILABLE result]
        AM -->|Yes| AO[Find conflicts and same-area alternatives]
        AO --> AP[Print OCCUPIED result]
        AN --> AQ[Pause, then return to menu]
        AP --> AQ
        AK --> AQ

        AG --> AR[Find and print room schedule]
        AR --> AS[Pause, then return to menu]
        AH --> AT[Run subject, section, or professor search]
        AT --> AU[Print matching schedules]
        AU --> AS
        AI --> AV[Group and print day schedules]
        AV --> AS

        AQ --> AD
        AS --> AD
        AE -->|Invalid choice| AW[Print error and return to menu]
        AW --> AD
        AE -->|0. Exit or input closes| AX([CLI program ends])
    end

    C -.->|If JavaFX cannot load, developer launches CLI separately| D
```

## Project structure

```text
src/
├── main/
│   ├── java/
│   │   ├── Main.java
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── ui/
│   └── resources/
│       ├── schedules.csv
│       └── ui.css
└── test/
    ├── java/
    └── resources/
```

`Main.java` starts the CLI application. `JavaFxApp` is the JavaFX application
entry point. They are separate launchers, but both use the same service layer.

## Requirements

- Java 17 or newer
- Maven
- Linux or Windows for the configured JavaFX classifiers

## Running the application

### JavaFX interface

```bash
mvn javafx:run
```

### CLI interface

```bash
mvn compile exec:java
```

The CLI uses standard Java input/output and is platform-independent. Maven
selects the JavaFX classifier for the host platform on Linux or Windows.

## Running tests

```bash
mvn clean test
```

The tests cover CSV validation, scheduling behavior, CLI workflows, and
JavaFX output formatting without requiring a display.

## Platform-specific JavaFX builds

The Maven profile selects the JavaFX dependencies for the host platform:

```bash
mvn -DskipTests package
```

To select a platform explicitly, use one of:

```bash
mvn -Plinux -DskipTests package
mvn -Pwindows -DskipTests package
```

## GitHub Releases

Download the archive for the target operating system from the resulting GitHub
Release and extract it. Start the application using:

- Linux: `ClassroomAvailability/bin/ClassroomAvailability`
- Windows: `ClassroomAvailability\ClassroomAvailability.exe`

The Linux and Windows packages must be built separately because JavaFX includes
platform-specific native libraries.
