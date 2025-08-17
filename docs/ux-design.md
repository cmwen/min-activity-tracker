UX Design: Min Activity Tracker

Purpose
- Document permission behavior, onboarding copy, key screens, and developer notes for the personal-usage MVP.

1. Permissions: OS-handled dialogs vs in-app guidance
- Verification:
  - Android system prompts are used for runtime permissions like Location, Activity Recognition, and Fine/Background location. Apps must call the appropriate request APIs; the OS displays the consent UI (system dialog).
  - Usage Access (for UsageStatsManager) and Notification access are special system-level permissions that are toggled in system Settings; the app cannot trigger the same inline dialog as normal runtime permissions. The app can open the corresponding Settings page using an Intent and should present an explanatory screen before that action.
  - AccessibilityService is enabled by the user in system Settings; apps can present an onboarding screen and a shortcut to the Accessibility settings, but the final toggle is done by the user in Settings.
  - Foreground service permission (if targetSDK requires it) and battery optimization whitelisting may require the user to act in Settings; app can surface instructions and deep links.

- UX implication: The app should not try to replicate system dialogs or mimic OS permission flows. Instead:
  - Provide clear, short microcopy explaining why each permission is needed and what the user will get.
  - Provide a one-tap CTA that opens the exact Settings screen when required.
  - Provide a persistent "Permissions" screen summarizing statuses and impacts.

2. README vs in-app guidance
- For a personal-first app, a README in the repo is a good canonical source for setup instructions (Usage Access, Notification access steps, background tracking notes). But keep essential guidance in-app because users (including the owner on their phone) expect immediate help there.
- Recommended approach:
  - Put full, detailed setup steps in `docs/README-setup.md` with screenshots and terminal or adb commands if needed.
  - Keep a concise in-app "Permissions and Setup" screen with CTAs and the single-sentence rationale for each permission.

3. Notifications metadata collection
- Feasibility:
  - Android provides NotificationListenerService which allows apps (with user consent via system Settings) to read notifications' metadata: package name, title, text, timestamp, and possible extras. Play Store policies are strict; use only if necessary and document reasons in onboarding.
  - Implementation note: NotificationListenerService requires the user to enable the listener in system Settings and the app must declare the service in the manifest.
- UX:
  - Treat notification access as a sensitive, optional toggle. Provide explicit examples of what will be recorded (no content sharing unless user opts in) and how it is used (e.g., correlate notification bursts with sessions).

4. Crash reporting
- Recommendation:
  - Use crash reporters for developer diagnostics, but make it opt-in to stay privacy-first. Example SDKs: Firebase Crashlytics, Sentry, Bugsnag.
  - If enabled, include an explicit setting and brief text describing the data sent (stack trace, device model, OS version, app version). Avoid sending any user-generated content or session-level data unless explicitly consented.
  - For personal use, Crashlytics is fine and frictionless; for privacy-focused public builds, prefer Sentry self-hosted or only local log capture until the user opts-in to upload.

5. UX deliverables in `docs/`
- Files to add (starter set):
  - `docs/ux-design.md` (this file)
  - `docs/README-setup.md` (detailed permission & device setup + screenshots)
  - `docs/export-schema.md` (JSON and CSV schemas with sample payloads)
  - `docs/wireframes.md` (screen-by-screen wireframes and component list)

6. Screens & components (compact)
- OnboardingFlow: 3 pages (value, Usage Access, Optional sensors + background consent)
- DashboardScreen: Today summary, top apps, quick export
- TimelineScreen: Day/hour timeline, session detail sheet
- AppDetailScreen: per-app totals, sparkline, session list
- PermissionsScreen: permission cards + CTAs
- ExportScreen: format, range, anonymize

7. Microcopy examples (permission card)
- Title: "Usage access — required"
- Body: "To record app sessions we need permission to see which app is in the foreground; this data stays on your device by default." 
- CTA: "Open settings"

8. Notes for engineers
- Use deep links Intents:
  - Usage Access: ACTION_USAGE_ACCESS_SETTINGS
  - Notification access: ACTION_NOTIFICATION_LISTENER_SETTINGS
  - Accessibility: ACTION_ACCESSIBILITY_SETTINGS
- Add graceful fallbacks for devices that hide or rename Settings pages.
- Record permission states and timestamps in the DB for audit and UI.

9. Next steps
- Commit `docs/README-setup.md` with step-by-step screenshots.
- Draft `docs/export-schema.md` with exact types and a sample JSON export.
- Optionally, add Compose prototypes under `app/src/main/java/.../ui/preview/` for the main screens.

