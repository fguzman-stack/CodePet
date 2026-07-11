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
- Firebase: Configurado como opcional (app check, AI)
- Sin Google Services JSON (el build advierte pero no falla)
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
├── MainActivity.kt                    ← Entry point, @AndroidEntryPoint → koinViewModel()
├── data/
│   ├── ChallengesData.kt              ← 88 retos de programación (Kotlin, JS, PHP, Python)
│   ├── CodingChallenge.kt             ← data class del reto
│   ├── database/
│   │   ├── AppDatabase.kt             ← Room DB singleton con patrón double-check locking
│   │   ├── PetDao.kt                  ← DAO con queries reactivas (Flow)
│   │   ├── PetStateEntity.kt          ← Entidad: estado completo de la mascota
│   │   ├── StudySessionEntity.kt      ← Entidad: sesiones de estudio históricas
│   │   └── FocusSessionEntity.kt      ← Entidad: sesión activa del timer Pomodoro
│   └── repository/
│       ├── PetRepository.kt           ← Capa de datos mascota + sesiones + focus
│       └── UserPreferencesRepository.kt ← DataStore (onboarding, tema, temas desbloq.)
├── di/
│   └── AppModule.kt                   ← Módulo Koin: BD, DAO, repos, ViewModel
├── navigation/
│   ├── AppNavigation.kt               ← Scaffold + bottom nav + enrutamiento de pantallas
│   └── Screen.kt                      ← sealed class con destinos (Home, Learn, Focus, Shop)
├── feature/
│   ├── home/
│   │   └── HomeScreen.kt              ← Tarjeta mascota + meters + botones de acción
│   ├── learn/
│   │   └── LearnScreen.kt             ← Retos de programación (normales y especiales)
│   ├── focus/
│   │   └── FocusScreen.kt             ← Pomodoro + bitácora de estudio
│   ├── shop/
│   │   └── ShopScreen.kt              ← Tienda con productos para la mascota
│   ├── games/
│   │   ├── MinigamesDialog.kt         ← Selector de minijuegos
│   │   ├── BinaryGuessGame.kt         ← Adivina el bit (5 rondas)
│   │   ├── BugSmasherGame.kt          ← Caza bugs 3×3 (10 segundos)
│   │   └── RockPaperSciGame.kt        ← Servidor, Script, Hacker (mejor de 3)
│   └── settings/
│       └── PersonalizeDialog.kt       ← Configuración (nombre, lenguaje, tema)
├── ui/
│   ├── components/
│   │   ├── MeterItem.kt               ← Barra de progreso individual
│   │   └── ViewportCard.kt            ← Card principal con mascota animada + meters
│   ├── screens/
│   │   └── OnboardingScreen.kt        ← Pantalla de bienvenida y naming
│   ├── theme/
│   │   ├── Color.kt                   ← Colores base Material 3
│   │   ├── Theme.kt                   ← Tema Material 3 con soporte dinámico (Android 12+)
│   │   ├── ThemeConfig.kt             ← 25 temas personalizados (colores, nombres)
│   │   └── Type.kt                    ← Tipografía base
│   └── viewmodel/
│       └── PetViewModel.kt            ← Toda la lógica de negocio (DI via Koin)
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
    viewModel { PetViewModel(get(), get()) }
}
```

- `CodeTamagotchiApp` inicia Koin en `onCreate()`
- `MainActivity` obtiene el ViewModel con `koinViewModel()`
- `PetViewModel` recibe `PetRepository` y `UserPreferencesRepository` por constructor

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
setContent { MyApplicationTheme { ... } }
    ↓
koinViewModel() → PetViewModel(PetRepository, UserPreferencesRepository)
    ↓
init { } del ViewModel:
    ├── Leer DataStore: hasSeenOnboarding (suspend)
    ├── Colectar Flow: currentTheme → actualizar tema
    ├── Colectar Flow: unlockedThemes → actualizar temas
    ├── Room: leer petState
    │   ├── null  → crear PetStateEntity default → loadChallenges("Kotlin")
    │   └── exist → applyDecay(petState) → loadChallenges(petState.language)
    └── Room: buscar FocusSession activa
        ├── null  → nada
        └── exist → reanudar timer (calcular tiempo restante)
    ↓
Composable:
    ├── hasSeenOnboarding == false → OnboardingScreen
    │       ↓
    │   completeOnboarding(petName)
    │       ↓ DataStore + Room
    │       ↓ → recomposición → hasSeenOnboarding = true → AppNavigation
    │
    └── hasSeenOnboarding == true → AppNavigation (HomeScreen por defecto)
```

