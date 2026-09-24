<div align="center">
  <img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=26&duration=3200&pause=900&color=81C784&center=true&vCenter=true&width=760&lines=%F0%9F%90%BE+Code+Tamagotchi;%3E+Your+virtual+pet+that+learns+to+code+with+you;%3E+Study.+Solve+challenges.+Evolve." alt="Code Tamagotchi - Your virtual pet for learning to program"/>
  <br/>
  <img src="app/src/main/res/drawable/iconoapp.png" width="150" alt="Code Tamagotchi Logo"/>
  <h1>🐾 Code Tamagotchi</h1>
  <p><em>Your study buddy that evolves while you code</em></p>
  <p>
    <a href="https://github.com/fguzman-stack/CodePet/releases"><img src="https://img.shields.io/github/v/release/fguzman-stack/CodePet?style=for-the-badge&logo=github&logoColor=white&color=7C4DFF" alt="Latest release"/></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-81C784?style=for-the-badge&logo=opensourceinitiative&logoColor=white" alt="MIT License"/></a>
    <a href="https://github.com/fguzman-stack/CodePet/actions/workflows/android.yml"><img src="https://img.shields.io/github/actions/workflow/status/fguzman-stack/CodePet/android.yml?style=for-the-badge&logo=githubactions&logoColor=white&label=Build" alt="Build status"/></a>
  </p>
  <p>
    <img src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=flat-square&logo=android&logoColor=white"/>
    <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Jetpack_Compose-UI-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white"/>
    <img src="https://img.shields.io/badge/Material_3-Material_You-6750A4?style=flat-square&logo=materialdesign&logoColor=white"/>
    <img src="https://img.shields.io/badge/Room-SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white"/>
    <img src="https://img.shields.io/badge/Koin-4.0.2-0099E5?style=flat-square&logo=koin&logoColor=white"/>
  </p>
  <br/>
  <pre>> ./code-tamagotchi --status
> <b>Status:</b> <code>ACTIVE</code>  <b>Platform:</b> <code>ANDROID</code>  <b>Language:</b> <code>KOTLIN / COMPOSE</code></pre>
  <p><b>Code Tamagotchi</b> turns programming study into an experience of care, progress and rewards. Solve challenges, finish Pomodoro sessions, earn Bytes and keep <b>Codey</b> happy, healthy and ready to compile.</p>
  <a href="https://github.com/fguzman-stack/CodePet/releases/latest"><img src="https://img.shields.io/badge/▶_Download_the_APK-81C784?style=for-the-badge&logo=android&logoColor=white" alt="Download the APK"/></a>
  <a href="#-features"><img src="https://img.shields.io/badge/✨_Explore_features-7C4DFF?style=for-the-badge&logo=github&logoColor=white" alt="Explore features"/></a>
</div>

<p align="center">
  <b>English</b> · <a href="README.es.md">🇪🇸 Español</a>
</p>

---

## 📋 Navigation

