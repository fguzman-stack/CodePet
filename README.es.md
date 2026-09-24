<div align="center">
  <img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=26&duration=3200&pause=900&color=81C784&center=true&vCenter=true&width=760&lines=%F0%9F%90%BE+Code+Tamagotchi;%3E+Tu+mascota+virtual+para+aprender+a+programar;%3E+Estudia.+Resuelve+retos.+Evoluciona." alt="Code Tamagotchi - Tu mascota virtual para aprender a programar"/>
  <br/>
  <img src="app/src/main/res/drawable/iconoapp.png" width="150" alt="Code Tamagotchi Logo"/>
  <h1>🐾 Code Tamagotchi</h1>
  <p><em>Tu compañero de estudio que evoluciona mientras programas</em></p>
  <p>
    <a href="https://github.com/fguzman-stack/CodePet/releases"><img src="https://img.shields.io/github/v/release/fguzman-stack/CodePet?style=for-the-badge&logo=github&logoColor=white&color=7C4DFF" alt="Última versión"/></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/Licencia-MIT-81C784?style=for-the-badge&logo=opensourceinitiative&logoColor=white" alt="Licencia MIT"/></a>
    <a href="https://github.com/fguzman-stack/CodePet/actions/workflows/android.yml"><img src="https://img.shields.io/github/actions/workflow/status/fguzman-stack/CodePet/android.yml?style=for-the-badge&logo=githubactions&logoColor=white&label=Build" alt="Estado del build"/></a>
  </p>
  <p>
    <img src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=flat-square&logo=android&logoColor=white"/>
    <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Jetpack_Compose-UI-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white"/>
    <img src="https://img.shields.io/badge/Material_3-Material_You-6750A4?style=flat-square&logo=materialdesign&logoColor=white"/>
    <img src="https://img.shields.io/badge/Room-SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white"/>
    <img src="https://img.shields.io/badge/Koin-4.0.2-0099E5?style=flat-square&logo=koin&logoColor=white"/>
    <img src="https://img.shields.io/badge/DataStore-Jetpack-FF6F00?style=flat-square&logo=android&logoColor=white"/>
  </p>
  <br/>
  <pre>> ./code-tamagotchi --status
> <b>Status:</b> <code>ACTIVE</code>  <b>Platform:</b> <code>ANDROID</code>  <b>Language:</b> <code>KOTLIN / COMPOSE</code></pre>
  <p><b>Code Tamagotchi</b> transforma el estudio de programación en una experiencia de cuidado, progreso y recompensas. Resuelve desafíos, completa sesiones Pomodoro, gana Bytes y mantén a <b>Codey</b> feliz, saludable y listo para compilar.</p>
  <a href="#-instalación"><img src="https://img.shields.io/badge/▶_Empezar_a_jugar-81C784?style=for-the-badge&logo=android&logoColor=white" alt="Empezar a jugar"/></a>
  <a href="#-características"><img src="https://img.shields.io/badge/✨_Explorar_funciones-7C4DFF?style=for-the-badge&logo=github&logoColor=white" alt="Explorar funciones"/></a>
</div>

<p align="center">
  <b>Español</b> · <a href="README.md">🇺🇸 English</a>
</p>

---

## 📋 Navegación

