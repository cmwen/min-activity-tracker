<!--
Guidance for automated coding agents working on the `min-activity-tracker` Android app.
Keep this file short and actionable. Update when project structure or build changes.
-->

# Overview

This is a small Android app (single-module Gradle project) built with Kotlin + Jetpack Compose.
Key module: `app/` (package `io.cmwen.min_activity_tracker`). The app currently renders a single Compose `MainActivity` and a theming package under `ui/theme`.

Why this layout: the project follows a minimal single-activity, Compose-first structure. New features should favor Compose UI and keep code organized under `app/src/main/java/io/cmwen/min_activity_tracker/...`.

## High-level architecture
- Single Gradle module `:app` (see `settings.gradle.kts`).
- UI implemented with Jetpack Compose (see `app/src/main/java/io/cmwen/min_activity_tracker/MainActivity.kt`).
- Theme utilities under `app/src/main/java/io/cmwen/min_activity_tracker/ui/theme/` (e.g., `Theme.kt`, `Color.kt`, `Type.kt`).

Data / business logic: currently absent; introduce packages under `.../data` and `.../ui` when adding features. Keep separation: UI in `ui.*`, logic in `domain.*` or `data.*`.

## Build & run
- Build system: Gradle Kotlin DSL. Top-level file: `build.gradle.kts`; module file: `app/build.gradle.kts`.
- Recommended (developer machine with Android SDK + Java 11 installed):

  - Build (from repo root): `./gradlew assembleDebug`
  - Run connected device / emulator: `./gradlew installDebug` or use Android Studio run targets.
  - Unit tests: `./gradlew test` (no unit tests exist yet but task is wired).
  - Instrumented tests: `./gradlew connectedAndroidTest` (requires device/emulator).

Notes: The project uses the Version Catalog (`gradle/libs.versions.toml`) via `libs` aliases; prefer existing aliases in `app/build.gradle.kts` when adding dependencies.

## Project conventions and patterns
- Package root: `io.cmwen.min_activity_tracker`. Place new code under this namespace.
- Compose-first UI: prefer Composables and keep small previewable functions (see `Greeting` composable in `MainActivity.kt`).
- Theming: use color and typography tokens in `ui/theme/`. Use `MinactivitytrackerTheme` wrapper for screens.
- Java/Kotlin target: JVM 11 (see `app/build.gradle.kts` options).
- Min SDK: 24, target/compile SDK: 36.

## Files to inspect when changing behavior
- `app/build.gradle.kts` — dependency aliases and Compose setup.
- `app/src/main/java/io/cmwen/min_activity_tracker/MainActivity.kt` — entry point and example Compose usage.
- `app/src/main/java/io/cmwen/min_activity_tracker/ui/theme/Theme.kt` — theming decisions and dynamic color usage on Android 12+.
- `app/src/main/res/values/` — colors, strings, and themes XML for resources that interoperate with Compose.

## Examples & idioms to follow
- Small, previewable composables: follow `Greeting(name: String)` pattern with a `@Preview` function.
- Use `MinactivitytrackerTheme { ... }` to wrap screen UI so system dynamic colors and typography are applied consistently.
- Prefer `enableEdgeToEdge()` in activities that want fullscreen edge-to-edge content (used in `MainActivity`).

## Integration & external dependencies
- Dependencies are declared via the Version Catalog (`gradle/libs.versions.toml`) and referenced with `libs.` aliases. Do not hardcode versions in `app/build.gradle.kts`.
- No remote services or native libraries are present currently.

## Tests, linting, and CI
- There is no CI config in the repo. If adding GitHub Actions, follow the Gradle wrapper usage (`./gradlew`) and run `./gradlew check` or `./gradlew assembleDebug` in the workflow.
- Unit and instrumented test tasks are available but minimal test code exists (see `app/src/test` and `app/src/androidTest`).

## What agents must not change
- Do not alter `local.properties` (SDK path) or `gradle/wrapper/*` files unless explicitly requested.
- Avoid changing applicationId, minSdk, targetSdk, or compileSdk without author approval.

## Quick checklist for code changes
1. Add new Kotlin classes under `app/src/main/java/io/cmwen/min_activity_tracker/...` following package naming.
2. Wrap UI with `MinactivitytrackerTheme` and add `@Preview` functions where applicable.
3. Use `libs.` aliases for dependencies; update `gradle/libs.versions.toml` only if adding new libraries.
4. Run `./gradlew build` and any relevant tests before committing.

## Questions for maintainers
- Should new features live in subpackages `ui`, `data`, `domain`, or in top-level packages? (I guessed `ui`, `data`, `domain`.)
- Are there style or lint rules we should follow (ktlint, detekt)? None are present — please provide if required.

---
If you want changes or extra sections (CI config, sample feature patch, or tests), tell me which area to expand.
