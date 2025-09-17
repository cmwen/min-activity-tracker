# Agent Instructions for `min-activity-tracker`

This document provides instructions for AI agents working on this Android project.

## Project Overview

- **Frameworks**: Kotlin + Jetpack Compose
- **Module**: Single module project in the `app/` directory.
- **Package**: `io.cmwen.min_activity_tracker`
- **Architecture**:
    - UI: Jetpack Compose (`app/src/main/java/io/cmwen/min_activity_tracker/MainActivity.kt`)
    - Theming: `app/src/main/java/io/cmwen/min_activity_tracker/ui/theme/`
    - Data/Business Logic: Create new packages under `.../data` and `.../domain` as needed.

## Build and Test

- **Build**: `./gradlew assembleDebug`
- **Run on device/emulator**: `./gradlew installDebug`
- **Unit Tests**: `./gradlew test`
- **Instrumented Tests**: `./gradlew connectedAndroidTest` (requires a connected device or emulator)

Use the Gradle Wrapper (`./gradlew`) for all build and test commands.

## Dependencies

- Dependencies are managed in `gradle/libs.versions.toml`.
- Use the `libs.` aliases in `app/build.gradle.kts`. Do not hardcode versions.

## Code Conventions

- New code should be placed under the `io.cmwen.min_activity_tracker` package.
- Follow the existing Compose patterns: create small, previewable Composables.
- Wrap screen UI in `MinactivitytrackerTheme`.
- The project targets JVM 11.

## What Not to Change

- Do not modify `local.properties` or `gradle/wrapper/*` files.
- Do not change `applicationId`, `minSdk`, `targetSdk`, or `compileSdk` without approval.
