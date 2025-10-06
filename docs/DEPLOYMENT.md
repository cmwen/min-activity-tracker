# Deployment & Release Guide

This document describes the release process, versioning strategy, and deployment procedures for Min Activity Tracker.

## Release Process

### 1. Pre-Release Checklist

Before creating a release, ensure:

- [ ] All tests pass (`./gradlew check`)
- [ ] Code coverage meets targets (80%+ on critical components)
- [ ] Detekt and ktlint checks pass
- [ ] Documentation is up to date
- [ ] CHANGELOG.md is updated
- [ ] Version numbers are bumped
- [ ] No hardcoded secrets or API keys
- [ ] ProGuard/R8 rules tested (release builds)

### 2. Versioning Strategy

We follow [Semantic Versioning](https://semver.org/):

**Format**: `MAJOR.MINOR.PATCH`

- **MAJOR**: Incompatible API changes or major features
- **MINOR**: New functionality, backwards compatible
- **PATCH**: Bug fixes and minor improvements

**Examples**:
- `1.0.0` - Initial stable release
- `1.1.0` - New export format added
- `1.1.1` - Bug fix for export crash
- `2.0.0` - Breaking change in data model

### 3. Version Code and Version Name

Update in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2      // Increment by 1 for each release
    versionName = "1.1.0" // Semantic version
}
```

**Version Code Rules**:
- Always increment by 1
- Never reuse a version code
- Used by Android for app updates

### 4. Release Types

#### Alpha Release
- Internal testing only
- Frequent releases (daily/weekly)
- Version: `1.0.0-alpha.1`, `1.0.0-alpha.2`
- Not published to production

#### Beta Release
- External testing with selected users
- Bi-weekly releases
- Version: `1.0.0-beta.1`, `1.0.0-beta.2`
- Announced to testers

#### Production Release
- Public stable release
- Monthly or as needed
- Version: `1.0.0`, `1.1.0`, `1.1.1`
- Full announcement and changelog

## Release Workflow

### Step 1: Prepare Release Branch

```bash
# Create release branch from develop
git checkout develop
git pull origin develop
git checkout -b release/v1.1.0
```

### Step 2: Update Version Numbers

Edit `app/build.gradle.kts`:
```kotlin
versionCode = 2
versionName = "1.1.0"
```

### Step 3: Update CHANGELOG.md

```markdown
## [1.1.0] - 2025-01-15

### Added
- JSON and CSV export functionality
- WorkManager background jobs for data collection
- Comprehensive test suite

### Fixed
- Battery sample collection timing issues
- Permission flow crashes on Android 13+

### Changed
- Improved database query performance
```

### Step 4: Run Full Test Suite

```bash
./gradlew clean
./gradlew check
./gradlew connectedAndroidTest  # If device available
```

### Step 5: Build Release APK

```bash
# Build unsigned release APK
./gradlew assembleRelease

# Or build signed APK (requires keystore)
./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=/path/to/keystore.jks \
  -Pandroid.injected.signing.store.password=$KEYSTORE_PASSWORD \
  -Pandroid.injected.signing.key.alias=$KEY_ALIAS \
  -Pandroid.injected.signing.key.password=$KEY_PASSWORD
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Step 6: Tag Release

```bash
git add .
git commit -m "Release v1.1.0"
git tag -a v1.1.0 -m "Release version 1.1.0"
```

### Step 7: Merge to Main

```bash
git checkout main
git merge --no-ff release/v1.1.0
git push origin main
git push origin v1.1.0
```

### Step 8: Create GitHub Release

Push the tag to trigger GitHub Actions release workflow:

```bash
git push origin v1.1.0
```

The automated workflow will:
1. Build signed release APK
2. Create GitHub Release
3. Upload APK as release asset
4. Generate changelog from git commits

Alternatively, create manually:
1. Go to GitHub Releases
2. Click "Draft a new release"
3. Choose tag `v1.1.0`
4. Set release title: `Release v1.1.0`
5. Add changelog from CHANGELOG.md
6. Upload `app-release.apk`
7. Publish release

### Step 9: Announce Release

- Update README.md status section
- Post announcement in Discussions
- Update project documentation
- Notify beta testers (if applicable)

## Signing Configuration

### Generate Keystore (First Time)

```bash
keytool -genkey -v \
  -keystore min-activity-tracker.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias min-activity-key
```

**Save keystore details securely**:
- Keystore path
- Keystore password
- Key alias
- Key password

### Configure GitHub Secrets

For automated releases, add these secrets to GitHub:

1. Go to Settings → Secrets and variables → Actions
2. Add secrets:
   - `SIGNING_KEY`: Base64 encoded keystore file
     ```bash
     base64 -i min-activity-tracker.jks | tr -d '\n'
     ```
   - `ALIAS`: Key alias
   - `KEY_STORE_PASSWORD`: Keystore password
   - `KEY_PASSWORD`: Key password

### Local Signing (gradle.properties)

For local development, add to `~/.gradle/gradle.properties`:

```properties
MIN_ACTIVITY_TRACKER_KEYSTORE_FILE=/path/to/min-activity-tracker.jks
MIN_ACTIVITY_TRACKER_KEYSTORE_PASSWORD=your_keystore_password
MIN_ACTIVITY_TRACKER_KEY_ALIAS=min-activity-key
MIN_ACTIVITY_TRACKER_KEY_PASSWORD=your_key_password
```

**Never commit keystore or passwords to git!**

## Release APK Verification

After building release APK, verify:

### 1. APK Size
```bash
ls -lh app/build/outputs/apk/release/app-release.apk
# Should be reasonable (< 10MB for this app)
```

### 2. Check Signing
```bash
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

### 3. Inspect APK Contents
```bash
# Using bundletool
bundletool dump manifest --bundle=app-release.aab

# Or using Android Studio
# Build → Analyze APK → Select app-release.apk
```

### 4. Test Installation
```bash
adb install app/build/outputs/apk/release/app-release.apk
# Test critical flows manually
```

## Rollback Procedure

If a release has critical issues:

### 1. Remove GitHub Release
- Go to Releases
- Delete problematic release
- Remove tag

### 2. Revert Git Tag
```bash
git tag -d v1.1.0
git push origin :refs/tags/v1.1.0
```

### 3. Create Hotfix

```bash
git checkout main
git checkout -b hotfix/v1.1.1
# Fix critical issue
git commit -m "Hotfix: Fix critical crash"
git tag v1.1.1
git push origin hotfix/v1.1.1
git push origin v1.1.1
```

## Distribution Channels

### GitHub Releases (Primary)
- Direct APK downloads
- Changelogs and release notes
- Open source distribution

### F-Droid (Future)
- Open source app store
- Reproducible builds
- Automatic updates

### Google Play Store (Optional)
- Wider audience reach
- Automatic updates
- Requires Google Play Console account

## Monitoring

After release, monitor:
- GitHub Issues for bug reports
- Download statistics
- User feedback in Discussions
- Crash reports (if crash reporting enabled)

## Changelog Template

```markdown
## [X.Y.Z] - YYYY-MM-DD

### Added
- New features and capabilities

### Changed
- Changes to existing functionality

### Deprecated
- Features marked for removal

### Removed
- Features removed in this version

### Fixed
- Bug fixes

### Security
- Security-related changes
```

## Resources

- [Semantic Versioning](https://semver.org/)
- [Keep a Changelog](https://keepachangelog.com/)
- [Android App Signing](https://developer.android.com/studio/publish/app-signing)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github)

---

**Last Updated**: 2025-01-15
