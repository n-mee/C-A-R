# Classroom Availability System

A Java application for viewing classroom schedules, checking room availability,
and searching schedule records. It provides both a JavaFX graphical interface
and a command-line interface backed by the same scheduling logic.

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
- `Room` represents a classroom and its location.
- `AvailabilityResult` contains the availability status, conflicts, and alternatives.

### Repository

The `repository` package handles data access:

- `ScheduleRepository` loads and validates `schedules.csv`.
- `RoomRepository` provides the known rooms and their locations.

Repositories do not decide how the data is presented to users.

### Service

`ScheduleService` contains the scheduling rules used by both interfaces. It
handles room lookup, conflict detection, availability checks, alternative-room
searches, grouping, and schedule searches.

### User interfaces

- `JavaFxApp` creates the graphical screens and sends user actions to
  `ScheduleService`.
- `ConsoleUI` provides the command-line menu.
- `JavaFxFormatter` and the CLI formatting methods turn results into readable
  output.

The interfaces share the service and repository classes instead of duplicating
the scheduling logic.

## Availability check flow

When a user checks a room:

1. The JavaFX form reads the selected day, room, start time, and end time.
2. The input times are parsed into `LocalTime` values.
3. `JavaFxApp` calls `ScheduleService.checkAvailability(...)`.
4. `ScheduleService` finds schedules for the selected room and day that overlap
   the requested time range.
5. If conflicts exist, the service searches for available rooms in the same area.
6. `AvailabilityResult` carries the result back to the UI.
7. `JavaFxFormatter` formats the result for display.

Two time ranges overlap when:

```text
requestedStart < existingEnd
and
requestedEnd > existingStart
```

This means a request beginning exactly when another schedule ends is not treated
as a conflict.

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
