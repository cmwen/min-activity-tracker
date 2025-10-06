# Testing Guide - Min Activity Tracker

This document describes the testing strategy, test organization, and how to run tests for the Min Activity Tracker project.

## Testing Philosophy

We follow a comprehensive testing strategy that includes:
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions and database operations
- **UI Tests**: Test user interface and interactions
- **End-to-End Tests**: Test complete user workflows

Our goal is to achieve **80%+ code coverage** on critical business logic and data layer components.

## Test Organization

### Unit Tests (`app/src/test/`)

Unit tests run on the JVM and don't require an Android device. They're fast and ideal for testing business logic.

```
app/src/test/java/io/cmwen/min_activity_tracker/
├── data/
│   └── repository/           # Repository implementation tests
├── features/
│   ├── export/              # Export functionality tests
│   └── workers/             # WorkManager worker tests
└── presentation/
    └── viewmodels/          # ViewModel logic tests
```

**Key Technologies:**
- JUnit 4/5
- MockK for mocking
- Truth for assertions
- Robolectric for Android framework dependencies
- Turbine for Flow testing
- Coroutines Test for suspending functions

### Integration Tests (`app/src/androidTest/`)

Integration tests run on an Android device or emulator and test component interactions.

```
app/src/androidTest/java/io/cmwen/min_activity_tracker/
├── data/
│   └── database/           # Database and DAO integration tests
└── features/
    └── permissions/        # UI component tests
```

**Key Technologies:**
- AndroidX Test
- Compose UI Testing
- Room Testing
- Hilt Testing
- UI Automator for system interactions

## Running Tests

### All Tests
```bash
./gradlew test                      # All unit tests
./gradlew connectedAndroidTest      # All instrumentation tests
./gradlew check                     # All checks (tests + lint)
```

### Specific Test Suites
```bash
./gradlew testDebugUnitTest                     # Debug unit tests
./gradlew testReleaseUnitTest                   # Release unit tests
./gradlew connectedDebugAndroidTest             # Debug instrumentation tests
```

### Individual Test Classes
```bash
./gradlew test --tests DataExporterTest
./gradlew test --tests "*.AnalysisWorkerTest"
```

### With Coverage
```bash
./gradlew testDebugUnitTest jacocoTestReport
# Report available at: app/build/reports/jacoco/jacocoTestReport/html/index.html
```

## Test Coverage Goals

| Component | Target Coverage | Current |
|-----------|----------------|---------|
| Data Layer (Repository) | 90% | TBD |
| Domain Layer (Use Cases) | 85% | TBD |
| Workers | 80% | TBD |
| Export Functionality | 90% | TBD |
| ViewModels | 75% | TBD |
| UI Components | 60% | TBD |

## Writing Tests

### Unit Test Example

```kotlin
@Test
fun `exportToJson creates valid JSON file with data`() = runTest {
    // Given
    val startTime = 1000L
    val endTime = 2000L
    val sessions = listOf(createTestSession())
    
    coEvery { sessionRepository.getSessionsInRange(startTime, endTime) } returns sessions
    
    // When
    val result = dataExporter.exportToJson(startTime, endTime)
    
    // Then
    assertThat(result.isSuccess).isTrue()
    coVerify { sessionRepository.getSessionsInRange(startTime, endTime) }
}
```

### Integration Test Example

```kotlin
@Test
fun insertAndRetrieveAppSession() = runTest {
    // Given
    val session = AppSessionEntity(/*...*/)
    
    // When
    sessionDao.insertSession(session)
    val retrieved = sessionDao.getSessionById(session.id)
    
    // Then
    assertThat(retrieved).isNotNull()
    assertThat(retrieved?.id).isEqualTo(session.id)
}
```

### UI Test Example

```kotlin
@Test
fun permissionsScreen_displaysTitle() {
    // Given
    composeTestRule.setContent {
        MinactivitytrackerTheme {
            PermissionsScreen(onNavigateBack = {})
        }
    }
    
    // Then
    composeTestRule
        .onNodeWithText("Permissions")
        .assertExists()
        .assertIsDisplayed()
}
```

## Test Utilities and Helpers

### Test Data Builders
Create reusable test data builders in `test/java/.../testutil/`:

```kotlin
object TestDataFactory {
    fun createTestSession(
        id: String = UUID.randomUUID().toString(),
        packageName: String = "com.test.app",
        duration: Long = 1000L
    ): AppSessionEntity {
        return AppSessionEntity(
            id = id,
            packageName = packageName,
            // ... other fields
        )
    }
}
```

### Mock Repositories
For ViewModel tests, create fake repository implementations:

```kotlin
class FakeSessionRepository : SessionRepository {
    private val sessions = mutableListOf<AppSessionEntity>()
    
    override suspend fun insertSession(session: AppSessionEntity) {
        sessions.add(session)
    }
    
    override suspend fun getAllSessions(): List<AppSessionEntity> = sessions
}
```

## CI/CD Integration

Tests run automatically on every push and pull request via GitHub Actions:

1. **Unit Tests**: Run on Ubuntu (fastest)
2. **Integration Tests**: Run on macOS with Android Emulator
3. **Code Coverage**: Generated and uploaded as artifact
4. **Lint Checks**: Detekt and ktlint

See `.github/workflows/ci.yml` for configuration.

## Troubleshooting

### Common Issues

**Issue**: Tests fail with "No tests found"
**Solution**: Ensure test class ends with `Test` and methods are annotated with `@Test`

**Issue**: MockK "no answer found" error
**Solution**: Use `coEvery` for suspend functions, `every` for regular functions

**Issue**: Compose UI test times out
**Solution**: Ensure `composeTestRule.waitForIdle()` or use semantic properties

**Issue**: Database tests fail with migration errors
**Solution**: Use `Room.inMemoryDatabaseBuilder()` for tests

### Running Tests in Android Studio

1. Right-click test class or package
2. Select "Run 'Tests in...'"
3. View results in Run panel
4. Generate coverage: "Run with Coverage"

## Test Performance

### Speed Optimization
- Use `@SmallTest`, `@MediumTest`, `@LargeTest` annotations
- Mock external dependencies
- Use in-memory database for tests
- Run unit tests first (faster feedback)

### Parallel Execution
```bash
./gradlew test --parallel --max-workers=4
```

## Best Practices

1. **AAA Pattern**: Arrange-Act-Assert structure
2. **Clear Test Names**: Use backticks for descriptive names
3. **One Assert Per Test**: Test one behavior per test method
4. **Mock External Dependencies**: Don't rely on network or real services
5. **Fast Tests**: Keep unit tests under 100ms
6. **Isolated Tests**: Tests should not depend on each other
7. **Meaningful Assertions**: Use Truth for readable assertions
8. **Test Edge Cases**: Null, empty, boundary conditions

## Resources

- [Android Testing Guide](https://developer.android.com/training/testing)
- [Compose Testing](https://developer.android.com/jetpack/compose/testing)
- [MockK Documentation](https://mockk.io/)
- [Truth Assertions](https://truth.dev/)
- [Robolectric](http://robolectric.org/)

---

**Note**: Keep this document updated as testing strategies evolve. Add new patterns and examples as they're discovered.
