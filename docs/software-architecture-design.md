# Software Architecture Design Document
## Min Activity Tracker Android App

### 1. Overview
This document outlines the software architecture for the Min Activity Tracker, a local-first Android application designed to track phone and app usage patterns with background data collection and analysis-ready exports.

### 2. Architecture Principles
- **Clean Architecture**: Separation of concerns with clear layer boundaries
- **Local-First**: Data stored locally by default, optional sync
- **Privacy-First**: Minimal data collection, user consent required
- **Error Transparency**: Direct error reporting in UI with automatic recovery
- **Background Resilience**: Robust background processing with Android limitations
- **Open Source Ready**: MIT license, comprehensive documentation

### 3. System Architecture

#### 3.1 Architecture Pattern: Clean Architecture + MVVM
```
┌─────────────────────────────────────────────────────┐
│                 Presentation Layer                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │   Compose   │  │ ViewModels  │  │   Screens   │ │
│  │     UI      │  │             │  │             │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│                  Domain Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │ Use Cases   │  │ Repositories│  │  Entities   │ │
│  │             │  │ (Interfaces)│  │             │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│                   Data Layer                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │ Room Database│  │ Data Sources│  │   Services  │ │
│  │             │  │             │  │             │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
```

#### 3.2 Component Architecture
```
┌─────────────────────────────────────────────────────┐
│                    UI Components                    │
│  MainActivity → Navigation → Screens → ViewModels   │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│                Background Services                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │ Tracking    │  │ WorkManager │  │ Foreground  │ │
│  │ Service     │  │   Jobs      │  │  Service    │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│                  Data Collection                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │ Usage Stats │  │ Battery     │  │ Location    │ │
│  │ Manager     │  │ Monitor     │  │ Provider    │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
```

### 4. Data Architecture

#### 4.1 Database Schema (Room)
```kotlin
@Entity(tableName = "app_sessions")
data class AppSessionEntity(
    @PrimaryKey val id: String,
    val packageName: String,
    val appLabel: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val durationMs: Long,
    val startBatteryPct: Int?,
    val endBatteryPct: Int?,
    val locationLatitude: Double?,
    val locationLongitude: Double?,
    val metadataJson: String?
)

@Entity(tableName = "device_events")
data class DeviceEventEntity(
    @PrimaryKey val id: String,
    val type: DeviceEventType,
    val timestamp: Long,
    val detailsJson: String?
)

@Entity(tableName = "battery_samples")
data class BatterySampleEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val levelPercent: Int,
    val chargingState: ChargingState,
    val temperature: Float?
)

@Entity(tableName = "analysis_reports")
data class AnalysisReportEntity(
    @PrimaryKey val id: String,
    val rangeStartTs: Long,
    val rangeEndTs: Long,
    val createdTs: Long,
    val reportType: AnalysisType,
    val metricsJson: String
)
```

#### 4.2 Data Flow
```
User Actions → UI Events → ViewModels → Use Cases → Repository → Data Sources → Room DB
                                                                          ↓
Background Services → Data Collection → Repository → Room DB
                                                   ↓
Export/Analysis → Use Cases → Repository → Room DB → JSON/CSV Files
```

### 5. Technology Stack

#### 5.1 Core Technologies
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 34 (Android 14)
- **Language**: Kotlin 100%
- **Build System**: Gradle Kotlin DSL
- **Architecture**: Clean Architecture + MVVM

#### 5.2 UI Framework
- **Jetpack Compose**: Modern declarative UI
- **Material 3**: Design system with dark mode support
- **Compose Navigation**: Type-safe navigation
- **Compose Charts**: Data visualization

#### 5.3 Data & Storage
- **Room**: Type-safe database access
- **SQLite**: Local database storage
- **DataStore**: Preferences and settings
- **Kotlinx Serialization**: JSON serialization

#### 5.4 Background Processing
- **WorkManager**: Periodic background tasks
- **Foreground Service**: Continuous tracking
- **Usage Stats Manager**: App usage tracking
- **Battery Manager**: Battery monitoring
- **Location Services**: Optional location tracking

#### 5.5 Dependency Injection
- **Hilt**: Dependency injection framework
- **Hilt WorkManager**: Background work integration

#### 5.6 Testing Frameworks

##### Unit Tests
- **JUnit 5**: Test framework
- **MockK**: Mocking library
- **Truth**: Assertion library
- **Turbine**: Flow testing
- **Room Testing**: Database testing

