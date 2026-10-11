# Testing

[Back to README](../README.md) · [Docs index](README.md)

## Commands

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:testDebugUnitTest --tests "com.tamagotchi.code.StatusCalculatorTest"
./gradlew :app:verifyRoborazziDebug
./gradlew :app:lintDebug
```

On Windows PowerShell, use `./gradlew.bat` instead of `./gradlew`.

## Coverage areas

| Area | Tests |
|:--|:--|
| Status and economy | `StatusCalculatorTest`, `GameEconomyTest` |
| Localization parity | `LocalizedContentTest` |
| Procedural pet rendering | `CodeyRendererTest`, `CodeySpriteTest` |
| Animation config | `PetAnimationConfigTest` |
| Theme contrast | `ThemeContrastTest` |
| Launcher widget | `CodePetWidgetRenderingTest` |
| Screenshot golden | `GreetingScreenshotTest` |

`TestApplication` initializes WorkManager for Robolectric tests that touch app startup or workers.
