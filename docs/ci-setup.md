# CI/CD Setup Documentation

## GitHub Secrets Required for Release

The release workflow requires the following GitHub secrets to be configured:

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
3. **Security**: Vulnerability scanning with Trivy

### Release Pipeline

Releases are triggered by pushing tags with format `v*` (e.g., `v1.0.0`).

The release pipeline:
1. Builds release APK
2. Signs the APK with provided secrets
3. Creates GitHub release with changelog
4. Uploads signed APK as release asset