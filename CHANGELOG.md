# Changelog

All notable changes to the Min Activity Tracker project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- WorkManager integration for background data collection
  - DataCollectionWorker for periodic app usage and battery sampling
  - AnalysisWorker for daily/weekly report generation
  - WorkScheduler for managing background jobs
- Data export functionality
  - JSON export with full session data
  - CSV export for spreadsheet analysis
  - Data anonymization option for privacy
- Comprehensive test coverage
  - Unit tests for export and worker functionality
  - Integration tests for database operations
  - UI tests for permission flows
  - Test coverage reporting with Jacoco
- CI/CD enhancements
  - Automated code quality checks (Detekt, ktlint)
  - Test coverage generation
  - Instrumented test execution with Android emulator
  - Security scanning with Trivy
- Documentation
  - Testing guide (TESTING.md)
  - Deployment guide (DEPLOYMENT.md)
  - Enhanced architecture documentation
- Hilt dependency injection
  - WorkManager integration with HiltWorkerFactory
  - Proper DI setup for all layers
- Additional dependencies
  - DataStore for preferences
  - Play Services Location for location tracking
  - Accompanist Permissions for permission handling
  - Truth assertions for better test readability
- User-facing controls
  - DataStore-backed settings for app usage, battery, location, and activity recognition toggles
  - Location privacy tools including anonymization defaults and one-tap history clearing
  - In-app export dialog with format selection and anonymization override
  - Activity Recognition background receiver with device event logging
  - Robolectric for unit testing Android components

### Changed
- Updated build configuration
  - Added BuildConfig feature flag
  - Enhanced test options for Robolectric
  - Added Jacoco for coverage reporting
- Enhanced AndroidManifest
  - Added activity recognition permission
  - Added background location permission
  - Added wake lock and boot completed permissions
  - Configured WorkManager custom initialization
- Updated CI workflow
  - Added code quality checks
  - Added instrumented tests on macOS
  - Added test artifacts upload
- Updated README with current project status

### Fixed
- WorkManager initialization with Hilt
- Test framework configuration issues
- Missing dependencies for comprehensive testing

## [0.1.0] - 2025-01-15 (Initial Foundation)

### Added
- Initial project setup with Jetpack Compose
- Clean architecture foundation
  - Data layer with Room database
  - Domain layer with use cases (planned)
  - Presentation layer with ViewModels
- Database layer
  - AppSessionEntity for tracking app usage
  - BatterySampleEntity for battery monitoring
  - DeviceEventEntity for system events
  - AnalysisReportEntity for periodic reports
  - Comprehensive DAOs with query methods
- Repository pattern implementation
  - SessionRepository and implementation
  - BatterySampleRepository and implementation
  - DeviceEventRepository and implementation
  - AnalysisReportRepository and implementation
- UI framework
  - Material 3 design system
  - Dark mode support
  - Basic navigation structure
  - Permissions screen placeholder
  - Dashboard screen placeholder
- Error handling framework
  - Global error handler
  - Error display UI components
  - Comprehensive error types
- Development tooling
  - Detekt for static analysis
  - ktlint for code formatting
  - GitHub Actions CI/CD pipeline
- Documentation
  - Product requirements document (PRD)
  - Software architecture design
  - UX design guidelines
  - Implementation roadmap
  - Contributing guidelines

### Changed
- Migrated from manual DI to Hilt
- Updated Kotlin to 2.1.20
- Target SDK updated to 36

### Security
- Added manifest permissions for required features
- Configured data extraction rules
- Proper permission handling structure

---

## Version History

- **0.1.0** - Initial foundation with database, architecture, and basic UI
- **Unreleased** - WorkManager, export, comprehensive tests, CI/CD improvements

---

**Note**: Versions prior to 1.0.0 are development releases and may have breaking changes between versions.
