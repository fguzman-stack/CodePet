# Getting Started

[Back to README](../README.md) · [Docs index](README.md)

## Play the app

1. Download the latest APK from [Releases](https://github.com/fguzman-stack/CodePet/releases/latest).
2. Install it on Android 7.0 or newer.
3. Name Codey, pick a focus language and start studying.

The app is designed to work locally: pet state, progress, preferences and rewards live on the device.

## Build locally

Use the Gradle wrapper from the repository root.

```bash
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet
./gradlew :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

On Windows PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Requirements

| Requirement | Value |
|:--|:--|
| JDK | 17+ |
| Android SDK | API 36 |
| minSdk | 24 / Android 7.0 |
| targetSdk | 36 |
| app version | 3.2.0 |

## Useful commands

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:lintDebug
./gradlew :app:verifyRoborazziDebug
```
