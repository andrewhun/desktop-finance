# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build (produces JAR with dependencies)
mvn clean package

# Run all tests
mvn clean test

# Run a single test class
mvn test -Dtest=NumberTextFieldUnitTest

# Run a single test method
mvn test -Dtest=NumberTextFieldUnitTest#testMinValueExceedingMax

# Run the application
mvn javafx:run
```

## Architecture

Desktop Finance is a JavaFX desktop application for personal finance management, currently in early development. The project targets Windows and is packaged as an MSI installer via CircleCI.

### Component Model

The app follows a **component-per-feature** pattern where each UI component owns its FXML layout, controller logic, and exception types.

- `Launcher` → delegates to `App` (JavaFX `Application`) → initializes the scene and attaches components
- `NumberTextField` is the only implemented component so far — a `VBox`-based custom control that loads `NumberTextField.fxml`, handles real-time input filtering (regex), and exposes a `getValue()` method that throws `NumberTextFieldException` on invalid state

### NumberTextField Component

Configured entirely via constructor parameters:
- `decimalPlaces` — precision of accepted numeric input
- `minValue` / `maxValue` — range validation (inclusive)
- `allowNegative` — whether negative numbers are valid
- `required` — whether empty field is an error

The component does not expose a value directly; callers must call `getValue()` and handle `NumberTextFieldException`, which carries a human-readable message for display in the UI.

### Testing

Tests use **JUnit 5 + TestFX** with headless rendering via `openjfx-monocle`. Two test categories exist per component:
- `*UnitTest` — validates parsing logic, regex patterns, and constructor behavior without starting JavaFX
- `*IntegrationTest` — exercises the live JavaFX component via TestFX robot interactions (focus changes, keyboard input)

The Surefire plugin is configured with the required `--add-exports` flags for headless JavaFX; do not remove them.

### CI / Packaging Pipeline

CircleCI uses a custom Docker image (`andrewhun/desktop-finance-cci:nsis`) with NSIS installed.

Branch → job mapping:
- `reset` → run tests via `xvfb-run`
- `staging` → build JAR → Launch4j EXE → NSIS MSI → commit packaged artifacts back to `staging`

The `shade/` directory and `installer/` artifacts at the repo root are outputs of the packaging pipeline, not source.

## Code Style

### Brace and Keyword Placement

Keywords that follow a closing brace — `else`, `catch`, `finally` — must always begin on a new line, never on the same line as the `}`. Apply this consistently to all Java code in the project.

**Correct:**
```java
try {
    // ...
}
catch (Exception e) {
    // ...
}
finally {
    // ...
}

if (condition) {
    // ...
}
else {
    // ...
}
```

**Incorrect:**
```java
try {
    // ...
} catch (Exception e) {
    // ...
} finally {
    // ...
}

if (condition) {
    // ...
} else {
    // ...
}
```