##### Integration Tests
- **Hilt Testing**: DI testing
- **WorkManager Testing**: Background work testing
- **Room Integration**: Database integration testing

##### UI Test Framework Comparison

| Framework | Pros | Cons | Recommendation |
|-----------|------|------|----------------|
| **Compose UI Testing** | - Native Compose support<br>- Fast execution<br>- Semantic tree access<br>- Built-in synchronization | - Limited to Compose UI<br>- Newer, fewer resources | **Primary Choice** |
| **Espresso** | - Mature framework<br>- Excellent documentation<br>- View hierarchy access<br>- Stable API | - Verbose syntax<br>- Slower execution<br>- Limited Compose support | **Fallback for complex scenarios** |
| **UI Automator** | - Cross-app testing<br>- System UI interaction<br>- Permission dialog handling | - Slower execution<br>- More brittle<br>- Complex setup | **Integration tests only** |

**Recommended Approach**: 
- Primary: Compose UI Testing for screen-level tests
- Secondary: Espresso for complex navigation flows
- Tertiary: UI Automator for permission flow testing

### 6. Error Handling Strategy

#### 6.1 Error Categories
1. **Permission Errors**: Runtime permission denied/revoked
2. **Database Errors**: Corruption, migration failures
3. **Background Processing Errors**: Service interruption, WorkManager failures
4. **Export Errors**: File system issues, format errors
5. **Network Errors**: Optional sync failures

#### 6.2 Error Handling Principles
- **Transparent Reporting**: Show errors directly in UI
- **Automatic Recovery**: Retry mechanisms where possible
- **Graceful Degradation**: Continue functioning with reduced capabilities
- **User Guidance**: Clear instructions for manual resolution

#### 6.3 Error Recovery Mechanisms
```kotlin
sealed class AppError {
    data class PermissionError(val permission: String) : AppError()
    data class DatabaseError(val cause: Throwable) : AppError()
    data class ExportError(val message: String) : AppError()
    data class BackgroundServiceError(val service: String) : AppError()
}

interface ErrorHandler {
    suspend fun handleError(error: AppError): ErrorResult
}

sealed class ErrorResult {
    object Recovered : ErrorResult()
    data class RequiresUserAction(val action: UserAction) : ErrorResult()
    data class Fatal(val message: String) : ErrorResult()
}
```

### 7. Performance Requirements
- **Battery Usage**: Maximum 5% battery drain per day
- **Memory Usage**: Target <100MB RAM usage
- **Storage**: Efficient data compression and retention policies
- **Background Processing**: Minimal CPU usage with smart scheduling

### 8. Security Considerations
- **Local Storage**: Room database with standard Android security
- **Permissions**: Runtime permission model with clear justifications
- **Data Export**: Optional anonymization features
- **Open Source**: Code transparency for security review

### 9. CI/CD Pipeline (GitHub Actions)
```yaml
# .github/workflows/ci.yml
- Build validation
- Unit tests
- Integration tests
- Lint checks (detekt, ktlint)
- Security scanning
- APK generation
- Release automation
```

### 10. Project Structure
```
app/
├── src/main/java/io/cmwen/min_activity_tracker/
│   ├── data/
│   │   ├── database/
│   │   ├── repository/
│   │   └── sources/
│   ├── domain/
│   │   ├── entities/
│   │   ├── repository/
│   │   └── usecases/
│   ├── presentation/
│   │   ├── screens/
│   │   ├── viewmodels/
│   │   └── navigation/
│   ├── services/
│   └── utils/
├── src/test/ (unit tests)
├── src/androidTest/ (integration tests)
└── src/debug/ (debug utilities)
```

### 11. Open Source Considerations
- **License**: MIT License for maximum compatibility
- **Documentation**: Comprehensive setup and contribution guides
- **LLM Integration**: Structured documentation for AI consumption
- **Community**: Clear issue templates and contribution guidelines

### 12. Future Considerations
- **Sync Framework**: Pluggable sync adapters for various backends
- **Widget Support**: Home screen widgets for quick stats
- **Wear OS**: Extension for wearable devices
- **Export Formats**: Additional formats (Parquet, etc.)
- **Advanced Analytics**: Machine learning insights

---

This architecture provides a solid foundation for a maintainable, testable, and extensible Android application that meets all specified requirements while maintaining clean separation of concerns and robust error handling.
