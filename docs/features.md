# Features Documentation

## Overview

Min Activity Tracker provides comprehensive app usage tracking with location context and advanced analytics features.

## Location Tracking

### Purpose
Track your location periodically to understand usage patterns in different contexts (home, work, travel, etc.). This helps correlate app usage with physical location for better insights.

### Battery Efficiency
- **Balanced Power Mode**: Uses `PRIORITY_BALANCED_POWER_ACCURACY` for optimal battery life
- **Smart Intervals**: Samples location every 30 seconds minimum
- **Passive Collection**: No continuous GPS usage, only periodic samples
- **Background Optimization**: Uses WorkManager for efficient scheduling

### Privacy
- All location data stored locally on device
- No cloud transmission
- Optional feature - can be disabled in settings
- Location data only used for personal analytics

### Configuration
Location tracking can be configured in the Settings screen:
- Enable/disable location tracking
- Adjust sampling frequency
- Clear location history
- Export location data

## App Usage Analytics

### Home Screen

The Home screen provides a comprehensive overview of your app usage:

#### Summary Cards
- **Today's Usage**: Total time spent in apps today
- **This Week**: Cumulative usage for the current week

#### Usage by Category
Visual chart showing time distribution across different app categories:
- Productivity
- Social Network
- Entertainment
- Games
- Communication
- Shopping
- Education
- Health & Fitness
- Travel
- News
- Photography
- Music
- Video
- Utilities
- Other

#### Top Apps Today
Quick view of your most-used apps with:
- App name
- Total time spent
- Number of sessions

#### Recent Activity
Chronological list of recent app sessions showing:
- App name
- Start time
- Duration

### Apps Screen

The Apps screen provides detailed categorized view of all your app usage:

#### Time Period Filters
Toggle between different time periods:
- **Today**: Current day usage
- **This Week**: Monday to current day
- **This Month**: Current calendar month
- **All Time**: Complete history

#### Category View
Apps grouped by category with aggregate statistics:
- **Total Time**: Sum of all usage in category
- **App Count**: Number of apps in category
- **Session Count**: Total sessions across all apps

#### Category Charts
Visual representation of usage distribution across categories using column charts for easy comparison.

#### Individual App Details
Each app shows:
- **App Name**: Extracted from package name
- **Category**: Automatically classified
- **Total Time**: Sum of all sessions in selected period
- **Session Count**: Number of times app was used
- **Category Badge**: Visual indicator of app type

### App Category Classification

Apps are automatically classified into categories based on package name analysis:

#### Productivity
Office suites, note-taking, calendars, task managers
- Microsoft Office, Google Docs
- Evernote, Notion
- Slack, Teams, Zoom

#### Social Network
Social media and networking platforms
- Facebook, Instagram, Twitter
- TikTok, Snapchat, LinkedIn
- Reddit, Pinterest

#### Entertainment
General entertainment apps
- Streaming services
- Comedy and entertainment platforms

#### Games
Gaming applications
- Clash of Clans, Candy Crush
- Minecraft, Roblox
- PUBG, Call of Duty Mobile

#### Communication
Messaging and email apps
- WhatsApp, Telegram, Signal
- Gmail, Outlook
- Discord

#### Shopping
E-commerce and shopping apps
- Amazon, eBay
- Walmart, Target
- Shopping apps

#### Education
Learning and educational platforms
- Udemy, Coursera
- Duolingo, Khan Academy
- School and university apps

#### Health & Fitness
Health tracking and fitness apps
- Strava, Fitbit
- MyFitnessPal
- Meditation apps (Headspace, Calm)

#### Travel
Navigation and travel apps
- Google Maps
- Uber, Lyft
- Airbnb, Booking

#### News
News and information apps
- BBC, CNN, NYTimes
- News aggregators

#### Photography
Photo and camera apps
- Instagram (also social)
- Camera apps
- Photo editors

#### Music
Music streaming and audio apps
- Spotify, Pandora
- YouTube Music
- Podcast apps

#### Video
Video streaming platforms
- YouTube, Netflix
- Disney+, Prime Video
- HBO, Hulu

#### Utilities
System and utility apps
- File managers
- Calculators
- Settings and tools

#### Other
Uncategorized or unrecognized apps

## Data Visualization

### Charts
The app uses Vico charting library for data visualization:
- **Column Charts**: Category usage comparison
- **Material 3 Themed**: Matches app theme
- **Interactive**: Touch-responsive charts
- **Accessible**: Screen reader friendly

### Design Principles
- **Clean Layout**: Card-based design for clarity
- **Color Coding**: Consistent color scheme
- **Typography**: Clear hierarchy with Material 3
- **Responsive**: Adapts to different screen sizes

## Background Processing

### WorkManager Integration
All background tasks use WorkManager for reliability:
- **Location Tracking Worker**: Periodic location samples
- **Usage Tracking Worker**: App session monitoring
- **Battery Sampling Worker**: Battery level tracking
- **Auto Export Worker**: Scheduled data exports

### Battery Optimization
- Respects Android battery optimization
- Uses efficient APIs
- Minimal wake locks
- Batch processing where possible

## Future Enhancements

### Planned Features
- [ ] Location-based usage insights ("Most used app at home")
- [ ] Time-of-day usage patterns
- [ ] Weekly/monthly trend analysis
- [ ] Custom categories
- [ ] Usage goals and limits
- [ ] Detailed location maps
- [ ] Export location data with usage
- [ ] Usage predictions based on location
- [ ] Focus mode based on location
- [ ] Smart notifications

## Technical Details

### Database Schema
- `app_sessions`: App usage sessions
- `location_samples`: Location data points
- `battery_samples`: Battery level history
- `device_events`: System events (screen on/off, etc.)

### Performance
- Efficient database queries with indexes
- Flow-based reactive UI updates
- Kotlin coroutines for async operations
- Room database with proper migrations

### Testing
- Unit tests for ViewModels
- UI tests with Compose Testing
- Integration tests for Workers
- Repository layer tests

## Privacy & Security

### Data Storage
- Local SQLite database
- No cloud sync
- Encrypted on device (Android default)

### Permissions
- Location: Optional, for context
- Usage Access: Required for app tracking
- Battery: No special permission needed

### User Control
- Disable any tracking feature
- Clear data anytime
- Export data for personal use
- No analytics or telemetry

## Troubleshooting

### Location Not Working
1. Check location permissions granted
2. Verify GPS is enabled
3. Check battery optimization settings
4. Ensure WorkManager is scheduling correctly

### Charts Not Showing
1. Ensure sufficient data collected
2. Check time period filter selection
3. Verify database not corrupted
4. Try clearing app cache

### High Battery Usage
1. Reduce location sampling frequency
2. Check battery optimization enabled
3. Verify background tasks running efficiently
4. Review app usage patterns

## Support

For issues or questions:
- GitHub Issues: Report bugs or request features
- Documentation: Check other docs in `/docs` folder
- Code: Review source code for implementation details
