# 📚 Documentación — Code Tamagotchi

> **Versión:** 1.0.0  
> **Plataforma:** Android (API 24+)  
> **Lenguaje:** Kotlin 2.2.10  
> **UI:** Jetpack Compose + Material 3  
> **Persistencia:** Room (SQLite)  
> **Arquitectura:** MVVM + Repository  
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
9. [Sistema de Retos](#9-sistema-de-retos)
10. [Minijuegos](#10-minijuegos)
11. [Sistema de Sonido](#11-sistema-de-sonido)
12. [Estados y Animaciones](#12-estados-y-animaciones)
13. [Notificaciones y Widgets](#13-notificaciones-y-widgets)
14. [Pruebas](#14-pruebas)
15. [Dependencias Externas](#15-dependencias-externas)
16. [Guía para Desarrolladores](#16-guía-para-desarrolladores)

---

## 1. Origen e Inspiración

### 1.1 Genesis

Code Tamagotchi nació como un proyecto generado inicialmente por **Google AI Studio**, un asistente de IA para prototipado rápido de apps Android. El código base original fue creado con fines educativos, combinando el concepto clásico de mascota virtual (Tamagotchi) con elementos de productividad para programadores.

### 1.2 Inspiraciones Directas

| Inspiración | Elemento adoptado |
|:------------|:-----------------|
| **Tamagotchi original (Bandai, 1996)** | Mascota que requiere cuidado constante, estados emocionales, decaimiento por abandono |
| **SUSH (Emotion Studio Inc.)** | Menú limpio centrado en la mascota, animación de rebote al tocar la mascota, corazón flotante como feedback visual, fondo personalizable por tema |
| **Duolingo** | Sistema de rachas (streaks), gamificación del aprendizaje |
| **Pomodoro Technique** | Temporizador de estudio con intervalos de 15/25/50 minutos |
| **Terminales hacker / Matrix** | Estética visual: fondos oscuros, tipografía monospace, colores verde neón, decoración tipo terminal |
| **Tamagotchi virtuales modernos (My Tamagotchi Forever, Pou)** | Estados emocionales con frases contextuales, sistema de alimentación y limpieza |

### 1.3 Propósito

Code Tamagotchi busca **gamificar el hábito de estudio en programación**. A diferencia de otras mascotas virtuales, aquí el progreso real del usuario (resolver retos de código, estudiar temas técnicos) se refleja directamente en la salud y felicidad de la mascota. Es un `console.log()` humano con sentimientos.

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

### 3.1 Patrón MVVM

```
┌──────────────────────────────────────────────────────────┐
│                      VIEW (Compose)                       │
│  CodeTamagotchiScreen.kt · OnboardingScreen.kt           │
│  Observa StateFlow del ViewModel · Emite eventos UI      │
├──────────────────────────────────────────────────────────┤
│                    VIEWMODEL                              │
│  PetViewModel.kt (AndroidViewModel)                      │
│  Lógica de negocio · Temporizador · Retos · Cuidados     │
│  SharedPreferences (temas, onboarding)                   │
│  StateFlow + mutableStateOf para estado reactivo          │
├──────────────────────────────────────────────────────────┤
│                    REPOSITORY                             │
│  PetRepository.kt                                        │
│  Abstracción entre ViewModel y capa de datos             │
│  Expone Flow<PetStateEntity?> y Flow<List<StudySession>> │
├──────────────────────────────────────────────────────────┤
│                     DATA (Room)                           │
│  AppDatabase.kt (singleton thread-safe)                  │
│  PetDao.kt (@Dao con consultas SQL reactivas)            │
│  PetStateEntity.kt · StudySessionEntity.kt               │
└──────────────────────────────────────────────────────────┘
```

### 3.2 Flujo de Datos

```
👤 Usuario toca la pantalla
        ↓
🎨 Composable (ViewportCard)
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
├── MainActivity.kt              ← Entry point, edge-to-edge, decide onboarding vs home
├── data/
│   ├── ChallengesData.kt        ← 11 retos de programación (Kotlin, JS, PHP, Python)
│   ├── SpecialChallengesData.kt ← 6 retos de algoritmos/lógica
│   ├── database/
│   │   ├── AppDatabase.kt       ← Room DB singleton con patrón double-check locking
│   │   ├── PetDao.kt            ← DAO con queries reactivas (Flow)
│   │   ├── PetStateEntity.kt    ← Entidad: estado completo de la mascota
│   │   └── StudySessionEntity.kt← Entidad: sesiones de estudio
│   └── repository/
│       └── PetRepository.kt     ← Capa de datos unificada
├── ui/
│   ├── screens/
│   │   ├── CodeTamagotchiScreen.kt  ← ~2800 líneas, UI principal completa
│   │   └── OnboardingScreen.kt      ← Pantalla de bienvenida y naming
│   ├── theme/
│   │   ├── Color.kt             ← Colores base Material 3
│   │   ├── Theme.kt             ← Tema Material 3 con soporte dinámico (Android 12+)
│   │   ├── ThemeConfig.kt       ← 23 temas personalizados (colores, nombres)
│   │   └── Type.kt              ← Tipografía base
│   └── viewmodel/
│       └── PetViewModel.kt      ← ~530 líneas, toda la lógica de negocio
└── util/
    └── SoundManager.kt          ← Efectos de sonido con ToneGenerator
```

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
¿hasSeenOnboarding?
    ├── NO  → OnboardingScreen (nombrar mascota)
    │           ↓
    │       completeOnboarding(petName)
    │           ↓
    │       SharedPreferences: has_seen_onboarding = true
    │           ↓
    │       Room: crear PetStateEntity(name = petName, defaults)
    │           ↓
    │       loadChallengesForLanguage("Kotlin")
    │           ↓
    │       → CodeTamagotchiScreen
    │
    └── SÍ  → CodeTamagotchiScreen
                ↓
            init { }
                ↓
            Room: leer petState
                ├── null  → crear PetStateEntity default → loadChallenges("Kotlin")
                └── exist → applyDecay(petState) → loadChallenges(petState.language)
```

### 4.2 Mapa de Navegación (Bottom Navigation)

```
┌─────────────────────────────────────────────────────┐
│                    TOP APP BAR                       │
│           [DeveloperMode] Code Tamagotchi [⚙️]      │
├─────────────────────────────────────────────────────┤
│                                                      │
│   ┌─────────────────────────────────────────────┐   │
│   │              VIEWPORT CARD                   │   │
│   │  (mascota + nombre + nivel + XP + quote)     │   │
│   │  ┌─────────────────────────────────────┐     │   │
│   │  │        🖼️ MASCOTA ANIMADA           │     │   │
│   │  │  (bounce al tocarla + ❤️ flotante)  │     │   │
│   │  └─────────────────────────────────────┘     │   │
│   │  ▓▓▓▓▓░░░ Vida    ▓▓░░░░░ Alimento ▓▓▓░░░░  │   │
│   │  💰 150 B   🔥 5 días   [😴]                 │   │
│   │  [Acariciar] [Limpiar] [Ajustar] [🎮Jugar]  │   │
│   └─────────────────────────────────────────────┘   │
│                                                      │
│   (siempre visible, scroll si es necesario)          │
│                                                      │
├─────────────────────────────────────────────────────┤
│  [🏠] Inicio   [💻] Aprender   [⏱️] Estudio   [🛒] │
│                        Tienda                        │
└─────────────────────────────────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Al tocar Aprender / Estudio / Tienda:

┌─────────────────────────────────────────────────────┐
│   (overlay semitransparente con slide-up)            │
│                                                      │
│   ┌─────────────────────────────────────────────┐   │
│   │           TERMINAL HUB                        │   │
│   │  ● ● ●  terminal@codey:~  v1.0.0            │   │
│   │  ─────────────────────────────────────────   │   │
│   │                                               │   │
│   │  [LEARN] → pestañas: Retos Normales /        │   │
│   │             Retos Especiales                  │   │
│   │  [STUDY] → pestañas: Temporizador / Bitácora │   │
│   │  [SHOP]  → lista de productos                │   │
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

La app **no** tiene manejo especial de `onPause`/`onResume` para el temporizador. Si el usuario cierra la app mientras el temporizador de estudio corre, el temporizador se pierde (no hay recompensa). El estado de la mascota persiste en Room.

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
    val currentStatus: String = "HAPPY"    // Estado: HAPPY, SAD, SICK, HUNGRY, SLEEPING, STUDYING, EXCITED
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
- `STUDYING → HAPPY/SAD/HUNGRY/SICK`: completeStudySession() (depende de niveles)
- `SLEEPING → HAPPY`: toggleSleep() (si energía ≥ 100) o automático al despertar
- `SAD → HAPPY`: Acariciar, dar comida, mejorar energía
- `HUNGRY → HAPPY`: Dar comida (hunger > 30)
- `SICK → HAPPY`: Dar medicina (health > 30)
- `HAPPY → EXCITED`: Acertar reto especial, desbloquear tema
- Cualquier estado → `SLEEPING`: toggleSleep()
- Cualquier estado → `STUDYING`: startStudyTimer()

### 5.3 Sistema de Decaimiento (applyDecay)

Cuando la app se inicia o se reanuda, se calcula el tiempo transcurrido:

```kotlin
val elapsedMs = now - state.lastUpdated
val hours = elapsedMs / (1000 * 60 * 60)
```

**Efectos por hora transcurrida:**

| Condición | Efecto |
|:----------|:-------|
| **Despierto** (status ≠ SLEEPING) | Hambre -4%/h, Energía -3%/h, Salud base -3.64%/h |
| **Durmiendo** (status = SLEEPING) | Energía +15%/h, Hambre -1.5%/h |
| **Hambre llega a 0%** | Salud extra -5%/h (daño por inanición) |
| **Energía < 10%** | Salud extra -2%/h |
| **Sin estudio > 36h** | Racha (streak) = 0 |
| **Sin estudio > 48h** | Salud extra -3%/h |
| **Energía ≥ 100% durmiendo** | Despierta automáticamente (status = HAPPY) |

**Propósito:** Esto evita que el usuario abandone la app por días y vuelva a encontrar la mascota igual. Fomenta el cuidado diario, como un Tamagotchi real.

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
// 3 corazones = 60% salud
// 0 corazones = 0% salud
```

---

## 6. Módulos y Funcionalidades

### 6.1 Onboarding (`OnboardingScreen.kt`)

**Propósito:** Primera experiencia del usuario.

**Flujo:**
1. Muestra imagen de mascota feliz + texto de bienvenida
2. Input para nombre (máx 15 caracteres)
3. Botón "Compilar y Empezar" (deshabilitado si nombre vacío)
4. Al completar: guarda `has_seen_onboarding` en SharedPreferences + crea `PetStateEntity` en Room

**Detalles técnicos:**
- Usa `mascota_happy.png` de drawable
- Fondo verde oscuro `#0C100D`
- Tipografía monospace en toda la pantalla

### 6.2 Pantalla Principal (`CodeTamagotchiScreen.kt`)

**Propósito:** Centro de toda la interacción con la mascota.

**Componentes:**

| Componente | Líneas | Función |
|:-----------|:------:|:--------|
| `CodeTamagotchiScreen` | 52-240 | Scaffold + navegación + diálogos |
| `ViewportCard` | 255-600 | Mascota + meters + acciones (todo en uno) |
| `MeterItem` | 640-690 | Barra de progreso individual |
| `TerminalHub` | 815-870 | Contenedor para paneles LEARN/STUDY/SHOP |
| `StudyHub` | 875-910 | Pestañas Temporizador / Bitácora |
| `TimerPanel` | 950-1190 | Pomodoro + selección de tema |
| `LearnHub` | 910-945 | Pestañas Retos Normales / Especiales |
| `QuizPanel` | 1195-1375 | Retos de programación multiselección |
| `ShopPanel` | 1380-1485 | Tienda con 4 productos + comida |
| `LogsPanel` | 1490-1600 | Historial de sesiones de estudio |
| `PersonalizeDialog` | 1605-1805 | Diálogo de configuración (nombre, lenguaje, tema) |
| `CareCenterSection` | 1810-2030 | Botones cuidar + alimentar + minijuegos (reemplazado por inline en ViewportCard) |
| `MinigamesDialog` | 2030-2145 | Selector de 3 minijuegos |
| `GameOptionCard` | 2145-2185 | Tarjeta de opción de juego |
| `BinaryGuessGame` | 2190-2325 | Adivina el bit (5 rondas) |
| `BugSmasherGame` | 2330-2465 | Caza bugs 3×3 (10 segundos) |
| `RockPaperSciGame` | 2470-2625 | Servidor, Script, Hacker (mejor de 3) |
| `SpecialChallengesPanel` | 2630-2815 | Retos especiales con desbloqueo de temas |

### 6.3 Pet Tap (Nuevo desde v1.0)

Cuando el usuario toca la mascota:

```
👆 Touch en la imagen
    ↓
onPetTap()
    ↓
┌─ viewModel.petThePet() ──────────────────────────┐
│  +10% Energía · +3% Salud                         │
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

### 6.4 Panel LEARN (Retos de Programación)

**Retos Normales (`ChallengesData.kt`):**

| ID | Lenguaje | Tipo | Título |
|:--:|:---------|:----:|:-------|
| 1 | Kotlin | TRIVIA | Inmutabilidad (val vs var) |
| 2 | Kotlin | DEBUG | Null safety (?.) |
| 3 | Kotlin | TRIVIA | Scope functions (run) |
| 4 | JavaScript | TRIVIA | typeof null |
| 5 | JavaScript | DEBUG | Comparación estricta (===) |
| 6 | JavaScript | TRIVIA | Closures |
| 7 | PHP | TRIVIA | Sintaxis de variables ($) |
| 8 | PHP | DEBUG | Fusión de arrays (array_merge) |
| 9 | PHP | TRIVIA | Comparación estricta (== vs ===) |
| 10 | Python | TRIVIA | Tipos mutables (tupla) |
| 11 | Python | DEBUG | List comprehension |

**Mecánica:**
- Se cargan 3 retos aleatorios del lenguaje seleccionado
- El usuario elige entre 4 opciones
- Feedback inmediato con explicación
- Acierto: +20-25 Bytes, +15-20 XP, +15% Alimento, +20% Salud
- Error: solo feedback, sin penalización

**Retos Especiales (`SpecialChallengesData.kt`):**

| ID | Tipo | Título |
|:--:|:----:|:-------|
| 101 | ALGORITHM | Encontrar el Único (bitwise) |
| 102 | ALGORITHM | Knapsack 0/1 |
| 103 | ALGORITHM | Invertir Lista Enlazada |
| 104 | ARCHITECTURE | Teorema CAP |
| 105 | ALGORITHM | Floyd's Cycle Detection |
| 106 | ALGORITHM | Árboles AVL |

**Mecánica especial:**
- Un reto aleatorio cada vez
- Al acertar: +50 Bytes, +10% Salud, **desbloquea un tema visual aleatorio**
- Los temas desbloqueados se guardan en SharedPreferences

### 6.5 Panel STUDY (Temporizador Pomodoro)

**Configuración:**
- Temas disponibles: Kotlin, JavaScript, PHP, Python, SQL, Clean Code, Git, Estructuras de Datos
- Duración: 15, 25 o 50 minutos

**Mecánica:**
```kotlin
startStudyTimer(minutes, topic) {
    status = "STUDYING"
    timerSecondsRemaining = minutes * 60
    // Cuenta regresiva cada 1 segundo
}

completeStudySession() {
    val baseBytes = minutes * 1
    val baseXP = minutes * 2
    val bonusBytes = if (minutes >= 25) 25 else 0
    val bonusXP = if (minutes >= 25) 50 else 0
    val energyCost = (minutes * 0.6f).coerceAtMost(40f)
    // streak++ si es un día nuevo y consecutivo
}
```

**Cálculo de racha (streak):**
```kotlin
if (primer estudio) → streak = 1
if (mismo día)      → streak sin cambios
if (día siguiente)  → streak += 1
if (día saltado)    → streak = 1 (se reinicia)
```

### 6.6 Panel SHOP (Tienda)

| Producto | Costo | Efecto |
|:---------|:-----:|:-------|
| Café Negro (CPU Booster) | 10 B | +20% Energía |
| Pizza de Código (Bytes Snack) | 15 B | +35% Alimento |
| Píldora Desbugueadora | 25 B | +30% Salud |
| Vacuna Super Compiler | 55 B | +75% Salud, +40% Alimento, +40% Energía |

**Comida adicional** (desde el menú de alimentación):
| Comida | Costo | Efecto |
|:-------|:-----:|:-------|
| Manzana Binaria | 2 B | +15% Alimento, +2% Energía |
| Pizza de Bytes | 6 B | +30% Alimento, +5% Energía |
| Sushi de Datos | 15 B | +55% Alimento, +5% Salud, +15% Energía |
| Café Espresso CPU | 5 B | -5% Alimento, +35% Energía |

### 6.7 Cuidados Gratuitos

| Acción | Efecto | Código |
|:-------|:-------|:-------|
| Acariciar (tocar mascota) | +10% Energía, +3% Salud | `petThePet()` |
| Limpiar | +6% Salud, +5% Energía | `cleanThePet()` |
| Dormir | Recupera energía con el tiempo | `toggleSleep()` → SLEEPING |
| Despertar | Vuelve al estado normal | `toggleSleep()` → HAPPY |

### 6.8 Minijuegos

**1. Adivina el Bit (`BinaryGuessGame`):**
- 5 rondas
- Cada ronda: un bit secreto (0 o 1)
- El usuario adivina
- Premio: `score × 4` Bytes, `score × 3`% Salud

**2. Caza de Bugs (`BugSmasherGame`):**
- Cuadrícula 3×3, 10 segundos
- Aparece un bug en una celda aleatoria
- Tocar el bug suma puntos y cambia de posición
- Premio: `score × 2` Bytes, `score × 1.5`% Salud (max 30%)

**3. Servidor, Script, Hacker (`RockPaperSciGame`):**
- Piedra, Papel, Tijera versión informática
- Servidor > Hacker > Script > Servidor
- Mejor de 3 rondas
- Premio (ganar): 20 Bytes, 25% Salud
- Premio (perder): 5 Bytes, 10% Salud

---

## 7. Base de Datos y Persistencia

### 7.1 Room Database

**Archivo:** `AppDatabase.kt`

```kotlin
@Database(entities = [PetStateEntity::class, StudySessionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "code_tamagotchi_db")
                    .build().also { INSTANCE = it }
            }
        }
    }
}
```

**Nombre de la base de datos:** `code_tamagotchi_db`

**Tablas:**
- `pet_state` (1 fila, singleton)
- `study_sessions` (N filas, autoincremental)

### 7.2 PetDao

```kotlin
@Dao
interface PetDao {
    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    fun getPetState(): Flow<PetStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePetState(state: PetStateEntity)

    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySessionEntity)
}
```

### 7.3 SharedPreferences

Usadas para datos que no requieren Room:

| Key | Tipo | Default | Propósito |
|:----|:----:|:-------:|:----------|
| `has_seen_onboarding` | Boolean | false | ¿Ya pasó el onboarding? |
| `app_theme` | String | "Matrix Green" | Tema visual activo |
| `unlocked_themes` | Set<String> | {"Matrix Green"} | Temas desbloqueados |

**Archivo:** `codetamagotchi_prefs`

### 7.4 Estructura de Carpetas de Assets

```
app/src/main/assets/pet/
├── README.md
├── normal/                          ← Estilo 2D ilustrado
│   ├── static/                      ← PNGs estáticos (usados en res/drawable/)
│   │   ├── mascota_happy.png
│   │   ├── mascota_sleeping.png
│   │   ├── mascota_studying.png
│   │   ├── mascota_sick.png
│   │   ├── mascota_sad.png
│   │   ├── mascota_hungry.png
│   │   └── mascota_excited.png
│   └── animations/                  ← Animaciones (futuro)
└── pixel_art/                       ← Estilo pixel art (premium/secreto)
    ├── static/
    └── animations/
```

---

## 8. Sistema de Temas

### 8.1 Estructura

```kotlin
data class AppThemeColors(
    val name: String,           // Nombre visible
    val background: Color,      // Fondo general
    val surface: Color,         // Superficies (cards)
    val primary: Color,         // Color primario (acento)
    val secondary: Color,       // Color secundario
    val onPrimary: Color,       // Texto sobre primary
    val textPrimary: Color,     // Texto principal
    val textSecondary: Color,   // Texto secundario
    val success: Color,         // Color de éxito
    val error: Color            // Color de error
)
```

### 8.2 Temas Disponibles (23)

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

### 8.3 Cómo se Aplican los Temas

```kotlin
val appTheme = ThemeRegistry.getTheme(currentTheme)
Scaffold(containerColor = appTheme.background)
// El resto de colores se usan en TerminalHub, botones, etc.
```

El sistema de tema **no** sobreescribe el Theme de Material 3. Los colores del `AppThemeColors` se aplican manualmente a elementos específicos (background del Scaffold, borde del TerminalHub, color de botones). El Material Theme sigue usando su propio color scheme (dinámico en Android 12+).

### 8.4 Desbloqueo de Temas

- **Tema inicial:** "Matrix Green" (siempre disponible)
- **Desbloqueables:** Al acertar un reto especial, se desbloquea un tema aleatorio de los bloqueados
- **Persistencia:** `SharedPreferences` → `unlocked_themes`

---

## 9. Sistema de Retos

### 9.1 Estructura de un Reto

```kotlin
data class CodingChallenge(
    val id: Int,
    val language: String,           // "Kotlin", "JavaScript", "PHP", "Python", "Lógica Especial"
    val type: String,               // "TRIVIA", "DEBUG", "ALGORITHM", "ARCHITECTURE"
    val title: String,              // Título del reto
    val question: String,           // Enunciado
    val codeSnippet: String?,       // Código de ejemplo (solo DEBUG)
    val options: List<String>,       // 4 opciones de respuesta
    val correctAnswerIndex: Int,    // Índice de la respuesta correcta (0-3)
    val explanation: String         // Explicación detallada
)
```

### 9.2 Carga de Retos

```kotlin
loadChallengesForLanguage("Kotlin") {
    val filtered = ChallengesData.challenges.filter { language == it.language }
    _activeChallenges.value = filtered.shuffled().take(3)
    _currentChallengeIndex.value = 0
}
```

Siempre se muestran 3 retos por lenguaje, en orden aleatorio. Al completar los 3, se recargan otros 3.

### 9.3 Sistema de Recompensas

```kotlin
// Reto normal TRIVIA
earnedBytes = 20
earnedXp = 15

// Reto normal DEBUG
earnedBytes = 25
earnedXp = 20

// Ambos tipos restauran:
hungerRestore = 15f
healthRestore = 20f
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

→ Si aciertas: ✓ "¡Excelente! El bit secreto era X."
→ Si fallas:   ✗ "Incorrecto. El bit secreto era X."
```

**Premio final:** `score × 4` Bytes, `score × 3`% Salud

### 10.2 Caza de Bugs

```
Tiempo: 10s    Bugs: 3
┌─────┬─────┬─────┐
│     │ 🐛 │     │  ← Bug visible en celda aleatoria
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

| Evento | Método | Tono | Duración |
|:-------|:-------|:-----|:---------|
| ✅ Respuesta correcta | `playSuccess()` | `TONE_PROP_ACK` | 100ms |
| ❌ Respuesta incorrecta | `playError()` | `TONE_PROP_BEEP2` | 150ms |
| ⬆️ Subida de nivel / Logro | `playLevelUp()` | 2× `TONE_PROP_BEEP` + `TONE_PROP_ACK` | 100+150+200ms |
| 👆 Clic / Interfaz | `playClick()` | `TONE_DTMF_A` | 50ms |
| 🛒 Comprar | `playBuy()` | `TONE_PROP_BEEP` | 100ms |
| 😴 Dormir | `playSleep()` | 2× `TONE_CDMA_SOFT_ERROR_LITE` | 100+1000ms |

### 11.3 Ciclo de Vida

```kotlin
// Creación en PetViewModel
val soundManager = SoundManager()

// Liberación
override fun onCleared() {
    soundManager.release()
}
```

---

## 12. Estados y Animaciones

### 12.1 Animaciones Infinitas (por estado)

Cada estado tiene una animación perpetua definida con `rememberInfiniteTransition`:

| Estado | Eje X | Eje Y | Escala | Velocidad |
|:-------|:-----:|:-----:|:------:|:---------:|
| HAPPY | 0 | ±10px flotación | 1.0 | 600ms |
| SLEEPING | 0 | ±5px flotación lenta | 1.0 | 2000ms |
| STUDYING | 0 | 0 | 1.0 | — |
| SICK | ±5px temblor | 0 | 1.0 | 100ms |
| SAD | 0 | 0 | 1.0 | — |
| HUNGRY | 0 | 0 | 0.95↔1.05 pulso | 1000ms |
| EXCITED | 0 | ±10px flotación rápida | 1.0 | 300ms |

### 12.2 Animaciones One-Shot (al interactuar)

| Acción | Animación | Duración |
|:-------|:----------|:---------|
| Tocar mascota | Bounce (escala 1→1.25→1) + corazón flotante | 900ms |
| Abrir panel | Slide-up (desde abajo) + fade-in | 300ms |
| Cerrar panel | Slide-down + fade-out | 300ms |
| Feedback reto | Cambio de color en la opción seleccionada | Instantáneo |
| Barra XP | `animateFloatAsState` (transición suave) | 300ms |
| Barras de estado | `animateFloatAsState` (transición suave) | 300ms |

### 12.3 Sistema de Quotes (Diálogos Contextuales)

Cada estado tiene frases temáticas que se eligen aleatoriamente:

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

- ❌ Notificaciones push (ni Firebase Cloud Messaging)
- ❌ Notificaciones locales (ni AlarmManager ni WorkManager)
- ❌ Widgets de Android (AppWidgetProvider)
- ❌ Recordatorios de cuidado

### 13.2 Por qué no están implementados

El proyecto se originó en Google AI Studio como un prototipo funcional. Las notificaciones y widgets fueron omitidos para mantener el scope inicial manejable. El sistema de **decaimiento (applyDecay)** actúa como un sustituto parcial: al abrir la app después de horas, la mascota refleja el tiempo perdido.

### 13.3 Plan para Futuras Versiones

- **Notificaciones locales:** WorkManager para checkear cada 4h si la mascota necesita atención, usando `state.health < 50f` o `state.hunger < 50f`
- **Widget:** AppWidgetProvider mostrando la mascota + 3 barras de estado + botón rápido para acariciar
- **Recordatorio de estudio:** Notificación programada a la hora que el usuario suele estudiar

---

## 14. Pruebas

### 14.1 Tests Unitarios

**Archivo:** `ExampleUnitTest.kt`

```kotlin
class ExampleUnitTest {
    @Test fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
```

Test básico de prueba (sin lógica específica de la app aún).

### 14.2 Tests Robolectric

**Archivo:** `ExampleRobolectricTest.kt`

```kotlin
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {
    @Test fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Code Tamagotchi", appName)
    }
}
```

Test de integración con Robolectric que verifica que el string `app_name` se cargue correctamente.

### 14.3 Screenshot Tests (Roborazzi)

**Archivo:** `GreetingScreenshotTest.kt`

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test fun greeting_screenshot() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = PetViewModel(application)
        composeTestRule.setContent {
            MyApplicationTheme {
                CodeTamagotchiScreen(viewModel = viewModel)
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/screenshots/greeting.png")
    }
}
```

Genera una captura de pantalla de la UI completa en `app/src/test/screenshots/greeting.png`.

### 14.4 Tests de Instrumentación

**Archivo:** `ExampleInstrumentedTest.kt`

```kotlin
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.tamagotchi.code", appContext.packageName)
    }
}
```

### 14.5 Cobertura Actual

| Tipo | Archivos | Estado |
|:-----|:---------|:-------|
| Unit test | 1 | ✅ Básico |
| Robolectric | 2 | ✅ Funcional |
| Screenshot | 1 | ✅ Genera PNG |
| Instrumentación | 1 | ✅ Básico |

**Nota:** No hay tests unitarios para `PetViewModel`, `PetRepository` o `PetDao` todavía. Tampoco hay tests parametrizados para `calculateLevel()` o `applyDecay()`.

---

## 15. Dependencias Externas

### 15.1 Lista Completa (`libs.versions.toml`)

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

### 15.2 Firebase: Qué se usa y qué no

**Usado (opcional):**
- `firebase-ai` — AI generativa (sin implementar en UI)
- `firebase-appcheck` + `firebase-appcheck-recaptcha` — Seguridad
- Firebase BOM — Gestión de versiones

**Comentado/No usado:**
- `firebase-auth` + `credentials` + `googleid` — Autenticación
- `firebase-firestore` — Base de datos cloud
- `play-services-location` — GPS
- `accompanist-permissions` — Permisos
- `camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view` — Cámara
- `coil-compose` — Carga de imágenes
- `datastore-preferences` — DataStore (se usa SharedPreferences)
- `navigation-compose` — Navegación (no se usa, es single-screen)

---

## 16. Guía para Desarrolladores

### 16.1 Cómo Compilar

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

**Nota sobre Firebase:** El archivo `google-services.json` no está incluido. El build advierte pero no falla gracias a:
```kotlin
googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }
```

### 16.2 Cómo Probar

```bash
# Tests unitarios
./gradlew testDebugUnitTest

# Tests de instrumentación (requiere emulador/dispositivo)
./gradlew connectedDebugAndroidTest

# Screenshot test (genera PNG)
./gradlew testDebugUnitTest --tests "*.GreetingScreenshotTest"
```

### 16.3 Cómo Agregar un Nuevo Reto

1. Abrir `ChallengesData.kt`
2. Agregar un nuevo `CodingChallenge` a la lista:

```kotlin
CodingChallenge(
    id = 12,
    language = "Kotlin",
    type = "TRIVIA",
    title = "Tu Nuevo Reto",
    question = "¿Pregunta?",
    options = listOf("Opción A", "Opción B", "Opción C", "Opción D"),
    correctAnswerIndex = 2,
    explanation = "Explicación detallada..."
)
```

### 16.4 Cómo Agregar un Nuevo Tema

1. Abrir `ThemeConfig.kt`
2. Agregar a `ThemeRegistry.allThemes`:

```kotlin
AppThemeColors(
    "Nombre del Tema",
    background = Color(0xFF000000),
    surface = Color(0xFF111111),
    primary = Color(0xFF00FF00),
    secondary = Color(0xFF00AA00),
    onPrimary = Color.Black,
    textPrimary = Color.White,
    textSecondary = Color.Gray,
    success = Color.Green,
    error = Color.Red
)
```

### 16.5 Cómo Agregar un Nuevo Minijuego

1. Crear el composable en `CodeTamagotchiScreen.kt`
2. Agregar la opción en `MinigamesDialog`:
   - Agregar `GameOptionCard` con título, descripción, ícono
   - Agregar `when` branch en `selectedGame`
3. El juego debe llamar a `viewModel.completeMinigame(bytes, happiness, energy)` al terminar

### 16.6 Convenciones de Código

- **Idioma:** Español (código, strings, comentarios)
- **UI:** Jetpack Compose con Material 3
- **Estado:** ViewModel con `StateFlow` + `mutableStateOf`
- **Persistencia:** Room para datos complejos, SharedPreferences para config
- **Async:** Corrutinas en `viewModelScope`
- **Tests:** Robolectric + Roborazzi
- **Tipografía:** `FontFamily.Monospace` para textos de terminal
- **Tests:** `testTag()` en elementos interactivos
- **Animaciones:** `rememberInfiniteTransition` para loops, `Animatable` para one-shot

---

> **Code Tamagotchi** — Donde los bugs se convierten en mascotas y el código en cariño.  
> Documentación generada para desarrolladores y curiosos.  
> ¿Preguntas? Abre un issue en [GitHub](https://github.com/fguzman-stack/CodePet).
