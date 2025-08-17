# Contributing to Min Activity Tracker

Thank you for your interest in contributing to Min Activity Tracker! This document provides guidelines and information for contributors.

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [How to Contribute](#how-to-contribute)
- [Code Style](#code-style)
- [Testing](#testing)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)

## Code of Conduct

This project follows a standard code of conduct:
- Be respectful and inclusive
- Focus on constructive feedback
- Help maintain a welcoming environment for all contributors
- Report inappropriate behavior to the maintainers

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK API 29+ (Android 10+)
- Git

### Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/cmwen/min-activity-tracker.git
   cd min-activity-tracker
   ```

2. **Open in Android Studio**
   - Import the project in Android Studio
   - Wait for Gradle sync to complete
   - Ensure all dependencies are downloaded

3. **Run the app**
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug  # with connected device/emulator
   ```

4. **Run tests**
   ```bash
   ./gradlew test                    # Unit tests
   ./gradlew connectedAndroidTest    # Integration tests
   ./gradlew testDebugUnitTest       # Debug unit tests
   ```

## How to Contribute

### Types of Contributions
- **Bug fixes**: Fix issues and improve stability
- **Feature development**: Implement new features from the roadmap
- **Documentation**: Improve docs, add examples, fix typos
- **Testing**: Add tests, improve test coverage
- **Performance**: Optimize battery usage, memory, or speed
- **Code quality**: Refactor, improve architecture

### Development Workflow

1. **Check existing issues** and discussions before starting work
2. **Create an issue** for new features or significant changes
3. **Fork the repository** and create a feature branch
4. **Make your changes** following the coding standards
5. **Write tests** for your changes
6. **Update documentation** if needed
7. **Submit a pull request**

## Code Style

### Kotlin Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use [ktlint](https://github.com/pinterest/ktlint) for formatting
- Run `./gradlew ktlintCheck` before committing

### Architecture Guidelines
- Follow Clean Architecture principles
- Use MVVM pattern for UI layer
- Keep business logic in use cases
- Use dependency injection (Hilt)

### Code Organization
```
app/src/main/java/io/cmwen/min_activity_tracker/
├── data/           # Data layer (repositories, data sources)
├── domain/         # Domain layer (entities, use cases)
├── presentation/   # UI layer (screens, view models)
├── services/       # Background services
└── utils/          # Utility classes
```

### Naming Conventions
- **Classes**: PascalCase (`AppSessionRepository`)
- **Functions**: camelCase (`getAppSessions()`)
- **Variables**: camelCase (`sessionDuration`)
- **Constants**: SCREAMING_SNAKE_CASE (`MAX_SESSIONS`)
- **Resources**: snake_case (`screen_dashboard`, `btn_export`)

## Testing

### Test Structure
- **Unit tests**: `app/src/test/` - Test business logic, view models
- **Integration tests**: `app/src/androidTest/` - Test database, services
- **UI tests**: `app/src/androidTest/` - Test user interfaces

### Testing Guidelines
- **Write tests first** for new features (TDD encouraged)
- **Mock dependencies** using MockK
- **Test error conditions** and edge cases
- **Maintain test coverage** above 80% for critical components

### Running Tests
```bash
# Unit tests
./gradlew test

# Integration tests
./gradlew connectedAndroidTest

# Specific test class
./gradlew test --tests="*AppSessionRepositoryTest"

# Test with coverage
./gradlew testDebugUnitTestCoverage
```

## Pull Request Process

### Before Submitting
1. **Ensure all tests pass**
   ```bash
   ./gradlew check
   ```

2. **Run code quality checks**
   ```bash
   ./gradlew ktlintCheck
   ./gradlew detekt
   ```

3. **Update documentation** if needed
4. **Add tests** for new functionality
5. **Update CHANGELOG.md** if applicable

### PR Requirements
- **Clear description** of changes and motivation
- **Link to related issue** if applicable
- **Screenshots** for UI changes
- **Test coverage** for new code
- **Documentation updates** for new features

### PR Template
```markdown
## Description
Brief description of changes

## Related Issue
Fixes #(issue number)

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Screenshots (if applicable)

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Tests added/updated
- [ ] Documentation updated
```

## Issue Reporting

### Bug Reports
Use the bug report template and include:
- **Android version** and device model
- **App version** or commit hash
- **Steps to reproduce** the issue
- **Expected vs actual behavior**
- **Logs or screenshots** if relevant

### Feature Requests
Use the feature request template and include:
- **Clear use case** and motivation
- **Detailed description** of the feature
- **Alternative solutions** considered
- **Implementation suggestions** if any

### Security Issues
For security vulnerabilities:
- **Do not** create public issues
- **Email maintainers** directly
- **Include details** about the vulnerability
- **Wait for confirmation** before disclosure

## Development Tips

### Debugging
- Use Android Studio debugger for complex issues
- Check Logcat for background service issues
- Use `adb shell dumpsys usagestats` for usage stats debugging

### Performance Testing
- Monitor battery usage with Battery Historian
- Use Android Studio Profiler for memory/CPU analysis
- Test on older devices (Android 10) for compatibility

### Database Changes
- Always create migration scripts
- Test migrations thoroughly
- Backup and restore functionality

## Recognition

Contributors will be recognized in:
- GitHub contributors list
- Release notes for significant contributions
- README.md acknowledgments section

## Questions?

- **General questions**: Use GitHub Discussions
- **Technical help**: Create an issue with "question" label
- **Private matters**: Email the maintainers

Thank you for contributing to Min Activity Tracker! 🎉
