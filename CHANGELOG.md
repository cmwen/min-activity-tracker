# Changelog

All notable changes to Min Activity Tracker will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Location Tracking**: Battery-efficient location tracking using Fused Location Provider API
  - Periodic location sampling with balanced power accuracy
  - LocationSample entity and DAO for Room database
  - LocationRepository for data access
  - LocationTrackingWorker for background collection
  - Configurable sampling intervals for battery optimization

- **App Category Classification**: Automatic categorization of apps into 14+ categories
  - Productivity, Social Network, Entertainment, Games
  - Communication, Shopping, Education, Health & Fitness
  - Travel, News, Photography, Music, Video
  - Utilities and Other
  - Keyword-based classification system
  - Extensible category definitions

- **Enhanced Home Screen**:
  - Summary cards showing today's and week's total usage time
  - Top Apps Today section with usage statistics
  - Usage by Category visualization with charts
  - Recent Activity list with session details
  - Material 3 design with card-based layout

- **Enhanced Apps Screen**:
  - Time period filters: Today, This Week, This Month, All Time
  - Category-grouped usage statistics with aggregate metrics
  - Visual charts using Vico library for category comparison
  - Per-app usage details with total time and session counts
  - Category badges for each app

- **Data Visualization**:
  - Vico charting library integration
  - Column charts for category usage comparison
  - Material 3 themed charts matching app design
  - Responsive chart layouts

- **Documentation**:
  - Comprehensive features.md guide
  - Updated README with new feature highlights
  - Category classification documentation
  - Privacy and troubleshooting sections
  - Technical implementation details

### Changed
- Database schema version updated to v2 for LocationSample entity
- DatabaseModule configured with fallbackToDestructiveMigration for development
- HomeViewModel refactored to provide rich summary state
- AppListViewModel enhanced with category grouping and filtering
- Material 3 UI components for consistent design

### Technical Details
- Added play-services-location dependency for location tracking
- Integrated Vico chart library (v2.0.0-alpha.28)
- Room database migration to version 2
- Hilt dependency injection for location components
- WorkManager for efficient background location tracking
- Kotlin Flow for reactive UI updates
- Compose UI with lazy lists for performance

### Performance
- Efficient location sampling with 30-second minimum intervals
- Battery-optimized with balanced power accuracy mode
- Database queries optimized with proper indexing
- Flow-based reactive updates minimize recomposition
- Lazy loading for large lists

### Privacy & Security
- All location data stored locally
- No cloud transmission of location or usage data
- Location tracking is optional
- User control over all tracking features
- Transparent data collection

## [2.0.2] - 2025-01-15

### Changed
- Automated app versioning via GitHub Actions

## [2.0.1] - Previous Release

Initial release with core tracking features.