<div align="center">
  [✨ Características](#-características) ·
  [📸 Capturas](#-capturas) ·
  [🛠️ Tecnologías](#️-tecnologías) ·
  [🏗️ Arquitectura](#️-arquitectura) ·
  [⚙️ Mecánicas](#-mecánicas-de-juego-en-código-real) ·
  [💾 Persistencia](#-persistencia--room-v5) ·
  [🧩 Sistemas vivos](#-sistemas-vivos-petviewmodel) ·
  [⏰ Trabajo en segundo plano](#-trabajo-en-segundo-plano-workmanager) ·
  [🔌 DI](#-inyección-de-dependencias-koin) ·
  [🖌️ Renderizado](#-renderizado-procedural) ·
  [📱 Widget](#-widget-para-pantalla-de-inicio) ·
  [🧪 Pruebas](#-pruebas) ·
  [⚡ Quick Start](#-quick-start) ·
  [🎮 Cómo jugar](#-cómo-jugar) ·
  [🎨 Temas visuales](#-temas-visuales) ·
  [🌍 Localización](#-localización) ·
  [🗺️ Roadmap](#️-roadmap) ·
  [🤝 Contribuir](#-contribuir)
</div>

---

## ✨ Características

<table>
<tr>
<td width="50%" valign="top">

### 🐾 Mascota virtual
- Estados emocionales dinámicos (HAPPY, SLEEPING, STUDYING, SICK, HUNGRY, SAD, EXCITED, DEAD)
- Salud, hambre y energía en tiempo real con decaimiento progresivo
- ☠️ **Sistema de muerte y revivir** — si la salud llega a 0, Codey aparece bajo una lápida y puedes revivirlo con Bytes (o gratis con stats bajos)
- Animaciones según el estado (respiración, temblor, pulso de hambre, parpadeo)
- Sistema de nivel, XP y Bytes (moneda virtual)
- Racha diaria de estudio y recompensas offline
- 🏅 **Evolución visual** — 5 etapas: Huevo → Cría → Adulto → Veterano → Legendario
- 😊 **Sistema de Moodlets** — eventos aleatorios que afectan el humor por horas
- 👫 **Pair Programming** — Buggy aparece aleatoriamente y multiplica XP ×1.5

</td>
<td width="50%" valign="top">

### 💻 Aprende programando
- Retos de **Kotlin**, **JavaScript**, **PHP** y **Python**
- Preguntas tipo trivia y debugging
- Retos especiales de algoritmos que desbloquean temas visuales
- Feedback educativo en cada respuesta con explicaciones de Codey
- Recompensas por progreso y precisión
- **96 desafíos de programación integrados** (90 normales + 6 especiales)

</td>
</tr>
<tr>
<td width="50%" valign="top">

### ⏱️ Pomodoro integrado
- Sesiones de 15, 25 o 50 minutos
- Bitácora persistente de estudio
- XP y Bytes por cada minuto
- Bonificación para sesiones de 25+ minutos (+50 Bytes, +75 XP)
- 8 temas de estudio: Kotlin, JavaScript, PHP, Python, SQL, Clean Code, Git, Estructuras de Datos
- Reanudación automática de sesiones activas al abrir la app

</td>
<td width="50%" valign="top">

### 🕹️ Arcade de Depuración
- 🐛 **Bug Hunt — Terminal Panic:** Encuentra bugs en fragmentos de código
- 🐙 **Git Rescue:** Decisiones Git para salvar un repo en llamas
- 🔧 **Refactor Rush:** Ordena bloques de código para que compilen
- 📝 **Code Review:** Identifica errores en 10 snippets multi-lenguaje
- 🏆 **Hackathon:** Evento semanal con 3 problemas de dificultad progresiva
- Explicaciones con humor de Codey en cada ronda
- Cooldown diario para evitar farm de moneda
- 🎮 Arcade clásico (Adivina el Bit, Caza de Bugs, Servidor/Script/Hacker)

</td>
</tr>
</table>

### ⚙️ Funcionalidades extra
- 🔇 **DND Mode** — Modo concentración que silencia notificaciones y da +10% XP
- 🌳 **Skill Tree** — Árbol de habilidades con 3 ramas y 9 nodos desbloqueables
- 🎫 **Season Pass** — Pase de batalla de 30 días con 20 niveles
- 📋 **Weekly Missions** — 3 misiones rotativas cada lunes con recompensas
- 📱 **Widget para pantalla de inicio** — mira a Codey con barras de vida, energía y hambre (compacto 2×2, redimensionable)

---

## 📸 Capturas

| Inicio | Retos | Pomodoro |
|:---:|:---:|:---:|
| <img src="docs/screenshots/01-home.png" width="220"/> | <img src="docs/screenshots/02-learn.png" width="220"/> | <img src="docs/screenshots/03-focus.png" width="220"/> |
| **Arcade** | **Tienda** | **Misiones Semanales** |
| <img src="docs/screenshots/04-games.png" width="220"/> | <img src="docs/screenshots/05-shop.png" width="220"/> | <img src="docs/screenshots/06-missions.png" width="220"/> |
| **Temas** | **Skill Tree** | **Season Pass** |
| <img src="docs/screenshots/07-themes.png" width="220"/> | <img src="docs/screenshots/08-skilltree.png" width="220"/> | <img src="docs/screenshots/09-seasonpass.png" width="220"/> |

> 🎨 12 temas premium con fondos animados Canvas únicos. 🐾 Mira a Codey evolucionar de Huevo a Legendario mientras estudias.

---

## ⚡ Quick Start

```bash
# Clonar
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet

# Compilar APK de desarrollo
./gradlew clean assembleDebug

# Instalar en dispositivo/emulador
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

El proyecto usa Gradle 9.x con Gradle Wrapper. **Requiere:** JDK 17+ y Android SDK (API 36). La app es **100% offline** — sin API keys, cuentas ni secretos para compilar o jugar.

**O simplemente descarga el [APK prediseñado desde Releases](https://github.com/fguzman-stack/CodePet/releases/latest)** — no necesitas herramientas de compilación.

---

## 🔄 Bucle de progreso

```
   ESTUDIA             RESUELVE              GANA
+------------+     +------------+      +------------+
| Pomodoro   | --> | Retos dev  | ---> | XP + Bytes |
+------------+     +------------+      +-----+------+
                                              |
                                              v
                                       +------------+
                                       | CUIDA A    |
                                       |   CODEY    |
                                       +-----+------+
                                             |
                                             v
                                       DESBLOQUEA TEMAS
```

> Cada sesión terminada fortalece a tu mascota. Cada reto correcto acelera su evolución.

---

## 😊 Estados de Codey

| Estado | Condición principal | Comportamiento |
|:-------|:--------------------|:---------------|
| 😊 `HAPPY` | Valores equilibrados | Flotación suave, parpadeo |
| 😴 `SLEEPING` | El usuario activa descanso | Recupera energía (+12/h), fondo de respiración |
| 📚 `STUDYING` | Pomodoro en curso | Concentrado en aprender |
| 🤒 `SICK` | Salud menor a 20% | Tiembla y necesita cuidado |
| 🍽️ `HUNGRY` | Hambre menor a 20% | Pulso de alerta |
| 😢 `SAD` | Energía menor a 15% | Baja actividad |
| 😆 `EXCITED` | Felicidad máxima | Rebote rápido |
| ☠️ `DEAD` | Salud llega a 0 | Robot tumbado junto a una lápida (dibujado por código, sin animación), diálogo de revivir |

> El estado se resuelve como **cadena de prioridad** en `StatusCalculator.determineStatus()` — mira las [mecánicas](#-mecánicas-de-juego-en-código-real).

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|:-----------|:----|
| **Kotlin 2.2.10** | Lenguaje principal |
| **Jetpack Compose** | UI declarativa con animaciones nativas |
| **Material 3** | Componentes y sistema visual (Material You) |
| **Room v5** | Persistencia local SQLite con migraciones |
| **DataStore** | Preferencias de usuario, temas, cooldowns |
| **ViewModel + StateFlow** | Estado reactivo y ciclo de vida |
| **Coroutines + Flow** | Procesos asíncronos y flujos de datos |
| **Koin 4.0.2** | Inyección de dependencias |
| **KSP** | Procesamiento de anotaciones en tiempo de compilación |
| **WorkManager** | Tareas periódicas (decaimiento cada 4h) |
| **Roborazzi** | Pruebas visuales por capturas |

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM + Repository** con inyección de dependencias via **Koin**.

```
+------------------------------------------------------------------+
|              🖥️ PRESENTACIÓN (UI Layer)                          |
|  Compose Screens · Material 3 · StateFlow · Animations           |
|  HomeScreen | LearnScreen | FocusScreen | ShopScreen             |
|  GamesScreen | BugHuntScreen | SettingsScreen | OnboardingScreen |
|  CodeReviewScreen | HackathonScreen                              |
+------------------------------------------------------------------+
|              🧠 DOMINIO (ViewModel)                              |
|  PetViewModel                                                     |
|    - Pet state management                                         |
|    - Study timer logic (start/cancel/complete)                    |
|    - Challenge submission & reward calculation                    |
|    - Shop purchases & skin equipping                                |
|    - Theme switching & unlock                                     |
|    - Game cooldowns                                               |
|    - Offline reward detection                                     |
|    - DND Mode, Moodlets, Pair Buddy, Skill Tree                   |
|    - Seasonal events: Hackathon, Weekly Missions, Season Pass     |
+------------------------------------------------------------------+
|              📊 DATOS (Data Layer)                                |
|  PetRepository | UserPreferencesRepository | AchievementsRepository|
|  DecayCalculator | RewardCalculator | LevelCalculator             |
|  StatusCalculator | SoundManager                                   |
+------------------------------------------------------------------+
|              💾 PERSISTENCIA (Storage Layer)                      |
|  Room / SQLite                                                    |
|    - PetStateEntity | StudySessionEntity | FocusSessionEntity     |
|    - CodeCardEntity | QuestEntity | OwnedItemEntity              |
|    - LanguageProgressEntity | ActivityLogEntity                  |
|  DataStore Preferences                                            |
|    - Onboarding, theme, difficulty, sound, motion, DND, colors   |
+------------------------------------------------------------------+
```

**Flujo de datos:**
```
Usuario → Composable (Screen) → ViewModel → Repository → DAO → SQLite
                ^                        |
                +------- StateFlow ------+
```

### Estructura del proyecto

```
CodePet/
├── app/src/main/java/com/tamagotchi/code/
│   ├── CodeTamagotchiApp.kt          # Application (Koin, canales, 3 workers, observador del widget)
│   ├── SplashActivity.kt             # Intro de 2s -> MainActivity
│   ├── MainActivity.kt               # Entry point (Theme, Nav)
│   ├── data/
│   │   ├── ChallengesData.kt         # 90 retos de código
│   │   ├── ChallengesDataEn.kt       # Espejo EN indexado por id estable
│   │   ├── SpecialChallengesData.kt  # 6 retos de algoritmos/arquitectura (+En)
│   │   ├── CodeReviewData.kt         # Code review snippets (+ CodeReviewDataEn)
│   │   ├── CodeCards.kt              # 40 tarjetas de trivia (+ CodeCardsEn)
│   │   ├── LocalizedContent.kt       # Extensiones localized() por tipo de contenido
│   │   ├── PersonalityMissions.kt    # Mission definitions
│   │   ├── database/
│   │   │   ├── AppDatabase.kt        # Room DB (v5, migrations)
│   │   │   ├── PetDao.kt             # DAO interface
│   │   │   ├── PetStateEntity.kt     # Pet entity
│   │   │   ├── ExpansionEntities.kt  # CodeCard, Quest, OwnedItem, ActivityLog entities
│   │   │   ├── StudySessionEntity.kt # Study log entity
│   │   │   └── FocusSessionEntity.kt # Pomodoro session entity
│   │   └── repository/
│   │       ├── PetRepository.kt      # Pet state CRUD
│   │       ├── UserPreferencesRepository.kt  # DataStore prefs
│   │       └── AchievementsRepository.kt     # Achievement system
│   ├── di/AppModule.kt               # Koin DI module
│   ├── navigation/AppNavigation.kt   # NavHost, Routes, BottomNav
│   ├── feature/
│   │   ├── home/HomeScreen.kt        # Pet viewport & stats
│   │   ├── learn/LearnScreen.kt      # Quiz & special challenges
│   │   ├── focus/FocusScreen.kt      # Pomodoro timer & logs
│   │   ├── shop/ShopScreen.kt        # In-game store
│   │   ├── onboarding/OnboardingScreen.kt  # 4-step intro
│   │   ├── games/
│   │   │   ├── GamesScreen.kt        # Game hub
│   │   │   ├── BugHuntScreen.kt      # Bug hunting mini-game
│   │   │   ├── GitRescueScreen.kt    # Git decision game
│   │   │   ├── RefactorRushScreen.kt # Code ordering game
│   │   │   ├── CodeReviewScreen.kt   # Code review mini-game
│   │   │   ├── HackathonScreen.kt    # Weekly hackathon event
│   │   │   ├── BinaryGuessGame.kt    # Classic arcade: Guess the Bit
│   │   │   ├── BugSmasherGame.kt     # Classic arcade: Bug Catch
│   │   │   ├── RockPaperSciGame.kt   # Classic arcade: Server/Script/Hacker
│   │   │   └── MinigamesDialog.kt    # Arcade launcher dialog
│   │   ├── settings/
│   │   │   ├── SettingsScreen.kt     # Full settings (theme, etc.)
│   │   │   ├── SettingsLanguageScreen.kt # Languages & focus duration
│   │   │   └── AboutScreen.kt        # Credits & version
│   │   └── skills/
│   │       ├── SeasonPassScreen.kt   # Season Pass
│   │       ├── SkillTreeScreen.kt    # Skill tree progression
│   │       └── WeeklyMissionsScreen.kt # Weekly mission board
│   ├── ui/
│   │   ├── viewmodel/PetViewModel.kt # cerebro central del juego (~1,500 líneas)
│   │   ├── components/
│   │   │   ├── ViewportCard.kt       # Main pet card
│   │   │   ├── MeterItem.kt          # Stat progress bars
│   │   │   ├── CodeySprite.kt        # Renderizado procedural de la mascota (Canvas)
│   │   │   ├── CodeyBitmap.kt        # Mismo renderer como Bitmap para el widget
│   │   │   ├── AnimatedThemeBackground.kt  # 12 animated backgrounds
│   │   │   └── PairBuddy.kt          # Pair programming buddy UI
│   │   └── theme/
│   │       ├── ThemeConfig.kt        # AppTheme data class + 12 themes
│   │       ├── Theme.kt              # Compose theme bridge
│   │       └── Type.kt               # Typography builder
│   ├── widget/
│   │   ├── CodePetWidgetProvider.kt  # RemoteViews + bitmap procedural
│   │   └── WidgetUpdateWorker.kt     # refresco horario
│   └── util/
│       ├── DecayCalculator.kt        # Time-based stat decay
│       ├── RewardCalculator.kt       # XP/Byte reward math
│       ├── LevelCalculator.kt        # Level progression
│       ├── StatusCalculator.kt       # Emotional state logic
│       ├── SoundManager.kt           # Sound effects
│       ├── DailyCommitGenerator.kt   # "Codey writes commits" flavor
│       ├── CommitWorker.kt           # Daily commit worker
│       ├── PetCheckWorker.kt         # Periodic decay worker
│       └── ContentKeys.kt            # claves de texto dinámico localizado
├── app/src/test/                     # tests JVM + Robolectric + Roborazzi
└── gradle/libs.versions.toml         # catálogo de versiones Gradle
```

---

## ⚙️ Mecánicas de juego en código real

> Todos los fragmentos de abajo están copiados literalmente del fuente — nada de pseudo-código.

### Decaimiento de stats (`util/DecayCalculator.kt`)

El decaimiento es **a prueba de offline**: nada corre en un timer mientras no estás. Cuando la app (o `PetCheckWorker`) despierta, el tiempo transcurrido desde `lastUpdated` se convierte en horas y se aplica en una sola pasada:

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

| Regla | Efecto |
|:--|:--|
| Decaimiento despierto | hambre **−2.5/h** · energía **−2/h** · salud **−1.5/h** |
| Dormido | energía **+12/h** (despierta solo al llegar a 100), hambre solo −1/h |
| Inanición (hambre = 0) | salud extra −3/h |
| Agotado (energía ≤ 10) | salud extra −1/h |
| Sin estudiar 72 h | salud extra −1.5/h |
| Sin estudiar 48 h | la racha vuelve a 0 |
| Salud ≤ 0 | `isDead = true`, estado `DEAD` |

### Resolución de estado (`util/StatusCalculator.kt`)

Una cadena de prioridad — gana la primera coincidencia, por eso una mascota dormida nunca se muestra como hambrienta:

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

### Economía (`util/RewardCalculator.kt`)

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

Las rachas usan **días calendario**, no ventanas móviles de 24 h:

```kotlin
val studiedYesterday = lastCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
        lastCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)
newStreak = if (studiedYesterday) currentStreak + 1 else 1
```

### Curva de nivel (`util/LevelCalculator.kt`)

```kotlin
var level = 1
var requiredXp = 100
while (xp >= requiredXp) {
    level++
    requiredXp += level * 100
}
```

XP acumulado: **L2 = 100 · L3 = 300 · L4 = 600 · L5 = 1000 · L6 = 1500** — cada nivel nuevo cuesta `nivel × 100` XP, así las etapas de evolución se sienten ganadas.

---

## 💾 Persistencia — Room v5

Ocho entidades, cinco versiones, cuatro migraciones explícitas:

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

| Migración | Qué hace (SQL real en `AppDatabase.kt`) |
|:--|:--|
| `1 → 2` | `CREATE TABLE focus_sessions` — estado vivo del timer Pomodoro |
| `2 → 3` | `ALTER TABLE pet_state ADD hasRenamed` |
| `3 → 4` | `ALTER TABLE pet_state ADD isDead` — sistema de muerte |
| `4 → 5` | `code_cards`, `quests`, `owned_items`, `language_progress`, `activity_logs` |

La fila de la mascota es un **patrón singleton** (`WHERE id = 1 LIMIT 1`), expuesto de forma reactiva:

```kotlin
@Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
fun getPetState(): Flow<PetStateEntity?>

@Transaction
suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
    updateFocusSessionStatus(sessionId, status)
    insertOrUpdatePetState(petState)
}
```

Campos de `PetStateEntity`: `name · language · level · xp · hunger · health · energy · bytes · streak · lastUpdated · lastStudyDate · currentStatus · hasRenamed · isDead`. Todo lo no crítico (tema, DND, cooldowns, moodlets, skill tree, season pass) vive en **DataStore Preferences** en lugar de forzar un cambio de esquema.

---

## 🧩 Sistemas vivos (PetViewModel)

### 😊 Moodlets — 6 eventos aleatorios

```kotlin
val eventTypes = listOf("bug_prod", "tabs", "spaces", "bad_commit", "clean_code", "spilled_coffee")
val type = eventTypes.random()
val duration = when (type) {
    "bug_prod" -> 2L
    "spilled_coffee" -> 1L
    "bad_commit" -> 2L
    "clean_code" -> 3L
    else -> 1L
} // horas, persistido con timestamp de expiración
```

Tirada: cada 30 minutos, 5% de probabilidad (`checkAndTriggerMoodlet`).

### 👫 Pair Programming Buddy

```kotlin
fun checkAndActivatePairBuddy() {
    if (pairBuddyState.value != null) return
    if (kotlin.random.Random.nextFloat() < 0.2f) activatePairBuddy()
}
// PairBuddyState(name = "Buggy", remainingChallenges = 3, xpMultiplier = 1.5f)
```

### 🌳 Skill Tree — XP que se quema, no se gasta

```kotlin
val cost = (node.currentTier + 1) * 100   // tier 1 = 100 XP, tier 2 = 200 XP...
if (pet.xp < cost) return@launch
val updated = pet.copy(xp = pet.xp - cost)
```

3 ramas × 3 nodos, cada nodo hasta `maxTier = 3`.

### 🎫 Season Pass

```kotlin
val newXp = current.xp + xp
val newLevel = current.level + (newXp / 100)
val remainingXp = newXp % 100
val updated = current.copy(xp = remainingXp, level = newLevel.coerceAtMost(20)) // luego persistido en DataStore
```

### 📋 Weekly Missions y 🏆 Hackathon

```kotlin
WeeklyMissionData("wm1", ..., 100, 50)   // XP, Bytes
WeeklyMissionData("wm2", ..., 200, 100)
WeeklyMissionData("wm3", ..., 150, 75)
// HackathonData(attempts, bestTimeMs, expiresAt = now + 48h)
```

Los títulos se resuelven por id estable al mostrarse (`LocalizedGameText.kt`), así el texto de misiones se traduce sin tocar el estado persistido.

### 🔇 Detección de DND — API real de Android

```kotlin
val filter = notificationManager.currentInterruptionFilter
isDndActive.value = filter == INTERRUPTION_FILTER_PRIORITY ||
        filter == INTERRUPTION_FILTER_NONE || filter == INTERRUPTION_FILTER_ALARMS
```

Cuando está activo: notificaciones silenciadas y **+10% XP** en recompensas de estudio.

### ☠️ Muerte y revivir

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

| Ruta | Coste | Stats restauradas |
|:--|:--|:--|
| `reviveWithBytes()` | precio en Bytes (dinámico) | 50 / 50 / 50 |
| `reviveForFree()` | 0 Bytes | 40 / 40 / 40 — la ruta de misericordia "reset duro" |

---

## ⏰ Trabajo en segundo plano (WorkManager)

Todos los jobs se programan desde `CodeTamagotchiApp.onCreate()`:

| Job | Programa | Política | Notas |
|:--|:--|:--|:--|
| `pet_check` | cada **4 h** | `KEEP` | `setRequiresBatteryNotLow(true)`, retardo inicial 2 h |
| `widget_update` | cada **1 h** | `KEEP` | mantiene frescos los medidores sin abrir la app |
| `daily_commit` | one-shot a **medianoche** | `REPLACE` | se reprograma solo para la medianoche siguiente |

```kotlin
val request = PeriodicWorkRequestBuilder<PetCheckWorker>(4, TimeUnit.HOURS)
    .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
    .setInitialDelay(2, TimeUnit.HOURS)
    .build()
WorkManager.getInstance(this)
    .enqueueUniquePeriodicWork("pet_check", ExistingPeriodicWorkPolicy.KEEP, request)
```

`PetCheckWorker` enruta las alertas por severidad a **5 canales de notificación** (agrupados bajo `pet_care`):

```kotlin
val channelId = when {
    petState.health < 5f || petState.hunger < 5f || petState.energy < 5f -> "pet_critical"
    petState.hunger < 20f -> "pet_hunger"
    petState.health < 20f -> "pet_health"
    petState.energy < 15f -> "pet_energy"
    else -> "pet_care_reminder"
}
```

El truco del widget: la **Application observa Room**, no el ciclo de vida de una pantalla, así que hasta las escrituras en segundo plano (workers) refrescan el launcher al instante:

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

## 🔌 Inyección de dependencias (Koin)

`di/AppModule.kt` — el grafo completo, 19 líneas:

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

Las pantallas lo obtienen con `koinViewModel()` — hay exactamente **un** cerebro de juego.

---

## 🖌️ Renderizado procedural

**Cero PNGs** para Codey. El estado persistido (String) se mapea a un enum y un solo `Canvas` dibuja todo:

```kotlin
internal enum class PetMood { HAPPY, SLEEPING, STUDYING, SICK, HUNGRY, SAD, EXCITED, DEAD }

internal fun codeyMood(status: String, isDead: Boolean = false): PetMood =
    if (isDead) PetMood.DEAD
    else PetMood.entries.firstOrNull { it.name == status } ?: PetMood.HAPPY
```

El renderizador es una caja de herramientas de extensiones `DrawScope`, todas en `CodeySprite.kt`:

`drawEgg` · `drawRobot` · `drawVisorFace` · `drawCore` · `drawAntenna` · `drawWing` · `drawLimb` · `drawBackpack` · `drawBadge` · `drawPropellerCap` · `drawHeadphones` · `drawCircuitCrown` · `drawEnergyCape` · `drawEvolutionBurst` · `drawDeadScene` · `drawTombstone` · `drawGhost`

Decisiones de diseño:
- Las **5 etapas de evolución** cambian `stagePalette()` y añaden piezas — huevo → robot bebé → … → corona de circuitos y capa de energía en Legendario
- Parpadeo, respiración, temblor y pulso de hambre salen de `rememberInfiniteTransition`, y todos respetan la preferencia **Reduce Motion**
- El **color de acento** del usuario (en DataStore, cuesta 30 Bytes) tiñe visor y núcleo — no hay dos mascotas idénticas
- La escena DEAD también está dibujada por completo: lápida + fantasma, deliberadamente estática

---

## 📱 Widget para pantalla de inicio

`RemoteViews` no puede ejecutar Compose, así que el widget renderiza el **mismo** Codey procedural a un `Bitmap` vía `CodeyBitmap.kt`:

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

Compacto 2×2, redimensionable, el tap abre `MainActivity`. `pixelMode` cambia el renderer a 8-bit cuando el tema **Retro Pixel** está activo. Cubierto por `CodePetWidgetRenderingTest` en JVM — sin emulador.

---

## 🧠 API pública de PetViewModel

| Método | Responsabilidad |
|:--|:--|
| `submitAnswer(optionIndex)` | corrección, recompensas, tiradas de moodlet/buddy, XP season pass, progreso de quests e idiomas |
| `startStudyTimer / completeFocusSession / cancelStudyTimer` | ciclo de vida Pomodoro + persistencia + auto-reanudación |
| `buyShopItem / buySkin / equipSkin` | economía de tienda, equipamiento con `@Transaction` |
| `petThePet / cleanThePet / toggleSleep` | acciones de cuidado (alimentan quests y commits diarios) |
| `canPlayGame / recordGamePlay` | cooldowns de 24 h del arcade |
| `checkDailyRewardEligibility / claimDailyReward` | recompensas offline y diaria |
| `loadMoodlet / triggerRandomMoodlet` | sistema de moodlets |
| `unlockSkillNode` | progresión del skill tree (quema de XP) |
| `addSeasonPassXp` | leveling del battle pass |
| `completeWeeklyMission` | tablero de misiones con reset los lunes |
| `submitHackathonSolution / consumeHackathonAttempt` | evento semanal de 48 h |
| `checkDeathState / reviveWithBytes / reviveForFree` | muerte y revivir |
| `checkDndMode` | sonda del interruption-filter del sistema |

---

## 🧪 Pruebas

| Test | Qué fija |
|:--|:--|
| `StatusCalculatorTest` | cadena de prioridad de estados y umbrales |
| `GameEconomyTest` | matemática de recompensas, cooldowns, validación de ajustes |
| `LocalizedContentTest` | paridad de ids ES↔EN en los 90+6 retos, 10 snippets y 40 cards |
| `CodeyRendererTest` / `CodeySpriteTest` | renderer procedural en cada etapa y estado de ánimo |
| `PetAnimationConfigTest` | configuración de animación por estado |
| `ThemeContrastTest` | contraste legible en los 12 temas |
| `CodePetWidgetRenderingTest` | bitmap del widget + construcción de RemoteViews |
| `GreetingScreenshotTest` | golden Roborazzi (`app/src/test/screenshots/greeting.png`) |

```bash
./gradlew :app:testDebugUnitTest          # todas las pruebas JVM/Robolectric
./gradlew :app:testDebugUnitTest --tests "com.tamagotchi.code.StatusCalculatorTest"
./gradlew :app:verifyRoborazziDebug       # regresión visual
./gradlew :app:lintDebug                  # lint de Android
```

---

## 🎮 Cómo jugar

| Paso | Acción | Recompensa |
|:----:|:-------|:-----------|
| `01` | Nombra a tu mascota durante el onboarding | Comienzo de la aventura |
| `02` | Completa retos en **LEARN** | XP, Bytes, salud y alimento |
| `03` | Inicia un Pomodoro en **STUDY** | XP y Bytes por minuto |
| `04` | Compra recursos en **SHOP** | Recupera estadísticas |
| `05` | Juega minijuegos | Bytes y bonificaciones |
| `06` | Mantén la racha diaria | Progreso constante |
| `07` | Resuelve retos especiales | Desbloquea nuevos temas visuales |

### Economía rápida

| Actividad | Bytes | XP |
|:----------|:-----:|:--:|
| Reto normal | +25 a +30 | +20 a +25 |
| Reto especial | +50 | — |
| Estudio por minuto | +2 | +3 |
| Bonus de sesión 25+ min | +50 | +75 |
| Code Review | Hasta +60 | — |
| Hackathon (completo) | +180 | +175 |

---

## 🎨 Temas visuales

Code Tamagotchi incluye **12 temas premium** con personalidad única. Cada uno configura colores, tipografía, esquinas, gradientes y un fondo animado Canvas único.

| Tema | Personalidad |
|:-----|:-------------|
| 🖥️ Matrix Green | Terminal hacker, monospace puro |
| 🌌 Galáctico | Púrpuras profundos y destellos cósmicos |
| 🌆 Cyberpunk | Rosa eléctrico y cian contra la oscuridad |
| 🌸 Sakura | Elegancia japonesa en rosa suave |
| ⚪ Minimalista | Blanco puro con acentos sutiles |
| 💡 Neón | Oscuridad total con destellos vibrantes |
| 🌊 Océano | Azules profundos, calma submarina |
| 🌋 Volcánico | Fuego bajo la superficie |
| ⚔️ Samurai | Acero, sangre y oro antiguo |
| 🌌 Aurora | Luces del norte en el cielo oscuro |
| 🌙 Nocturno | Noche elegante estilo iOS |
| 🕹️ Retro Pixel | **TEMA FINAL** — 8-bit definitivo, pixel art |

> Selecciona desde un carrusel visual en **Configuración**. Resuelve desafíos especiales para desbloquear nuevos temas.

---

## 🌍 Localización

Code Tamagotchi es totalmente bilingüe (**español + inglés**) y sigue el **idioma del sistema** automáticamente — no hay selector dentro de la app:

- ✅ UI, diálogos, textos de juegos, notificaciones y widget: `values/` (es) + `values-en/` (en)
- ✅ Contenido educativo: 90 retos de código, 6 desafíos de algoritmos, 10 snippets de Code Review y 40 code cards traducidos al inglés (emparejados por id estable, verificado en `LocalizedContentTest`)
- ⚠️ Las etiquetas de los **canales de notificación** (Ajustes de Android) se crean una sola vez con el idioma activo en la primera instalación
- ⚠️ Los nombres de los temas se muestran en español por diseño (son identificadores estables persistidos en preferencias)

Las traducciones viven junto a los datos originales: `ChallengesDataEn.kt`, `SpecialChallengesDataEn.kt`, `CodeReviewDataEn.kt`, `CodeCardsEn.kt`. PRs para mejorar redacción (en cualquier idioma) son muy bienvenidas.

El mecanismo es una única extensión por tipo de contenido, resuelta al mostrarse — el español queda como fuente de verdad y la DB nunca guarda traducciones:

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

`LocalizedContentTest` verifica que cada id en español tiene su gemelo inglés y que el orden de opciones (y por tanto `correctAnswerIndex`) se conserva.

---

## 🗺️ Roadmap

- [x] 🐾 Mascota virtual con estados emocionales y decaimiento
- [x] 💻 90 retos + 6 especiales de programación (Kotlin, JS, PHP, Python)
- [x] ⏱️ Pomodoro persistente con reanudación automática
- [x] 🎨 12 temas premium con fondos animados Canvas
- [x] 🕹️ 6 minijuegos: Bug Hunt, Git Rescue, Refactor Rush, Code Review, Hackathon, Arcade Clásico
- [x] 🏅 Evolución visual de la mascota (5 etapas: Huevo → Legendario)
- [x] 😊 Sistema de Moodlets (6 eventos aleatorios)
- [x] 👫 Pair Programming Buddy (Buggy, XP ×1.5)
- [x] 🔇 DND Mode (modo concentración +10% XP)
- [x] 🌳 Skill Tree (3 ramas, 9 nodos desbloqueables)
- [x] 🎫 Season Pass (30 días, 20 niveles)
- [x] 📋 Weekly Missions (3 misiones, reset cada lunes)
- [x] 🏆 Logros locales y sistema de recompensas
- [x] 🔔 Notificaciones con personalidad (recordatorios y recompensas)
- [x] 📱 Widget para pantalla de inicio
- [ ] 🌐 Retos en Ruby, Go, Rust y Swift
- [ ] ☁️ Sincronización con Firebase Firestore
- [ ] 👥 Funcionalidad multijugador y rankings

---

## 🤝 Contribuir

1. Haz fork del repositorio
2. Crea una rama (`git checkout -b feature/nueva-funcionalidad`)
3. Haz commit de tus cambios (`git commit -m 'feat: agrega nueva funcionalidad'`)
4. Haz push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un [Pull Request](https://github.com/fguzman-stack/CodePet/pulls)

---

## 📄 Licencia

Bajo **licencia MIT** — que en cristiano significa que puedes:

- ✅ Usarlo para lo que quieras, incluido uso comercial
- ✅ Hacer fork y adaptarlo (retos de nuevos lenguajes, otras plataformas, uso en clases...)
- ✅ Traducir la UI a cualquier idioma y publicar tu fork
- ✅ Vender un servicio o derivado construido sobre CodePet

El único requisito es conservar el aviso de copyright y el texto de licencia en las copias del código. Sin CLA, sin copyleft — con atribución en el código basta. Si construyes algo chulo sobre CodePet, un saludo en los [Discussions](https://github.com/fguzman-stack/CodePet/discussions) siempre se agradece, pero no es obligatorio. 😉

---

<div align="center">
  <p>
    <a href="https://github.com/fguzman-stack/CodePet/stargazers">
      <img src="https://img.shields.io/github/stars/fguzman-stack/CodePet?style=for-the-badge&logo=github&color=FFD54F" alt="Estrellas del repositorio"/>
    </a>
    <a href="https://github.com/fguzman-stack/CodePet/fork">
      <img src="https://img.shields.io/github/forks/fguzman-stack/CodePet?style=for-the-badge&logo=github&color=64B5F6" alt="Forks del repositorio"/>
    </a>
  </p>
  <p><b>Code Tamagotchi v3.0</b> — Donde los bugs se convierten en mascotas y el código en cariño.</p>
  <p>¿Preguntas? Abre un <a href="https://github.com/fguzman-stack/CodePet/issues">issue</a> o consulta la <a href="documentacion.md">documentación completa</a>.</p>
</div>
