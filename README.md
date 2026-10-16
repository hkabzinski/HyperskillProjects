# Hyperskill Projects

Three Hyperskill Java projects. Each folder is a standalone Gradle build with its own
wrapper and its own dependencies, so the projects share no code and can be built, run
and tested independently.

| Project | What it does |
| --- | --- |
| [JSON Database (Java)](<JSON Database (Java)>) | Client/server key-value database over TCP sockets, storing nested JSON documents |
| [Simple Search Engine (Java)](<Simple Search Engine (Java)>) | Console search over a people file, backed by an inverted index with ALL/ANY/NONE matching |
| [Simple Tic-Tac-Toe (Java)](<Simple Tic-Tac-Toe (Java)>) | Console tic-tac-toe with move validation and game-state analysis |

## Requirements

JDK 24. Gradle is supplied by the wrapper (9.6.1), so no local Gradle install is needed.

## Running

Every command runs from **inside** the project folder, using that project's wrapper.
On Windows use `.\gradlew.bat`, on Linux and macOS `./gradlew`.

### JSON Database

Start the server, then send requests from a second terminal:

```
.\gradlew.bat runServer
.\gradlew.bat runClient --args="-t set -k name -v Sorabh"
.\gradlew.bat runClient --args="-t get -k name"
```

A request can also be read from a file in `src/client/data`:

```
.\gradlew.bat runClient --args="-in setFile.json"
```

### Simple Search Engine

```
.\gradlew.bat run -Pdata=people.txt
```

### Simple Tic-Tac-Toe

```
.\gradlew.bat run
```

## Testing

Each project uses JUnit 5 and measures coverage with JaCoCo, which is configured to fail
the build below 80% instruction coverage:

```
.\gradlew.bat test
.\gradlew.bat test jacocoTestCoverageVerification
```

The HTML coverage report is written to `build/reports/jacoco/test/html/index.html`.

## Layout

Each project follows the standard Gradle layout, so the source roots need no manual
configuration:

```
<project>/
├── build.gradle
├── gradlew / gradlew.bat
├── settings.gradle
├── src/main/java/     production code
├── src/test/java/     unit tests
└── stages/            earlier stage solutions, kept for reference only
```

`stages/` is deliberately outside the source sets and excluded from the IDE: each file
there declares another `Main` in the project's own package, so it neither compiles on its
own nor coexists with the finished entry point.
