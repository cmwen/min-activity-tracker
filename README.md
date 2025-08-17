# Min Activity Tracker

A privacy-first, local-first Android application for tracking phone and app usage patterns with comprehensive data collection and analysis-ready exports.

## 🎯 Purpose

Min Activity Tracker helps you understand your digital habits by collecting detailed usage data directly on your device. All data stays local by default, with optional export capabilities for personal analysis or LLM-powered insights.

## ✨ Features

- **App Usage Tracking**: Detailed session recording with foreground/background detection
- **Battery Monitoring**: Correlate app usage with battery consumption
- **Location Context**: Optional location tracking for usage pattern analysis
- **Data Export**: JSON, CSV, and SQLite exports for external analysis
- **Privacy First**: All data stored locally, no cloud dependencies
- **Background Resilience**: Robust tracking that handles Android's background limitations
- **Dark Mode**: Full Material 3 design with dark theme support

## 🏗️ Architecture

Built with modern Android development practices:
- **Clean Architecture** with MVVM pattern
- **Jetpack Compose** for declarative UI
- **Room Database** for local storage
- **Hilt** for dependency injection
- **WorkManager** for background tasks
- **Material 3** design system

## 📱 Requirements

- **Android 10+** (API level 29)
- **Usage Access** permission (required)
- **Location** permission (optional)
- **Battery optimization** whitelist (recommended)

## 🚀 Quick Start

### Installation

1. Download the latest APK from [Releases](https://github.com/cmwen/min-activity-tracker/releases)
2. Install the APK on your Android device
3. Grant necessary permissions during onboarding

### Permissions Setup

1. **Usage Access**: Required for app session tracking
   - Open Settings → Apps → Special access → Usage access
   - Enable for Min Activity Tracker

2. **Location** (Optional): For location-based usage insights
   - Grant when prompted or in app settings

3. **Battery Optimization**: Recommended for consistent tracking
   - Settings → Battery → Battery optimization
   - Set to "Not optimized" for Min Activity Tracker

## 📊 Data Collection

### What We Track
- **App Sessions**: Package name, start/end times, duration
- **Battery Samples**: Battery level changes and charging state
- **Device Events**: Screen on/off, charging events
- **Location** (Optional): GPS coordinates for context

### Data Storage
- All data stored locally in SQLite database
- No cloud sync by default
- Export options available for manual backup

## 📤 Export Formats

### JSON Export
```json
{
  "sessions": [
    {
      "id": "uuid",
      "packageName": "com.example.app",
      "appLabel": "Example App",
      "startTime": "2025-01-15T10:30:00Z",
      "endTime": "2025-01-15T10:45:00Z",
      "duration": 900000,
      "batteryStart": 85,
      "batteryEnd": 83
    }
  ],
  "metadata": {
    "exportTime": "2025-01-15T15:00:00Z",
    "version": "1.0.0"
  }
}
```

### CSV Export
Pre-configured for analysis tools like Excel, R, or Python pandas.

## 🔧 Development

### Setup
```bash
git clone https://github.com/cmwen/min-activity-tracker.git
cd min-activity-tracker
./gradlew assembleDebug
```

### Testing
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Integration tests
./gradlew check                   # All checks
```

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed development guidelines.

## 📖 Documentation

- [Product Requirements](docs/product-requirements-design.md)
- [UX Design Guidelines](docs/ux-design.md)
- [Software Architecture](docs/software-architecture-design.md)
- [Implementation Roadmap](docs/implementation-roadmap.md)
- [Contributing Guide](CONTRIBUTING.md)

## 🤖 LLM Integration

This project includes structured documentation for AI/LLM consumption. See [LLM.txt](LLM.txt) for machine-readable project information.

## 🔒 Privacy

- **Local First**: All data stays on your device by default
- **Minimal Collection**: Only necessary data for functionality
- **User Control**: Granular privacy settings
- **Open Source**: Complete code transparency

## 🏗️ Current Status

**Development Stage**: Planning & Architecture
- [x] Requirements & design documents
- [x] Architecture planning
- [ ] Core infrastructure (In Progress)
- [ ] Data collection implementation
- [ ] UI development
- [ ] Testing & polish

See [Implementation Roadmap](docs/implementation-roadmap.md) for detailed progress.

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### Areas for Contribution
- Core app functionality
- Data analysis features
- Performance optimization
- Documentation improvements
- Testing coverage

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Android team for excellent development tools
- Jetpack Compose team for modern UI framework
- Open source community for inspiration and libraries

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/cmwen/min-activity-tracker/issues)
- **Discussions**: [GitHub Discussions](https://github.com/cmwen/min-activity-tracker/discussions)
- **Documentation**: [Project Wiki](https://github.com/cmwen/min-activity-tracker/wiki)

---

**Note**: This is a personal-first application focused on privacy and local data storage. Use responsibly and in accordance with your local privacy laws.
