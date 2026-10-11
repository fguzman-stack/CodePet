# Architecture

[Back to README](../README.md) · [Docs index](README.md)

## Stack

| Area | Technology |
|:--|:--|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 |
| State | ViewModel + StateFlow + Coroutines |
| Persistence | Room + DataStore |
| Dependency injection | Koin 4.0.2 |
| Background work | WorkManager |
| Tests | JUnit, Robolectric, Compose UI tests, Roborazzi |

## App shape

CodePet is a single-module Android app under `:app`. Sources live in `app/src/main/java/com/tamagotchi/code` and the application id is `com.tamagotchi.code`.

## Entry points

- `SplashActivity` shows the intro and launches `MainActivity`.
- `CodeTamagotchiApp` starts Koin, notification channels, WorkManager jobs and widget updates.
- `MainActivity` is Compose-only and calls `AppNavigation`.

## Data flow

```text
User -> Composable screen -> PetViewModel -> Repository -> DAO -> Room
                 ^                              |
                 +---------- StateFlow ---------+
```

## Main package map

```text
app/src/main/java/com/tamagotchi/code/
├── data/                 static content, Room, repositories
├── di/                   Koin module
├── feature/              screens by feature area
├── navigation/           AppNavigation and route constants
├── ui/components/        Codey, cards, backgrounds and shared UI
├── ui/theme/             theme definitions and Material setup
├── ui/viewmodel/         PetViewModel
├── util/                 calculators, workers and sound helpers
└── widget/               launcher widget provider and worker
```

## Dependency graph

The graph is intentionally small: database, DAO, repositories and `PetViewModel` are registered in `di/AppModule.kt`.

`PetViewModel` owns the persistent game behavior so feature screens stay thin and call into one shared state holder.