### 4.2 Mapa de Navegación (Bottom Navigation)

```
┌─────────────────────────────────────────────────────┐
│                    TOP BAR                           │
│           Code Tamagotchi [⚙️ personalizar]         │
├─────────────────────────────────────────────────────┤
│                                                      │
│   ┌─────────────────────────────────────────────┐   │
│   │              HOME SCREEN                     │   │
│   │  (ViewportCard: mascota + meters + botones)  │   │
│   │  ┌─────────────────────────────────────┐     │   │
│   │  │        🖼️ MASCOTA ANIMADA           │     │   │
│   │  │  (bounce al tocarla + ❤️ flotante)  │     │   │
│   │  └─────────────────────────────────────┘     │   │
│   │  ▓▓▓▓▓░░░ Vida    ▓▓░░░░░ Alimento ▓▓▓░░░░  │   │
│   │  💰 Bytes   🔥 Streak   😴 Dormir           │   │
│   │  [Acariciar] [Limpiar] [🎮 Jugar]           │   │
│   └─────────────────────────────────────────────┘   │
│                                                      │
├─────────────────────────────────────────────────────┤
│  [🏠] Inicio   [💻] Aprender   [⏱️] Focus   [🛒]  │
│                        Tienda                        │
└─────────────────────────────────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Al tocar Aprender / Focus / Tienda:

┌─────────────────────────────────────────────────────┐
│   (overlay con slide-up desde abajo)                 │
│                                                      │
│   ┌─────────────────────────────────────────────┐   │
│   │           TERMINAL HUB                        │   │
│   │  ● ● ●  terminal@codey:~  v2.0.0            │   │
│   │  ─────────────────────────────────────────   │   │
│   │                                               │   │
│   │  [LEARN] → Retos de programación por lenguaje │   │
│   │  [FOCUS] → Pomodoro (15/25/50 min) + bitácora│   │
│   │  [SHOP]  → Productos + comida                 │   │
│   │                                               │   │
│   └─────────────────────────────────────────────┘   │
│                                                      │
└─────────────────────────────────────────────────────┘
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

### 6.1 Onboarding (`OnboardingScreen.kt`)

**Propósito:** Primera experiencia del usuario.

**Flujo:**
1. Muestra imagen de mascota feliz + texto de bienvenida
2. Input para nombre (máx 15 caracteres)
3. Botón "Compilar y Empezar"
4. Al completar: `UserPreferencesRepository.setOnboardingCompleted()` + crea `PetStateEntity`

### 6.2 Pantalla Principal (`AppNavigation.kt` + `HomeScreen.kt`)

**Propósito:** Centro de toda la interacción con la mascota. Scaffold con bottom navigation y sistema de paneles modales.

**Componentes:**

| Componente | Archivo | Función |
|:-----------|:--------|:--------|
| `AppNavigation` | `navigation/AppNavigation.kt` | Scaffold + bottom nav + diálogos |
| `Screen` | `navigation/Screen.kt` | sealed class (Home, Learn, Focus, Shop) |
| `HomeScreen` | `feature/home/HomeScreen.kt` | ViewportCard + botones de acción |
| `ViewportCard` | `ui/components/ViewportCard.kt` | Mascota animada + meters + nombre |
| `MeterItem` | `ui/components/MeterItem.kt` | Barra de progreso individual |
| `LearnScreen` | `feature/learn/LearnScreen.kt` | Retos de programación con pestañas |
| `FocusScreen` | `feature/focus/FocusScreen.kt` | Pomodoro + bitácora de estudio |
| `ShopScreen` | `feature/shop/ShopScreen.kt` | Tienda con productos |
| `PersonalizeDialog` | `feature/settings/PersonalizeDialog.kt` | Configuración (nombre, lenguaje, tema) |
| `MinigamesDialog` | `feature/games/MinigamesDialog.kt` | Selector de 3 minijuegos |
| `BinaryGuessGame` | `feature/games/BinaryGuessGame.kt` | Adivina el bit (5 rondas) |
| `BugSmasherGame` | `feature/games/BugSmasherGame.kt` | Caza bugs 3×3 (10 segundos) |
| `RockPaperSciGame` | `feature/games/RockPaperSciGame.kt` | Servidor, Script, Hacker (mejor de 3) |

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

### 6.8 Minijuegos

**1. Adivina el Bit (`BinaryGuessGame`):**
- 5 rondas, adivinar bit secreto (0/1)
- Premio: `score × 4` Bytes, `score × 3`% Salud

**2. Caza de Bugs (`BugSmasherGame`):**
- Cuadrícula 3×3, 10 segundos, tocar el bug que aparece aleatoriamente
- Premio: `score × 2` Bytes, `score × 1.5`% Salud (max 30%)

**3. Servidor, Script, Hacker (`RockPaperSciGame`):**
- Servidor > Hacker > Script > Servidor, mejor de 3
- Premio (ganar): 20 Bytes, 25% Salud
- Premio (perder): 5 Bytes, 10% Salud

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
data class AppThemeColors(
    val name: String,
    val background: Color,
    val surface: Color,
    val primary: Color,
    val secondary: Color,
    val onPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val success: Color,
    val error: Color
)
```

