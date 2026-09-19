# AGENTS.md

## Project Shape
- Single-module Android app: root project `Code Tamagotchi` includes only `:app`.
- Kotlin sources live under `app/src/main/java/com/tamagotchi/code` despite the `java` path.
- Main package/application id is `com.tamagotchi.code`; minSdk 24, targetSdk 36, compileSdk 36.1.
- Use the Gradle wrapper only (`.\gradlew.bat` in PowerShell, `./gradlew` elsewhere). Requires JDK 17+ and Android SDK API 36.

## High-Value Commands
- Build debug APK: `.\gradlew.bat :app:assembleDebug`
- Run JVM/Robolectric unit tests: `.\gradlew.bat :app:testDebugUnitTest`
- Run one test class: `.\gradlew.bat :app:testDebugUnitTest --tests "com.tamagotchi.code.StatusCalculatorTest"`
- Run Android lint: `.\gradlew.bat :app:lintDebug`
- Run instrumented tests, requires connected emulator/device: `.\gradlew.bat :app:connectedDebugAndroidTest`
- Roborazzi screenshot tasks exist: `.\gradlew.bat :app:verifyRoborazziDebug` and `.\gradlew.bat :app:recordRoborazziDebug`.

## App Wiring
- Launcher is `SplashActivity`; it waits ~2 seconds then starts `MainActivity`.
- `CodeTamagotchiApp` starts Koin, creates notification channels, and schedules WorkManager jobs for pet checks, widget updates, and daily commits.
- `MainActivity` is Compose-only: it gets `PetViewModel` with `koinViewModel()`, resolves the current theme, then calls `AppNavigation`.
- Dependency graph is in `di/AppModule.kt`: Room database, DAO, repositories, and `PetViewModel` are registered there.
- Most app behavior is centralized in `ui/viewmodel/PetViewModel.kt`; feature screens call into it rather than owning persistent game state.

## Persistence And Data
- Room database is `code_tamagotchi_db` in `data/database/AppDatabase.kt`; current version is 5 and migrations 1->2, 2->3, 3->4, 4->5 are explicit.
- Any Room schema/entity change must update the database version and add a migration; `exportSchema` is currently false, so there is no schema directory to update.
- DataStore-backed preferences and cooldown/progress state live in repository classes under `data/repository`.
- Built-in learning content is static Kotlin data: `data/ChallengesData.kt`, `data/SpecialChallengesData.kt`, `data/CodeReviewData.kt`, and related files.

## UI And Assets
- Navigation routes are `Routes` constants inside `navigation/AppNavigation.kt`; there is no separate `Screen.kt`.
- Themes are split between `ui/theme/ThemeConfig.kt` and animated backgrounds in `ui/components/AnimatedThemeBackground.kt`.
- Codey is drawn procedurally by `ui/components/CodeySprite.kt` (Compose Canvas, no image assets); the launcher widget renders the same logic to a bitmap via `ui/components/CodeyBitmap.kt`. `app/src/main/assets/pet/` is unused (see its README); do not add pet PNGs.
- User-visible strings belong in Android resources under `app/src/main/res/values*`, not hardcoded in new UI.

## Test Notes
- Unit tests use JUnit, Robolectric, Compose UI test APIs, and Roborazzi from the debug unit-test variant.
- Screenshot test `GreetingScreenshotTest` writes `app/src/test/screenshots/greeting.png`; do not update that golden unless the UI change intentionally affects it.
- `TestApplication` initializes WorkManager for Robolectric tests; prefer it for tests that touch app startup or workers.

## Local Files And Signing
- `local.properties`, keystores, and `keystore.properties` are ignored by `.gitignore`; they may exist locally but should not be edited or relied on for normal debug builds.
- Release signing reads `KEYSTORE_PATH`, `STORE_PASSWORD`, and `KEY_PASSWORD`, falling back to local `keystore.properties`/`my-upload-key.jks`; debug builds use `debug.keystore` only if it exists.
