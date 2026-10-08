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
mvn exec:java -Dexec.mainClass=Main
```

## Running tests

```bash
mvn clean test
```

The tests cover CSV validation, scheduling behavior, CLI workflows, and
JavaFX output formatting without requiring a display.

## Windows packaging check

On Windows, the Maven profile selects the Windows JavaFX dependencies:

```bash
mvn -Pwindows -DskipTests package
```

On Linux, the default configuration selects the Linux JavaFX dependencies.

## GitHub Releases

Pushing a version tag beginning with `v` starts the release workflow. The
workflow builds a self-contained application image on Linux and Windows,
including the Java runtime, JavaFX libraries, application code, and resources.
It then attaches one archive for each platform to the GitHub Release.

For example:

```bash
git tag v1.0.0
git push origin v1.0.0
```

Download the archive for the target operating system from the resulting GitHub
Release and extract it. Start the application using:

- Linux: `ClassroomAvailability/bin/ClassroomAvailability`
- Windows: `ClassroomAvailability\ClassroomAvailability.exe`

The Linux and Windows packages must be built separately because JavaFX includes
platform-specific native libraries.