### 8.2 Temas Disponibles (25)

| # | Tema | Background | Primary | Estilo |
|:-:|:-----|:-----------|:--------|:-------|
| 1 | Matrix Green | `#0C100D` | `#2E7D32` | 🌑 Oscuro |
| 2 | Galáctico | `#090A0F` | `#6200EA` | 🌑 Oscuro |
| 3 | Cyberpunk | `#0F0B1E` | `#F50057` | 🌑 Oscuro |
| 4 | Bosque Encantado | `#0D1F15` | `#388E3C` | 🌑 Oscuro |
| 5 | Sakura | `#FCE4EC` | `#E91E63` | 🌕 Claro |
| 6 | Minimalista | `#FFFFFF` | `#212121` | 🌕 Claro |
| 7 | Neón | `#000000` | `#39FF14` | 🌑 Oscuro |
| 8 | Océano | `#001F3F` | `#0074D9` | 🌑 Oscuro |
| 9 | Volcánico | `#2A0800` | `#FF4500` | 🌑 Oscuro |
| 10 | Ártico | `#E0FFFF` | `#00BFFF` | 🌕 Claro |
| 11 | Vaporwave | `#2B00FF` | `#00FFFF` | 🌑 Oscuro |
| 12 | Café | `#3E2723` | `#8D6E63` | 🌑 Oscuro |
| 13 | Retro | `#F4A460` | `#8B4513` | 🌕 Claro |
| 14 | Pixel Art | `#2C3E50` | `#E74C3C` | 🌑 Oscuro |
| 15 | Samurai | `#1C1C1C` | `#C62828` | 🌑 Oscuro |
| 16 | Medieval | `#2E2B2A` | `#FFD700` | 🌑 Oscuro |
| 17 | Desierto | `#EDC9AF` | `#D2691E` | 🌕 Claro |
| 18 | Aurora | `#0B192C` | `#00FF7F` | 🌑 Oscuro |
| 19 | Cristal | `#F0F8FF` | `#9370DB` | 🌕 Claro |
| 20 | Nocturno | `#000000` | `#0A84FF` | 🌑 Oscuro |
| 21 | Tropical | `#FFFAF0` | `#FF7F50` | 🌕 Claro |
| 22 | Otoño | `#5C2C16` | `#FFA500` | 🌑 Oscuro |
| 23 | Hacker | `#000000` | `#00FF00` | 🌑 Oscuro |
| 24 | Magma | `#1A0000` | `#FF0000` | 🌑 Oscuro |
| 25 | Fantasma | `#E8ECEF` | `#6C7A89` | 🌕 Claro |