<div align="center">
  [✨ Features](#-features) ·
  [📸 Screenshots](#-screenshots) ·
  [🛠️ Tech Stack](#️-tech-stack) ·
  [🏗️ Architecture](#️-architecture) ·
  [⚙️ Mechanics](#-game-mechanics-in-real-code) ·
  [💾 Persistence](#-persistence--room-v5) ·
  [🧩 Live systems](#-live-systems-petviewmodel) ·
  [⏰ Background work](#-background-work-workmanager) ·
  [🔌 DI](#-dependency-injection-koin) ·
  [🖌️ Rendering](#-procedural-rendering) ·
  [📱 Widget](#-home-screen-widget) ·
  [🧪 Tests](#-testing) ·
  [⚡ Quick Start](#-quick-start) ·
  [🎮 How to Play](#-how-to-play) ·
  [🎨 Themes](#-themes) ·
  [🌍 Localization](#-localization) ·
  [🗺️ Roadmap](#️-roadmap) ·
  [🤝 Contributing](#-contributing)
</div>

---

## ✨ Features

<table>
<tr>
<td width="50%" valign="top">

### 🐾 Virtual pet
- Dynamic emotional states (HAPPY, SLEEPING, STUDYING, SICK, HUNGRY, SAD, EXCITED, DEAD)
- Health, hunger and energy in real time with progressive decay
- ☠️ **Death & revival system** — if health hits 0, Codey shows up under a tombstone and you can revive him with Bytes
- State-based animations (breathing, trembling, hunger pulse, blinking)
- 🖌️ Codey is drawn **100% procedurally** with Compose Canvas — no sprite PNGs; the home-screen widget reuses the same renderer
- Level, XP and Bytes (virtual currency) system
- Daily study streaks and offline rewards
- 🏅 **Visual evolution** — 5 stages: Egg → Baby → Adult → Veteran → Legendary
- 😊 **Moodlets** — random events that shift the mood for hours
- 👫 **Pair Programming** — Buggy shows up randomly and multiplies XP ×1.5

</td>
<td width="50%" valign="top">

### 💻 Learn by coding
- Challenges in **Kotlin**, **JavaScript**, **PHP** and **Python**
- Trivia and debugging questions
- Special algorithm challenges that unlock visual themes
- Educational feedback on every answer, in Codey's voice
- Progress and accuracy rewards
- **96 built-in coding challenges** (90 regular + 6 special)

</td>
</tr>
<tr>
<td width="50%" valign="top">

### ⏱️ Built-in Pomodoro
- 15, 25 or 50 minute sessions
- Persistent study log
- XP and Bytes per minute
- Bonus for 25+ minute sessions (+50 Bytes, +75 XP)
- 8 study topics: Kotlin, JavaScript, PHP, Python, SQL, Clean Code, Git, Data Structures
- Active sessions auto-resume when opening the app

</td>
<td width="50%" valign="top">

### 🕹️ Debugging Arcade
- 🐛 **Bug Hunt — Terminal Panic:** find bugs in code snippets
- 🐙 **Git Rescue:** Git decisions to save a repo on fire
- 🔧 **Refactor Rush:** reorder code blocks until it compiles
- 📝 **Code Review:** spot errors in 10 multi-language snippets
- 🏆 **Hackathon:** weekly event with 3 problems of rising difficulty
- Codey's humorous explanations on every round
- Daily cooldown to prevent currency farming
- 🎮 Classic arcade (Guess the Bit, Bug Catch, Server/Script/Hacker)

</td>
</tr>
</table>

### ⚙️ Extra features
- 🔇 **DND Mode** — focus mode that mutes notifications and grants +10% XP
- 🌳 **Skill Tree** — 3 branches, 9 unlockable nodes
- 🏆 **Daily rewards** streak & offline rewards
- 🎫 **Season Pass** — 30-day battle pass with 20 levels
- 📋 **Weekly Missions** — 3 rotating missions every Monday with rewards
- 📱 **Home-screen widget** — watch Codey with health, energy and hunger meters (compact 2×2, resizable)

---

## 📸 Screenshots

| Home | Challenges | Pomodoro |
|:---:|:---:|:---:|
| <img src="docs/screenshots/01-home.png" width="220"/> | <img src="docs/screenshots/02-learn.png" width="220"/> | <img src="docs/screenshots/03-focus.png" width="220"/> |
| **Arcade** | **Shop** | **Weekly Missions** |
| <img src="docs/screenshots/04-games.png" width="220"/> | <img src="docs/screenshots/05-shop.png" width="220"/> | <img src="docs/screenshots/06-missions.png" width="220"/> |
| **Themes** | **Skill Tree** | **Season Pass** |
| <img src="docs/screenshots/07-themes.png" width="220"/> | <img src="docs/screenshots/08-skilltree.png" width="220"/> | <img src="docs/screenshots/09-seasonpass.png" width="220"/> |

> 🎨 12 premium themes with unique animated Canvas backgrounds. 🐾 Watch Codey evolve from Egg to Legendary as you study.

---

## ⚡ Quick Start

```bash
# Clone
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet

# Build the dev APK
./gradlew clean assembleDebug

# Install on device/emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Or just grab the [prebuilt APK from Releases](https://github.com/fguzman-stack/CodePet/releases/latest)** — no build tools needed.

The project uses Gradle 9.x with the Gradle Wrapper. **Requires:** JDK 17+, Android SDK (API 36). The app is **100% offline** — no API keys, accounts or secrets needed to build or play.

---

## 🔄 Progress loop

```
    STUDIES             SOLVES               EARNS
 +------------+     +------------+      +------------+
 | Pomodoro   | --> | Dev        | ---> | XP + Bytes |
 |            |     | challenges |      |            |
 +------------+     +------------+      +-----+------+
                                              |
                                              v
                                       +------------+
                                       |   CARES    |
                                       |   CODEY    |
                                       +-----+------+
                                             |
                                             v
                                        UNLOCKS THEMES
```

> Every finished session strengthens your pet. Every correct challenge speeds up its evolution.

---

## 😊 Codey's states

| State | Main trigger | Behavior |
|:-------|:--------------------|:---------------|
| 😊 `HAPPY` | Balanced stats | Gentle floating, blinking |
| 😴 `SLEEPING` | User enables rest | Recovers energy (+12/h), breathing backdrop |
| 📚 `STUDYING` | Pomodoro in progress | Focused on learning |
| 🤒 `SICK` | Health below 20% | Trembles, needs care |
| 🍽️ `HUNGRY` | Hunger below 20% | Alert pulse |
| 😢 `SAD` | Energy below 15% | Low activity |
| 😆 `EXCITED` | Max happiness | Fast bouncing |
| ☠️ `DEAD` | Health hits 0 | Static tombstone, revival dialog |

> Status is a **priority chain** resolved in `StatusCalculator.determineStatus()` — see [game mechanics](#-game-mechanics-in-real-code).

---

## 🛠️ Tech Stack

| Technology | Used for |
|:-----------|:----|
| **Kotlin 2.2.10** | Main language |
| **Jetpack Compose** | Declarative UI with native animations |
| **Material 3** | Components & visual system (Material You) |
| **Room v5** | Local SQLite persistence with migrations |
| **DataStore** | User prefs, themes, cooldowns |
| **ViewModel + StateFlow** | Reactive state & lifecycle |
| **Coroutines + Flow** | Async work and data streams |
| **Koin 4.0.2** | Dependency injection |
| **KSP** | Compile-time annotation processing |
| **WorkManager** | Periodic jobs (stat decay every 4h) |
| **Roborazzi** | Visual regression tests via screenshots |

---

## 🏗️ Architecture

The project follows **MVVM + Repository** with dependency injection via **Koin**.

```
+------------------------------------------------------------------+
|              🖥️ PRESENTATION (UI Layer)                          |
|  Compose Screens · Material 3 · StateFlow · Animations           |
|  HomeScreen | LearnScreen | FocusScreen | ShopScreen             |
|  GamesScreen | BugHuntScreen | SettingsScreen | OnboardingScreen |
|  CodeReviewScreen | HackathonScreen                              |
+------------------------------------------------------------------+
|              🧠 DOMAIN (ViewModel)                               |
|  PetViewModel                                                     |
|    - Pet state management                                         |
|    - Study timer logic (start/cancel/complete)                    |
|    - Challenge submission & reward calculation                    |
|    - Shop purchases & skin equipping                                |
|    - Theme switching & unlock                                     |
|    - Game cooldowns                                               |
|    - Offline reward detection                                     |
|    - DND Mode, sound effects, daily commit generator               |
|    - Seasonal events: Hackathon, Weekly Missions, Season Pass     |
+------------------------------------------------------------------+
|              📊 DATA (Data Layer)                                 |
|  PetRepository | UserPreferencesRepository | AchievementsRepository|
|  DecayCalculator | RewardCalculator | LevelCalculator             |
|  StatusCalculator | SoundManager                                   |
|  (Moodlets, Pair Buddy & Skill Tree logic live in the ViewModel)  |
+------------------------------------------------------------------+
|              💾 PERSISTENCE (Storage Layer)                       |
|  Room / SQLite                                                    |
|    - PetStateEntity | StudySessionEntity | FocusSessionEntity     |
|    - CodeCardEntity | QuestEntity | OwnedItemEntity              |
|    - LanguageProgressEntity | ActivityLogEntity                  |
|  DataStore Preferences                                            |
|    - Onboarding, theme, difficulty, sound, motion, DND, colors   |
+------------------------------------------------------------------+
```

**Data flow:**
```
User → Composable (Screen) → ViewModel → Repository → DAO → SQLite
                ^                        |
                +------- StateFlow ------+
```

### Project structure

```
CodePet/
├── app/src/main/java/com/tamagotchi/code/
│   ├── CodeTamagotchiApp.kt          # Application (Koin, channels, 3 WorkManager jobs, widget observer)
│   ├── SplashActivity.kt             # 2s intro -> MainActivity
│   ├── MainActivity.kt               # Entry point (Theme, Nav, koinViewModel)
│   ├── data/
│   │   ├── ChallengesData.kt         # 90 coding challenges
│   │   ├── ChallengesDataEn.kt       # EN mirror keyed by stable challenge id
│   │   ├── SpecialChallengesData.kt  # 6 algorithm/architecture challenges (+En)
│   │   ├── CodeReviewData.kt         # Code review snippets (+ CodeReviewDataEn)
│   │   ├── CodeCards.kt              # 40 trivia cards (+ CodeCardsEn)
│   │   ├── LocalizedContent.kt       # localized() extensions per content type
│   │   ├── PersonalityMissions.kt    # Mission definitions
│   │   ├── database/                 # AppDatabase (v5), PetDao, entities
│   │   └── repository/               # Pet, preferences & achievements repos
│   ├── di/AppModule.kt               # Koin DI module (entire graph, 19 lines)
│   ├── navigation/AppNavigation.kt   # NavHost, Routes, BottomNav
│   ├── feature/
│   │   ├── home/ learn/ focus/ shop/ onboarding/ settings/
│   │   ├── games/                    # BugHunt, GitRescue, RefactorRush,
│   │   │                             # CodeReview, Hackathon + classic arcade
│   │   │                             # (BinaryGuess, BugSmasher, RockPaperSci)
│   │   └── skills/                   # SeasonPass, SkillTree, WeeklyMissions
│   ├── ui/
│   │   ├── viewmodel/PetViewModel.kt # central game brain (~1,500 lines)
│   │   ├── components/               # ViewportCard, CodeySprite (procedural
│   │   │                             # Canvas), CodeyBitmap (widget renderer),
│   │   │                             # AnimatedThemeBackground, PairBuddy...
│   │   └── theme/                    # ThemeConfig (12 themes), Theme, Type
│   ├── widget/
│   │   ├── CodePetWidgetProvider.kt  # RemoteViews + procedural bitmap
│   │   └── WidgetUpdateWorker.kt     # hourly refresh
│   └── util/                         # Decay/Reward/Level/Status calculators,
│                                     # SoundManager, DailyCommitGenerator,
│                                     # CommitWorker, PetCheckWorker, ContentKeys
├── app/src/test/                     # JVM + Robolectric + Roborazzi tests
└── gradle/libs.versions.toml         # Gradle version catalog
```

---

## ⚙️ Game mechanics in real code

> Every snippet below is copied verbatim from the source — no pseudo-code.

### Stat decay (`util/DecayCalculator.kt`)

Decay is **offline-safe**: nothing runs on a timer while you're away. When the app (or `PetCheckWorker`) wakes up, the elapsed time since `lastUpdated` is converted to hours and applied in one pass:

```kotlin
val hours = elapsedMs.toFloat() / (1000f * 60f * 60f)
...
if (state.currentStatus == "SLEEPING") {
    newEnergy = (newEnergy + (hours * 12f)).coerceIn(0f, 100f)
    newHunger = (newHunger - (hours * 1f)).coerceIn(0f, 100f)
    if (newEnergy >= 100f) {
        newStatus = "HAPPY"
    }
} else {
    newHunger = (newHunger - (hours * 2.5f)).coerceIn(0f, 100f)
    newEnergy = (newEnergy - (hours * 2f)).coerceIn(0f, 100f)
}

val baseHealthDecay = hours * 1.5f
newHealth = (newHealth - baseHealthDecay).coerceIn(0f, 100f)
```

| Rule | Effect |
|:--|:--|
| Awake decay | hunger **−2.5/h** · energy **−2/h** · health **−1.5/h** |
| Sleeping | energy **+12/h** (auto-wakes at 100), hunger only −1/h |
| Starving (hunger = 0) | extra health −3/h |
| Exhausted (energy ≤ 10) | extra health −1/h |
| No study in 72 h | extra health −1.5/h |
| No study in 48 h | streak resets to 0 |
| Health ≤ 0 | `isDead = true`, status `DEAD` |

### Status resolution (`util/StatusCalculator.kt`)

A priority chain — first match wins, which is why a sleeping pet never shows as hungry:

```kotlin
return when {
    isDead -> "DEAD"
    isExcited -> "EXCITED"
    isStudying -> "STUDYING"
    isSleeping -> "SLEEPING"
    health < 20f -> "SICK"
    hunger < 20f -> "HUNGRY"
    energy < 15f -> "SAD"
    else -> "HAPPY"
}
```

### Economy (`util/RewardCalculator.kt`)

```kotlin
val baseBytes = minutes * 2
val baseXP = minutes * 3
val bonusBytes = if (minutes >= 25) 50 else 0
val bonusXP = if (minutes >= 25) 75 else 0
val energyCost = (minutes * 0.5f).coerceAtMost(30f)
```

```kotlin
fun calculateChallengeReward(type: String): ChallengeReward {
    val bytes = if (type == "DEBUG") 30 else 25
    val xp = if (type == "DEBUG") 25 else 20
    return ChallengeReward(bytes = bytes, xp = xp, hungerRestore = 15f, healthRestore = 20f)
}

fun calculateMinigameReward(score: Int, maxBytes: Int): Int {
    return (score * 10).coerceAtMost(maxBytes)
}

fun calculateRefactorReward(attempts: Int): Int {
    return maxOf(50 - attempts * 5, 10)
}
```

Streaks use **calendar days**, not rolling 24 h windows:

```kotlin
val studiedYesterday = lastCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
        lastCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)
newStreak = if (studiedYesterday) currentStreak + 1 else 1
```

### Level curve (`util/LevelCalculator.kt`)

```kotlin
var level = 1
var requiredXp = 100
while (xp >= requiredXp) {
    level++
    requiredXp += level * 100
}
```

Cumulative XP: **L2 = 100 · L3 = 300 · L4 = 600 · L5 = 1000 · L6 = 1500** — each new level costs `level × 100` XP, so evolution stages feel earned.

---

## 💾 Persistence — Room v5

Eight entities, five versions, four explicit migrations:

```kotlin
@Database(
    entities = [
        PetStateEntity::class, StudySessionEntity::class, FocusSessionEntity::class,
        CodeCardEntity::class, QuestEntity::class, OwnedItemEntity::class,
        LanguageProgressEntity::class, ActivityLogEntity::class
    ],
    version = 5,
    exportSchema = false
)
```

| Migration | What it does (real SQL in `AppDatabase.kt`) |
|:--|:--|
| `1 → 2` | `CREATE TABLE focus_sessions` — live Pomodoro timer state |
| `2 → 3` | `ALTER TABLE pet_state ADD hasRenamed` |
| `3 → 4` | `ALTER TABLE pet_state ADD isDead` — death system |
| `4 → 5` | `code_cards`, `quests`, `owned_items`, `language_progress`, `activity_logs` |

The pet row is a **singleton pattern** (`WHERE id = 1 LIMIT 1`), exposed reactively:

```kotlin
@Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
fun getPetState(): Flow<PetStateEntity?>

@Transaction
suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
    updateFocusSessionStatus(sessionId, status)
    insertOrUpdatePetState(petState)
}
```

`PetStateEntity` fields: `name · language · level · xp · hunger · health · energy · bytes · streak · lastUpdated · lastStudyDate · currentStatus · hasRenamed · isDead`. Anything non-critical (theme, DND, cooldowns, moodlets, skill tree, season pass) lives in **DataStore Preferences** instead of a schema change.

---

## 🧩 Live systems (PetViewModel)

### 😊 Moodlets — 6 random events

```kotlin
val eventTypes = listOf("bug_prod", "tabs", "spaces", "bad_commit", "clean_code", "spilled_coffee")
val type = eventTypes.random()
val duration = when (type) {
    "bug_prod" -> 2L
    "spilled_coffee" -> 1L
    "bad_commit" -> 2L
    "clean_code" -> 3L
    else -> 1L
} // hours, persisted with an expiry timestamp
```

Roll: every 30 minutes, 5% chance (`checkAndTriggerMoodlet`).

### 👫 Pair Programming Buddy

```kotlin
fun checkAndActivatePairBuddy() {
    if (pairBuddyState.value != null) return
    if (kotlin.random.Random.nextFloat() < 0.2f) activatePairBuddy()
}
// PairBuddyState(name = "Buggy", remainingChallenges = 3, xpMultiplier = 1.5f)
```

### 🌳 Skill Tree — XP burned, not spent

```kotlin
val cost = (node.currentTier + 1) * 100   // tier 1 = 100 XP, tier 2 = 200 XP...
if (pet.xp < cost) return@launch
val updated = pet.copy(xp = pet.xp - cost)
```

3 branches × 3 nodes, each node upgradable through `maxTier = 3`.

### 🎫 Season Pass

```kotlin
val newXp = current.xp + xp
val newLevel = current.level + (newXp / 100)
val remainingXp = newXp % 100
val updated = current.copy(xp = remainingXp, level = newLevel.coerceAtMost(20)) // then persisted to DataStore
```

### 📋 Weekly Missions & 🏆 Hackathon

```kotlin
WeeklyMissionData("wm1", ..., 100, 50)   // XP, Bytes
WeeklyMissionData("wm2", ..., 200, 100)
WeeklyMissionData("wm3", ..., 150, 75)
// HackathonData(attempts, bestTimeMs, expiresAt = now + 48h)
```

Titles are resolved by stable id at display time (`LocalizedGameText.kt`), so mission text localizes without touching the persisted state.

### 🔇 DND detection — real Android API

```kotlin
val filter = notificationManager.currentInterruptionFilter
isDndActive.value = filter == INTERRUPTION_FILTER_PRIORITY ||
        filter == INTERRUPTION_FILTER_NONE || filter == INTERRUPTION_FILTER_ALARMS
```

When active: notifications silenced and **+10% XP** on learning rewards.

### ☠️ Death & revival

```kotlin
fun reviveWithBytes() {
    val cost = deathReviveCost.value
    if (pet.bytes < cost) return@launch
    val revived = pet.copy(
        isDead = false, health = 50f, energy = 50f, hunger = 50f,
        currentStatus = "HAPPY", bytes = pet.bytes - cost, ...
    )
}
```

| Path | Cost | Restored stats |
|:--|:--|:--|
| `reviveWithBytes()` | Bytes price (dynamic) | 50 / 50 / 50 |
| `reviveForFree()` | 0 Bytes | 40 / 40 / 40 — the "hard reset" mercy path |

---

## ⏰ Background work (WorkManager)

All jobs are scheduled from `CodeTamagotchiApp.onCreate()`:

| Job | Schedule | Policy | Notes |
|:--|:--|:--|:--|
| `pet_check` | every **4 h** | `KEEP` | `setRequiresBatteryNotLow(true)`, 2 h initial delay |
| `widget_update` | every **1 h** | `KEEP` | keeps launcher meters fresh even if the app never opens |
| `daily_commit` | one-shot at **midnight** | `REPLACE` | reschedules itself for the next midnight |

```kotlin
val request = PeriodicWorkRequestBuilder<PetCheckWorker>(4, TimeUnit.HOURS)
    .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
    .setInitialDelay(2, TimeUnit.HOURS)
    .build()
WorkManager.getInstance(this)
    .enqueueUniquePeriodicWork("pet_check", ExistingPeriodicWorkPolicy.KEEP, request)
```

`PetCheckWorker` routes alerts by severity into **5 notification channels** (grouped under `pet_care`):

```kotlin
val channelId = when {
    petState.health < 5f || petState.hunger < 5f || petState.energy < 5f -> "pet_critical"
    petState.hunger < 20f -> "pet_hunger"
    petState.health < 20f -> "pet_health"
    petState.energy < 15f -> "pet_energy"
    else -> "pet_care_reminder"
}
```

The widget-update trick: the **Application observes Room**, not a screen lifecycle, so even background writes (workers) refresh the launcher instantly:

```kotlin
combine(petRepository.petState.distinctUntilChanged(), preferences.currentTheme)
    { state, theme -> state to theme }.collect { (state, theme) ->
        val pixelMode = theme == "Retro Pixel"
        manager.getAppWidgetIds(provider).forEach { id ->
            CodePetWidgetProvider.updateAppWidget(this, manager, id, state, pixelMode)
        }
    }
```

---

## 🔌 Dependency injection (Koin)

`di/AppModule.kt` — the entire graph, 19 lines:

```kotlin
val appModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().petDao() }
    single { PetRepository(get()) }
    single { UserPreferencesRepository(androidContext()) }
    single { AchievementsRepository(androidContext()) }
    viewModel { PetViewModel(get(), get(), get()) }
}
```

Screens get it with `viewModel<PetViewModel>(koin = koin)` / `koinViewModel()` — there is exactly **one** game brain.

---

## 🖌️ Procedural rendering

There are **zero PNGs** for Codey. The persisted status string maps to an enum and a single `Canvas` draws everything:

```kotlin
internal enum class PetMood { HAPPY, SLEEPING, STUDYING, SICK, HUNGRY, SAD, EXCITED, DEAD }

internal fun codeyMood(status: String, isDead: Boolean = false): PetMood =
    if (isDead) PetMood.DEAD
    else PetMood.entries.firstOrNull { it.name == status } ?: PetMood.HAPPY
```

The renderer is a toolbox of `DrawScope` extensions, all in `CodeySprite.kt`:

`drawEgg` · `drawRobot` · `drawVisorFace` · `drawCore` · `drawAntenna` · `drawWing` · `drawLimb` · `drawBackpack` · `drawBadge` · `drawPropellerCap` · `drawHeadphones` · `drawCircuitCrown` · `drawEnergyCape` · `drawEvolutionBurst` · `drawDeadScene` · `drawTombstone` · `drawGhost`

Design decisions:
- **5 evolution stages** swap `stagePalette()` and bolt on parts — egg → baby bot → … → circuit crown + energy cape at Legendary
- Blinking, breathing, trembling and hunger-pulse come from `rememberInfiniteTransition`, and every one respects the **Reduce Motion** preference
- The user's **accent color** (persisted in DataStore, costs 30 Bytes) tints visor and core — so no two pets look identical
- The DEAD scene is fully drawn too: tombstone + ghost, deliberately static

---

## 📱 Home-screen widget

`RemoteViews` can't run Compose, so the widget renders the **same** procedural Codey into a `Bitmap` via `CodeyBitmap.kt`:

```kotlin
views.setImageViewBitmap(
    R.id.widget_pet_image,
    renderCodeyBitmap(
        petState?.level ?: 1,
        effectiveStatus ?: "HAPPY",
        petState?.isDead ?: false,
        pixelMode = pixelMode
    )
)
views.setProgressBar(R.id.health_bar, 100, health, false)
```

Compact 2×2, resizable, tap opens `MainActivity`. `pixelMode` switches the renderer to 8-bit when the **Retro Pixel** theme is active. Covered by `CodePetWidgetRenderingTest` on the JVM — no emulator needed.

---

## 🧠 Public API cheat-sheet (`PetViewModel`)

| Method | Responsibility |
|:--|:--|
| `submitAnswer(optionIndex)` | grading, rewards, moodlet/buddy rolls, season-pass XP, quest & language progress |
| `startStudyTimer / completeFocusSession / cancelStudyTimer` | Pomodoro lifecycle + persistence + auto-resume |
| `buyShopItem / buySkin / equipSkin` | shop economy, `@Transaction` item equipping |
| `petThePet / cleanThePet / toggleSleep` | care actions (drive quests & daily commits) |
| `canPlayGame / recordGamePlay` | 24 h arcade cooldowns |
| `checkDailyRewardEligibility / claimDailyReward` | offline & daily rewards |
| `loadMoodlet / triggerRandomMoodlet` | moodlet system |
| `unlockSkillNode` | skill-tree progression (XP burn) |
| `addSeasonPassXp` | battle-pass leveling |
| `completeWeeklyMission` | Monday-reset mission board |
| `submitHackathonSolution / consumeHackathonAttempt` | 48 h weekly event |
| `checkDeathState / reviveWithBytes / reviveForFree` | death & revival |
| `checkDndMode` | system interruption-filter probe |

---

## 🧪 Testing

| Test | What it pins down |
|:--|:--|
| `StatusCalculatorTest` | status priority chain and thresholds |
| `GameEconomyTest` | reward math, cooldown logic, settings validation |
| `LocalizedContentTest` | ES↔EN id parity for all 90+6 challenges, 10 snippets, 40 cards |
| `CodeyRendererTest` / `CodeySpriteTest` | procedural renderer across every stage & mood |
| `PetAnimationConfigTest` | animation config per state |
| `ThemeContrastTest` | readable contrast on all 12 themes |
| `CodePetWidgetRenderingTest` | widget bitmap + RemoteViews construction |
| `GreetingScreenshotTest` | Roborazzi golden (`app/src/test/screenshots/greeting.png`) |

```bash
./gradlew :app:testDebugUnitTest          # all JVM/Robolectric tests
./gradlew :app:testDebugUnitTest --tests "com.tamagotchi.code.StatusCalculatorTest"
./gradlew :app:verifyRoborazziDebug       # visual regression
./gradlew :app:lintDebug                  # Android lint
```

---

## 🎮 How to Play

| Step | Action | Reward |
|:----:|:-------|:-----------|
| `01` | Name your pet during onboarding | Start of the adventure |
| `02` | Complete challenges in **LEARN** | XP, Bytes, health and food |
| `03` | Start a Pomodoro in **STUDY** | XP and Bytes per minute |
| `04` | Buy resources in **SHOP** | Restore stats |
| `05` | Play minigames | Bytes and bonuses |
| `06` | Keep the daily streak | Steady progress |
| `07` | Solve special challenges | Unlock new visual themes |

### Quick economy

| Activity | Bytes | XP |
|:----------|:-----:|:--:|
| Regular challenge | +25 to +30 | +20 to +25 |
| Special challenge | +50 | — |
| Study per minute | +2 | +3 |
| 25+ min session bonus | +50 | +75 |
| Code Review | Up to +60 | — |
| Hackathon (full) | +180 | +175 |

---

## 🎨 Themes

Code Tamagotchi ships with **12 premium themes**. Each configures colors, typography, corners, gradients and a unique animated Canvas background.

| Theme | Personality |
|:-----|:-------------|
| 🖥️ Matrix Green | Hacker terminal, pure monospace |
| 🌌 Galactic | Deep purples and cosmic flashes |
| 🌆 Cyberpunk | Electric pink and cyan against the dark |
| 🌸 Sakura | Japanese elegance in soft pink |
| ⚪ Minimalist | Pure white with subtle accents |
| 💡 Neon | Total darkness with vibrant flashes |
| 🌊 Ocean | Deep blues, underwater calm |
| 🌋 Volcanic | Fire under the surface |
| ⚔️ Samurai | Steel, blood and ancient gold |
| 🌌 Aurora | Northern lights on the dark sky |
| 🌙 Night | Elegant iOS-style night |
| 🕹️ Retro Pixel | **FINAL THEME** — definitive 8-bit, pixel art |

> Pick one from the visual carousel in **Settings**. Solve special challenges to unlock new themes.

---

## 🌍 Localization

Code Tamagotchi is fully bilingual (**English + Spanish**) and follows your **system language** automatically — there is no in-app switcher:

- ✅ UI, dialogs, game texts, notifications and widget: `values/` (es) + `values-en/` (en)
- ✅ Learning content: 90 code challenges, 6 special algorithm challenges, 10 code-review snippets and 40 code cards translated to English (matched by stable id, order-checked in `LocalizedContentTest`)
- ⚠️ Notification **channel labels** (Android Settings) are created once with the language active at first install
- ⚠️ Theme names are shown in Spanish by design (they are stable identifiers stored in preferences)

Translations live next to the original data: `ChallengesDataEn.kt`, `SpecialChallengesDataEn.kt`, `CodeReviewDataEn.kt`, `CodeCardsEn.kt`. PRs to fix wording (either language) are very welcome.

The mechanism is a single extension per content type, resolved at display time — Spanish stays the source of truth and the DB never stores translations:

```kotlin
fun isEnglishContent(): Boolean = Locale.getDefault().language.equals("en", ignoreCase = true)

fun CodingChallenge.localized(): CodingChallenge {
    if (!isEnglishContent()) return this
    val en = ChallengesDataEn.translations[id]
        ?: SpecialChallengesDataEn.translations[id]
        ?: return this
    return copy(language = en.language, title = en.title, question = en.question,
        codeSnippet = en.codeSnippet ?: codeSnippet, options = en.options, explanation = en.explanation)
}
```

`LocalizedContentTest` asserts that every Spanish id has an English twin and that option order (hence `correctAnswerIndex`) is preserved.

---

## 🗺️ Roadmap

- [x] 🐾 Virtual pet with emotional states and decay
- [x] 💻 90 coding challenges + 6 special (Kotlin, JS, PHP, Python)
- [x] ⏱️ Persistent Pomodoro with auto-resume
- [x] 🎨 12 premium themes with animated Canvas backgrounds
- [x] 🕹️ 6 minigames: Bug Hunt, Git Rescue, Refactor Rush, Code Review, Hackathon, Classic Arcade
- [x] 🏅 Visual pet evolution (5 stages: Egg → Legendary)
- [x] 😊 Moodlet system (6 random events)
- [x] 👫 Pair Programming Buddy (Buggy, XP ×1.5)
- [x] 🔇 DND Mode (focus mode +10% XP)
- [x] 🌳 Skill Tree (3 branches, 9 unlockable nodes)
- [x] 🎫 Season Pass (30 days, 20 levels, free/premium)
- [x] 📋 Weekly Missions (3 missions, reset every Monday)
- [x] 🏆 Local achievements and reward system
- [x] 🔔 Personality notifications (reminders & rewards)
- [x] 📱 Home-screen widget
- [ ] 🌐 Challenges in Ruby, Go, Rust and Swift ← **want to help? this is the easiest contribution!**
- [ ] ☁️ Firebase Firestore sync
- [ ] 👥 Multiplayer and leaderboards

---

## 🤝 Contributing

The fastest way in: **add coding challenges** for a language we don't cover yet (Rust, Go, Ruby, Swift...). Check [CONTRIBUTING.md](CONTRIBUTING.md) and the [`good first issue`](https://github.com/fguzman-stack/CodePet/labels/good%20first%20issue) label.

1. Fork the repository
2. Create a branch (`git checkout -b feature/new-challenges-rust`)
3. Commit your changes (`git commit -m 'feat: add Rust challenges'`)
4. Push the branch (`git push origin feature/new-challenges-rust`)
5. Open a [Pull Request](https://github.com/fguzman-stack/CodePet/pulls)

### Not a dev? Still useful:
- 🐛 Report bugs and missing challenge answers
- 🎨 Suggest theme palettes or hat designs
- 🌍 Improve these translations (open a `docs:` PR)

---

## 📄 License

Released under the **MIT License** — which in plain language means you are free to:

- ✅ Use it for anything, including commercial projects
- ✅ Fork and adapt it (new challenge languages, other platforms, classroom tools...)
- ✅ Translate the UI to any language and ship the fork publicly
- ✅ Sell a service or derivative built on top of it

The only requirement is keeping the copyright notice and license text in source copies. No CLA, no copyleft — attribution in the code is enough. If you build something cool on top of CodePet, a shout-out in the [Discussions](https://github.com/fguzman-stack/CodePet/discussions) is always welcome, but not required. 😉

---

## ⭐ Star History

<a href="https://star-history.com/#fguzman-stack/CodePet&Date">
  <img src="https://api.star-history.com/svg?repos=fguzman-stack/CodePet&type=Date" width="600" alt="Star History Chart"/>
</a>

---

<div align="center">
  <p>
    <a href="https://github.com/fguzman-stack/CodePet/stargazers">
      <img src="https://img.shields.io/github/stars/fguzman-stack/CodePet?style=for-the-badge&logo=github&color=FFD54F" alt="Repository stars"/>
    </a>
    <a href="https://github.com/fguzman-stack/CodePet/fork">
      <img src="https://img.shields.io/github/forks/fguzman-stack/CodePet?style=for-the-badge&logo=github&color=64B5F6" alt="Repository forks"/>
    </a>
  </p>
  <p><b>Code Tamagotchi v3.0</b> — where bugs become pets and code becomes affection.</p>
  <p>Questions? Open an <a href="https://github.com/fguzman-stack/CodePet/issues">issue</a> or check the <a href="documentacion.md">full documentation</a> (ES).</p>
  <p><sub>MIT License · Made with ☕ and 🐛</sub></p>
</div>
