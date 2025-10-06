# CI/CD Setup Documentation

## GitHub Secrets Required for Release

The release workflow supports both **unsigned** and **signed** APK generation.

### Unsigned APK (Default)
No secrets required! The release workflow will automatically generate an unsigned APK for testing.

### Signed APK (Optional)
To generate a signed APK for production, configure these GitHub secrets:

### Android APK Signing Secrets

1. **SIGNING_KEY**: Base64 encoded keystore file
   ```bash
   base64 -i your-keystore.jks | tr -d '\n'
   ```

2. **ALIAS**: Keystore alias name
   - Example: `my-app-key`

3. **KEY_STORE_PASSWORD**: Keystore password
   - The password used to create the keystore

4. **KEY_PASSWORD**: Key password 
   - The password for the specific key alias

### Setting Up Secrets

1. Go to your repository settings
2. Navigate to "Secrets and variables" → "Actions"
3. Click "New repository secret"
4. Add each of the required secrets above

### Creating a Keystore (First Time Setup)

```bash
keytool -genkey -v -keystore my-app-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-app-key
```

## CI Pipeline Status

The CI pipeline is configured to run on:
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

### CI Jobs

1. **Test**: Unit tests, lint checks, code quality (ktlint/detekt)
2. **Build**: Creates debug APK
3. **Instrumented Tests**: Android emulator tests (optional, can fail)
4. **Security**: Vulnerability scanning with Trivy

### Release Pipeline

Releases are triggered by pushing tags with format `v*` (e.g., `v1.0.0`).

#### Pre-release Tags
Tags containing `alpha`, `beta`, or `rc` are marked as pre-releases:
- `v1.0.0-alpha.1` → Pre-release
- `v1.0.0-beta.1` → Pre-release  
- `v1.0.0-rc.1` → Pre-release
- `v1.0.0` → Stable release

#### Release Process

The release pipeline:
1. **Pre-release job** (always runs):
   - Builds unsigned release APK
   - Creates GitHub release with changelog
   - Uploads unsigned APK as release asset
   - Suitable for testing and internal distribution

2. **Release-signed job** (optional, only for stable releases):
   - Only runs if signing secrets are configured
   - Builds and signs the APK
   - Uploads signed APK to the same release
   - Suitable for production distribution

### Creating a Release

```bash
# Create a pre-release (alpha/beta)
git tag v1.0.0-alpha.1
git push origin v1.0.0-alpha.1

# Create a stable release
git tag v1.0.0
git push origin v1.0.0
```

The unsigned APK will always be generated. If you've configured signing secrets,
a signed APK will also be generated for stable releases.

## Permissions

The CI/CD pipeline requires these GitHub Actions permissions:

### CI Workflow
- `contents: read` - Read repository contents
- `security-events: write` - Upload security scan results
- `actions: read` - Read workflow information

### Release Workflow
- `contents: write` - Create releases and upload assets

These are automatically granted to GitHub Actions in most repositories.
If you encounter permission errors, check your repository settings under:
Settings → Actions → General → Workflow permissions