### 8.3 Persistencia de Temas

Los temas se guardan en **DataStore** (migrado desde SharedPreferences en v2.0):
- `currentTheme` → Flow vía DataStore
- `unlockedThemes` → Flow vía DataStore
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

### 10.1 Adivina el Bit

```
Ronda 1 de 5
┌──────────────────┐
│        ?         │  ← Bit secreto oculto
└──────────────────┘
   [0]          [1]

→ Si aciertas: ✓ "El bit secreto era X."
→ Si fallas:   ✗ "Incorrecto. Era X."
```

**Premio:** `score × 4` Bytes, `score × 3`% Salud

### 10.2 Caza de Bugs

```
Tiempo: 10s    Bugs atrapados: 3
┌─────┬─────┬─────┐
│     │ 🐛 │     │
├─────┼─────┼─────┤
│     │     │     │
├─────┼─────┼─────┤
│     │     │     │
└─────┴─────┴─────┘
```

**Premio:** `score × 2` Bytes, `score × 1.5`% Salud (máx 30%)

### 10.3 Servidor, Script, Hacker

```
TÚ: 1 | CPU: 0 (Mejor de 3)

    TÚ        VS    COMPILADOR
  [Servidor]       [Hacker]

═══ Reglas ═══
Servidor 🔒 > Hacker 👤
Hacker   👤 > Script 📄
Script   📄 > Servidor 🔒
```

**Premio (ganar):** 20 Bytes, 25% Salud
**Premio (perder):** 5 Bytes, 10% Salud

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
    viewModel { PetViewModel(get(), get()) }
}
```

### 14.2 Consumo en MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
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
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {
    // No más AndroidViewModel ni getSharedPreferences manual
    // No más AppDatabase.getDatabase() en init
    // Todo inyectado por Koin
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

Nota: v2.0 requiere actualización del test porque `CodeTamagotchiScreen.kt` fue eliminado y reemplazado por `AppNavigation.kt`.

### 15.4 Tests de Instrumentación

**Archivo:** `ExampleInstrumentedTest.kt` — verifica `packageName`.

### 15.5 Cobertura Actual

| Tipo | Archivos | Estado |
|:-----|:---------|:-------|
| Unit test | 1 | ✅ Básico |
| Robolectric | 2 | ✅ Funcional |
| Screenshot | 1 | ⚠️ Requiere actualización |
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
- **Navegación:** Sistema de bottom nav con sealed class `Screen` (sin NavHost)
- **DI:** Migración a **Koin 4.0.2** (no usa Gradle plugin, compatible con AGP 9.x)
- **DataStore:** SharedPreferences migrado a **DataStore Preferences** (`UserPreferencesRepository`)

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
- Deprecation warnings eliminados (`fallbackToDestructiveMigration(false)`, Koin DSL `org.koin.core.module.dsl.viewModel`, `Icons.AutoMirrored.Filled.KeyboardArrowLeft`)
- Build: **0 warnings, 0 errors**

---

> **Code Tamagotchi v2.0** — Donde los bugs se convierten en mascotas y el código en cariño.  
> Documentación generada para desarrolladores y curiosos.  
> ¿Preguntas? Abre un issue en [GitHub](https://github.com/fguzman-stack/CodePet).
