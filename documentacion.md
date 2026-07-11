# Documentación — Code Tamagotchi

> **Versión:** 2.0.0  
> **Plataforma:** Android (API 24+)  
> **Lenguaje:** Kotlin 2.2.10  
> **UI:** Jetpack Compose + Material 3  
> **Persistencia:** Room (SQLite) + DataStore (Preferences)  
> **DI:** Koin 4.0.2  
> **Arquitectura:** MVVM + Repository + Feature Modules  
> **Package:** `com.tamagotchi.code`

---

## Índice

1. [Origen e Inspiración](#1-origen-e-inspiración)
2. [Concepto y Propósito](#2-concepto-y-propósito)
3. [Arquitectura General](#3-arquitectura-general)
4. [Flujo Completo de la App](#4-flujo-completo-de-la-app)
5. [Sistema de la Mascota](#5-sistema-de-la-mascota)
6. [Módulos y Funcionalidades](#6-módulos-y-funcionalidades)
7. [Base de Datos y Persistencia](#7-base-de-datos-y-persistencia)
8. [Sistema de Temas](#8-sistema-de-temas)
9. [Sistema de Retos (88 challenges)](#9-sistema-de-retos)
10. [Minijuegos](#10-minijuegos)
11. [Sistema de Sonido](#11-sistema-de-sonido)
12. [Estados y Animaciones](#12-estados-y-animaciones)
13. [Notificaciones y Widgets](#13-notificaciones-y-widgets)
14. [Inyección de Dependencias (Koin)](#14-inyección-de-dependencias)
15. [Pruebas](#15-pruebas)
16. [Dependencias Externas](#16-dependencias-externas)
17. [Guía para Desarrolladores](#17-guía-para-desarrolladores)
18. [Changelog vs v1.0](#18-changelog)

---

## 1. Origen e Inspiración

### 1.1 Genesis

Code Tamagotchi nació como un proyecto generado inicialmente por **Google AI Studio**, un asistente de IA para prototipado rápido de apps Android. El código base original fue creado con fines educativos, combinando el concepto clásico de mascota virtual (Tamagotchi) con elementos de productividad para programadores.

### 1.2 Inspiraciones Directas

| Inspiración | Elemento adoptado |
|:------------|:-----------------|
| **Tamagotchi original (Bandai, 1996)** | Mascota que requiere cuidado constante, estados emocionales, decaimiento por abandono |
| **SUSH** | Menú limpio centrado en la mascota, animación de rebote al tocar, corazón flotante, fondo personalizable |
| **Duolingo** | Sistema de rachas (streaks), gamificación del aprendizaje |
| **Pomodoro Technique** | Temporizador de estudio con intervalos de 15/25/50 minutos |
| **Terminales hacker / Matrix** | Estética visual: fondos oscuros, tipografía monospace, colores verde neón, decoración tipo terminal |
| **Tamagotchi virtuales modernos** | Estados emocionales con frases contextuales, sistema de alimentación y limpieza |

### 1.3 Propósito

Code Tamagotchi busca **gamificar el hábito de estudio en programación**. A diferencia de otras mascotas virtuales, aquí el progreso real del usuario (resolver retos de código, estudiar temas técnicos) se refleja directamente en la salud y felicidad de la mascota.

### 1.4 Público Objetivo

- Estudiantes de programación que quieren mantener constancia
- Desarrolladores que disfrutan del humor técnico
- Fans de mascotas virtuales con estética retro-tech
- Personas que responden bien a la gamificación para mantener hábitos

### 1.5 Estado Actual

La app está completamente funcional pero en fase **beta**:
- Builds: Solo debug (sin firma release configurada)
- Sin sistema de notificaciones ni widgets todavía

---

## 2. Concepto y Propósito

### 2.1 ¿Qué es Code Tamagotchi?

Es una **mascota virtual para programadores** que vive en tu celular. Tiene hambre, energía, salud, y estados de ánimo que cambian según cómo la trates. La diferencia con un Tamagotchi común: **para mantenerla feliz tienes que programar y estudiar**.

### 2.2 Ciclo Básico

```
Estudiar código → Ganas XP y Bytes → Mejoras a tu mascota (comida, medicina)
                                        ↓
                              La mascota sube de nivel
                                        ↓
                              Desbloqueas temas visuales
                                        ↓
                              Te motiva a seguir estudiando
```

### 2.3 Filosofía de Diseño

- **La mascota es el centro**: ocupa la mayor parte de la pantalla, todo lo demás son overlays
- **Estética terminal**: monospace, verde matrix, fondos oscuros, decoración de ventana de terminal
- **Humor técnico**: los mensajes de la mascota son chistes de programación
- **Feedback inmediato**: cada acción tiene una animación o sonido de respuesta
- **Castigo realista**: si no estudias por días, la mascota se enferma (como un Tamagotchi real)

---

## 3. Arquitectura General

### 3.1 Patrón MVVM + Feature Modules

```
┌──────────────────────────────────────────────────────────────┐
│                      VIEW (Compose)                           │
│  AppNavigation.kt · feature/{home,learn,focus,shop,games}/   │
│  Observa StateFlow del ViewModel · Emite eventos UI          │
├──────────────────────────────────────────────────────────────┤
│                      VIEWMODEL                                │
│  PetViewModel.kt                                              │
│  Lógica de negocio · Temporizador · Retos · Cuidados         │
│  DataStore (temas, onboarding) · Room (mascota, sesiones)    │
│  StateFlow + mutableStateOf para estado reactivo              │
│  @Inject constructor vía Koin                                 │
├──────────────────────────────────────────────────────────────┤
│                      REPOSITORY                               │
│  PetRepository.kt · UserPreferencesRepository.kt              │
│  Abstracción entre ViewModel y capa de datos                  │
│  Expone Flow<PetStateEntity>, Flow<List<StudySession>>, etc. │
├──────────────────────────────────────────────────────────────┤
│                     DATA (Room + DataStore)                   │
│  AppDatabase (singleton via Koin)                             │
│  PetDao (@Dao con queries reactivas)                          │
│  PetStateEntity · StudySessionEntity · FocusSessionEntity     │
│  DataStore Preferences (user_preferences)                     │
│  → UserPreferencesRepository                                  │
└──────────────────────────────────────────────────────────────┘
```

### 3.2 Flujo de Datos

```
👤 Usuario toca la pantalla
        ↓
🎨 Composable (HomeScreen → ViewportCard)
        ↓ llama
🧠 PetViewModel.petThePet()
        ↓
📦 PetRepository.savePetState(updated)
        ↓
💾 PetDao.insertOrUpdatePetState(entity)
        ↓
🗄️ Room SQLite (code_tamagotchi_db)

        ── Luego ──

💾 Room emite cambio → Flow<PetStateEntity?>
        ↓
📦 PetRepository.petState (Flow)
        ↓
🧠 PetViewModel (stateIn con WhileSubscribed)
        ↓
🎨 collectAsStateWithLifecycle() → recomposición automática
```

### 3.3 Mapa de Archivos

```
app/src/main/java/com/tamagotchi/code/
├── CodeTamagotchiApp.kt              ← Application class (inicia Koin)
├── MainActivity.kt                    ← Entry point, koinViewModel() + tema dinámico
├── data/
│   ├── ChallengesData.kt              ← 88 retos de programación (Kotlin, JS, PHP, Python)
│   ├── CodingChallenge.kt             ← data class del reto
│   ├── SpecialChallengesData.kt       ← 6 retos especiales (desbloquean temas)
│   ├── database/
│   │   ├── AppDatabase.kt             ← Room DB v2 con migración M1→2
│   │   ├── PetDao.kt                  ← DAO con queries reactivas (Flow)
│   │   ├── PetStateEntity.kt          ← Entidad: estado completo de la mascota
│   │   ├── StudySessionEntity.kt      ← Entidad: sesiones de estudio históricas
│   │   └── FocusSessionEntity.kt      ← Entidad: sesión activa del timer Pomodoro
│   └── repository/
│       ├── PetRepository.kt           ← Capa de datos mascota + sesiones + focus
│       ├── UserPreferencesRepository.kt ← DataStore (onboarding, tema, cooldown juegos)
│       └── AchievementsRepository.kt  ← DataStore para logros desbloqueados
├── di/
│   └── AppModule.kt                   ← Módulo Koin: BD, DAO, repos, ViewModel
├── navigation/
│   ├── AppNavigation.kt               ← Scaffold + bottom nav + 12 rutas
│   └── Screen.kt                      ← sealed class con 13 destinos
├── feature/
│   ├── home/
│   │   └── HomeScreen.kt              ← Tarjeta mascota + meters + diálogo offline
│   ├── learn/
│   │   └── LearnScreen.kt             ← Retos programación + especiales + temas
│   ├── focus/
│   │   └── FocusScreen.kt             ← Pomodoro persistente + bitácora
│   ├── shop/
│   │   └── ShopScreen.kt              ← Tienda con productos para la mascota
│   ├── games/
│   │   ├── GamesScreen.kt             ← Hub arcade con cooldown diario
│   │   ├── BugHuntScreen.kt           ← Encuentra bugs (10 snippets, explicaciones)
│   │   ├── GitRescueScreen.kt         ← Decisiones Git (8 escenarios, progreso rama)
│   │   ├── RefactorRushScreen.kt      ← Ordena bloques (12 puzzles, aleatorio)
│   │   ├── MinigamesDialog.kt         ← Arcade clásico
│   │   ├── BinaryGuessGame.kt         ← Adivina el bit (5 rondas)
│   │   ├── BugSmasherGame.kt          ← Caza bugs 3×3 (10 segundos)
│   │   └── RockPaperSciGame.kt        ← Servidor, Script, Hacker
│   ├── onboarding/
│   │   └── OnboardingScreen.kt        ← 4 pasos con HorizontalPager
│   └── settings/
│       ├── SettingsScreen.kt          ← Configuración con carrusel de temas
│       └── SettingsLanguageScreen.kt  ← Lenguaje principal + temas + dificultad
├── ui/
│   ├── components/
│   │   ├── MeterItem.kt               ← Barra de progreso animada
│   │   └── ViewportCard.kt            ← Card con mascota + meters + emociones
│   ├── theme/
│   │   ├── Color.kt                   ← Colores base Material 3
│   │   ├── Theme.kt                   ← MyApplicationTheme + LocalAppTheme + LocalReduceMotion
│   │   ├── ThemeConfig.kt             ← 12 temas premium con AppTheme
│   │   └── Type.kt                    ← buildTypography() dinámica por tema
│   └── viewmodel/
│       └── PetViewModel.kt            ← Toda la lógica de negocio (~820 líneas)
└── util/
    └── SoundManager.kt                ← Efectos de sonido con ToneGenerator
```

### 3.4 DI: Koin

La app usa **Koin** como framework de inyección de dependencias. El módulo `AppModule.kt` declara:

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

- `CodeTamagotchiApp` inicia Koin en `onCreate()`
- `MainActivity` obtiene el ViewModel con `koinViewModel()`
- `PetViewModel` recibe `PetRepository`, `UserPreferencesRepository` y `AchievementsRepository` por constructor

---

## 4. Flujo Completo de la App

### 4.1 Inicio (MainActivity.kt)

```
App abierta
    ↓
onCreate()
    ↓
enableEdgeToEdge()
    ↓
setContent {
    val appTheme = ThemeRegistry.getTheme(currentTheme.value)
    MyApplicationTheme(appTheme = appTheme, reduceMotion = ...) { ... }
}
    ↓
koinViewModel() → PetViewModel(PetRepository, UserPreferencesRepository, AchievementsRepository)
    ↓
init { } del ViewModel:
    ├── Leer DataStore: hasSeenOnboarding (suspend)
    ├── Colectar Flow: currentTheme → actualizar tema
    ├── Colectar Flow: unlockedThemes → actualizar temas
    ├── Colectar Flow: unlockedAchievements → actualizar logros
    ├── Colectar Flow: gameCooldowns → control farm de moneda
    ├── Room: leer petState
    │   ├── null  → crear PetStateEntity default → loadChallenges("Kotlin")
    │   └── exist → applyDecay(petState) → loadChallenges(petState.language)
    └── Room: buscar FocusSession activa
        ├── null  → nada
        ├── exist + tiempo restante → reanudar timer
        └── exist + tiempo agotado → recompensa offline + diálogo
    ↓
Composable:
    ├── hasSeenOnboarding == false → OnboardingScreen (4 pasos: presentación,
    │   explicación, elegir temas, nombrar mascota)
    │       ↓
    │   completeOnboarding(name, topics) → DataStore + Room
    │       ↓ → recomposición → hasSeenOnboarding = true → AppNavigation
    │
    └── hasSeenOnboarding == true → AppNavigation (HomeScreen por defecto)
```

### 4.2 Mapa de Navegación (Bottom Navigation)

```
┌──────────────────────────────────────────────────────────────────┐
│                        TOP BAR (Material 3)                       │
│           Code Tamagotchi                    [⚙️ Configuración]  │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│   ┌──────────────────────────────────────────────────────────┐   │
│   │                   HOME SCREEN                             │   │
│   │      (ViewportCard: mascota + meters + botones)           │   │
│   │  ┌──────────────────────────────────────────────────┐     │   │
│   │  │            🖼️ MASCOTA ANIMADA                    │     │   │
│   │  │  (bounce + ❤️ flotante al tocarla)               │     │   │
│   │  └──────────────────────────────────────────────────┘     │   │
│   │  ▓▓▓▓▓░░░ Salud    ▓▓░░░░░ Hambre  ▓▓▓░░░░ Energía       │   │
│   │  💰 Bytes   🔥 Racha   😴 Dormir   🧹 Limpiar            │   │
│   │  [Acariciar] [🎮 Jugar]                                    │   │
│   └──────────────────────────────────────────────────────────┘   │
│                                                                   │
├──────────────────────────────────────────────────────────────────┤
│  [🏠] Inicio  [💻] Aprender  [⏱️] Focus  [🛒] Tienda            │
└──────────────────────────────────────────────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Pantallas adicionales (acceso desde top bar o bottom nav):

┌──────────────────────────────────────────────────────────────────┐
│  ⚙️ CONFIGURACIÓN (SettingsScreen)                               │
│  ├─ Perfil: nombre, nivel, renombrar                             │
│  ├─ Aprendizaje: lenguajes, temas activos, dificultad            │
│  ├─ Experiencia: 🎨 carrusel de temas, sonido, vibración,        │
│  │               reducir animaciones                             │
│  ├─ Recordatorios: estructura local (placeholder)                │
│  └─ Datos: exportar (mock), restablecer (doble confirmación)     │
│                                                                   │
│  🕹️ ARCADE (GamesScreen)                                         │
│  ├─ Bug Hunt → Encuentra bugs en 5 rondas (con explicaciones)    │
│  ├─ Git Rescue → 5 decisiones Git con progreso de rama           │
│  ├─ Refactor Rush → Ordena bloques (12 puzzles aleatorios)       │
│  └─ Arcade Clásico → Minijuegos antiguos                         │
│    ⏳ Cooldown diario de 24h por juego                           │
│                                                                   │
│  🎓 ONBOARDING (primera apertura)                                │
│  ├─ Paso 1: "Tu compañero de código" + animación mascota         │
│  ├─ Paso 2: "Tu práctica la hace evolucionar" + tarjetas         │
│  ├─ Paso 3: "Elige tu ruta" → FilterChip con 8 temas             │
│  └─ Paso 4: "Dale nombre a tu copiloto" (máx 15 caracteres)      │
└──────────────────────────────────────────────────────────────────┘
```

### 4.3 Manejo de Ciclo de Vida

```kotlin
onCleared() {
    soundManager.release()  // Liberar ToneGenerator
}
```

**Temporizador persistente:** Al cerrar la app durante un estudio, la `FocusSessionEntity` persiste con estado `RUNNING`. Al reabrir, el ViewModel restaura el temporizador calculando `(plannedDurationMs - (now - startedAt))` y lo reanuda automáticamente.

---

## 5. Sistema de la Mascota

### 5.1 Entidad PetStateEntity

```kotlin
@Entity(tableName = "pet_state")
data class PetStateEntity(
    @PrimaryKey val id: Int = 1,          // Siempre 1 (singleton)
    val name: String = "Codey",            // Nombre editable
    val language: String = "Kotlin",       // Lenguaje de especialidad
    val level: Int = 1,                    // Nivel actual (1+)
    val xp: Int = 0,                       // Experiencia total
    val hunger: Float = 100f,              // Hambre (0-100)
    val health: Float = 100f,              // Salud (0-100)
    val energy: Float = 100f,              // Energía (0-100)
    val bytes: Int = 50,                   // Moneda virtual
    val lastUpdated: Long = now,           // Última interacción (epoch ms)
    val streak: Int = 0,                   // Días consecutivos de estudio
    val lastStudyDate: Long = 0,           // Última fecha de estudio
    val currentStatus: String = "HAPPY"    // HAPPY, SAD, SICK, HUNGRY, SLEEPING, STUDYING, EXCITED
)
```

### 5.2 Mapa de Estados y Transiciones

```
                 ┌──────────┐
                 │  EXCITED │ ← Desbloquear tema, mucho cariño
                 └────┬─────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
   ┌────▼────┐  ┌────▼────┐  ┌────▼────┐
   │  HAPPY  │  │ STUDYING│  │ SLEEPING│
   └────┬────┘  └────┬────┘  └────┬────┘
        │             │             │
   ┌────▼────┐  ┌────▼────┐  ┌────▼────┐
   │  SAD    │  │  HUNGRY │  │  SICK   │
   └─────────┘  └─────────┘  └─────────┘
```

**Transiciones:**
- `HAPPY ↔ SLEEPING`: toggleSleep()
- `HAPPY → STUDYING`: startStudyTimer()
- `STUDYING → HAPPY/SAD/HUNGRY/SICK`: completeStudySession()
- `SLEEPING → HAPPY`: toggleSleep() o automático al recuperar energía
- `SAD → HAPPY`: Acariciar, dar comida, mejorar energía
- `HUNGRY → HAPPY`: Dar comida (hunger > 20)
- `SICK → HAPPY`: Dar medicina (health > 20)
- `HAPPY → EXCITED`: Acertar reto, desbloquear tema
- Cualquier estado → `SLEEPING`: toggleSleep()
- Cualquier estado → `STUDYING`: startStudyTimer()

### 5.3 Sistema de Decaimiento (applyDecay) — v2.0

Cuando la app se inicia o se reanuda, se calcula el tiempo transcurrido:

```kotlin
val elapsedMs = now - state.lastUpdated
val hours = elapsedMs / (1000 * 60 * 60)
```

**Efectos por hora transcurrida (v2.0 rebalanceado):**

| Condición | Efecto v1.0 | Efecto v2.0 |
|:----------|:-----------|:-----------|
| **Despierto** — Hambre | -4%/h | **-2.5%/h** |
| **Despierto** — Energía | -3%/h | **-2%/h** |
| **Despierto** — Salud base | -3.64%/h | **-1.5%/h** |
| **Durmiendo** — Energía | +15%/h | **+12%/h** |
| **Durmiendo** — Hambre | -1.5%/h | **-1%/h** |
| **Hambre = 0%** — Salud extra | -5%/h | **-3%/h** |
| **Energía < 10%** — Salud extra | -2%/h | **-1%/h** |
| **Sin estudio > 36h** — Streak | = 0 | **> 48h** |
| **Sin estudio > 48h** — Salud extra | -3%/h | **> 72h: -1.5%/h** |

**Propósito:** El rebalance hace la mascota menos punitiva. Antes moría en ~27h; ahora sobrevive ~67h sin cuidados. La penalización por abandono prolongado se reduce para dar más margen al usuario ocasional.

### 5.4 Cálculo de Nivel

```kotlin
fun calculateLevel(xp: Int, currentLevel: Int): Int {
    var level = 1
    var requiredXp = 100
    while (xp >= requiredXp) {
        level++
        requiredXp += level * 100
    }
    return level
}
```

| Nivel | XP Requerido | XP Acumulado |
|:-----:|:------------:|:------------:|
| 1 | 0 | 0 |
| 2 | 100 | 100 |
| 3 | 200 | 300 |
| 4 | 300 | 600 |
| 5 | 400 | 1000 |
| 10 | 1000 | 5500 |
| n | n × 100 | n(n+1)/2 × 100 |

### 5.5 Fórmula de la Barra de Salud (Visual)

```kotlin
val currentHearts = (state.health / 20f).toInt().coerceIn(0, 5)
// 5 corazones = 100% salud
```

### 5.6 Umbrales de Estados (v2.0)

| Estado | v1.0 | v2.0 |
|:-------|:----:|:----:|
| SICK | health < 30 | **health < 20** |
| HUNGRY | hunger < 30 | **hunger < 20** |
| SAD | energy < 20 | **energy < 15** |

---

## 6. Módulos y Funcionalidades

### 6.1 Onboarding (`feature/onboarding/OnboardingScreen.kt`)

**Propósito:** Primera experiencia del usuario en 4 pasos con HorizontalPager.

**Flujo:**
1. **Paso 1 — "Tu compañero de código":** Mascota, animación, terminal `> hola_mundo`
2. **Paso 2 — "Tu práctica la hace evolucionar":** 3 tarjetas (retos, Focus, cuidados), explicación XP/Bytes
3. **Paso 3 — "Elige tu ruta":** FilterChip multiselección (8 temas), Ruta inicial, contador (máx 3)
4. **Paso 4 — "Dale nombre a tu copiloto":** Input (máx 15), preview `> ¡Compilado! Soy {name}`
5. **Skip:** Diálogo de confirmación → usa "Codey" + Ruta inicial
6. Al completar: `setOnboardingCompleted()`, `setSelectedTopics()`, crea `PetStateEntity(name)`, navega a Home

### 6.2 Pantalla Principal (`AppNavigation.kt` + `HomeScreen.kt`)

**Propósito:** Centro de toda la interacción con la mascota. Scaffold con bottom navigation y 13 rutas.

**Componentes:**

| Componente | Archivo | Función |
|:-----------|:--------|:--------|
| `AppNavigation` | `navigation/AppNavigation.kt` | Scaffold + bottom nav + 13 rutas (when) |
| `Screen` | `navigation/Screen.kt` | sealed class: Home, Learn, Focus, Shop, Settings, SettingsLanguage, Games, BugHunt, GitRescue, RefactorRush |
| `HomeScreen` | `feature/home/HomeScreen.kt` | ViewportCard + botones + diálogo offline reward |
| `ViewportCard` | `ui/components/ViewportCard.kt` | Mascota animada + meters + nombre |
| `MeterItem` | `ui/components/MeterItem.kt` | Barra de progreso animada |
| `LearnScreen` | `feature/learn/LearnScreen.kt` | Retos de programación (normales + especiales) |
| `FocusScreen` | `feature/focus/FocusScreen.kt` | Pomodoro persistente + bitácora de estudio |
| `ShopScreen` | `feature/shop/ShopScreen.kt` | Tienda con productos y comida |
| `SettingsScreen` | `feature/settings/SettingsScreen.kt` | Configuración completa con carrusel de temas |
| `SettingsLanguageScreen` | `feature/settings/SettingsLanguageScreen.kt` | Lenguaje principal, temas activos, dificultad |
| `GamesScreen` | `feature/games/GamesScreen.kt` | Hub arcade con cooldown diario |
| `BugHuntScreen` | `feature/games/BugHuntScreen.kt` | Encuentra bugs en código (10 snippets) |
| `GitRescueScreen` | `feature/games/GitRescueScreen.kt` | Decisiones Git con progreso visual |
| `RefactorRushScreen` | `feature/games/RefactorRushScreen.kt` | Ordena bloques (12 puzzles) |
| `MinigamesDialog` | `feature/games/MinigamesDialog.kt` | Arcade clásico (3 juegos legacy) |
| `BinaryGuessGame` | `feature/games/BinaryGuessGame.kt` | Adivina el bit (legacy) |
| `BugSmasherGame` | `feature/games/BugSmasherGame.kt` | Caza bugs 3×3 (legacy) |
| `RockPaperSciGame` | `feature/games/RockPaperSciGame.kt` | Servidor, Script, Hacker (legacy) |

### 6.3 Pet Tap

```
👆 Touch en la imagen
    ↓
onPetTap()
    ↓
┌─ viewModel.petThePet() ──────────────────────────┐
│  +15% Energía · +5% Salud   (v2.0: +50% más)     │
│  Sonido: click                                    │
└───────────────────────────────────────────────────┘
┌─ Animación ───────────────────────────────────────┐
│  1. bounceScale: 1.0 → 1.25 (100ms)              │
│  2. bounceScale: 1.25 → 1.0 (spring, 0.3)        │
│  3. bounceOffsetY: 0 → -20dp (100ms) → 0 (spring)│
│  4. heartOffsetY: 0 → -120dp (800ms, linear)      │
│  5. heartAlpha: 1.0 → 0.0 (800ms, linear)         │
│  6. ❤️ flotante visible (900ms)                   │
└───────────────────────────────────────────────────┘
```

### 6.4 Panel LEARN (Retos de Programación) — 88 retos

El banco de retos se expandió de 11 a 88 challenges:

| Lenguaje | Cantidad | Tipos |
|:---------|:--------:|:------|
| Kotlin | 20 | TRIVIA, DEBUG |
| JavaScript | 20 | TRIVIA, DEBUG |
| PHP | 19 | TRIVIA, DEBUG |
| Python | 29 | TRIVIA, DEBUG |

**Mecánica:**
- Se cargan 3 retos aleatorios del lenguaje seleccionado
- El usuario elige entre 4 opciones
- Feedback inmediato con explicación
- Acierto v2.0: +25-30 Bytes, +20-25 XP, +15% Alimento, +20% Salud
- Error: solo feedback, sin penalización

### 6.5 Panel FOCUS (Pomodoro)

**Configuración:**
- Temas: Kotlin, JavaScript, PHP, Python, SQL, Clean Code, Git, Estructuras de Datos
- Duración: 15, 25 o 50 minutos

**Mecánica (v2.0 rebalanceada):**

```kotlin
startStudyTimer(minutes, topic) {
    status = "STUDYING"
    timerSecondsRemaining = minutes * 60
    // Persiste FocusSessionEntity en Room
    // Al cerrar app → se reanuda al abrir
}

completeStudySession() {
    val baseBytes = minutes * 2      // antes: minutes * 1
    val baseXP = minutes * 3          // antes: minutes * 2
    val bonusBytes = if (minutes >= 25) 50 else 0  // antes: 25
    val bonusXP = if (minutes >= 25) 75 else 0      // antes: 50
    val energyCost = (minutes * 0.5f).coerceAtMost(30f)  // antes: 0.6 max 40
}
```

**Recompensas comparativas para 25 min:**

| Concepto | v1.0 | v2.0 |
|:---------|:----:|:----:|
| Bytes base | 25 | **50** |
| XP base | 50 | **75** |
| Bonus 25+ | +25 B, +50 XP | **+50 B, +75 XP** |
| Total | 75 B, 125 XP | **150 B, 225 XP** |
| Costo energía | -15 | **-12.5** |

### 6.6 Panel SHOP (Tienda)

| Producto | Costo | Efecto |
|:---------|:-----:|:-------|
| Café Negro (CPU Booster) | 10 B | +20% Energía |
| Pizza de Código (Bytes Snack) | 15 B | +35% Alimento |
| Píldora Desbugueadora | 25 B | +30% Salud |
| Vacuna Super Compiler | 55 B | +75% Salud, +40% Alimento, +40% Energía |

**Comida adicional:**
| Comida | Costo | Efecto |
|:-------|:-----:|:-------|
| Manzana Binaria | 2 B | +15% Alimento, +2% Energía |
| Pizza de Bytes | 6 B | +30% Alimento, +5% Energía |
| Sushi de Datos | 15 B | +55% Alimento, +5% Salud, +15% Energía |
| Café Espresso CPU | 5 B | -5% Alimento, +35% Energía |

### 6.7 Cuidados Gratuitos (v2.0 buffed)

| Acción | Efecto v1.0 | Efecto v2.0 |
|:-------|:-----------|:-----------|
| Acariciar (tocar mascota) | +10% E, +3% S | **+15% E, +5% S** |
| Limpiar | +6% S, +5% E | **+12% S, +8% E** |
| Dormir | Recupera energía | Sin cambios |
| Despertar | Vuelve a HAPPY | Sin cambios |

### 6.8 Configuración (`SettingsScreen.kt`)

**Propósito:** Todas las opciones de personalización y administración.

**Secciones:**
- **Perfil:** nombre, nivel actual, botón renombrar (diálogo con validación 1-15 chars)
- **Aprendizaje:** atajo a `SettingsLanguageScreen` (lenguaje principal, temas activos, dificultad)
- **Experiencia:** 🎨 carrusel de temas visual con LazyRow, toggle sonido/vibración, reducir animaciones
- **Recordatorios:** estructura visual (placeholder sin lógica de notificaciones)
- **Datos:** exportar progreso (mock JSON), restablecer todo (doble confirmación con diálogo en dos pasos)

**Temas visuales:**
- 12 temas premium con emoji, tipografía única, corner radius, gradientes
- Carrusel con preview de colores, dots de paleta, check en activo, candado en bloqueados
- Animación suave de borde con `animateColorAsState`
- Persistencia en DataStore + CompositionLocal (`LocalAppTheme`)

---

## 7. Base de Datos y Persistencia

### 7.1 Room Database

**Archivo:** `AppDatabase.kt` — versión 2 (fallbackToDestructiveMigration)

```kotlin
@Database(entities = [PetStateEntity::class, StudySessionEntity::class, FocusSessionEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "code_tamagotchi_db")
                    .fallbackToDestructiveMigration(false)
                    .build().also { INSTANCE = it }
            }
        }
    }
}
```

**Tablas:**
| Tabla | Propósito | Filas |
|:------|:----------|:------|
| `pet_state` | Estado singleton de la mascota | 1 |
| `study_sessions` | Historial de sesiones completadas | N |
| `focus_sessions` | Sesiones de timer (activas y completadas) | N |

### 7.2 FocusSessionEntity

```kotlin
@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val plannedDurationMinutes: Int,
    val startedAt: Long,          // epoch ms
    val status: String            // "RUNNING", "COMPLETED", "CANCELLED"
)
```

**Queries clave:**
```kotlin
@Query("SELECT * FROM focus_sessions WHERE status = 'RUNNING' LIMIT 1")
fun getActiveFocusSession(): Flow<FocusSessionEntity?>

@Query("SELECT * FROM focus_sessions ORDER BY id DESC LIMIT 1")
fun getLatestFocusSession(): Flow<FocusSessionEntity?>
```

### 7.3 DataStore (reemplaza SharedPreferences)

**Archivo:** `UserPreferencesRepository.kt`

```kotlin
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    val hasSeenOnboarding: Flow<Boolean>
    val currentTheme: Flow<String>
    val unlockedThemes: Flow<Set<String>>

    suspend fun setOnboardingCompleted()
    suspend fun setTheme(theme: String)
    suspend fun addUnlockedTheme(theme: String)
    suspend fun getHasSeenOnboarding(): Boolean
}
```

**Keys migradas desde SharedPreferences:**
| Key | Tipo | Default |
|:----|:----:|:--------|
| `has_seen_onboarding` | Boolean | false |
| `app_theme` | String | "Matrix Green" |
| `unlocked_themes` | Set<String> | {"Matrix Green"} |

### 7.4 PetDao — Queries Completas

```kotlin
@Dao
interface PetDao {
    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    fun getPetState(): Flow<PetStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePetState(state: PetStateEntity)

    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Insert
    suspend fun insertStudySession(session: StudySessionEntity)

    // Focus Sessions
    @Query("SELECT * FROM focus_sessions ORDER BY id DESC LIMIT 1")
    fun getLatestFocusSession(): Flow<FocusSessionEntity?>

    @Query("SELECT * FROM focus_sessions WHERE status = 'RUNNING' LIMIT 1")
    suspend fun getActiveFocusSession(): FocusSessionEntity?

    @Insert
    suspend fun insertFocusSession(session: FocusSessionEntity)

    @Query("UPDATE focus_sessions SET status = :status WHERE id = :id")
    suspend fun updateFocusSessionStatus(id: Long, status: String)
}
```

### 7.5 PetRepository

```kotlin
class PetRepository(private val petDao: PetDao) {
    val petState: Flow<PetStateEntity?>
    val studySessions: Flow<List<StudySessionEntity>>
    val latestFocusSession: Flow<FocusSessionEntity?>

    suspend fun savePetState(state: PetStateEntity)
    suspend fun addStudySession(session: StudySessionEntity)
    suspend fun saveFocusSession(session: FocusSessionEntity)
    suspend fun getActiveFocusSession(): FocusSessionEntity?
    suspend fun updateFocusSessionStatus(id: Long, status: String)
}
```

### 7.6 Estructura de Carpetas de Assets

```
app/src/main/assets/pet/
├── normal/
│   ├── static/
│   │   ├── mascota_happy.png
│   │   ├── mascota_sleeping.png
│   │   ├── mascota_studying.png
│   │   ├── mascota_sick.png
│   │   ├── mascota_sad.png
│   │   ├── mascota_hungry.png
│   │   └── mascota_excited.png
│   └── animations/
└── pixel_art/
    ├── static/
    └── animations/
```

---

## 8. Sistema de Temas

### 8.1 Estructura

```kotlin
data class AppTheme(
    val name: String,           // "Matrix Green"
    val emoji: String,          // "🖥️"
    val description: String,    // "Terminal hacker. Código verde sobre negro."
    val isDark: Boolean,        // true = darkColorScheme, false = lightColorScheme

    // Color palette (12 colores)
    val background: Color, val surface: Color, val surfaceVariant: Color,
    val primary: Color, val secondary: Color, val tertiary: Color,
    val onPrimary: Color, val textPrimary: Color, val textSecondary: Color,
    val accent: Color, val success: Color, val error: Color,

    // Typography (distinta por tema)
    val fontFamily: FontFamily,      // body (Monospace, Serif, SansSerif)
    val titleFontFamily: FontFamily, // títulos (puede diferir del body)
    val titleWeight: FontWeight,     // Bold, Thin, ExtraBold, Black...

    // Visual style
    val cornerRadius: Dp,       // 0dp (Retro Pixel) a 20dp (Galáctico)
    val borderWidth: Dp,        // 0dp a 2dp
    val usesGradients: Boolean,
    val gradientColors: List<Color>
)
```

### 8.2 Temas Disponibles (12 premium)

| # | Tema | Emoji | Dark | Fuente | Esquinas | Gradiente |
|:-:|:-----|:-----:|:----:|:-------|:--------:|:---------:|
| 1 | Matrix Green | 🖥️ | Sí | Monospace | 4dp | — |
| 2 | Galáctico | 🌌 | Sí | SansSerif | 20dp | ✅ |
| 3 | Cyberpunk | ⚡ | Sí | Monospace | 2dp | ✅ |
| 4 | Sakura | 🌸 | No | Serif | 16dp | ✅ |
| 5 | Minimalista | ◻️ | No | SansSerif | 8dp | — |
| 6 | Neón | 💜 | Sí | Monospace | 12dp | — |
| 7 | Océano | 🌊 | Sí | SansSerif | 14dp | ✅ |
| 8 | Volcánico | 🌋 | Sí | Serif | 6dp | ✅ |
| 9 | Samurai | ⚔️ | Sí | Serif | 4dp | — |
| 10 | Aurora | ✨ | Sí | SansSerif | 18dp | ✅ |
| 11 | Nocturno | 🌙 | Sí | SansSerif | 12dp | — |
| 12 | Retro Pixel | 👾 | Sí | Monospace | 0dp | — |

### 8.3 Integración con Material 3

Cada `AppTheme` se convierte a un `ColorScheme` completo de Material 3 mediante:

```kotlin
fun AppTheme.toColorScheme(): ColorScheme {
    val builder = if (isDark) ::darkColorScheme else ::lightColorScheme
    return builder(
        primary = primary, onPrimary = onPrimary,
        primaryContainer = primary.copy(alpha = 0.20f),
        secondary = secondary, tertiary = tertiary,
        background = background, surface = surface,
        surfaceVariant = surfaceVariant,
        error = error, outline = textSecondary.copy(alpha = 0.5f),
        ...
    )
}
```

Además, la tipografía se construye dinámicamente vía `buildTypography(bodyFont, titleFont, titleWeight)`.

### 8.4 Composición Local

```kotlin
val LocalAppTheme = staticCompositionLocalOf { ThemeRegistry.allThemes.first() }
```

Cualquier componente puede leer el tema actual completo con `LocalAppTheme.current` para acceder a propiedades como `cornerRadius`, `emoji`, `borderWidth`, etc.

### 8.5 Selección Visual

Los temas se seleccionan mediante un **carrusel horizontal** (`LazyRow`) en SettingsScreen:

- Cada tarjeta muestra: gradiente de colores, emoji, nombre, descripción, dots de paleta
- El tema activo tiene borde brillante + check + etiqueta "Activo"
- Los temas bloqueados muestran candado y están deshabilitados
- Animación suave de borde (`animateColorAsState`)

### 8.6 Persistencia

- `currentTheme` → Flow vía DataStore (default: "Matrix Green")
- `unlockedThemes` → Flow vía DataStore (default: {"Matrix Green"})
- Los temas se desbloquean al acertar retos especiales mediante `addUnlockedTheme()`

---

## 9. Sistema de Retos

### 9.1 Estructura de un Reto

```kotlin
data class CodingChallenge(
    val id: Int,
    val language: String,
    val type: String,               // "TRIVIA", "DEBUG"
    val title: String,
    val question: String,
    val codeSnippet: String?,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)
```

### 9.2 Banco de Retos (88)

| Lenguaje | IDs | Tipo | Temas cubiertos |
|:---------|:---:|:-----|:----------------|
| **Kotlin** (20) | 1-3, 12-25, 86-87 | TRIVIA + DEBUG | val/var, null safety, scope functions, data class, Elvis, extension functions, coroutines, when, sealed class, smart cast, Flow vs LiveData, colecciones, inline, operator overloading, object, lambdas |
| **JavaScript** (20) | 4-6, 26-40 | TRIVIA + DEBUG | typeof null, === vs ==, closures, let/var, promises, spread, template literals, arrow functions, hoisting, destructuring, map, event loop, falsy, fetch, coerción, this, JSON, null vs undefined |
| **PHP** (19) | 7-9, 41-55 | TRIVIA + DEBUG | $variables, array_merge, == vs ===, $_POST, foreach, isset/empty, clases, extends, print_r, session_start, include/require, arrays asociativos, concatenación, funciones variables, self::, PDO, setcookie, ternario |
| **Python** (29) | 10-11, 56-85, 88-90 | TRIVIA + DEBUG | tuplas, list comprehension, PEP 8, list vs tuple, with, mutable defaults, *args/**kwargs, decoradores, range, dict.get, f-strings, try/except, herencia múltiple, generadores, shallow copy, sets, __init__.py, / vs //, @classmethod, zip, global, dict comprehension, lambda, re vs match, assert, enumerate, referencias, sys.argv, JSON, super, virtualenv, type hints, asyncio |

### 9.3 Carga de Retos

```kotlin
loadChallengesForLanguage("Kotlin") {
    val filtered = ChallengesData.challenges.filter { language == it.language }
    _activeChallenges.value = filtered.shuffled().take(3)
    _currentChallengeIndex.value = 0
}
```

Siempre se muestran 3 retos aleatorios por lenguaje. Al completar los 3, se recargan otros 3 aleatorios.

### 9.4 Sistema de Recompensas (v2.0)

```kotlin
// Reto normal TRIVIA
earnedBytes = 25     // antes: 20
earnedXp = 20        // antes: 15

// Reto normal DEBUG
earnedBytes = 30     // antes: 25
earnedXp = 25        // antes: 20

// Ambos restauran: hunger +15, health +20 (sin cambios)
```

---

## 10. Minijuegos

La app tiene 3 juegos originales (Arcade de Depuración) más 3 juegos clásicos (Arcade Clásico legacy). Todos los juegos entregan Bytes y afecto, nunca XP de estudio. Cada juego tiene cooldown diario de 24h.

### 10.1 Bug Hunt — Terminal Panic

**Propósito:** Encontrar la línea con bug en fragmentos de código ficticio.

**Mecánica:**
- 5 rondas de 60 segundos
- Se muestra un snippet de 4-5 líneas de código
- El jugador toca la línea que contiene el bug
- 10 snippets diferentes en el pool (aleatorio cada partida)
- Después de responder: explicación técnica + humor de Codey

**Sistema de recompensa:**
- `score × 10` Bytes (máx 50)
- `+10%` Salud, `-5%` Energía
- Logro "Cazador de bugs" si score = 10

**Ejemplo de ronda:**
```
Ronda 3 de 5
⌛ 42s

Codey: ¡Los paréntesis no son decoración! Son parte de la sintaxis.

1. val nums = listOf(1, 2, 3)
2. for i in nums {          ← BUG (falta paréntesis)
3.     print(i)
4. }

Explicación: En Kotlin el for usa paréntesis: for (i in nums).
```

### 10.2 Git Rescue

**Propósito:** Elegir el comando Git correcto en situaciones de merge, rebase y conflictos.

**Mecánica:**
- 5 escenarios secuenciales de 8 en el pool
- Cada escenario: situación narrativa + 3 opciones (correcta, plausible, absurda)
- Después de responder: explicación + humor
- Progreso visual de rama con círculos numerados

**Sistema de recompensa:**
- `score × 15` Bytes (máx 45)
- `+10%` Salud, `-5%` Energía
- Logro "Git sin pánico" si score = 3

**Ejemplo:**
```
Paso 2 de 5

Codey: revert HEAD es más seguro que reset --hard

Situación: Has hecho cambios locales y rompiste todo.
> git reset --hard HEAD
> git revert HEAD                ← CORRECTA
> git rm -rf .

Explicación: revert crea un nuevo commit preservando la historia.
```

### 10.3 Refactor Rush

**Propósito:** Ordenar bloques de código en el orden correcto.

**Mecánica:**
- 1 puzzle aleatorio de 12 en el banco
- Bloques desordenados, se mueven con botones ▲/▼
- Múltiples intentos permitidos
- Penalización por intentos extra (menos Bytes)

**Sistema de recompensa:**
- `max(50 - attempts × 5, 10)` Bytes
- `+10%` Salud, `-5%` Energía

### 10.4 Arcade Clásico (Legacy)

Sección secundaria en GamesScreen que conserva los minijuegos originales:

#### 10.4.1 Adivina el Bit
- 5 rondas, adivinar bit secreto (0/1)
- Premio: `score × 4` Bytes, `score × 3`% Salud

#### 10.4.2 Caza de Bugs
- Cuadrícula 3×3, 10 segundos, tocar el bug
- Premio: `score × 2` Bytes, `score × 1.5`% Salud (máx 30%)

#### 10.4.3 Servidor, Script, Hacker
- Servidor > Hacker > Script > Servidor, mejor de 3
- Premio (ganar): 20 Bytes, 25% Salud
- Premio (perder): 5 Bytes, 10% Salud

### 10.5 Cooldown Diario

Cada juego registra su última fecha de juego en DataStore (`game_last_played`). Un juego solo puede jugarse si pasaron 24h desde la última partida. El cooldown se muestra visualmente: tarjeta atenuada, botón deshabilitado, texto "En enfriamiento".

---

## 11. Sistema de Sonido

### 11.1 SoundManager

Usa `ToneGenerator` del sistema Android (generación de tonos, sin archivos de audio):

```kotlin
class SoundManager {
    private var toneGen: ToneGenerator? = null

    init {
        toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    }
}
```

### 11.2 Mapa de Sonidos

| Evento | Método | Tono |
|:-------|:-------|:-----|
| ✅ Respuesta correcta | `playSuccess()` | `TONE_PROP_ACK` (100ms) |
| ❌ Respuesta incorrecta | `playError()` | `TONE_PROP_BEEP2` (150ms) |
| ⬆️ Subida de nivel / Logro | `playLevelUp()` | 2× beep + ack |
| 👆 Clic / Interfaz | `playClick()` | `TONE_DTMF_A` (50ms) |
| 🛒 Comprar | `playBuy()` | `TONE_PROP_BEEP` (100ms) |
| 😴 Dormir | `playSleep()` | 2× `TONE_CDMA_SOFT_ERROR_LITE` |

### 11.3 Ciclo de Vida

```kotlin
val soundManager = SoundManager()   // creado en PetViewModel

override fun onCleared() {
    soundManager.release()
}
```

---

## 12. Estados y Animaciones

### 12.1 Animaciones Infinitas (por estado)

| Estado | Eje X | Eje Y | Escala | Velocidad |
|:-------|:-----:|:-----:|:------:|:---------:|
| HAPPY | 0 | ±10px flotación | 1.0 | 600ms |
| SLEEPING | 0 | ±5px flotación lenta | 1.0 | 2000ms |
| STUDYING | 0 | 0 | 1.0 | — |
| SICK | ±5px temblor | 0 | 1.0 | 100ms |
| SAD | 0 | 0 | 1.0 | — |
| HUNGRY | 0 | 0 | 0.95↔1.05 pulso | 1000ms |
| EXCITED | 0 | ±10px flotación rápida | 1.0 | 300ms |

### 12.2 Animaciones One-Shot

| Acción | Animación | Duración |
|:-------|:----------|:---------|
| Tocar mascota | Bounce (escala 1→1.25→1) + ❤️ flotante | 900ms |
| Abrir panel | Slide-up + fade-in | 300ms |
| Cerrar panel | Slide-down + fade-out | 300ms |
| Feedback reto | Color en opción seleccionada | Instantáneo |
| Barras de estado | `animateFloatAsState` | 300ms |

### 12.3 Sistema de Quotes

```kotlin
"HAPPY" → "¡Compilar sin advertencias es mi pasión!"
"SLEEPING" → "Zzz... if (dream) { sleep() } else { repeat() }... Zzz"
"SICK" → "Error 500: Necesito desbuguear urgente."
"HUNGRY" → "NullPointerException en mi estómago."
"EXCITED" → "¡Podría compilar el kernel de Linux en 1 segundo!"
```

---

## 13. Notificaciones y Widgets

### 13.1 Estado Actual

**No implementado.** La app actualmente no tiene:
- ❌ Notificaciones push
- ❌ Notificaciones locales
- ❌ Widgets de Android
- ❌ Recordatorios

### 13.2 Plan para Futuras Versiones

- **Notificaciones locales:** WorkManager para checkear cada 4h si la mascota necesita atención
- **Widget:** AppWidgetProvider mostrando la mascota + barras de estado
- **Recordatorio de estudio:** Notificación programada

---

## 14. Inyección de Dependencias

### 14.1 Koin Setup

**Application class** (`CodeTamagotchiApp.kt`):
```kotlin
class CodeTamagotchiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CodeTamagotchiApp)
            modules(appModule)
        }
    }
}
```

**AppModule** (`di/AppModule.kt`):
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

### 14.2 Consumo en MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme = ThemeRegistry.getTheme(currentTheme.value)
            MyApplicationTheme(appTheme = appTheme, reduceMotion = ...) {
                val petViewModel: PetViewModel = koinViewModel()
                // ...
            }
        }
    }
}
```

### 14.3 ViewModel con Constructor Injection

```kotlin
class PetViewModel(
    private val repository: PetRepository,
    private val userPreferences: UserPreferencesRepository,
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {
    // 3 dependencias inyectadas por Koin
    // DataStore para temas, onboarding, logros, cooldown juegos
    // Room para estado mascota, sesiones de estudio y focus
}
```

---

## 15. Pruebas

### 15.1 Tests Unitarios

**Archivo:** `ExampleUnitTest.kt` — test básico de prueba.

### 15.2 Tests Robolectric

**Archivo:** `ExampleRobolectricTest.kt` — verifica carga de `app_name`.

### 15.3 Screenshot Tests (Roborazzi)

**Archivo:** `GreetingScreenshotTest.kt`

Actualizado en v2.0: ahora recibe `PetRepository`, `UserPreferencesRepository` y `AchievementsRepository` por constructor (mockeado con `fakePetState`).

### 15.4 Tests de Instrumentación

**Archivo:** `ExampleInstrumentedTest.kt` — verifica `packageName`.

### 15.5 Cobertura Actual

| Tipo | Archivos | Estado |
|:-----|:---------|:-------|
| Unit test | 1 | ✅ Básico |
| Robolectric | 2 | ✅ Funcional |
| Screenshot | 1 | ✅ Actualizado |
| Instrumentación | 1 | ✅ Básico |

---

## 16. Dependencias Externas

### 16.1 Lista Completa

| Librería | Versión | Propósito |
|:---------|:-------:|:----------|
| Kotlin | 2.2.10 | Lenguaje |
| AGP | 9.1.1 | Android Gradle Plugin |
| Compose BOM | 2025.01.00 | UI |
| Material 3 | via BOM | Componentes Material Design 3 |
| Room | 2.7.0 | Persistencia SQLite |
| Room Compiler (KSP) | 2.7.0 | Generación de código Room |
| Lifecycle (ViewModel) | 2.9.0 | Arquitectura MVVM |
| Activity Compose | 1.10.0 | Integración Activity + Compose |
| Coroutines | 1.10.1 | Async |
| **Koin Android** | **4.0.2** | Inyección de dependencias |
| **Koin Compose** | **4.0.2** | Integración Koin + Compose |
| **DataStore Prefs** | **1.1.7** | Persistencia de preferencias |
| **Navigation Compose** | via BOM | Navegación entre pantallas |
| Firebase BOM | 33.0.0 | Firebase suite (opcional) |
| Firebase App Check | via BOM | Seguridad |
| Retrofit | 2.11.0 | HTTP client |
| OkHttp | 4.12.0 | HTTP engine |
| Moshi | 1.15.2 | JSON parsing |
| Moshi Codegen (KSP) | 1.15.2 | Generación de adapters Moshi |
| Robolectric | 4.14.1 | Tests unitarios de Android |
| Roborazzi | 1.8.0 | Screenshot tests |
| Secrets Gradle Plugin | 2.0.1 | Variables de entorno (.env) |
| Google Services | 4.4.2 | Firebase Google Services |

**Nuevas en v2.0:**
- Koin Android 4.0.2
- Koin Compose 4.0.2
- DataStore Preferences 1.1.7
- Navigation Compose (via Compose BOM)

**Eliminadas de v1.0:**
- `firebase-auth` + `credentials` + `googleid` (siempre estuvieron comentadas)
- `firebase-firestore` (siempre comentado)
- `play-services-location` (siempre comentado)
- `accompanist-permissions` (siempre comentado)
- `camera-*` (siempre comentado)
- `coil-compose` (siempre comentado)

---

## 17. Guía para Desarrolladores

### 17.1 Cómo Compilar

**Requisitos:**
- Android Studio Hedgehog+
- JDK 17+
- SDK Android 24+
- Gradle 8.x (wrapper incluido)

```bash
# Compilar debug APK
./gradlew clean assembleDebug

# APK generado en:
# app/build/outputs/apk/debug/app-debug.apk
```

**Nota sobre Firebase:** El archivo `google-services.json` no está incluido. El build advierte pero no falla.

### 17.2 Cómo Probar

```bash
# Tests unitarios
./gradlew testDebugUnitTest

# Tests de instrumentación (requiere emulador/dispositivo)
./gradlew connectedDebugAndroidTest

# Screenshot test
./gradlew testDebugUnitTest --tests "*.GreetingScreenshotTest"
```

### 17.3 Cómo Agregar un Nuevo Reto

1. Abrir `ChallengesData.kt`
2. Agregar un nuevo `CodingChallenge` a la lista:

```kotlin
CodingChallenge(
    id = 91,
    language = "Kotlin",
    type = "TRIVIA",
    title = "Tu Nuevo Reto",
    question = "¿Pregunta?",
    options = listOf("Opción A", "Opción B", "Opción C", "Opción D"),
    correctAnswerIndex = 2,
    explanation = "Explicación detallada..."
)
```

### 17.4 Cómo Agregar un Nuevo Tema

1. Abrir `ThemeConfig.kt`
2. Agregar a `ThemeRegistry.allThemes` un `AppThemeColors`

### 17.5 Cómo Agregar un Nuevo Minijuego

1. Crear el composable en `feature/games/`
2. Agregar la opción en `MinigamesDialog`
3. El juego debe llamar a `viewModel.completeMinigame(bytes, happiness, energy)` al terminar

### 17.6 Cómo Agregar una Nueva Feature Screen

1. Crear el archivo en `feature/mi-feature/`
2. Agregar el destino en `Screen.kt` (sealed class)
3. Agregar el `when` branch en `AppNavigation.kt`
4. Agregar el botón en la bottom navigation bar

### 17.7 Convenciones de Código

- **Idioma:** Español (código, strings, comentarios)
- **UI:** Jetpack Compose con Material 3
- **Estado:** ViewModel con `StateFlow` + `mutableStateOf`
- **DI:** Koin (módulos en `di/`)
- **Persistencia:** Room para datos complejos, DataStore para preferencias
- **Async:** Corrutinas en `viewModelScope`
- **Tests:** Robolectric + Roborazzi
- **Tipografía:** `FontFamily.Monospace` para textos de terminal
- **Animaciones:** `rememberInfiniteTransition` para loops, `Animatable` para one-shot
- **Navegación:** sealed class `Screen` con `when` exhaustivo (sin NavHost)

---

## 18. Changelog

### v2.0.0 — Refactorización Mayor

#### 🏗️ Arquitectura
- **Modularización:** `CodeTamagotchiScreen.kt` (~2925 líneas) dividido en `feature/{home,learn,focus,shop,games,settings}/`, `navigation/AppNavigation.kt` y `ui/components/`
- **Navegación:** Sistema de bottom nav con sealed class `Screen` (sin NavHost), 13 rutas total
- **DI:** Migración a **Koin 4.0.2** (no usa Gradle plugin, compatible con AGP 9.x)
- **DataStore:** SharedPreferences migrado a **DataStore Preferences** (`UserPreferencesRepository`) + nuevo `AchievementsRepository`
- **GreetingScreenshotTest:** actualizado con 3 parámetros en constructor

#### 🎮 Nuevos Juegos (Arcade de Depuración)
- **Bug Hunt — Terminal Panic:** 10 snippets de código, 5 rondas, explicaciones con humor de Codey
- **Git Rescue:** 8 escenarios Git, 5 decisiones, progreso visual de rama
- **Refactor Rush:** 12 puzzles de ordenamiento, aleatorio por partida, penalización por intentos
- **Cooldown diario de 24h por juego:** persistido en DataStore, UI atenuada + botón deshabilitado

#### 🎨 Sistema de Temas Premium (12)
- Rediseño completo: `AppTheme` con emoji, tipografía única, corner radius, gradientes, border
- `toColorScheme()` → mapeo a Material 3 ColorScheme completo
- `buildTypography()` dinámica por tema (Monospace/Serif/SansSerif + title weight)
- `LocalAppTheme` CompositionLocal para acceso desde cualquier componente
- Carrusel visual en Settings (LazyRow + preview colores + check/candado + borde animado)
- Persistencia: `currentTheme` + `unlockedThemes` en DataStore

#### ⚙️ Configuración Completa (`SettingsScreen.kt`)
- Perfil: nombre, nivel, renombrar con diálogo validado
- Aprendizaje: lenguaje principal, temas activos, dificultad
- Experiencia: carrusel de temas, sonido, vibración, reducir animaciones
- Recordatorios: estructura visual (placeholder)
- Datos: exportar progreso (mock), restablecer con **doble confirmación**

#### 🎓 Onboarding Rediseñado (4 pasos)
- Paso 1: "Tu compañero de código" + animación mascota + terminal
- Paso 2: "Tu práctica la hace evolucionar" + tarjetas XP/Bytes
- Paso 3: "Elige tu ruta" → FilterChip multiselección (8 temas, máx 3)
- Paso 4: "Dale nombre a tu copiloto" (input 1-15 chars, preview)
- Skip: diálogo de confirmación → Codey + ruta inicial

#### 🏆 Logros
- Nuevo `AchievementsRepository` en DataStore (Flow persistente)
- Logros: "Cazador de bugs" (Bug Hunt score = 5), "Git sin pánico" (Git Rescue score ≥ 3)
- Se muestran en SettingsScreen

#### ⏱️ Timer Persistente
- Nueva entidad `FocusSessionEntity` en Room
- El temporizador sobrevive al cierre de la app (se reanuda automáticamente al abrir)
- Queries: `getActiveFocusSession()`, `getLatestFocusSession()`, `updateFocusSessionStatus()`

#### ⚖️ Rebalance de Mecánicas
- Decaimiento reducido ~50% (menos punitivo)
- Recompensas de estudio aumentadas ~100%
- Interacciones (acariciar, limpiar) potenciadas ~50%
- Streak más indulgente (resetea a las 48h en vez de 36h)
- Umbrales de estado más permisivos

#### 📚 Banco de Retos
- Expandido de **11 → 88 challenges**
- Kotlin: 20, JavaScript: 20, PHP: 19, Python: 29
- Temas cubiertos: 80+ conceptos de programación

#### 🧹 Limpieza
- Eliminados ~20 imports/dependencias comentadas (Firebase Auth, Camera, Coil, etc.)
- Eliminado archivo duplicado `ui/screens/OnboardingScreen.kt`
- Deprecation warnings eliminados (`fallbackToDestructiveMigration(false)`, Koin DSL, `Icons.AutoMirrored`)
- Build: **0 warnings, 0 errors**

---

> **Code Tamagotchi v2.0** — Donde los bugs se convierten en mascotas y el código en cariño.  
> Documentación generada para desarrolladores y curiosos.  
> ¿Preguntas? Abre un issue en [GitHub](https://github.com/fguzman-stack/CodePet).
