Product Requirements Design (PRD) — Personal Activity Tracker

1. Title
- Personal Activity Tracker — local-first Android app to record phone/app usage, system activity, and battery impact; exportable and syncable; data prepared for LLM-based analysis and journaling.

2. Background & Goals
- Background: Track phone and app usage, store history locally, and analyze battery impact; this app's primary role is reliable data collection. LLM-based analysis is out of scope for the initial app and may run on another device that ingests exported/synced data.
- Primary goals:
  - Capture activity using native Android APIs (app usage, screen on/off, app foreground/background, battery samples, and location/motion context where consented).
  - Persist data locally in a SQLite database (Room is recommended). A local SQLite file is acceptable for the MVP; synchronization to other devices may be implemented later using external or user-chosen sync tools (manual transfer, Nextcloud, rsync, etc.).
  - Provide analysis-ready exports (JSON/CSV/SQLite dump) so other tools or devices can perform heavier analysis.
  - Enable battery consumption correlation and simple on-device summaries where feasible, but prioritize correct and complete data capture.

3. Users & Personas
- Primary user: Single personal user; privacy-first, local-first.

4. Key User Journeys
- J1: Onboard and grant necessary permissions (Usage Access, notifications, optional Accessibility).
- J2: Passive background tracking of app foreground sessions and battery samples.
- J3: Open UI to review timeline, per-app totals, and battery correlation charts.
- J4: Run analysis jobs (weekly/daily) and view insights.
- J5: Export history (JSON/CSV) or run LLM summary job.

5. Functional Requirements
Must:
- FR1: Passive recording of app usage sessions (packageName, startTs, endTs, duration).
- FR2: Record screen and device events (screen on/off, charging start/stop).
- FR3: Periodic battery sampling and event-based battery records.
- FR4: Persist data in Room/SQLite with proper indices and migrations. A local SQLite file is sufficient for MVP; exports/imports should be supported for transfer.
- FR5: Background tracking via a foreground service when necessary; use WorkManager for periodic tasks.
- FR6: Permission flows and graceful degradation when permissions are not granted.
- FR7: UI for timeline and per-app summaries; export to JSON/CSV.
- FR8: Capture location and motion/context data as part of each session when the user explicitly opts in and grants permission (GPS/location, activity recognition). Location capture must be opt-in and documented in onboarding.

Should:
- FR9: Periodic analysis jobs (WorkManager) for weekly summaries.
- FR10: Widget or quick shortcut for stats.
- FR11: Clear onboarding and privacy toggles.

May:
- FR12: Integrations with external services (Nextcloud, Google Fit) — optional (sync implementations should be pluggable).

6. Non-functional Requirements
- NFR1: Min SDK 24, target/compile 36.
- NFR2: Low battery & CPU overhead; use callbacks over polling when possible.
- NFR3: Configurable data retention (default 1 year) and pruning.
- NFR4: Privacy-first: local-only by default; sync opt-in.
- NFR5: Optional data encryption at rest and secure sync.

7. Data Model (suggested)
- AppSession(id, packageName, label, startTs, endTs, durationMs, startBatteryPct, endBatteryPct, metadataJson)
- DeviceEvent(id, type, ts, detailsJson)
- BatterySample(id, ts, levelPct, chargingState)
- AnalysisReport(id, rangeStart, rangeEnd, createdTs, metricsJson)

8. Permissions & Platform APIs
- Use UsageStatsManager for app usage (user must grant Usage Access).
- Provide optional AccessibilityService for finer-grained detection (explicit consent required).
- Use BATTERY_CHANGED broadcast for battery sampling.
- Use Android location APIs (FusedLocationProvider / LocationManager) for location samples when the user opts in; request foreground/background location permissions according to platform rules (explain trade-offs in onboarding).
- Use Activity Recognition APIs (ActivityRecognitionClient) or sensor fusion for motion/context where appropriate and consented.
- Use Foreground Service + WorkManager for background reliability; document Android background limitations.

9. Sync & Export
- MVP: Local SQLite file is acceptable. Implement export/import (JSON/CSV) and a SQLite dump to support manual transfer between devices.
- Later: Optional encrypted sync via user’s server/Nextcloud or other user-selected sync tooling; sync should be an opt-in, pluggable feature.

10. LLM & Journaling
- Do not implement LLM functionality in this app for now. The app's focus is reliable and complete data collection and export. Other devices or services may consume exported data and run LLM-based analysis/journaling.
- Still provide clean, well-documented export formats (JSON/CSV/SQLite) that downstream tools can ingest easily.

11. Success Metrics & Milestones
- KPIs: Data fidelity (coverage), battery overhead (<2% daily), export integrity, successful location capture when opted-in.
- Milestones:
  - M1 (2 weeks): MVP — background capture (app sessions, battery samples, device events), Room DB (local SQLite), timeline UI, JSON/CSV export, and optional SQLite dump.
  - M2 (4 weeks): Add location/motion capture (opt-in), analysis jobs and battery charts.
  - M3 (6 weeks): Manual sync/import/export flows and onboarding improvements.
  - M4: Optional pluggable sync adapters (Nextcloud/user server).

12. Constraints & Risks
- Android background restrictions limit continuous tracking.
- AccessibilityService increases privacy sensitivity and Play policy risk.
- Accurate battery attribution to apps may be approximate.
- Location capture adds privacy and regulatory considerations; ensure clear consent and ability to disable location collection.

13. Acceptance Criteria (MVP)
- AC1: App records foreground sessions and stores them in Room for at least one week.
- AC2: UI shows per-app totals for today and 7 days.
- AC3: Export includes required fields (packageName, startTs, endTs, duration, startBattery, endBattery).
- AC4: WorkManager job stores a weekly AnalysisReport.
- AC5: Onboarding shows how to enable Usage Access.

14. Open Questions
- Do you want automatic sync or manual export/import for initial releases? (recommend manual export/import for MVP)
- Confirm: include location/motion data as required (opt-in) — we've moved this to a required capture point in the PRD.
- LLM: deferred — will be handled by other devices/services; no in-app LLM for now.

15. Next steps
- Break M1 into implementation tasks and create code skeleton (Room entities, background service, simple Compose UI, export).
- Implement MVP and run smoke tests.
