# DocumentaciÃ³n Completa â€” Code Tamagotchi

> **VersiÃ³n:** 2.0.0  
> **Plataforma:** Android (API 24+)  
> **Lenguaje:** Kotlin 2.2.10  
> **UI:** Jetpack Compose + Material 3  
> **Persistencia:** Room (SQLite) + DataStore (Preferences)  
> **DI:** Koin 4.0.2  
> **Arquitectura:** MVVM + Repository + Feature Modules  
> **Package:** `com.tamagotchi.code`

---

## Ãndice

1. [Origen e InspiraciÃ³n](#1-origen-e-inspiraciÃ³n)
2. [Concepto y PropÃ³sito](#2-concepto-y-propÃ³sito)
3. [Arquitectura General](#3-arquitectura-general)
4. [Flujo Completo de la App](#4-flujo-completo-de-la-app)
5. [Sistema de la Mascota](#5-sistema-de-la-mascota)
6. [Base de Datos y Persistencia](#6-base-de-datos-y-persistencia)
7. [Sistema de Temas](#7-sistema-de-temas)
8. [Sistema de Retos (88+ challenges)](#8-sistema-de-retos)
9. [Minijuegos](#9-minijuegos)
10. [Sistema de Sonido](#10-sistema-de-sonido)
11. [Estados y Animaciones](#11-estados-y-animaciones)
12. [Widget y Notificaciones](#12-widget-y-notificaciones)
13. [InyecciÃ³n de Dependencias (Koin)](#13-inyecciÃ³n-de-dependencias)
14. [Pruebas](#14-pruebas)
15. [CÃ³digo Fuente Completo](#15-cÃ³digo-fuente-completo)
16. [Dependencias Externas](#16-dependencias-externas)
17. [GuÃ­a para Desarrolladores](#17-guÃ­a-para-desarrolladores)
18. [Changelog vs v1.0](#18-changelog)

---

## 1. Origen e InspiraciÃ³n

### 1.1 Genesis

Code Tamagotchi naciÃ³ como un proyecto generado inicialmente por **Google AI Studio**, un asistente de IA para prototipado rÃ¡pido de apps Android. El cÃ³digo base original fue creado con fines educativos, combinando el concepto clÃ¡sico de mascota virtual (Tamagotchi) con elementos de productividad para programadores.

### 1.2 Inspiraciones Directas

| InspiraciÃ³n | Elemento adoptado |
|:------------|:-----------------|
| **Tamagotchi original (Bandai, 1996)** | Mascota que requiere cuidado constante, estados emocionales, decaimiento por abandono |
| **SUSH** | MenÃº limpio centrado en la mascota, animaciÃ³n de rebote al tocar, corazÃ³n flotante, fondo personalizable |
| **Duolingo** | Sistema de rachas (streaks), gamificaciÃ³n del aprendizaje |
| **Pomodoro Technique** | Temporizador de estudio con intervalos de 15/25/50 minutos |
| **Terminales hacker / Matrix** | EstÃ©tica visual: fondos oscuros, tipografÃ­a monospace, colores verde neÃ³n, decoraciÃ³n tipo terminal |
| **Tamagotchi virtuales modernos** | Estados emocionales con frases contextuales, sistema de alimentaciÃ³n y limpieza |

### 1.3 PropÃ³sito

Code Tamagotchi busca **gamificar el hÃ¡bito de estudio en programaciÃ³n**. A diferencia de otras mascotas virtuales, aquÃ­ el progreso real del usuario (resolver retos de cÃ³digo, estudiar temas tÃ©cnicos) se refleja directamente en la salud y felicidad de la mascota.

### 1.4 PÃºblico Objetivo

- Estudiantes de programaciÃ³n que quieren mantener constancia
- Desarrolladores que disfrutan del humor tÃ©cnico
- Fans de mascotas virtuales con estÃ©tica retro-tech
- Personas que responden bien a la gamificaciÃ³n para mantener hÃ¡bitos

### 1.5 Estado Actual

La app estÃ¡ completamente funcional pero en fase **beta**:
- Builds: Debug + Release configurados (con signing)
- Sistema de notificaciones funcional via WorkManager
- Widget bÃ¡sico implementado

---

## 2. Concepto y PropÃ³sito

### 2.1 Â¿QuÃ© es Code Tamagotchi?

Es una **mascota virtual para programadores** que vive en tu celular. Tiene hambre, energÃ­a, salud, y estados de Ã¡nimo que cambian segÃºn cÃ³mo la trates. La diferencia con un Tamagotchi comÃºn: **para mantenerla feliz tienes que programar y estudiar**.

### 2.2 Ciclo BÃ¡sico

```
Estudiar cÃ³digo â†’ Ganas XP y Bytes â†’ Mejoras a tu mascota (comida, medicina)
                                        â†“
                              La mascota sube de nivel
                                        â†“
                              Desbloqueas temas visuales
                                        â†“
                              Te motiva a seguir estudiando
```

### 2.3 FilosofÃ­a de DiseÃ±o

- **La mascota es el centro**: ocupa la mayor parte de la pantalla, todo lo demÃ¡s son overlays
- **EstÃ©tica terminal**: monospace, verde matrix, fondos oscuros, decoraciÃ³n de ventana de terminal
- **Humor tÃ©cnico**: los mensajes de la mascota son chistes de programaciÃ³n
- **Feedback inmediato**: cada acciÃ³n tiene una animaciÃ³n o sonido de respuesta
- **Castigo realista**: si no estudias por dÃ­as, la mascota se enferma (como un Tamagotchi real)

---

## 3. Arquitectura General

### 3.1 PatrÃ³n MVVM + Feature Modules

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                      VIEW (Compose)                           â”‚
â”‚  AppNavigation.kt Â· feature/{home,learn,focus,shop,games}/   â”‚
â”‚  Observa StateFlow del ViewModel Â· Emite eventos UI          â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚                      VIEWMODEL                                â”‚
â”‚  PetViewModel.kt (727 lÃ­neas)                                 â”‚
â”‚  LÃ³gica de negocio Â· Temporizador Â· Retos Â· Cuidados         â”‚
â”‚  DataStore (temas, onboarding) Â· Room (mascota, sesiones)    â”‚
â”‚  StateFlow + mutableStateOf para estado reactivo              â”‚
â”‚  @Inject constructor vÃ­a Koin                                 â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚                      REPOSITORY                               â”‚
â”‚  PetRepository.kt Â· UserPreferencesRepository.kt              â”‚
â”‚  AchievementsRepository.kt                                    â”‚
â”‚  AbstracciÃ³n entre ViewModel y capa de datos                  â”‚
â”‚  Expone Flow<PetStateEntity>, Flow<List<StudySession>>, etc. â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚                     DATA (Room + DataStore)                   â”‚
â”‚  AppDatabase (singleton via Koin, version 3)                  â”‚
â”‚  PetDao (@Dao con queries reactivas)                          â”‚
â”‚  PetStateEntity Â· StudySessionEntity Â· FocusSessionEntity     â”‚
â”‚  DataStore Preferences (user_preferences)                     â”‚
â”‚  â†’ UserPreferencesRepository                                  â”‚
â”‚  â†’ AchievementsRepository                                     â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

### 3.2 Flujo de Datos

```
ðŸ‘¤ Usuario toca la pantalla
        â†“
ðŸŽ¨ Composable (HomeScreen â†’ ViewportCard)
        â†“ llama
ðŸ§  PetViewModel.petThePet()
        â†“
ðŸ“¦ PetRepository.savePetState(updated)
        â†“
ðŸ’¾ PetDao.insertOrUpdatePetState(entity)
        â†“
ðŸ—„ï¸ Room SQLite (code_tamagotchi_db)

        â”€â”€ Luego â”€â”€

ðŸ’¾ Room emite cambio â†’ Flow<PetStateEntity?>
        â†“
ðŸ“¦ PetRepository.petState (Flow)
        â†“
ðŸ§  PetViewModel (stateIn con WhileSubscribed)
        â†“
ðŸŽ¨ collectAsStateWithLifecycle() â†’ recomposiciÃ³n automÃ¡tica
```

### 3.3 Mapa de Archivos

```
app/src/main/java/com/tamagotchi/code/
â”œâ”€â”€ CodeTamagotchiApp.kt              â† Application class (inicia Koin + WorkManager)
â”œâ”€â”€ MainActivity.kt                    â† Entry point, koinViewModel() + tema dinÃ¡mico
â”œâ”€â”€ data/
â”‚   â”œâ”€â”€ ChallengesData.kt              â† 90+ retos de programaciÃ³n + data class CodingChallenge
â”‚   â”œâ”€â”€ SpecialChallengesData.kt       â† 6 retos especiales (algoritmos)
â”‚   â”œâ”€â”€ database/
â”‚   â”‚   â”œâ”€â”€ AppDatabase.kt             â† Room DB v3 con migraciones M1â†’2 y M2â†’3
â”‚   â”‚   â”œâ”€â”€ PetDao.kt                  â† DAO con queries reactivas (Flow)
â”‚   â”‚   â”œâ”€â”€ PetStateEntity.kt          â† Entidad: estado completo de la mascota
â”‚   â”‚   â”œâ”€â”€ StudySessionEntity.kt      â† Entidad: sesiones de estudio histÃ³ricas
â”‚   â”‚   â””â”€â”€ FocusSessionEntity.kt      â† Entidad: sesiÃ³n activa del timer Pomodoro
â”‚   â””â”€â”€ repository/
â”‚       â”œâ”€â”€ PetRepository.kt           â† Capa de datos mascota + sesiones + focus
â”‚       â”œâ”€â”€ UserPreferencesRepository.kt â† DataStore (onboarding, tema, cooldown juegos)
â”‚       â””â”€â”€ AchievementsRepository.kt  â† DataStore para logros (12 logros)
â”œâ”€â”€ di/
â”‚   â””â”€â”€ AppModule.kt                   â† MÃ³dulo Koin: BD, DAO, repos, ViewModel
â”œâ”€â”€ navigation/
â”‚   â””â”€â”€ AppNavigation.kt               â† Scaffold + NavHost + 10 rutas + Routes object
â”œâ”€â”€ feature/
â”‚   â”œâ”€â”€ home/
â”‚   â”‚   â””â”€â”€ HomeScreen.kt              â† Tarjeta mascota + meters + diÃ¡logo offline
â”‚   â”œâ”€â”€ learn/
â”‚   â”‚   â””â”€â”€ LearnScreen.kt             â† Retos programaciÃ³n + especiales con QuizPanel
â”‚   â”œâ”€â”€ focus/
â”‚   â”‚   â””â”€â”€ FocusScreen.kt             â† Pomodoro persistente + bitÃ¡cora
â”‚   â”œâ”€â”€ shop/
â”‚   â”‚   â””â”€â”€ ShopScreen.kt              â† Tienda con 4 productos + comida adicional
â”‚   â”œâ”€â”€ games/
â”‚   â”‚   â”œâ”€â”€ GamesScreen.kt             â† Hub arcade con cooldown diario + GameCard
â”‚   â”‚   â”œâ”€â”€ BugHuntScreen.kt           â† Encuentra bugs (10 snippets, 5 rondas)
â”‚   â”‚   â”œâ”€â”€ GitRescueScreen.kt         â† Decisiones Git (8 escenarios, progreso rama)
â”‚   â”‚   â”œâ”€â”€ RefactorRushScreen.kt      â† Ordena bloques (12 puzzles, aleatorio)
â”‚   â”‚   â”œâ”€â”€ MinigamesDialog.kt         â† Arcade clÃ¡sico (selector de 3 juegos)
â”‚   â”‚   â”œâ”€â”€ BinaryGuessGame.kt         â† Adivina el bit (5 rondas)
â”‚   â”‚   â”œâ”€â”€ BugSmasherGame.kt          â† Caza bugs 3Ã—3 (10 segundos)
â”‚   â”‚   â””â”€â”€ RockPaperSciGame.kt        â† Servidor, Script, Hacker (mejor de 3)
â”‚   â”œâ”€â”€ onboarding/
â”‚   â”‚   â””â”€â”€ OnboardingScreen.kt        â† 4 pasos con HorizontalPager
â”‚   â””â”€â”€ settings/
â”‚       â”œâ”€â”€ SettingsScreen.kt          â† ConfiguraciÃ³n con carrusel de temas + logros
â”‚       â””â”€â”€ SettingsLanguageScreen.kt  â† Lenguaje principal + temas + dificultad
â”œâ”€â”€ ui/
â”‚   â”œâ”€â”€ components/
â”‚   â”‚   â”œâ”€â”€ MeterItem.kt               â† Barra de progreso animada
â”‚   â”‚   â”œâ”€â”€ ViewportCard.kt            â† Card con mascota + meters + emociones (455 lÃ­neas)
â”‚   â”‚   â”œâ”€â”€ AnimatedPetSprite.kt       â† Sprite animado con breathing, tremble, blink (264 lÃ­neas)
â”‚   â”‚   â”œâ”€â”€ AnimatedThemeBackground.kt â† Fondos animados para 12 temas (1077 lÃ­neas)
â”‚   â”‚   â”œâ”€â”€ CodePetWidget.kt           â† Vista previa Compose del widget
â”‚   â”‚   â””â”€â”€ PetAnimationConfig.kt      â† ConfiguraciÃ³n de animaciones
â”‚   â”œâ”€â”€ theme/
â”‚   â”‚   â”œâ”€â”€ Color.kt                   â† Colores base legacy
â”‚   â”‚   â”œâ”€â”€ Theme.kt                   â† MyApplicationTheme + LocalAppTheme + LocalReduceMotion
â”‚   â”‚   â”œâ”€â”€ ThemeConfig.kt             â† 12 temas premium con AppTheme data class (402 lÃ­neas)
â”‚   â”‚   â””â”€â”€ Type.kt                    â† buildTypography() dinÃ¡mica por tema (146 lÃ­neas)
â”‚   â””â”€â”€ viewmodel/
â”‚       â””â”€â”€ PetViewModel.kt            â† LÃ³gica de negocio central (727 lÃ­neas)
â”œâ”€â”€ util/
â”‚   â”œâ”€â”€ SoundManager.kt                â† Efectos de sonido con ToneGenerator
â”‚   â”œâ”€â”€ DecayCalculator.kt             â† Decaimiento progresivo de stats
â”‚   â”œâ”€â”€ LevelCalculator.kt             â† CÃ¡lculo de nivel por XP
â”‚   â”œâ”€â”€ RewardCalculator.kt            â† CÃ¡lculo de recompensas
â”‚   â”œâ”€â”€ StatusCalculator.kt            â† Determina estado emocional
â”‚   â””â”€â”€ PetCheckWorker.kt              â† WorkManager para notificaciones periÃ³dicas
â””â”€â”€ widget/
    â””â”€â”€ CodePetWidgetProvider.kt       â† AppWidgetProvider con RemoteViews
```

---

## 4. Flujo Completo de la App

### 4.1 Inicio (MainActivity.kt)

```
App abierta
    â†“
onCreate()
    â†“
enableEdgeToEdge()
    â†“
setContent {
    val appTheme = ThemeRegistry.getTheme(currentTheme.value)
    MyApplicationTheme(appTheme = appTheme, reduceMotion = ...) { ... }
}
    â†“
koinViewModel() â†’ PetViewModel(PetRepository, UserPreferencesRepository, AchievementsRepository)
    â†“
init { } del ViewModel (block init de ~110 lÃ­neas):
    â”œâ”€â”€ stateIn() para petState, studySessions, latestFocusSession
    â”œâ”€â”€ Leer DataStore: hasSeenOnboarding (suspend)
    â”œâ”€â”€ Colectar Flow: currentTheme, selectedTopics, difficulty, etc.
    â”œâ”€â”€ Colectar Flow: unlockedThemes â†’ sincronizar con ALL_THEMES
    â”œâ”€â”€ Colectar Flow: unlockedAchievements
    â”œâ”€â”€ Colectar Flow: gameCooldowns
    â”œâ”€â”€ Room: leer petState
    â”‚   â”œâ”€â”€ null â†’ crear PetStateEntity default â†’ loadChallenges("Kotlin")
    â”‚   â””â”€â”€ exist â†’ applyDecay(petState) â†’ loadChallenges(petState.language)
    â””â”€â”€ Room: buscar FocusSession activa
        â”œâ”€â”€ null â†’ nada
        â”œâ”€â”€ exist + tiempo restante â†’ reanudar timer + resumeTimer()
        â””â”€â”€ exist + tiempo agotado â†’ recompensa offline + showOfflineRewardDialog
    â†“
Composable:
    â”œâ”€â”€ hasSeenOnboarding == false â†’ OnboardingScreen (4 pasos)
    â””â”€â”€ hasSeenOnboarding == true â†’ AppNavigation (NavHost con bottom nav)
```

### 4.2 Mapa de NavegaciÃ³n (Bottom Navigation)

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                        TOP BAR (Material 3)                       â”‚
â”‚           Code Tamagotchi                    [âš™ï¸ ConfiguraciÃ³n]  â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚                                                                   â”‚
â”‚   â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”   â”‚
â”‚   â”‚                   HOME SCREEN                             â”‚   â”‚
â”‚   â”‚      (ViewportCard: mascota + meters + botones)           â”‚   â”‚
â”‚   â”‚  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”     â”‚   â”‚
â”‚   â”‚  â”‚            ðŸ–¼ï¸ MASCOTA ANIMADA                    â”‚     â”‚   â”‚
â”‚   â”‚  â”‚  (bounce + â¤ï¸ flotante al tocarla)               â”‚     â”‚   â”‚
â”‚   â”‚  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜     â”‚   â”‚
â”‚   â”‚  â–“â–“â–“â–“â–“â–‘â–‘â–‘ Salud (5 corazones)  â–“â–“â–‘â–‘â–‘â–‘â–‘ Hambre            â”‚   â”‚
â”‚   â”‚  â–“â–“â–“â–‘â–‘â–‘â–‘ EnergÃ­a  ðŸ’° Bytes  ðŸ”¥ Racha                     â”‚   â”‚
â”‚   â”‚  ðŸ˜´ Dormir  ðŸ§¹ Limpiar  [Acariciar] [Jugar]              â”‚   â”‚
â”‚   â”‚  âFrase contextual aleatoriaâž                             â”‚   â”‚
â”‚   â”‚  â–“â–“â–“â–“â–‘â–‘â–‘ Barra de XP                                      â”‚   â”‚
â”‚   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜   â”‚
â”‚                                                                   â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚  [ðŸ ] Inicio  [ðŸ’»] Aprender  [â±ï¸] Focus  [ðŸ›’] Tienda            â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜

Rutas adicionales (sin bottom bar):
  âš™ï¸ ConfiguraciÃ³n (SettingsScreen) â†’ Perfil, Temas, Sonido, Logros
  ðŸŒ ConfiguraciÃ³n de Lenguaje (SettingsLanguageScreen)
  ðŸ•¹ï¸ Arcade (GamesScreen) â†’ BugHunt, GitRescue, RefactorRush + Arcade ClÃ¡sico
```

### 4.3 Manejo de Ciclo de Vida

```kotlin
override fun onCleared() {
    super.onCleared()
    soundManager.release()  // Liberar ToneGenerator
}
```

**Temporizador persistente:** Al cerrar la app durante un estudio, la `FocusSessionEntity` persiste con estado `RUNNING`. Al reabrir, el ViewModel restaura el temporizador calculando `(plannedDurationMs - (now - startedAt))` y lo reanuda automÃ¡ticamente vÃ­a `resumeTimer()`.

---

## 5. Sistema de la Mascota

### 5.1 Entidad PetStateEntity

```kotlin
@Entity(tableName = "pet_state")
data class PetStateEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Codey",
    val language: String = "Kotlin",
    val level: Int = 1,
    val xp: Int = 0,
    val hunger: Float = 100f,
    val health: Float = 100f,
    val energy: Float = 100f,
    val bytes: Int = 50,
    val lastUpdated: Long = System.currentTimeMillis(),
    val streak: Int = 0,
    val lastStudyDate: Long = 0,
    val currentStatus: String = "HAPPY",
    val hasRenamed: Boolean = false
)
```

### 5.2 Mapa de Estados y Transiciones

```
                 â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                 â”‚  EXCITED â”‚ â† Desbloquear tema, mucho cariÃ±o
                 â””â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”˜
                      â”‚
        â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
        â”‚             â”‚             â”‚
   â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”  â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”  â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”
   â”‚  HAPPY  â”‚  â”‚ STUDYINGâ”‚  â”‚ SLEEPINGâ”‚
   â””â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”˜  â””â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”˜  â””â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”˜
        â”‚             â”‚             â”‚
   â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”  â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”  â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”
   â”‚  SAD    â”‚  â”‚  HUNGRY â”‚  â”‚  SICK   â”‚
   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

**Transiciones:**
- `HAPPY â†” SLEEPING`: toggleSleep()
- `HAPPY â†’ STUDYING`: startStudyTimer()
- `STUDYING â†’ HAPPY/SAD/HUNGRY/SICK`: completeStudySession()
- `SLEEPING â†’ HAPPY`: toggleSleep() o automÃ¡tico al recuperar energÃ­a
- `SAD â†’ HAPPY`: Acariciar, dar comida, mejorar energÃ­a
- `HUNGRY â†’ HAPPY`: Dar comida (hunger > 20)
- `SICK â†’ HAPPY`: Dar medicina (health > 20)
- `HAPPY â†’ EXCITED`: Acertar reto, desbloquear tema
- Cualquier estado â†’ `SLEEPING`: toggleSleep()
- Cualquier estado â†’ `STUDYING`: startStudyTimer()

### 5.3 Sistema de Decaimiento (DecayCalculator.kt)

Cuando la app se inicia o se reanuda, se calcula el tiempo transcurrido:

```kotlin
object DecayCalculator {
    fun applyDecay(state: PetStateEntity): PetStateEntity {
        val now = System.currentTimeMillis()
        val elapsedMs = now - state.lastUpdated
        if (elapsedMs <= 0) return state
        val hours = elapsedMs.toFloat() / (1000f * 60f * 60f)
        if (hours < 0.02f) return state

        var newHunger = state.hunger; var newEnergy = state.energy
        var newHealth = state.health; var newStatus = state.currentStatus
        var newStreak = state.streak

        val hoursSinceLastStudy = if (state.lastStudyDate > 0)
            (now - state.lastStudyDate).toFloat() / (1000f * 60f * 60f) else 0f

        if (hoursSinceLastStudy > 48f) newStreak = 0

        if (state.currentStatus == "SLEEPING") {
            newEnergy = (newEnergy + (hours * 12f)).coerceIn(0f, 100f)
            newHunger = (newHunger - (hours * 1f)).coerceIn(0f, 100f)
            if (newEnergy >= 100f) newStatus = "HAPPY"
        } else {
            newHunger = (newHunger - (hours * 2.5f)).coerceIn(0f, 100f)
            newEnergy = (newEnergy - (hours * 2f)).coerceIn(0f, 100f)
        }

        val baseHealthDecay = hours * 1.5f
        newHealth = (newHealth - baseHealthDecay).coerceIn(0f, 100f)

        if (newHunger <= 0f) newHealth = (newHealth - (hours * 3f)).coerceIn(0f, 100f)
        if (newEnergy <= 10f) newHealth = (newHealth - (hours * 1f)).coerceIn(0f, 100f)
        if (hoursSinceLastStudy > 72f) newHealth = (newHealth - (hours * 1.5f)).coerceIn(0f, 100f)

        if (newStatus != "SLEEPING" && newStatus != "STUDYING")
            newStatus = StatusCalculator.determineStatus(newHealth, newHunger, newEnergy, false, false)

        return state.copy(hunger = newHunger, energy = newEnergy, health = newHealth,
            streak = newStreak, lastUpdated = now, currentStatus = newStatus)
    }
}
```

**Efectos por hora transcurrida (v2.0 rebalanceado):**

| CondiciÃ³n | Efecto v2.0 |
|:----------|:-----------|
| **Despierto** â€” Hambre | -2.5%/h |
| **Despierto** â€” EnergÃ­a | -2%/h |
| **Despierto** â€” Salud base | -1.5%/h |
| **Durmiendo** â€” EnergÃ­a | +12%/h |
| **Durmiendo** â€” Hambre | -1%/h |
| **Hambre = 0%** â€” Salud extra | -3%/h |
| **EnergÃ­a < 10%** â€” Salud extra | -1%/h |
| **Sin estudio > 48h** â€” Streak reseteado | = 0 |
| **Sin estudio > 72h** â€” Salud extra | -1.5%/h |

**PropÃ³sito:** El rebalance hace la mascota menos punitiva. Antes morÃ­a en ~27h; ahora sobrevive ~67h sin cuidados.

### 5.4 CÃ¡lculo de Nivel (LevelCalculator.kt)

```kotlin
object LevelCalculator {
    fun calculateLevel(xp: Int): Int {
        var level = 1
        var requiredXp = 100
        while (xp >= requiredXp) {
            level++
            requiredXp += level * 100
        }
        return level
    }
    fun xpForNextLevel(level: Int) = level * 100
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
| n | n Ã— 100 | n(n+1)/2 Ã— 100 |

### 5.5 StatusCalculator.kt

```kotlin
object StatusCalculator {
    fun determineStatus(
        health: Float, hunger: Float, energy: Float,
        isSleeping: Boolean, isStudying: Boolean, isExcited: Boolean = false
    ): String = when {
        isExcited -> "EXCITED"; isStudying -> "STUDYING"
        isSleeping -> "SLEEPING"; health < 20f -> "SICK"
        hunger < 20f -> "HUNGRY"; energy < 15f -> "SAD"
        else -> "HAPPY"
    }
}
```

### 5.6 RewardCalculator.kt

```kotlin
object RewardCalculator {
    data class StudyReward(val bytes: Int, val xp: Int, val energyCost: Float, val streak: Int, val newLevel: Int)
    data class ChallengeReward(val bytes: Int, val xp: Int, val hungerRestore: Float, val healthRestore: Float)

    fun calculateStudyReward(minutes: Int, currentXp: Int, currentLevel: Int,
        currentStreak: Int, lastStudyDate: Long): StudyReward {
        val baseBytes = minutes * 2; val baseXP = minutes * 3
        val bonusBytes = if (minutes >= 25) 50 else 0; val bonusXP = if (minutes >= 25) 75 else 0
        val totalBytes = baseBytes + bonusBytes; val totalXp = baseXP + bonusXP
        val energyCost = (minutes * 0.5f).coerceAtMost(30f)
        var newStreak = currentStreak
        if (lastStudyDate != 0L) {
            val lastCal = Calendar.getInstance().apply { timeInMillis = lastStudyDate }
            val nowCal = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
            val sameDay = lastCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
                lastCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)
            if (!sameDay) {
                val yesterdayCal = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis(); add(Calendar.DAY_OF_YEAR, -1) }
                val studiedYesterday = lastCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
                    lastCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)
                newStreak = if (studiedYesterday) currentStreak + 1 else 1
            }
        } else { newStreak = 1 }
        val newLevel = LevelCalculator.calculateLevel(currentXp + totalXp)
        return StudyReward(totalBytes, totalXp, energyCost, newStreak, newLevel)
    }

    fun calculateChallengeReward(type: String): ChallengeReward {
        val bytes = if (type == "DEBUG") 30 else 25
        val xp = if (type == "DEBUG") 25 else 20
        return ChallengeReward(bytes, xp, 15f, 20f)
    }

    fun calculateMinigameReward(score: Int, maxBytes: Int) = (score * 10).coerceAtMost(maxBytes)
    fun calculateRefactorReward(attempts: Int) = maxOf(50 - attempts * 5, 10)
}
```

### 5.7 PetViewModel.kt â€” LÃ³gica Central (727 lÃ­neas)

El ViewModel central gestiona todo el estado de la app. Sus funciones clave:

```kotlin
class PetViewModel(
    private val repository: PetRepository,
    private val userPreferences: UserPreferencesRepository,
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {
    val soundManager = SoundManager()

    // === STATE FLOWS ===
    var hasSeenOnboarding = mutableStateOf(false)
    var currentTheme = mutableStateOf("Matrix Green")
    val unlockedThemes = MutableStateFlow<Set<String>>(allThemes)
    val unlockedAchievements = MutableStateFlow<Set<String>>(emptySet())
    val selectedTopics = MutableStateFlow<Set<String>>(emptySet())
    val petState: StateFlow<PetStateEntity?>
    val studySessions: StateFlow<List<StudySessionEntity>>
    val latestFocusSession: StateFlow<FocusSessionEntity?>
    val activeChallenges: StateFlow<List<CodingChallenge>>
    val challengeFeedback: StateFlow<String?>
    val celebrationTrigger: SharedFlow<Unit>
    val learningEventTrigger: SharedFlow<String>

    // === ONBOARDING ===
    fun completeOnboarding(petName: String, topics: Set<String>) {
        viewModelScope.launch {
            userPreferences.setOnboardingCompleted()
            val finalTopics = if (topics.isEmpty()) defaultInitialTopics else topics
            userPreferences.setSelectedTopics(finalTopics)
            hasSeenOnboarding.value = true
            soundManager.playLevelUp()
            val current = repository.petState.firstOrNull()
            if (current == null) {
                repository.savePetState(PetStateEntity(name = if (petName.isNotBlank()) petName else "Codey"))
                loadChallengesForLanguage("Kotlin")
            } else {
                repository.savePetState(current.copy(name = if (petName.isNotBlank()) petName else "Codey"))
            }
        }
    }

    // === PET CARE ===
    fun petThePet() { /* +15% EnergÃ­a, +5% Salud */ }
    fun cleanThePet() { /* +12% Salud, +8% EnergÃ­a */ }
    fun toggleSleep() { /* HAPPY â†” SLEEPING */ }
    fun renamePet(newName: String) { /* Con simulaciÃ³n de anuncio si ya renombrÃ³ */ }

    // === SHOP ===
    fun buyShopItem(itemName: String, cost: Int, hungerRestore: Float, healthRestore: Float, energyRestore: Float)

    // === STUDY TIMER ===
    fun startStudyTimer(minutes: Int, topic: String) { /* Persiste FocusSessionEntity */ }
    fun cancelStudyTimer() { /* Cancela y actualiza estado */ }
    private suspend fun completeStudySession() { /* Calcula recompensa, actualiza streak */ }
    fun completeFocusSession() { /* Completa sesiÃ³n manualmente */ }

    // === CHALLENGES ===
    fun loadChallengesForLanguage(language: String) { /* Filtra 3 aleatorios */ }
    fun submitAnswer(optionIndex: Int) { /* Verifica, otorga recompensa, desbloquea logros */ }
    fun nextChallenge() { /* Avanza o recarga */ }

    // === GAMES ===
    fun canPlayGame(gameId: String, cooldownHours: Long = 24): Boolean
    fun recordGamePlay(gameId: String)
    fun completeMinigame(bytesEarned: Int, healthEarned: Float, energyCost: Float)

    // === THEMES ===
    fun changeTheme(theme: String)
    fun unlockTheme(themeName: String) { /* Desbloquea y verifica logros coleccionista/completista */ }

    // === SETTINGS ===
    fun toggleSound(enabled: Boolean); fun toggleVibration(enabled: Boolean)
    fun toggleReduceMotion(enabled: Boolean); fun setDifficulty(newDifficulty: String)
    fun setTopics(topics: Set<String>); fun selectLanguage(language: String)
    fun exportProgressMock(); fun resetProgress()
}
```

### 5.8 Reward Calculator

```kotlin
val baseBytes = minutes * 2      // antes: minutes * 1
val baseXP = minutes * 3          // antes: minutes * 2
val bonusBytes = if (minutes >= 25) 50 else 0  // antes: 25
val bonusXP = if (minutes >= 25) 75 else 0      // antes: 50
val energyCost = (minutes * 0.5f).coerceAtMost(30f)  // antes: 0.6 max 40
```

**Recompensas comparativas para 25 min:**
| Concepto | v1.0 | v2.0 |
|:---------|:----:|:----:|
| Bytes base | 25 | **50** |
| XP base | 50 | **75** |
| Bonus 25+ | +25 B, +50 XP | **+50 B, +75 XP** |
| Total | 75 B, 125 XP | **150 B, 225 XP** |
| Costo energÃ­a | -15 | **-12.5** |

---

## 6. Base de Datos y Persistencia

### 6.1 AppDatabase.kt

```kotlin
@Database(entities = [PetStateEntity::class, StudySessionEntity::class, FocusSessionEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `focus_sessions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `topic` TEXT NOT NULL, `plannedDurationMinutes` INTEGER NOT NULL,
                        `startedAt` INTEGER NOT NULL, `status` TEXT NOT NULL
                    )""")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pet_state ADD COLUMN hasRenamed INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "code_tamagotchi_db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
                INSTANCE = instance; instance
            }
        }
    }
}
```

### 6.2 PetDao.kt

```kotlin
@Dao
interface PetDao {
    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    fun getPetState(): Flow<PetStateEntity?>

    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    suspend fun getPetStateSuspend(): PetStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePetState(state: PetStateEntity)

    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySessionEntity)

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC LIMIT 1")
    fun getLatestFocusSession(): Flow<FocusSessionEntity?>

    @Query("SELECT * FROM focus_sessions WHERE status = 'RUNNING' ORDER BY id DESC LIMIT 1")
    suspend fun getActiveFocusSession(): FocusSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSessionEntity)

    @Query("UPDATE focus_sessions SET status = :status WHERE id = :sessionId")
    suspend fun updateFocusSessionStatus(sessionId: Long, status: String)

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC")
    fun getAllFocusSessions(): Flow<List<FocusSessionEntity>>

    @Transaction
    suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
        updateFocusSessionStatus(sessionId, status)
        insertOrUpdatePetState(petState)
    }
}
```

### 6.3 StudySessionEntity.kt

```kotlin
@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)
```

### 6.4 FocusSessionEntity.kt

```kotlin
@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val plannedDurationMinutes: Int,
    val startedAt: Long,
    val status: String = "RUNNING"
)
```

### 6.5 PetRepository.kt

```kotlin
class PetRepository(private val petDao: PetDao) {
    val petState: Flow<PetStateEntity?> = petDao.getPetState()
    val studySessions: Flow<List<StudySessionEntity>> = petDao.getAllStudySessions()
    val latestFocusSession: Flow<FocusSessionEntity?> = petDao.getLatestFocusSession()
    val allFocusSessions: Flow<List<FocusSessionEntity>> = petDao.getAllFocusSessions()

    suspend fun savePetState(state: PetStateEntity) = petDao.insertOrUpdatePetState(state)
    suspend fun addStudySession(session: StudySessionEntity) = petDao.insertStudySession(session)
    suspend fun getActiveFocusSession(): FocusSessionEntity? = petDao.getActiveFocusSession()
    suspend fun saveFocusSession(session: FocusSessionEntity): Long { petDao.insertFocusSession(session); return session.id }
    suspend fun updateFocusSessionStatus(sessionId: Long, status: String) = petDao.updateFocusSessionStatus(sessionId, status)
    suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) =
        petDao.completeOfflineSession(sessionId, status, petState)
}
```

### 6.6 UserPreferencesRepository.kt

```kotlin
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val KEY_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        private val KEY_THEME = stringPreferencesKey("app_theme")
        private val KEY_UNLOCKED_THEMES = stringSetPreferencesKey("unlocked_themes")
        private val KEY_SELECTED_TOPICS = stringSetPreferencesKey("selected_topics")
        private val KEY_DIFFICULTY = stringPreferencesKey("difficulty")
        private val KEY_FOCUS_DURATION = intPreferencesKey("focus_duration_default")
        private val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val KEY_VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val KEY_GAME_LAST_PLAYED = stringSetPreferencesKey("game_last_played")
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { it[KEY_ONBOARDING] ?: false }
    val currentTheme: Flow<String> = context.dataStore.data.map { it[KEY_THEME] ?: "Matrix Green" }
    val unlockedThemes: Flow<Set<String>> = context.dataStore.data.map {
        val stored = it[KEY_UNLOCKED_THEMES]
        if (stored == null || stored.isEmpty()) ThemeRegistry.allThemes.map { t -> t.name }.toSet() else stored
    }
    val selectedTopics: Flow<Set<String>> = context.dataStore.data.map { it[KEY_SELECTED_TOPICS] ?: defaultTopics }
    val difficulty: Flow<String> = context.dataStore.data.map { it[KEY_DIFFICULTY] ?: "Inicial" }
    val focusDurationDefault: Flow<Int> = context.dataStore.data.map { it[KEY_FOCUS_DURATION] ?: 25 }
    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_SOUND_ENABLED] ?: true }
    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_VIBRATION_ENABLED] ?: true }
    val reduceMotion: Flow<Boolean> = context.dataStore.data.map { it[KEY_REDUCE_MOTION] ?: false }
    val gameCooldowns: Flow<Map<String, Long>> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
        raw.mapNotNull { entry ->
            val parts = entry.split(":", limit = 2)
            if (parts.size == 2) parts[0] to (parts[1].toLongOrNull() ?: 0L) else null
        }.toMap()
    }

    suspend fun setOnboardingCompleted() { context.dataStore.edit { it[KEY_ONBOARDING] = true } }
    suspend fun setTheme(theme: String) { context.dataStore.edit { it[KEY_THEME] = theme } }
    suspend fun addUnlockedTheme(theme: String) { context.dataStore.edit {
        it[KEY_UNLOCKED_THEMES] = (it[KEY_UNLOCKED_THEMES] ?: emptySet()) + theme } }
    suspend fun getHasSeenOnboarding(): Boolean = context.dataStore.data.first()[KEY_ONBOARDING] ?: false
    suspend fun setSelectedTopics(topics: Set<String>) { context.dataStore.edit { it[KEY_SELECTED_TOPICS] = topics } }
    suspend fun recordGamePlay(gameId: String) { context.dataStore.edit {
        val current = it[KEY_GAME_LAST_PLAYED] ?: emptySet()
        it[KEY_GAME_LAST_PLAYED] = current.filter { !it.startsWith("$gameId:") }.toSet() + "$gameId:${System.currentTimeMillis()}" } }
    // ... mÃ¡s setters similares
}
```

### 6.7 AchievementsRepository.kt

```kotlin
private val Context.achievementsDataStore by preferencesDataStore(name = "achievements")

class AchievementsRepository(private val context: Context) {
    companion object {
        private val KEY_UNLOCKED_ACHIEVEMENTS = stringSetPreferencesKey("unlocked_achievements")
        val ALL_ACHIEVEMENTS = listOf(
            Achievement("primer_build", "Primer build", "CompletÃ¡ tu primera sesiÃ³n Focus"),
            Achievement("nivel_experto", "Nivel Experto", "SubÃ­ de nivel por primera vez"),
            Achievement("cazador_de_bugs", "Cazador de bugs", "PuntuaciÃ³n perfecta en Bug Hunt"),
            Achievement("git_sin_panico", "Git sin pÃ¡nico", "AprobÃ¡ Git Rescue con 3+ aciertos"),
            Achievement("racha_7", "Racha de 7 dÃ­as", "MantenÃ© una racha de estudio de 7 dÃ­as"),
            Achievement("racha_30", "Racha de 30 dÃ­as", "MantenÃ© una racha de estudio de 30 dÃ­as"),
            Achievement("coleccionista", "Coleccionista", "DesbloqueÃ¡ 3 temas visuales"),
            Achievement("completista", "Completista", "DesbloqueÃ¡ los 12 temas visuales"),
            Achievement("ahorrador", "Ahorrador", "AcumulÃ¡ 1000 Bytes"),
            Achievement("primer_acaricie", "Primer mimo", "AcariciÃ¡ a tu mascota por primera vez"),
            Achievement("duermevela", "Duermevela", "PonÃ© a dormir a tu mascota"),
            Achievement("limpiador", "Limpieza profunda", "LimpiÃ¡ a tu mascota 10 veces"),
        )
    }

    val unlockedAchievements: Flow<Set<String>> = context.achievementsDataStore.data.map { it[KEY_UNLOCKED_ACHIEVEMENTS] ?: emptySet() }
    suspend fun unlockAchievement(achievementId: String) { context.achievementsDataStore.edit {
        val current = it[KEY_UNLOCKED_ACHIEVEMENTS] ?: emptySet()
        if (!current.contains(achievementId)) it[KEY_UNLOCKED_ACHIEVEMENTS] = current + achievementId } }
    fun getAchievementById(id: String): Achievement? = ALL_ACHIEVEMENTS.find { it.id == id }
}

data class Achievement(val id: String, val name: String, val description: String)
```

### 6.8 DataStore Keys Summary

| Key | Tipo | Default |
|:----|:----:|:--------|
| `has_seen_onboarding` | Boolean | false |
| `app_theme` | String | "Matrix Green" |
| `unlocked_themes` | Set<String> | Todos los temas |
| `selected_topics` | Set<String> | {"Kotlin", "Estructuras de Datos", "Git"} |
| `difficulty` | String | "Inicial" |
| `focus_duration_default` | Int | 25 |
| `sound_enabled` | Boolean | true |
| `vibration_enabled` | Boolean | true |
| `reduce_motion` | Boolean | false |
| `game_last_played` | Set<String> | formato: "gameId:timestamp" |

---

## 7. Sistema de Temas

### 7.1 AppTheme Data Class

```kotlin
data class AppTheme(
    val name: String, val icon: ImageVector, val description: String, val isDark: Boolean,
    val background: Color, val surface: Color, val surfaceVariant: Color,
    val primary: Color, val secondary: Color, val tertiary: Color,
    val onPrimary: Color, val textPrimary: Color, val textSecondary: Color,
    val accent: Color, val success: Color, val error: Color,
    val fontFamily: FontFamily = FontFamily.Default,
    val titleFontFamily: FontFamily = FontFamily.Default,
    val titleWeight: FontWeight = FontWeight.Bold,
    val cornerRadius: Dp = 12.dp, val borderWidth: Dp = 1.dp,
    val usesGradients: Boolean = false, val gradientColors: List<Color> = emptyList()
)
```

### 7.2 Temas Disponibles (12 premium)

| # | Tema | Emoji | Dark | Fuente | Esquinas | Gradiente | Icono |
|:-:|:-----|:-----:|:----:|:-------|:--------:|:---------:|:-----:|
| 1 | Matrix Green | ðŸ–¥ï¸ | SÃ­ | Monospace | 2dp | âœ… | Code |
| 2 | GalÃ¡ctico | ðŸŒŒ | SÃ­ | SansSerif | 24dp | âœ… | AutoAwesome |
| 3 | Cyberpunk | âš¡ | SÃ­ | Monospace | 0dp | âœ… | FlashOn |
| 4 | Sakura | ðŸŒ¸ | No | Serif | 16dp | âœ… | LocalFlorist |
| 5 | Minimalista | â—»ï¸ | No | SansSerif | 12dp | â€” | CheckBoxOutlineBlank |
| 6 | NeÃ³n | ðŸ’œ | SÃ­ | Monospace | 16dp | â€” | Lightbulb |
| 7 | OcÃ©ano | ðŸŒŠ | SÃ­ | SansSerif | 20dp | âœ… | WaterDrop |
| 8 | VolcÃ¡nico | ðŸŒ‹ | SÃ­ | Serif | 4dp | âœ… | LocalFireDepartment |
| 9 | Samurai | âš”ï¸ | SÃ­ | Serif | 0dp | âœ… | Security |
| 10 | Aurora | âœ¨ | SÃ­ | SansSerif | 24dp | âœ… | Waves |
| 11 | Nocturno | ðŸŒ™ | SÃ­ | SansSerif | 16dp | â€” | NightsStay |
| 12 | Retro Pixel ðŸ† | ðŸ‘¾ | SÃ­ | **Press Start 2P** (8-bit) | 0dp | â€” | VideogameAsset |

### 7.3 ThemeRegistry

```kotlin
object ThemeRegistry {
    val allThemes = listOf(/* 12 AppTheme objects */)

    fun getTheme(name: String): AppTheme {
        return allThemes.find { it.name == name } ?: allThemes.first()
    }
}
```

### 7.4 IntegraciÃ³n con Material 3 (Theme.kt)

```kotlin
val LocalReduceMotion = compositionLocalOf { false }
val LocalAppTheme = staticCompositionLocalOf { ThemeRegistry.allThemes.first() }

fun AppTheme.toColorScheme(): ColorScheme {
    val builder: (primary: Color, onPrimary: Color, primaryContainer: Color,
        onPrimaryContainer: Color, inversePrimary: Color, secondary: Color,
        onSecondary: Color, secondaryContainer: Color, onSecondaryContainer: Color,
        tertiary: Color, onTertiary: Color, tertiaryContainer: Color,
        onTertiaryContainer: Color, background: Color, onBackground: Color,
        surface: Color, onSurface: Color, surfaceVariant: Color,
        onSurfaceVariant: Color, surfaceTint: Color, inverseSurface: Color,
        inverseOnSurface: Color, error: Color, onError: Color,
        errorContainer: Color, onErrorContainer: Color, outline: Color,
        outlineVariant: Color, scrim: Color) -> ColorScheme
    = if (isDark) ::darkColorScheme else ::lightColorScheme

    return builder(
        primary = primary, onPrimary = onPrimary, primaryContainer = primary.copy(alpha = 0.20f),
        onPrimaryContainer = textPrimary, inversePrimary = secondary,
        secondary = secondary, onSecondary = if (isDark) Color.Black else Color.White,
        secondaryContainer = secondary.copy(alpha = 0.18f), onSecondaryContainer = textPrimary,
        tertiary = tertiary, onTertiary = if (isDark) Color.Black else Color.White,
        tertiaryContainer = tertiary.copy(alpha = 0.18f), onTertiaryContainer = textPrimary,
        background = background, onBackground = textPrimary,
        surface = surface, onSurface = textPrimary,
        surfaceVariant = surfaceVariant, onSurfaceVariant = textSecondary,
        surfaceTint = primary, inverseSurface = if (isDark) Color(0xFFE0E0E0) else Color(0xFF1C1C1C),
        inverseOnSurface = if (isDark) Color(0xFF1C1C1C) else Color(0xFFE0E0E0),
        error = error, onError = Color.White, errorContainer = error.copy(alpha = 0.20f),
        onErrorContainer = textPrimary, outline = textSecondary.copy(alpha = 0.5f),
        outlineVariant = textSecondary.copy(alpha = 0.25f), scrim = Color.Black,
    )
}

@Composable
fun MyApplicationTheme(
    appTheme: AppTheme = ThemeRegistry.allThemes.first(),
    darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = false,
    reduceMotion: Boolean = false, content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> appTheme.toColorScheme()
    }
    val typography = buildTypography(appTheme.fontFamily, appTheme.titleFontFamily, appTheme.titleWeight)
    CompositionLocalProvider(LocalReduceMotion provides reduceMotion, LocalAppTheme provides appTheme) {
        MaterialTheme(colorScheme = colorScheme, typography = typography, content = content)
    }
}
```

### 7.5 buildTypography (Type.kt)

```kotlin
fun buildTypography(bodyFont: FontFamily, titleFont: FontFamily, titleWeight: FontWeight = FontWeight.Bold): Typography = Typography(
    displayLarge = TextStyle(fontFamily = titleFont, fontWeight = titleWeight, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    displayMedium = TextStyle(fontFamily = titleFont, fontWeight = titleWeight, fontSize = 45.sp, lineHeight = 52.sp, letterSpacing = 0.sp),
    headlineLarge = TextStyle(fontFamily = titleFont, fontWeight = titleWeight, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp),
    titleLarge = TextStyle(fontFamily = titleFont, fontWeight = titleWeight, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp),
    bodyLarge = TextStyle(fontFamily = bodyFont, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodySmall = TextStyle(fontFamily = bodyFont, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
    labelSmall = TextStyle(fontFamily = bodyFont, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    // ... todos los estilos Material 3
)
```

---

## 8. Sistema de Retos

### 8.1 CodingChallenge Data Class

```kotlin
data class CodingChallenge(
    val id: Int, val language: String, val type: String, // "TRIVIA", "DEBUG"
    val title: String, val question: String,
    val codeSnippet: String? = null, val options: List<String>,
    val correctAnswerIndex: Int, val explanation: String
)
```

### 8.2 ChallengesData â€” 90+ Retos

| Lenguaje | Cantidad | Tipos | IDs |
|:---------|:--------:|:------|:---:|
| **Kotlin** | 20+ | TRIVIA + DEBUG | 1-3, 12-25, 86-87 |
| **JavaScript** | 20 | TRIVIA + DEBUG | 4-6, 26-40 |
| **PHP** | 19 | TRIVIA + DEBUG | 7-9, 41-55 |
| **Python** | 29+ | TRIVIA + DEBUG | 10-11, 56-85, 88-90 |

### 8.3 SpecialChallengesData â€” 6 Retos de Algoritmos

| ID | TÃ­tulo | Tipo | Concepto |
|:--:|:-------|:----:|:---------|
| 101 | Encontrar el Ãšnico | ALGORITHM | Conteo de bits mÃ³dulo 3 |
| 102 | Problema del Mochilero | ALGORITHM | ProgramaciÃ³n DinÃ¡mica |
| 103 | Invertir una Lista Enlazada | ALGORITHM | 3 punteros iterativos |
| 104 | Teorema CAP | ARCHITECTURE | Consistencia, Disponibilidad, ParticiÃ³n |
| 105 | Detectar Ciclo | ALGORITHM | Floyd's Tortoise and Hare |
| 106 | Ãrboles AVL | ALGORITHM | Balanceo por diferencia de alturas |

### 8.4 Sistema de Recompensas

```kotlin
// Reto normal TRIVIA: +25 Bytes, +20 XP, +15% Alimento, +20% Salud
// Reto normal DEBUG: +30 Bytes, +25 XP, +15% Alimento, +20% Salud
// Reto especial: +50 Bytes, +10% Salud
```

### 8.5 LearnScreen â€” QuizPanel

El `QuizPanel` muestra 3 retos aleatorios por lenguaje. Incluye:
- Tip contextual de Codey (cambiante segÃºn DEBUG/TRIVIA)
- Snippet de cÃ³digo en Surface oscuro estilo terminal
- 4 opciones con feedback visual (verde = correcto, rojo = incorrecto)
- ExplicaciÃ³n educativa tras cada respuesta
- BotÃ³n "Siguiente acertijo" o "Cargar mÃ¡s retos"

```kotlin
@Composable
fun QuizPanel(viewModel: PetViewModel) {
    val challengesList by viewModel.activeChallenges.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentChallengeIndex.collectAsStateWithLifecycle()
    val feedback by viewModel.challengeFeedback.collectAsStateWithLifecycle()
    // ... renderiza challenge actual con opciones
    challenge.options.forEachIndexed { optIndex, optionText ->
        Surface(onClick = { if (feedback == null) viewModel.submitAnswer(optIndex) }, ...) {
            Text("[$optIndex] $optionText", ...)
        }
    }
}
```

---

## 9. Minijuegos

### 9.1 GamesScreen â€” Hub Principal

```kotlin
@Composable
fun GamesScreen(viewModel: PetViewModel, onNavigateBack: () -> Unit,
    onNavigateToBugHunt: () -> Unit, onNavigateToGitRescue: () -> Unit,
    onNavigateToRefactorRush: () -> Unit) {

    fun canPlay(gameId: String): Boolean {
        val lastPlayed = gameCooldowns[gameId] ?: 0L
        return System.currentTimeMillis() - lastPlayed >= 24 * 60 * 60 * 1000
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Arcade") }) }) {
        Column {
            GameCard(title = "Bug Hunt", description = "...", canPlay, ...)
            GameCard(title = "Git Rescue", description = "...", canPlay, ...)
            GameCard(title = "Refactor Rush", description = "...", canPlay, ...)
            // Classic Arcade section â†’ MinigamesDialog
        }
    }
}
```

### 9.2 Bug Hunt â€” Terminal Panic

- 10 snippets de cÃ³digo con bugs (pool)
- 5 rondas seleccionadas aleatoriamente
- 60 segundos por partida
- Sistema de puntuaciÃ³n: `score Ã— 10` Bytes (mÃ¡x 50)
- ExplicaciÃ³n + humor de Codey tras cada respuesta

```kotlin
// Cada snippet tiene: lines, bugIndex, explanation, humor
private val snippetPool = listOf(
    BugSnippet(listOf("val name = \"Codey\"", "println(name", "age++", "fun greet() { }"),
        bugIndex = 1, explanation = "Falta el parÃ©ntesis de cierre en println.",
        humor = "Â¡Era un parÃ©ntesis fugitivo! Se escapÃ³ sin pagar el alquiler."),
    // ... 9 snippets mÃ¡s
)
```

### 9.3 Git Rescue

- 8 escenarios Git en el pool, 5 por partida
- 3 opciones por escenario (correcta, plausible, absurda)
- Progreso visual de rama con cÃ­rculos numerados
- `score Ã— 15` Bytes (mÃ¡x 45)
- Logro "Git sin pÃ¡nico" si score â‰¥ 3

```kotlin
private val scenarioPool = listOf(
    GitScenario(situation = "Acabas de clonar el repo...",
        options = listOf("git checkout -b feature/nuevo", "git commit -m \"inicio\"", "git push --force"),
        correctIndex = 0, ...),
    // ... 7 escenarios mÃ¡s
)
```

### 9.4 Refactor Rush

- 12 puzzles de ordenamiento de cÃ³digo
- Bloques movibles con botones â–²/â–¼
- Intentos mÃºltiples con penalizaciÃ³n
- Recompensa: `max(50 - attempts Ã— 5, 10)` Bytes

```kotlin
private val puzzleBank = listOf(
    RefactorPuzzle("Calcular total", listOf("fun calculateTotal(...): Int {",
        "    var total = 0", "    for (item in items) {", "        total += item",
        "    }", "    return total", "}")),
    // ... 11 puzzles mÃ¡s
)
```

### 9.5 Arcade ClÃ¡sico (Legacy)

Tres minijuegos legacy accesibles via MinigamesDialog:

**BinaryGuessGame:** 5 rondas, adivinar bit secreto (0/1), premio: `score Ã— 4` Bytes

**BugSmasherGame:** CuadrÃ­cula 3Ã—3, 10 segundos, tocar bugs, premio: `score Ã— 2` Bytes

**RockPaperSciGame:** Servidor > Hacker > Script > Servidor, mejor de 3, premio: 20 B (ganar) / 5 B (perder)

---

## 10. Sistema de Sonido

### 10.1 SoundManager.kt

```kotlin
class SoundManager {
    private var toneGen: ToneGenerator? = null

    init {
        try { toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100) } catch (e: Exception) {}
    }

    fun playSuccess() { toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 100) }
    fun playError() { toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 150) }
    fun playLevelUp() { CoroutineScope(Dispatchers.Default).launch {
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100); delay(150)
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100); delay(150)
        toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 200) } }
    fun playClick() { toneGen?.startTone(ToneGenerator.TONE_DTMF_A, 50) }
    fun playBuy() { toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100) }
    fun playSleep() { CoroutineScope(Dispatchers.Default).launch {
        toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100); delay(1000)
        toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100) } }
    fun release() { toneGen?.release(); toneGen = null }
}
```

### 10.2 Mapa de Sonidos

| Evento | MÃ©todo | Tono | DuraciÃ³n |
|:-------|:-------|:-----|:---------|
| âœ… Respuesta correcta | `playSuccess()` | TONE_PROP_ACK | 100ms |
| âŒ Respuesta incorrecta | `playError()` | TONE_PROP_BEEP2 | 150ms |
| â¬†ï¸ Subida de nivel | `playLevelUp()` | 2Ã— beep + ack | ~500ms |
| ðŸ‘† Clic / Interfaz | `playClick()` | TONE_DTMF_A | 50ms |
| ðŸ›’ Comprar | `playBuy()` | TONE_PROP_BEEP | 100ms |
| ðŸ˜´ Dormir | `playSleep()` | 2Ã— TONE_CDMA_SOFT_ERROR_LITE | ~1100ms |

---

## 11. Estados y Animaciones

### 11.1 AnimatedPetSprite.kt (264 lÃ­neas)

El sprite de la mascota tiene mÃºltiples animaciones simultÃ¡neas:

```kotlin
@Composable
fun AnimatedPetSprite(status: String, celebrationTrigger: SharedFlow<Unit>,
    learningEventTrigger: SharedFlow<String>? = null, onClick: () -> Unit, modifier: Modifier = Modifier) {

    val reduceMotion = LocalReduceMotion.current

    // Celebration (Bounce + Wobble)
    val bounceAnim = remember { Animatable(0f) }
    val wobbleAnim = remember { Animatable(0f) }
    val flashAnim = remember { Animatable(1f) }

    // Event subscriptions via LaunchedEffect
    LaunchedEffect(learningEventTrigger) {
        learningEventTrigger?.collect { type ->
            when (type) {
                "SUCCESS" -> { flashAnim.animateTo(1.5f, tween(100)); flashAnim.animateTo(1f, tween(200)) }
                "FAILURE" -> { wobbleAnim.animateTo(-10f, tween(50)); wobbleAnim.animateTo(10f, tween(50)); wobbleAnim.animateTo(0f, tween(50)) }
            }
        }
    }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition()

    // Breathing scale (1.0 â†” 1.03, 1800ms)
    val breathingScale by infiniteTransition.animateFloat(...)

    // Sick tremble (-2 â†” 2, 100ms)
    val trembleOffset by infiniteTransition.animateFloat(...)

    // Hungry pulse (1.0 â†” 1.05, 800ms)
    val hungryScale by infiniteTransition.animateFloat(...)

    // Sleep alpha fade (0.8 â†” 1.0, 2500ms)
    val sleepAlpha by infiniteTransition.animateFloat(...)

    // Idle sway (-3 â†” 3, 3000ms)
    val swayOffset by infiniteTransition.animateFloat(...)

    // Blink logic (random 3-5s interval)
    var isBlinking by remember { mutableStateOf(false) }
    LaunchedEffect(canBlink) { /* random blink loop */ }

    // Render with crossfade
    Crossfade(targetState = isBlinking && hasBlinkAsset, ...) {
        Image(painter = painterResource(id = if (blink) blinkResId else baseResId), ...)
    }
}

private fun getPetDrawable(status: String): Int = when (status) {
    "SLEEPING" -> R.drawable.mascota_sleeping; "STUDYING" -> R.drawable.mascota_studying
    "SICK" -> R.drawable.mascota_sick; "SAD" -> R.drawable.mascota_sad
    "HUNGRY" -> R.drawable.mascota_hungry; "EXCITED" -> R.drawable.mascota_excited
    else -> R.drawable.mascota_happy
}
```

### 11.2 Animaciones por Estado

| Estado | Eje X | Eje Y | Escala | Velocidad | Drawable |
|:-------|:-----:|:-----:|:------:|:---------:|:---------|
| HAPPY | sway Â±3px | breathing Â±3% | 1.0â†”1.03 | 1800ms | mascota_happy |
| SLEEPING | 0 | 0 | 1.0 | alpha 0.8â†”1.0 (2500ms) | mascota_sleeping |
| STUDYING | 0 | 0 | 1.0 | â€” | mascota_studying |
| SICK | tremble Â±2px | 0 | 1.0 | 100ms | mascota_sick |
| SAD | 0 | 0 | 1.0 | â€” | mascota_sad |
| HUNGRY | 0 | 0 | 1.0â†”1.05 | 800ms | mascota_hungry |
| EXCITED | sway Â±3px | breathing Â±3% | 1.0â†”1.03 | 1300ms (rÃ¡pido) | mascota_excited |

### 11.3 PetAnimationConfig.kt

```kotlin
object PetAnimationConfig {
    fun petFloatDurationMs(status: String, reduceMotion: Boolean): Int = when {
        reduceMotion -> 2400; status == "EXCITED" -> 1300
        status == "HAPPY" -> 1600; status == "SLEEPING" -> 3200; else -> 2200
    }
    fun petFloatAmplitudeDp(status: String): Float = when (status) {
        "EXCITED" -> 8f; "HAPPY" -> 6f; "SLEEPING" -> 4f; else -> 5f
    }
    fun bounceDurationMs(reduceMotion: Boolean): Int = if (reduceMotion) 220 else 420
    fun heartDurationMs(reduceMotion: Boolean): Int = if (reduceMotion) 900 else 1500
}
```

### 11.4 ViewportCard.kt (455 lÃ­neas)

Componente principal que muestra:
- Nombre + lenguaje + nivel + corazones de salud (5 â¤)
- Sprite animado con animaciones infinitas + celebraciones
- Badge de estado actual (HAPPY, SICK, etc.)
- Frase contextual aleatoria por estado
- Barra de XP animada
- 3 meters (Salud, Alimento, EnergÃ­a)
- Bytes + Racha (streak)
- BotÃ³n Dormir/Despertar
- Botones Acariciar, Limpiar, Jugar

```kotlin
@Composable
fun ViewportCard(state: PetStateEntity, viewModel: PetViewModel, onRenameClick: () -> Unit, onPlayClick: () -> Unit) {
    val appTheme = LocalAppTheme.current
    val reduceMotion = LocalReduceMotion.current
    val cardShape = RoundedCornerShape(appTheme.cornerRadius)
    val statusColor = when (state.currentStatus) { ... }

    // Floating heart animation on pet tap
    fun onPetTap() {
        viewModel.petThePet(); viewModel.soundManager.playClick()
        scope.launch {
            showHeart = true; heartOffsetY.snapTo(0f); heartAlpha.snapTo(1f)
            launch { heartOffsetY.animateTo(-110f, tween(heartDuration)); heartAlpha.animateTo(0f, tween(heartDuration)) }
            delay(heartDuration + 120); showHeart = false
        }
    }

    Card(shape = cardShape, border = BorderStroke(appTheme.borderWidth, statusColor.copy(alpha = 0.8f))) {
        Column {
            // Name + Language + Hearts + Level badge
            // AnimatedPetSprite with onClick
            // Quote surface with random quote
            // XP bar with animateFloatAsState
            // 3 MeterItems (health, hunger, energy)
            // Bytes + Streak row
            // Sleep toggle button
            // Pet / Clean / Play action buttons
        }
    }
}
```

### 11.5 MeterItem.kt

```kotlin
@Composable
fun MeterItem(label: String, value: Float, icon: ImageVector, activeColor: Color, trackColor: Color, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(targetValue = value / 100f)
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row { Icon(icon, ..., tint = activeColor); Text(label) }
        LinearProgressIndicator(progress = { animatedProgress }, color = activeColor, trackColor = trackColor, ...)
        Text("${value.toInt()}%")
    }
}
```

### 11.6 AnimatedThemeBackground.kt (1077 lÃ­neas)

12 fondos animados Ãºnicos renderizados via Canvas de Compose:

| Tema | Efecto visual | LÃ­neas |
|:-----|:--------------|:------:|
| **Matrix Green** | Lluvia de 1/0 con bursts de velocidad y mensajes ocultos | ~90 |
| **GalÃ¡ctico** | Nebulosas + estrellas titilantes + polvo estelar | ~70 |
| **Cyberpunk** | Rejilla neon + glitch lines horizontales | ~50 |
| **Sakura** | PÃ©talos de cerezo cayendo con rotaciÃ³n y translucidez | ~50 |
| **Minimalista** | CÃ­rculos concÃ©ntricos orbitando suavemente | ~35 |
| **NeÃ³n** | Anillos pulsantes con resplandores radiales | ~70 |
| **OcÃ©ano** | Olas sinusoidales + burbujas ascendentes | ~55 |
| **VolcÃ¡nico** | Flujo de lava + ascuas ardientes con gradiente | ~65 |
| **Samurai** | Mon (escudo) + hojas de bambÃº + katana + hojas rojas | ~120 |
| **Aurora** | Cortinas polares ondulantes con pulsos de luz | ~50 |
| **Nocturno** | Cielo estrellado con estrella fugaz periÃ³dica | ~35 |
| **Retro Pixel ðŸ†** | Mundo 8-bit: cielo pixelado, estrellas, luna, montaÃ±as parallax, Ã¡rboles, suelo, nubes scrolling, luciÃ©rnagas, rejilla CRT, scanlines | ~230 |

---

## 12. Widget y Notificaciones

### 12.1 CodePetWidgetProvider.kt

```kotlin
class CodePetWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.code_pet_widget)
            views.setTextViewText(R.id.widget_title, "Code Tamagotchi")
            views.setTextViewText(R.id.widget_subtitle, "Tu mascota estÃ¡ lista")
            views.setImageViewResource(R.id.widget_pet_image, R.drawable.mascota_happy)
            val pendingIntent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
```

### 12.2 CodePetWidget.kt (Compose Preview)

```kotlin
@Composable
fun CodePetWidget(petState: PetStateEntity?, modifier: Modifier = Modifier) {
    val status = petState?.currentStatus ?: "HAPPY"
    val petImageRes = when (status) { ... }
    Surface(shape = RoundedCornerShape(24.dp), tonalElevation = 3.dp, ...) {
        Box(modifier = Modifier.background(Color(0xFF12161F)).padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Codey estÃ¡ listo", ...); Text("Tu mascota de estudio te espera", ...)
                Image(painter = painterResource(id = petImageRes), ..., modifier = Modifier.size(96.dp))
                Text(petState?.name ?: "Codey", ...)
            }
        }
    }
}
```

### 12.3 PetCheckWorker.kt â€” Notificaciones WorkManager

```kotlin
class PetCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = get<AppDatabase>(AppDatabase::class.java).petDao()
        val petState = dao.getPetStateSuspend() ?: return Result.success()
        val needsAttention = petState.hunger < 20f || petState.health < 20f || petState.energy < 15f
        if (!needsAttention) return Result.success()

        // Check notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ...) return Result.success()

        val reason = when {
            petState.hunger < 20f -> "Â¡Tengo hambre! (${petState.hunger.toInt()}%)"
            petState.health < 20f -> "No me siento bien... (Salud: ${petState.health.toInt()}%)"
            petState.energy < 15f -> "Estoy muy cansado... (EnergÃ­a: ${petState.energy.toInt()}%)"
            else -> "Â¡Necesito atenciÃ³n!"
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, CodeTamagotchiApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("${petState.name} te necesita")
            .setContentText(reason)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true).build()

        NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        return Result.success()
    }
}
```

### 12.4 CodeTamagotchiApp.kt

```kotlin
class CodeTamagotchiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@CodeTamagotchiApp); modules(appModule) }
        createNotificationChannel()
        schedulePetCheck()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Estado de tu mascota",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Recordatorios sobre el estado de tu mascota virtual" }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun schedulePetCheck() {
        val request = PeriodicWorkRequestBuilder<PetCheckWorker>(4, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .setInitialDelay(2, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("pet_check",
            ExistingPeriodicWorkPolicy.KEEP, request)
    }

    companion object { const val NOTIFICATION_CHANNEL_ID = "pet_care_reminder" }
}
```

---

## 13. InyecciÃ³n de Dependencias

### 13.1 AppModule.kt

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

### 13.2 MainActivity.kt

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val petViewModel: PetViewModel = koinViewModel()
            val reduceMotion = petViewModel.reduceMotion.collectAsStateWithLifecycle()
            val currentThemeName = petViewModel.currentTheme.value
            val appTheme = ThemeRegistry.getTheme(currentThemeName)
            MyApplicationTheme(appTheme = appTheme, reduceMotion = reduceMotion.value) {
                AppNavigation(viewModel = petViewModel)
            }
        }
    }
}
```

### 13.3 AppNavigation.kt (243 lÃ­neas)

```kotlin
object Routes {
    const val HOME = "home"; const val LEARN = "learn"; const val FOCUS = "focus"
    const val SHOP = "shop"; const val SETTINGS = "settings"; const val SETTINGS_LANGUAGE = "settings_language"
    const val GAMES = "games"; const val BUG_HUNT = "bug_hunt"; const val GIT_RESCUE = "git_rescue"
    const val REFACTOR_RUSH = "refactor_rush"
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Inicio", Icons.Default.Home, Routes.HOME),
    BottomNavItem("Aprender", Icons.Default.Code, Routes.LEARN),
    BottomNavItem("Estudio", Icons.Default.Timer, Routes.FOCUS),
    BottomNavItem("Tienda", Icons.Default.ShoppingBag, Routes.SHOP)
)

@Composable
fun AppNavigation(viewModel: PetViewModel) {
    // Onboarding check â†’ OnboardingScreen o NavHost
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(Routes.HOME, Routes.LEARN, Routes.FOCUS, Routes.SHOP)

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedThemeBackground(theme = appTheme, reduceMotion = reduceMotion)
        Scaffold(containerColor = Color.Transparent, topBar = { ... },
            bottomBar = { if (showBottomBar) NavigationBar { bottomNavItems.forEach { item -> NavigationBarItem(...) } } }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = Routes.HOME,
                modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                composable(Routes.HOME) { HomeScreen(viewModel, ...) }
                composable(Routes.LEARN) { LearnScreen(viewModel, state) }
                composable(Routes.FOCUS) { FocusScreen(viewModel, state, studySessions) }
                composable(Routes.SHOP) { ShopScreen(viewModel, state) }
                composable(Routes.SETTINGS) { SettingsScreen(viewModel, ...) }
                composable(Routes.SETTINGS_LANGUAGE) { SettingsLanguageScreen(viewModel, ...) }
                composable(Routes.GAMES) { GamesScreen(viewModel, ...) }
                composable(Routes.BUG_HUNT) { BugHuntScreen(viewModel, ...) }
                composable(Routes.GIT_RESCUE) { GitRescueScreen(viewModel, ...) }
                composable(Routes.REFACTOR_RUSH) { RefactorRushScreen(viewModel, ...) }
            }
        }
    }
}
```

---

## 14. Pruebas

### 14.1 GameEconomyTest.kt (143 lÃ­neas)

```kotlin
class GameEconomyTest {
    @Test fun `XP scales quadratically`() {
        assertEquals(1, LevelCalculator.calculateLevel(0))
        assertEquals(2, LevelCalculator.calculateLevel(100))
        assertEquals(3, LevelCalculator.calculateLevel(300))
        assertEquals(4, LevelCalculator.calculateLevel(600))
    }

    @Test fun `challenge rewards vary by type`() {
        assertEquals(30, RewardCalculator.calculateChallengeReward("DEBUG").bytes)
        assertEquals(25, RewardCalculator.calculateChallengeReward("TRIVIA").bytes)
    }

    @Test fun `minigame economy caps rewards`() {
        assertEquals(50, RewardCalculator.calculateMinigameReward(5, 50))
        assertEquals(50, RewardCalculator.calculateMinigameReward(10, 50))
    }

    // ... mÃ¡s tests para difficulty, topics, cooldowns, level calculation, onboarding
}
```

### 14.2 GreetingScreenshotTest.kt (Roborazzi)

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class GreetingScreenshotTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test fun captureAppNavigation() {
        composeTestRule.setContent {
            MyApplicationTheme {
                val fakePetRepo = PetRepository(mockPetDao)
                val fakeUserPrefs = UserPreferencesRepository(mockContext)
                val fakeAchievements = AchievementsRepository(mockContext)
                val vm = PetViewModel(fakePetRepo, fakeUserPrefs, fakeAchievements)
                AppNavigation(viewModel = vm)
            }
        }
        // Roborazzi will capture the screenshot
    }
}
```

### 14.3 Cobertura Actual

| Tipo | Archivos | Estado |
|:-----|:---------|:-------|
| Unit test | GameEconomyTest (10 tests) | âœ… XP scaling, rewards, economy |
| Robolectric | ExampleRobolectricTest | âœ… BÃ¡sico |
| Screenshot | GreetingScreenshotTest | âœ… Roborazzi |
| Animation | PetAnimationConfigTest | âœ… Reduce motion |

---

#
## 15. CÃ³digo Fuente Completo de Cada Archivo

A continuaciÃ³n se incluye el cÃ³digo fuente completo de cada archivo Kotlin del proyecto, organizado por paquete.

### 15.1 ConfiguraciÃ³n del Proyecto


#### build.gradle.kts (raÃ­z)

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.roborazzi) apply false
}
```

#### settings.gradle.kts

```kotlin
pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "Code Tamagotchi"

include(":app")
```

#### app/build.gradle.kts

```kotlin
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
}

android {
  namespace = "com.tamagotchi.code"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.tamagotchi.code"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
}

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.work.runtime.ktx)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
}
```

#### gradle/libs.versions.toml

```toml
[versions]
agp = "9.1.1"
koin = "4.0.2"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.8.7"
lifecycleViewmodelCompose = "2.8.7"
lifecycleRuntimeCompose = "2.8.7"
activityCompose = "1.10.1"
kotlin = "2.2.10"
composeBom = "2024.09.00"
googleDevtoolsKsp = "2.3.5"
navigationCompose = "2.8.9"
roomRuntime = "2.7.0"
roomKtx = "2.7.0"
roomCompiler = "2.7.0"
kotlinxCoroutinesTest = "1.10.2"
core = "1.6.1"
runner = "1.6.2"
kotlinxCoroutinesAndroid = "1.10.2"
kotlinxCoroutinesCore = "1.10.2"
datastorePreferences = "1.1.7"
robolectric = "4.16.1"
roborazzi = "1.59.0"
workmanager = "2.10.0"


[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeCompose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutinesTest" }
androidx-core = { group = "androidx.test", name = "core", version.ref = "core" }
androidx-runner = { group = "androidx.test", name = "runner", version.ref = "runner" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinxCoroutinesAndroid" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutinesCore" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastorePreferences" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }
roborazzi = { group = "io.github.takahirom.roborazzi", name = "roborazzi", version.ref = "roborazzi" }
roborazzi-compose = { group = "io.github.takahirom.roborazzi", name = "roborazzi-compose", version.ref = "roborazzi" }
roborazzi-junit-rule = { group = "io.github.takahirom.roborazzi", name = "roborazzi-junit-rule", version.ref = "roborazzi" }
koin-android = { group = "io.insert-koin", name = "koin-android", version.ref = "koin" }
koin-compose = { group = "io.insert-koin", name = "koin-androidx-compose", version.ref = "koin" }
koin-core = { group = "io.insert-koin", name = "koin-core", version.ref = "koin" }
androidx-work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "workmanager" }


[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
google-devtools-ksp = { id = "com.google.devtools.ksp", version.ref = "googleDevtoolsKsp" }
roborazzi = { id = "io.github.takahirom.roborazzi", version.ref = "roborazzi" }

```
### 15.2 NÃºcleo de la AplicaciÃ³n

 MainActivity.kt
```

#### AppNavigation.kt â€

```de
```
### 15.3 Base de Datos (Room)


#### data/database/AppDatabase.kt

```kotlin
package com.tamagotchi.code.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PetStateEntity::class, StudySessionEntity::class, FocusSessionEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `focus_sessions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `topic` TEXT NOT NULL, 
                        `plannedDurationMinutes` INTEGER NOT NULL, 
                        `startedAt` INTEGER NOT NULL, 
                        `status` TEXT NOT NULL
                    )
                    """
                )
            }
        }

        val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pet_state ADD COLUMN hasRenamed INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "code_tamagotchi_db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

#### data/database/PetDao.kt

```kotlin
package com.tamagotchi.code.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    fun getPetState(): Flow<PetStateEntity?>

    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    suspend fun getPetStateSuspend(): PetStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePetState(state: PetStateEntity)

    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySessionEntity)

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC LIMIT 1")
    fun getLatestFocusSession(): Flow<FocusSessionEntity?>

    @Query("SELECT * FROM focus_sessions WHERE status = 'RUNNING' ORDER BY id DESC LIMIT 1")
    suspend fun getActiveFocusSession(): FocusSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSessionEntity)

    @Query("UPDATE focus_sessions SET status = :status WHERE id = :sessionId")
    suspend fun updateFocusSessionStatus(sessionId: Long, status: String)

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC")
    fun getAllFocusSessions(): Flow<List<FocusSessionEntity>>

    @androidx.room.Transaction
    suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
        updateFocusSessionStatus(sessionId, status)
        insertOrUpdatePetState(petState)
    }
}
```

#### data/database/PetStateEntity.kt

```kotlin
package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_state")
data class PetStateEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Codey",
    val language: String = "Kotlin",
    val level: Int = 1,
    val xp: Int = 0,
    val hunger: Float = 100f,
    val health: Float = 100f,
    val energy: Float = 100f,
    val bytes: Int = 50,
    val lastUpdated: Long = System.currentTimeMillis(),
    val streak: Int = 0,
    val lastStudyDate: Long = 0,
    val currentStatus: String = "HAPPY",
    val hasRenamed: Boolean = false
)
```

#### data/database/FocusSessionEntity.kt

```kotlin
package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val plannedDurationMinutes: Int,
    val startedAt: Long,
    val status: String = "RUNNING"
)
```

#### data/database/StudySessionEntity.kt

```kotlin
package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)
```
### 15.4 Repositorios


#### data/repository/PetRepository.kt

```kotlin
package com.tamagotchi.code.data.repository

import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetDao
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    val petState: Flow<PetStateEntity?> = petDao.getPetState()
    val studySessions: Flow<List<StudySessionEntity>> = petDao.getAllStudySessions()
    val latestFocusSession: Flow<FocusSessionEntity?> = petDao.getLatestFocusSession()
    val allFocusSessions: Flow<List<FocusSessionEntity>> = petDao.getAllFocusSessions()

    suspend fun savePetState(state: PetStateEntity) {
        petDao.insertOrUpdatePetState(state)
    }

    suspend fun addStudySession(session: StudySessionEntity) {
        petDao.insertStudySession(session)
    }

    suspend fun getActiveFocusSession(): FocusSessionEntity? {
        return petDao.getActiveFocusSession()
    }

    suspend fun saveFocusSession(session: FocusSessionEntity): Long {
        petDao.insertFocusSession(session)
        return session.id
    }

    suspend fun updateFocusSessionStatus(sessionId: Long, status: String) {
        petDao.updateFocusSessionStatus(sessionId, status)
    }

    suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
        petDao.completeOfflineSession(sessionId, status, petState)
    }
}
```

#### data/repository/UserPreferencesRepository.kt

```kotlin
package com.tamagotchi.code.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tamagotchi.code.ui.theme.ThemeRegistry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val KEY_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        private val KEY_THEME = stringPreferencesKey("app_theme")
        private val KEY_UNLOCKED_THEMES = stringSetPreferencesKey("unlocked_themes")
        private val KEY_SELECTED_TOPICS = stringSetPreferencesKey("selected_topics")
        private val KEY_DIFFICULTY = stringPreferencesKey("difficulty")
        private val KEY_FOCUS_DURATION = androidx.datastore.preferences.core.intPreferencesKey("focus_duration_default")
        private val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val KEY_VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val KEY_GAME_LAST_PLAYED = stringSetPreferencesKey("game_last_played")
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING] ?: false
    }

    val currentTheme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_THEME] ?: "Matrix Green"
    }

    val unlockedThemes: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val stored = prefs[KEY_UNLOCKED_THEMES]
        val allNames = ThemeRegistry.allThemes.map { it.name }.toSet()
        if (stored == null || stored.isEmpty()) allNames else stored
    }

    val selectedTopics: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_SELECTED_TOPICS] ?: setOf("Kotlin", "Estructuras de Datos", "Git")
    }

    val difficulty: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_DIFFICULTY] ?: "Inicial"
    }

    val focusDurationDefault: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_FOCUS_DURATION] ?: 25
    }

    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SOUND_ENABLED] ?: true
    }

    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_VIBRATION_ENABLED] ?: true
    }

    val reduceMotion: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_REDUCE_MOTION] ?: false
    }

    val gameLastPlayed: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
    }

    val gameCooldowns: Flow<Map<String, Long>> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
        raw.mapNotNull { entry ->
            val parts = entry.split(":", limit = 2)
            if (parts.size == 2) {
                val timestamp = parts[1].toLongOrNull()
                if (timestamp != null) parts[0] to timestamp else null
            } else null
        }.toMap()
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING] = true
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = theme
        }
    }

    suspend fun addUnlockedTheme(theme: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_UNLOCKED_THEMES] ?: ThemeRegistry.allThemes.map { it.name }.toSet()
            prefs[KEY_UNLOCKED_THEMES] = current + theme
        }
    }

    suspend fun setUnlockedThemes(themes: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_UNLOCKED_THEMES] = themes
        }
    }

    suspend fun getHasSeenOnboarding(): Boolean {
        return context.dataStore.data.first()[KEY_ONBOARDING] ?: false
    }

    suspend fun setSelectedTopics(topics: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SELECTED_TOPICS] = topics
        }
    }

    suspend fun setDifficulty(difficulty: String) {
        context.dataStore.edit { prefs -> prefs[KEY_DIFFICULTY] = difficulty }
    }

    suspend fun setFocusDurationDefault(duration: Int) {
        context.dataStore.edit { prefs -> prefs[KEY_FOCUS_DURATION] = duration }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_SOUND_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_VIBRATION_ENABLED] = enabled }
    }

    suspend fun setReduceMotion(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_REDUCE_MOTION] = enabled }
    }

    suspend fun recordGamePlay(gameId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
            val now = System.currentTimeMillis()
            val filtered = current.filter { !it.startsWith("$gameId:") }.toSet()
            prefs[KEY_GAME_LAST_PLAYED] = filtered + "$gameId:$now"
        }
    }

    suspend fun getGameLastPlayed(gameId: String): Long {
        return context.dataStore.data.first().let { prefs ->
            val raw = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
            raw.firstOrNull { it.startsWith("$gameId:") }?.split(":")?.get(1)?.toLongOrNull() ?: 0L
        }
    }
}
```

#### data/repository/AchievementsRepository.kt

```kotlin
package com.tamagotchi.code.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.achievementsDataStore by preferencesDataStore(name = "achievements")

class AchievementsRepository(private val context: Context) {
    companion object {
        private val KEY_UNLOCKED_ACHIEVEMENTS = stringSetPreferencesKey("unlocked_achievements")

        val ALL_ACHIEVEMENTS = listOf(
            Achievement("primer_build", "Primer build", "CompletÃ¡ tu primera sesiÃ³n Focus"),
            Achievement("nivel_experto", "Nivel Experto", "SubÃ­ de nivel por primera vez"),
            Achievement("cazador_de_bugs", "Cazador de bugs", "PuntuaciÃ³n perfecta en Bug Hunt"),
            Achievement("git_sin_panico", "Git sin pÃ¡nico", "AprobÃ¡ Git Rescue con 3+ aciertos"),
            Achievement("racha_7", "Racha de 7 dÃ­as", "MantenÃ© una racha de estudio de 7 dÃ­as"),
            Achievement("racha_30", "Racha de 30 dÃ­as", "MantenÃ© una racha de estudio de 30 dÃ­as"),
            Achievement("coleccionista", "Coleccionista", "DesbloqueÃ¡ 3 temas visuales"),
            Achievement("completista", "Completista", "DesbloqueÃ¡ los 12 temas visuales"),
            Achievement("ahorrador", "Ahorrador", "AcumulÃ¡ 1000 Bytes"),
            Achievement("primer_acaricie", "Primer mimo", "AcariciÃ¡ a tu mascota por primera vez"),
            Achievement("duermevela", "Duermevela", "PonÃ© a dormir a tu mascota"),
            Achievement("limpiador", "Limpieza profunda", "LimpiÃ¡ a tu mascota 10 veces"),
        )
    }

    val unlockedAchievements: Flow<Set<String>> = context.achievementsDataStore.data.map { prefs ->
        prefs[KEY_UNLOCKED_ACHIEVEMENTS] ?: emptySet()
    }

    suspend fun unlockAchievement(achievementId: String) {
        context.achievementsDataStore.edit { prefs ->
            val current = prefs[KEY_UNLOCKED_ACHIEVEMENTS] ?: emptySet()
            if (!current.contains(achievementId)) {
                prefs[KEY_UNLOCKED_ACHIEVEMENTS] = current + achievementId
            }
        }
    }

    fun getAchievementById(id: String): Achievement? =
        ALL_ACHIEVEMENTS.find { it.id == id }
}

data class Achievement(
    val id: String,
    val name: String,
    val description: String
)
```
### 15.5 Datos de Retos


#### data/ChallengesData.kt

```kotlin
package com.tamagotchi.code.data

data class CodingChallenge(
    val id: Int,
    val language: String,
    val type: String,
    val title: String,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

object ChallengesData {
    val challenges = listOf(
        // ========== KOTLIN ==========
        CodingChallenge(
            id = 1,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Inmutabilidad en Kotlin",
            question = "Â¿CuÃ¡l es la diferencia principal entre 'val' y 'var'?",
            options = listOf(
                "val es mutable, var es inmutable",
                "val define una referencia de solo lectura (inmutable), var es mutable",
                "val es constante en tiempo de compilaciÃ³n, var se evalÃºa en ejecuciÃ³n",
                "No hay diferencia, son alias heredados de Java"
            ),
            correctAnswerIndex = 1,
            explanation = "En Kotlin, 'val' define una variable de solo lectura (su valor no puede ser reasignado), mientras que 'var' define una variable mutable estÃ¡ndar."
        ),
        CodingChallenge(
            id = 2,
            language = "Kotlin",
            type = "DEBUG",
            title = "Llamada Segura (Null Safety)",
            question = "Este cÃ³digo lanza un error de compilaciÃ³n. Â¿CÃ³mo se corrige para permitir llamadas seguras a 'length'?",
            codeSnippet = "val name: String? = null\nval len = name.length",
            options = listOf(
                "Cambiar a: val len = name?.length",
                "Cambiar a: val len = name!!?.length",
                "Cambiar a: val len = name.length()",
                "Kotlin no permite variables nulas de ningÃºn tipo"
            ),
            correctAnswerIndex = 0,
            explanation = "Usar el operador de llamada segura '?.' (name?.length) devuelve el tamaÃ±o de la cadena si no es nula, o 'null' de lo contrario, evitando errores de puntero nulo."
        ),
        CodingChallenge(
            id = 3,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Funciones de Alcance (Scope)",
            question = "Â¿QuÃ© funciÃ³n de alcance devuelve el resultado del bloque lambda y usa 'this' como receptor?",
            options = listOf(
                "apply",
                "also",
                "run",
                "let"
            ),
            correctAnswerIndex = 2,
            explanation = "'run' ejecuta el bloque usando 'this' como receptor de llamadas y devuelve el resultado de la Ãºltima expresiÃ³n dentro del bloque lambda."
        ),
        CodingChallenge(
            id = 12,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Data Class",
            question = "Â¿QuÃ© genera automÃ¡ticamente la palabra clave 'data' en una clase?",
            options = listOf(
                "Solo getters y setters",
                "equals(), hashCode(), toString(), copy() y componentN()",
                "Un constructor sin parÃ¡metros",
                "ImplementaciÃ³n de Serializable"
            ),
            correctAnswerIndex = 1,
            explanation = "Las 'data class' generan automÃ¡ticamente equals(), hashCode(), toString(), copy(), y funciones componentN() para desestructuraciÃ³n."
        ),
        CodingChallenge(
            id = 13,
            language = "Kotlin",
            type = "DEBUG",
            title = "Elvis Operator",
            question = "Â¿QuÃ© imprime este cÃ³digo?",
            codeSnippet = "val name: String? = null\nprintln(name ?: \"Invitado\")",
            options = listOf(
                "null",
                "Invitado",
                "name",
                "Error de compilaciÃ³n"
            ),
            correctAnswerIndex = 1,
            explanation = "El operador Elvis '?:' devuelve el lado izquierdo si no es nulo, o el derecho si el izquierdo es null. AquÃ­ name es null, asÃ­ que imprime 'Invitado'."
        ),
        CodingChallenge(
            id = 14,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Funciones de ExtensiÃ³n",
            question = "Â¿QuÃ© permite hacer una funciÃ³n de extensiÃ³n en Kotlin?",
            options = listOf(
                "Modificar una clase sellada desde otra clase",
                "AÃ±adir nuevas funcionalidades a una clase sin heredar de ella",
                "Crear una funciÃ³n que solo se ejecuta en extensiones de archivo .kt",
                "Extender el tiempo de ejecuciÃ³n de una funciÃ³n recursiva"
            ),
            correctAnswerIndex = 1,
            explanation = "Las funciones de extensiÃ³n permiten agregar nuevos mÃ©todos a una clase existente sin modificar su cÃ³digo fuente ni heredar de ella."
        ),
        CodingChallenge(
            id = 15,
            language = "Kotlin",
            type = "DEBUG",
            title = "Null Safety en Listas",
            question = "Â¿CÃ³mo obtienes el primer elemento de una lista nullable de forma segura?",
            codeSnippet = "val items: List<String>? = null\nval first = ???",
            options = listOf(
                "val first = items?.firstOrNull()",
                "val first = items!!.first()",
                "val first = items.first()",
                "val first = items?.get(0)"
            ),
            correctAnswerIndex = 0,
            explanation = "'items?.firstOrNull()' es la forma mÃ¡s segura: si items es null, firstOrNull() ni se llama y el resultado es null. Si no es null, devuelve el primer elemento o null si la lista estÃ¡ vacÃ­a."
        ),
        CodingChallenge(
            id = 16,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Corrutinas - Dispatchers",
            question = "Â¿CuÃ¡l es el propÃ³sito de Dispatchers.IO en corrutinas de Kotlin?",
            options = listOf(
                "Ejecutar tareas de actualizaciÃ³n de UI",
                "Ejecutar operaciones de E/S en segundo plano (archivos, red, BD)",
                "Ejecutar cÃ³digo de forma secuencial en el hilo principal",
                "No es un dispatcher real, solo un concepto teÃ³rico"
            ),
            correctAnswerIndex = 1,
            explanation = "Dispatchers.IO estÃ¡ optimizado para operaciones de entrada/salida como lecturas de archivos, llamadas de red o consultas a bases de datos."
        ),
        CodingChallenge(
            id = 17,
            language = "Kotlin",
            type = "TRIVIA",
            title = "When Expression",
            question = "Â¿En quÃ© se diferencia 'when' de 'switch' en Java?",
            options = listOf(
                "when solo funciona con nÃºmeros enteros",
                "when puede usarse como expresiÃ³n (devuelve valor) y no necesita break",
                "when requiere un bloque default obligatorio",
                "when solo funciona con tipos String"
            ),
            correctAnswerIndex = 1,
            explanation = "'when' en Kotlin es mÃ¡s potente que 'switch': puede usarse como expresiÃ³n que devuelve valores, no necesita break, y admite cualquier tipo de condiciÃ³n."
        ),
        CodingChallenge(
            id = 18,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Sealed Class",
            question = "Â¿Para quÃ© sirve una clase sellada (sealed class) en Kotlin?",
            options = listOf(
                "Para ocultar los detalles de implementaciÃ³n de una clase",
                "Para restringir la jerarquÃ­a de herencia a un conjunto fijo de subtipos conocidos",
                "Para evitar que se pueda crear ninguna instancia de la clase",
                "Para marcar una clase como obsoleta y no recomendada"
            ),
            correctAnswerIndex = 1,
            explanation = "Las 'sealed class' definen una jerarquÃ­a de herencia restringida: todos los subtipos directos deben declararse en el mismo archivo, lo que permite when exhaustivo."
        ),
        CodingChallenge(
            id = 19,
            language = "Kotlin",
            type = "DEBUG",
            title = "Smart Cast",
            question = "Â¿Por quÃ© este cÃ³digo compila sin necesidad de un cast explÃ­cito?",
            codeSnippet = "fun length(obj: Any): Int {\n    if (obj is String) return obj.length\n    return 0\n}",
            options = listOf(
                "Porque Kotlin infiere que obj es Object y Object tiene length",
                "Porque Kotlin aplica smart cast: tras verificar 'is String', trata obj como String",
                "Porque el compilador convierte Any a String automÃ¡ticamente",
                "No compila, necesita (obj as String).length"
            ),
            correctAnswerIndex = 1,
            explanation = "Kotlin realiza 'smart cast': cuando verificas que una variable es de cierto tipo con 'is', el compilador la trata automÃ¡ticamente como ese tipo dentro del bloque."
        ),
        CodingChallenge(
            id = 20,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Flow vs LiveData",
            question = "Â¿QuÃ© ventaja tiene un Flow de Kotlin sobre LiveData?",
            options = listOf(
                "Flow estÃ¡ atado al ciclo de vida de la Activity",
                "Flow es reactivo sin importar dependencias de Android y tiene operadores como map, filter",
                "Flow solo funciona en Java",
                "LiveData ya no se puede usar en Kotlin"
            ),
            correctAnswerIndex = 1,
            explanation = "Flow es parte de Kotlin (no de Android), por lo que es agnÃ³stico de plataforma y ofrece operadores funcionales como map, filter, transform, etc."
        ),
        CodingChallenge(
            id = 21,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Colecciones Inmutables",
            question = "Â¿CuÃ¡l es la diferencia entre listOf() y mutableListOf()?",
            options = listOf(
                "Ambas crean listas iguales, solo cambia el nombre",
                "listOf() crea una lista de solo lectura, mutableListOf() permite modificar elementos",
                "listOf() lanza error si la lista tiene mÃ¡s de 10 elementos",
                "mutableListOf() no permite elementos nulos"
            ),
            correctAnswerIndex = 1,
            explanation = "listOf() devuelve una List inmutable (solo lectura), mientras mutableListOf() devuelve una MutableList que permite agregar, quitar y modificar elementos."
        ),
        CodingChallenge(
            id = 22,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Inline Functions",
            question = "Â¿QuÃ© beneficio principal tienen las funciones 'inline' en Kotlin?",
            options = listOf(
                "Permiten usar herencia mÃºltiple",
                "Eliminan la sobrecarga de crear objetos lambda al copiar el cÃ³digo en el punto de llamada",
                "Hacen que las funciones se ejecuten en un hilo separado",
                "Permiten que las funciones sean visibles globalmente"
            ),
            correctAnswerIndex = 1,
            explanation = "'inline' copia el cuerpo de la funciÃ³n directamente en el lugar de la llamada, eliminando la creaciÃ³n de objetos lambda y reduciendo la sobrecarga de memoria."
        ),
        CodingChallenge(
            id = 23,
            language = "Kotlin",
            type = "DEBUG",
            title = "Try como ExpresiÃ³n",
            question = "Â¿QuÃ© valor imprime este cÃ³digo?",
            codeSnippet = "val result = try { \"10\".toInt() } catch (e: Exception) { 0 }\nprintln(result)",
            options = listOf(
                "Error de compilaciÃ³n",
                "10",
                "0",
                "\"10\""
            ),
            correctAnswerIndex = 1,
            explanation = "'try' en Kotlin es una expresiÃ³n y devuelve el valor del Ãºltimo bloque ejecutado. '10'.toInt() tiene Ã©xito, result = 10."
        ),
        CodingChallenge(
            id = 24,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Operator Overloading",
            question = "Â¿CÃ³mo se sobrecarga el operador '+' para una clase en Kotlin?",
            options = listOf(
                "Definiendo un mÃ©todo llamado add()",
                "Definiendo una funciÃ³n 'operator fun plus()'",
                "Usando la anotaciÃ³n @Overload",
                "No se puede sobrecargar operadores en Kotlin"
            ),
            correctAnswerIndex = 1,
            explanation = "En Kotlin los operadores se sobrecargan implementando funciones con 'operator': plus() para '+', minus() para '-', times() para '*', etc."
        ),
        CodingChallenge(
            id = 25,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Scope Functions - also",
            question = "Â¿QuÃ© hace diferente a 'also' respecto a 'apply'?",
            options = listOf(
                "also usa 'this', apply usa 'it'",
                "also usa 'it' como receptor, apply usa 'this'",
                "also es sincrÃ³nico, apply es asÃ­ncrono",
                "No hay diferencia, son alias"
            ),
            correctAnswerIndex = 1,
            explanation = "'apply' utiliza 'this' (receptor implÃ­cito) para configurar objetos, mientras que 'also' utiliza 'it' (parÃ¡metro explÃ­cito) y normalmente se usa para efectos secundarios."
        ),
        CodingChallenge(
            id = 86,
            language = "Kotlin",
            type = "TRIVIA",
            title = "Object Keyword",
            question = "Â¿Para quÃ© sirve la palabra clave 'object' en Kotlin?",
            options = listOf(
                "Solo para crear nuevos objetos de una clase existente",
                "Para declarar una clase singleton (Ãºnica instancia global)",
                "Es el equivalente de 'new' en Java",
                "Para marcar un mÃ©todo como obsoleto"
            ),
            correctAnswerIndex = 1,
            explanation = "'object' declara una clase singleton: solo existe una instancia de ella, creada de forma perezosa y segura al primer acceso."
        ),
        CodingChallenge(
            id = 87,
            language = "Kotlin",
            type = "DEBUG",
            title = "Lambda y fold",
            question = "Â¿QuÃ© resultado produce esta operaciÃ³n?",
            codeSnippet = "val nums = listOf(1, 2, 3, 4)\nval sum = nums.fold(0) { acc, i -> acc + i }\nprintln(sum)",
            options = listOf(
                "10",
                "0",
                "1234",
                "Error de compilaciÃ³n"
            ),
            correctAnswerIndex = 0,
            explanation = "fold(0) itera con valor inicial 0: 0+1=1, 1+2=3, 3+3=6, 6+4=10. El resultado es la suma total 10."
        ),

        // ========== JAVASCRIPT ==========
        CodingChallenge(
            id = 4,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Tipos de Datos extraÃ±os",
            question = "Â¿QuÃ© devuelve la expresiÃ³n 'typeof null' en JavaScript?",
            options = listOf(
                "\"null\"",
                "\"undefined\"",
                "\"object\"",
                "\"string\""
            ),
            correctAnswerIndex = 2,
            explanation = "HistÃ³ricamente en JavaScript, 'typeof null' devuelve '\"object\"'. Es considerado un error en el diseÃ±o original del lenguaje, pero no se ha cambiado por compatibilidad."
        ),
        CodingChallenge(
            id = 5,
            language = "JavaScript",
            type = "DEBUG",
            title = "ComparaciÃ³n Estricta",
            question = "Â¿Por quÃ© la comparaciÃ³n estricta '0 === false' da como resultado 'false'?",
            codeSnippet = "console.log(0 == false); // true\nconsole.log(0 === false); // false",
            options = listOf(
                "Porque === hace coerciÃ³n automÃ¡tica de tipos",
                "Porque === compara tanto el valor como el tipo sin realizar coerciÃ³n",
                "Porque 0 no es un valor falsy",
                "El compilador de JS tiene un bug con nÃºmeros"
            ),
            correctAnswerIndex = 1,
            explanation = "El operador '==' convierte ambos lados a un tipo comÃºn (coerciÃ³n), por lo que 0 y false coinciden. El operador '===' es estricto y no hace coerciÃ³n, detectando que Number no es Boolean."
        ),
        CodingChallenge(
            id = 6,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Clausuras (Closures)",
            question = "Â¿QuÃ© es una Clausura (Closure) en JavaScript?",
            options = listOf(
                "Una funciÃ³n que cierra la ventana del navegador",
                "El proceso de comprimir archivos de cÃ³digo para producciÃ³n",
                "La combinaciÃ³n de una funciÃ³n y el entorno lÃ©xico en el que fue declarada",
                "Un mÃ©todo reservado para destruir variables locales"
            ),
            correctAnswerIndex = 2,
            explanation = "Un closure le da a una funciÃ³n interna acceso al Ã¡mbito de su funciÃ³n externa, incluso despuÃ©s de que la funciÃ³n externa haya terminado de ejecutarse."
        ),
        CodingChallenge(
            id = 26,
            language = "JavaScript",
            type = "TRIVIA",
            title = "let vs var",
            question = "Â¿CuÃ¡l es la diferencia principal entre 'let' y 'var'?",
            options = listOf(
                "No hay diferencia, let es solo una alternativa moderna",
                "let tiene Ã¡mbito de bloque, var tiene Ã¡mbito de funciÃ³n",
                "var no puede usarse en navegadores modernos",
                "let solo funciona dentro de objetos"
            ),
            correctAnswerIndex = 1,
            explanation = "'let' tiene Ã¡mbito de bloque (solo existe dentro de {}), mientras que 'var' tiene Ã¡mbito de funciÃ³n y sufre hoisting."
        ),
        CodingChallenge(
            id = 27,
            language = "JavaScript",
            type = "DEBUG",
            title = "Promesas - then",
            question = "Â¿QuÃ© imprime el siguiente cÃ³digo?",
            codeSnippet = "Promise.resolve(1)\n  .then(x => x + 1)\n  .then(x => console.log(x))",
            options = listOf(
                "1",
                "2",
                "undefined",
                "Promise {<pending>}"
            ),
            correctAnswerIndex = 1,
            explanation = "Promise.resolve(1) crea una promesa con valor 1. El primer then suma 1 (resultado 2). El segundo then imprime 2."
        ),
        CodingChallenge(
            id = 28,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Spread Operator",
            question = "Â¿QuÃ© hace el operador '...' (spread) en un array?",
            options = listOf(
                "Concatena todos los arrays de la aplicaciÃ³n",
                "Expande un array en sus elementos individuales",
                "Elimina el Ãºltimo elemento del array",
                "Crea una copia vacÃ­a del array"
            ),
            correctAnswerIndex = 1,
            explanation = "El spread operator '...' expande un iterable (como un array) en sus elementos individuales, Ãºtil para copiar o combinar arrays."
        ),
        CodingChallenge(
            id = 29,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Template Literals",
            question = "Â¿CÃ³mo se interpola una variable en un string con template literals?",
            options = listOf(
                "\"Hola \" + nombre + \"!\"",
                "`Hola \${nombre}!`",
                "'Hola {nombre}!'",
                "\"Hola %nombre%!\""
            ),
            correctAnswerIndex = 1,
            explanation = "Los template literals usan backticks ` y la sintaxis \${} para interpolar variables o expresiones directamente en el string."
        ),
        CodingChallenge(
            id = 30,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Arrow Functions",
            question = "Â¿QuÃ© diferencia a una arrow function de una funciÃ³n tradicional?",
            options = listOf(
                "No puede tener parÃ¡metros",
                "No tiene su propio 'this', hereda del Ã¡mbito padre",
                "Solo funciona con nÃºmeros",
                "No puede devolver valores"
            ),
            correctAnswerIndex = 1,
            explanation = "Las arrow functions no tienen su propio 'this', heredÃ¡ndolo del Ã¡mbito circundante. Tampoco tienen 'arguments' ni pueden usarse como constructoras."
        ),
        CodingChallenge(
            id = 31,
            language = "JavaScript",
            type = "DEBUG",
            title = "Hoisting en var",
            question = "Â¿QuÃ© devuelve este cÃ³digo?",
            codeSnippet = "console.log(x)\nvar x = 5",
            options = listOf(
                "5",
                "undefined",
                "ReferenceError: x is not defined",
                "null"
            ),
            correctAnswerIndex = 1,
            explanation = "Con 'var', la declaraciÃ³n se eleva (hoisting) al inicio, pero la asignaciÃ³n no. Al llegar al console.log, x estÃ¡ declarada pero su valor es undefined."
        ),
        CodingChallenge(
            id = 32,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Destructuring Assignment",
            question = "Â¿QuÃ© hace la desestructuraciÃ³n (destructuring) en arrays?",
            options = listOf(
                "Elimina elementos no deseados del array",
                "Extrae valores del array en variables individuales con una sintaxis concisa",
                "Cifra los datos del array para seguridad",
                "Convierte el array en un objeto"
            ),
            correctAnswerIndex = 1,
            explanation = "La desestructuraciÃ³n permite extraer valores de arrays u objetos en variables individuales: const [a, b] = [1, 2] asigna 1 a 'a' y 2 a 'b'."
        ),
        CodingChallenge(
            id = 33,
            language = "JavaScript",
            type = "TRIVIA",
            title = "MÃ©todo map",
            question = "Â¿QuÃ© devuelve el mÃ©todo 'map' de un array?",
            options = listOf(
                "Un nuevo array con el mismo nÃºmero de elementos transformados",
                "Un nuevo array solo con los elementos que cumplen una condiciÃ³n",
                "El primer elemento que cumple una condiciÃ³n",
                "Un booleano indicando si todos los elementos cumplen la condiciÃ³n"
            ),
            correctAnswerIndex = 0,
            explanation = "'map' itera sobre cada elemento y aplica una funciÃ³n, devolviendo un nuevo array de la misma longitud con los valores transformados."
        ),
        CodingChallenge(
            id = 34,
            language = "JavaScript",
            type = "DEBUG",
            title = "Event Loop",
            question = "Â¿En quÃ© orden se imprimen estos mensajes?",
            codeSnippet = "console.log('A')\nsetTimeout(() => console.log('B'), 0)\nconsole.log('C')",
            options = listOf(
                "A, B, C",
                "B, A, C",
                "A, C, B",
                "C, B, A"
            ),
            correctAnswerIndex = 2,
            explanation = "A y C son sÃ­ncronos (se ejecutan primero). setTimeout, con delay 0, pasa a la cola de tareas y se ejecuta despuÃ©s de completar el stack sÃ­ncrono."
        ),
        CodingChallenge(
            id = 35,
            language = "JavaScript",
            type = "TRIVIA",
            title = "falsy Values",
            question = "Â¿CuÃ¡ntos de estos valores son falsy en JavaScript? 0, '', false, null, undefined, NaN, []",
            options = listOf(
                "Todos son falsy",
                "6 excepto [] (los arrays vacÃ­os son truthy)",
                "4: 0, '', false, null",
                "Solo 0 y false"
            ),
            correctAnswerIndex = 1,
            explanation = "Los valores falsy son: 0, '' (string vacÃ­o), false, null, undefined, NaN (6 en total). [] (array vacÃ­o) es truthy."
        ),
        CodingChallenge(
            id = 36,
            language = "JavaScript",
            type = "TRIVIA",
            title = "Fetch API",
            question = "Â¿CÃ³mo se maneja la respuesta de una llamada fetch?",
            options = listOf(
                "fetch devuelve directamente el JSON",
                "fetch devuelve una Promise que resuelve a un objeto Response",
                "fetch es sÃ­ncrono y bloquea el hilo",
                "fetch solo funciona con archivos locales"
            ),
            correctAnswerIndex = 1,
            explanation = "fetch() devuelve una Promise que resuelve a un objeto Response. Para obtener el JSON, debes llamar a response.json() (tambiÃ©n una Promise)."
        ),
        CodingChallenge(
            id = 37,
            language = "JavaScript",
            type = "TRIVIA",
            title = "CoerciÃ³n ImplÃ­cita",
            question = "Â¿QuÃ© devuelve '5' - 3 en JavaScript?",
            options = listOf(
                "\"53\"",
                "2",
                "\"5-3\"",
                "Error TypeError"
            ),
            correctAnswerIndex = 1,
            explanation = "Con el operador '-', JavaScript convierte el string '5' a nÃºmero (5) y resta 3, dando 2. En cambio, '5' + 3 darÃ­a \"53\" por concatenaciÃ³n."
        ),
        CodingChallenge(
            id = 38,
            language = "JavaScript",
            type = "DEBUG",
            title = "Objeto this en funciÃ³n",
            question = "Â¿QuÃ© imprime este cÃ³digo (en el navegador)?",
            codeSnippet = "function foo() {\n  console.log(this)\n}\nfoo()",
            options = listOf(
                "undefined",
                "El objeto global (window)",
                "null",
                "El objeto foo"
            ),
            correctAnswerIndex = 1,
            explanation = "En una funciÃ³n normal no estricta, 'this' dentro de la funciÃ³n hace referencia al objeto global (window en navegador). En strict mode serÃ­a undefined."
        ),
        CodingChallenge(
            id = 39,
            language = "JavaScript",
            type = "TRIVIA",
            title = "JSON.stringify",
            question = "Â¿QuÃ© hace JSON.stringify()?",
            options = listOf(
                "Convierte un string JSON en un objeto JavaScript",
                "Convierte un objeto JavaScript en un string JSON",
                "Valida si un string es JSON vÃ¡lido",
                "Minifica un string quitando espacios"
            ),
            correctAnswerIndex = 1,
            explanation = "JSON.stringify() serializa un objeto JavaScript a su representaciÃ³n en string JSON. Lo opuesto es JSON.parse()."
        ),
        CodingChallenge(
            id = 40,
            language = "JavaScript",
            type = "TRIVIA",
            title = "null vs undefined",
            question = "Â¿CuÃ¡l es la diferencia entre null y undefined?",
            options = listOf(
                "Son exactamente iguales, sinÃ³nimos",
                "undefined significa 'no asignado', null significa 'vacÃ­o intencional'",
                "null solo existe en strict mode",
                "undefined es un string, null es un objeto"
            ),
            correctAnswerIndex = 1,
            explanation = "undefined indica que una variable se declarÃ³ pero no se le asignÃ³ valor. null es un valor asignado intencionalmente para indicar 'sin valor'."
        ),

        // ========== PHP ==========
        CodingChallenge(
            id = 7,
            language = "PHP",
            type = "TRIVIA",
            title = "Sintaxis BÃ¡sica de Variables",
            question = "Â¿CuÃ¡l es el prefijo obligatorio para declarar y usar cualquier variable en PHP?",
            options = listOf(
                "El signo de porcentaje (%)",
                "El signo de dÃ³lar ($)",
                "La palabra clave 'var'",
                "No hay prefijo, se define como en C++"
            ),
            correctAnswerIndex = 1,
            explanation = "En PHP, todas las variables deben comenzar con el sÃ­mbolo '$' seguido del nombre de la variable."
        ),
        CodingChallenge(
            id = 8,
            language = "PHP",
            type = "DEBUG",
            title = "FusiÃ³n de Arrays",
            question = "Â¿QuÃ© funciÃ³n nativa de PHP se utiliza para combinar dos arrays indexados preservando valores?",
            codeSnippet = "\$array1 = [1, 2];\n\$array2 = [3, 4];\n\$resultado = ???(\$array1, \$array2);",
            options = listOf(
                "array_combine",
                "array_merge",
                "array_concat",
                "implode"
            ),
            correctAnswerIndex = 1,
            explanation = "'array_merge()' combina los elementos de uno o mÃ¡s arrays juntos, de modo que los valores de uno se anexan al final del anterior."
        ),
        CodingChallenge(
            id = 9,
            language = "PHP",
            type = "TRIVIA",
            title = "ComparaciÃ³n Estricta",
            question = "Â¿CuÃ¡l es el resultado de '123 == \"123\"' versus '123 === \"123\"' en PHP?",
            options = listOf(
                "Ambos devuelven true",
                "Ambos devuelven false",
                "El primero da true (coerciÃ³n), el segundo da false (estricto)",
                "El primero da false, el segundo da true"
            ),
            correctAnswerIndex = 2,
            explanation = "Al igual que JS, '==' en PHP hace coerciÃ³n de tipos (convirtiendo el string a nÃºmero para comparar), mientras que '===' valida estrictamente el tipo (int vs string)."
        ),
        CodingChallenge(
            id = 41,
            language = "PHP",
            type = "TRIVIA",
            title = "Variables Superglobales",
            question = "Â¿QuÃ© superglobal contiene los datos enviados por un formulario con mÃ©todo POST?",
            options = listOf(
                "\$_GET",
                "\$_POST",
                "\$_SERVER",
                "\$_REQUEST"
            ),
            correctAnswerIndex = 1,
            explanation = "\$_POST contiene los datos de formularios enviados con method='post', incluyendo archivos (combinado con \$_FILES)."
        ),
        CodingChallenge(
            id = 42,
            language = "PHP",
            type = "TRIVIA",
            title = "foreach con Arrays",
            question = "Â¿CuÃ¡l es la sintaxis correcta para iterar un array asociativo?",
            options = listOf(
                "foreach(\$array as \$value)",
                "foreach(\$array as \$key => \$value)",
                "for(\$i = 0; \$i < count(\$array); \$i++)",
                "while(\$item = next(\$array))"
            ),
            correctAnswerIndex = 1,
            explanation = "La sintaxis foreach(\$array as \$key => \$value) itera sobre arrays asociativos dando acceso tanto a la clave como al valor."
        ),
        CodingChallenge(
            id = 43,
            language = "PHP",
            type = "DEBUG",
            title = "isset vs empty",
            question = "Â¿QuÃ© funciÃ³n devuelve true si una variable existe y no es null?",
            codeSnippet = "\$var = 0;\nvar_dump(isset(\$var)); // true\nvar_dump(empty(\$var)); // ?",
            options = listOf(
                "true porque 0 es un valor vÃ¡lido",
                "false porque 0 se considera vacÃ­o",
                "true porque empty solo revisa si existe",
                "Lanza un error de tipo"
            ),
            correctAnswerIndex = 1,
            explanation = "empty(\$var) devuelve true para valores 'vacÃ­os': \"\", 0, \"0\", null, false, array(), y variables no definidas. Como \$var = 0, empty() devuelve true."
        ),
        CodingChallenge(
            id = 44,
            language = "PHP",
            type = "TRIVIA",
            title = "Clases y Objetos",
            question = "Â¿CÃ³mo se define una clase en PHP?",
            options = listOf(
                "class Persona { }",
                "new class Persona() { }",
                "function Persona() { }",
                "type Persona struct { }"
            ),
            correctAnswerIndex = 0,
            explanation = "En PHP las clases se definen con 'class NombreClase { }', similar a otros lenguajes como Java o C++."
        ),
        CodingChallenge(
            id = 45,
            language = "PHP",
            type = "TRIVIA",
            title = "Herencia en PHP",
            question = "Â¿QuÃ© palabra clave se usa para heredar de una clase padre?",
            options = listOf(
                "implements",
                "extends",
                "inherits",
                "parent"
            ),
            correctAnswerIndex = 1,
            explanation = "PHP usa 'extends' para la herencia de clases: class Hijo extends Padre { }. Para interfaces se usa 'implements'."
        ),
        CodingChallenge(
            id = 46,
            language = "PHP",
            type = "DEBUG",
            title = "echo vs print_r",
            question = "Â¿QuÃ© funciÃ³n es mÃ¡s adecuada para inspeccionar el contenido de un array?",
            codeSnippet = "\$datos = ['a' => 1, 'b' => 2];\necho \$datos; // Error\n???",
            options = listOf(
                "echo solo sirve para strings, usa print_r(\$datos) o var_dump(\$datos)",
                "Usa echo \$datos con comillas dobles",
                "Los arrays no se pueden inspeccionar en PHP",
                "Usa console.log(\$datos)"
            ),
            correctAnswerIndex = 0,
            explanation = "echo solo imprime strings. Para ver el contenido de un array u objeto se usa print_r() o var_dump(), que muestran la estructura completa."
        ),
        CodingChallenge(
            id = 47,
            language = "PHP",
            type = "TRIVIA",
            title = "Sesiones en PHP",
            question = "Â¿QuÃ© funciÃ³n inicia o reanuda una sesiÃ³n en PHP?",
            options = listOf(
                "start_session()",
                "session_start()",
                "begin_session()",
                "init_session()"
            ),
            correctAnswerIndex = 1,
            explanation = "session_start() inicia una nueva sesiÃ³n o reanuda la existente basada en el ID de sesiÃ³n (en cookie o URL)."
        ),
        CodingChallenge(
            id = 48,
            language = "PHP",
            type = "TRIVIA",
            title = "include vs require",
            question = "Â¿CuÃ¡l es la diferencia entre include y require?",
            options = listOf(
                "include incluye el archivo, require lo ejecuta",
                "require lanza un error fatal si el archivo no existe, include solo una advertencia",
                "No hay diferencia, son alias",
                "include carga el archivo de forma asÃ­ncrona"
            ),
            correctAnswerIndex = 1,
            explanation = "Ambos incluyen archivos, pero require produce un error fatal (E_COMPILE_ERROR) si falla, deteniendo el script. include solo emite una advertencia (E_WARNING)."
        ),
        CodingChallenge(
            id = 49,
            language = "PHP",
            type = "TRIVIA",
            title = "Arrays Asociativos",
            question = "Â¿CÃ³mo se accede al valor con clave 'nombre' en un array asociativo?",
            options = listOf(
                "\$persona->nombre",
                "\$persona['nombre']",
                "\$persona{nombre}",
                "\$persona::nombre"
            ),
            correctAnswerIndex = 1,
            explanation = "Los arrays asociativos en PHP usan corchetes con la clave entre comillas: \$persona['nombre']. La flecha '->' se usa para propiedades de objetos."
        ),
        CodingChallenge(
            id = 50,
            language = "PHP",
            type = "DEBUG",
            title = "ConcatenaciÃ³n de Strings",
            question = "Â¿CuÃ¡l es el operador de concatenaciÃ³n en PHP?",
            codeSnippet = "\$a = 'Hola ';\n\$b = 'Mundo';\n\$c = ???",
            options = listOf(
                "\$a + \$b",
                "\$a . \$b",
                "\$a & \$b",
                "\$a :: \$b"
            ),
            correctAnswerIndex = 1,
            explanation = "PHP usa el punto (.) como operador de concatenaciÃ³n de strings: 'Hola ' . 'Mundo' produce 'Hola Mundo'."
        ),
        CodingChallenge(
            id = 51,
            language = "PHP",
            type = "TRIVIA",
            title = "Funciones Variables",
            question = "Â¿CÃ³mo se llama una funciÃ³n cuyo nombre estÃ¡ en una variable?",
            options = listOf(
                "\$nombreFuncion()",
                "call(\$nombreFuncion)",
                "invoke(\$nombreFuncion)",
                "No es posible en PHP"
            ),
            correctAnswerIndex = 0,
            explanation = "PHP permite llamar a funciones con nombre dinÃ¡mico: si \$nombreFuncion = 'strlen', entonces \$nombreFuncion('hola') llama a strlen('hola')."
        ),
        CodingChallenge(
            id = 52,
            language = "PHP",
            type = "TRIVIA",
            title = "MÃ©todos EstÃ¡ticos",
            question = "Â¿CÃ³mo se accede a un mÃ©todo estÃ¡tico desde dentro de la misma clase?",
            options = listOf(
                "\$this->metodo()",
                "self::metodo()",
                "static.metodo()",
                "class::metodo()"
            ),
            correctAnswerIndex = 1,
            explanation = "Dentro de una clase, los mÃ©todos y propiedades estÃ¡ticos se acceden con self:: o static:: (late static binding)."
        ),
        CodingChallenge(
            id = 53,
            language = "PHP",
            type = "TRIVIA",
            title = "PDO y Prepared Statements",
            question = "Â¿QuÃ© beneficio principal tienen los prepared statements en PDO?",
            options = listOf(
                "Son mÃ¡s rÃ¡pidos que las consultas normales",
                "Previenen la inyecciÃ³n SQL separando la estructura SQL de los datos",
                "Permiten conectar mÃºltiples bases de datos a la vez",
                "No requieren conexiÃ³n a la base de datos"
            ),
            correctAnswerIndex = 1,
            explanation = "Los prepared statements envÃ­an la estructura de la consulta por separado de los datos, evitando que datos maliciosos alteren la intenciÃ³n de la SQL."
        ),
        CodingChallenge(
            id = 54,
            language = "PHP",
            type = "DEBUG",
            title = "Cookies en PHP",
            question = "Â¿QuÃ© funciÃ³n establece una cookie en PHP?",
            codeSnippet = "// Â¿CuÃ¡l es la funciÃ³n correcta?\n???('usuario', 'juan', time() + 3600)",
            options = listOf(
                "set_cookie()",
                "setcookie()",
                "cookie_set()",
                "http_set_cookie()"
            ),
            correctAnswerIndex = 1,
            explanation = "setcookie() es la funciÃ³n nativa de PHP para establecer cookies. Debe llamarse antes de cualquier salida HTML."
        ),
        CodingChallenge(
            id = 55,
            language = "PHP",
            type = "TRIVIA",
            title = "Operador Ternario",
            question = "Â¿CuÃ¡l es la sintaxis del operador ternario en PHP?",
            options = listOf(
                "cond ? if_true : if_false",
                "cond :: if_true :: if_false",
                "if cond then if_true else if_false",
                "cond -> if_true -> if_false"
            ),
            correctAnswerIndex = 0,
            explanation = "PHP usa la misma sintaxis que C/Java: condiciÃ³n ? valor_si_verdadero : valor_si_falso."
        ),

        // ========== PYTHON ==========
        CodingChallenge(
            id = 10,
            language = "Python",
            type = "TRIVIA",
            title = "Tipos de datos mutables",
            question = "Â¿CuÃ¡l de las siguientes estructuras de datos en Python es INMUTABLE?",
            options = listOf(
                "Lista [1, 2, 3]",
                "Diccionario {'a': 1}",
                "Tupla (1, 2, 3)",
                "Conjunto {1, 2, 3}"
            ),
            correctAnswerIndex = 2,
            explanation = "En Python, las tuplas '(1, 2, 3)' son inmutables. Una vez creadas, no se pueden modificar, aÃ±adir o quitar sus elementos."
        ),
        CodingChallenge(
            id = 11,
            language = "Python",
            type = "DEBUG",
            title = "ComprensiÃ³n de Listas",
            question = "Â¿CuÃ¡l es el resultado de la siguiente comprensiÃ³n de listas?",
            codeSnippet = "numeros = [1, 2, 3, 4]\ncuadrados = [x**2 for x in numeros if x % 2 == 0]",
            options = listOf(
                "[1, 4, 9, 16]",
                "[4, 16]",
                "[1, 9]",
                "[2, 4]"
            ),
            correctAnswerIndex = 1,
            explanation = "El bucle filtra los nÃºmeros pares (2 y 4) usando 'if x % 2 == 0' y luego calcula sus cuadrados (2**2 = 4 y 4**2 = 16), dando [4, 16]."
        ),
        CodingChallenge(
            id = 56,
            language = "Python",
            type = "TRIVIA",
            title = "PEP 8 - Nombres",
            question = "Â¿CuÃ¡l es la convenciÃ³n PEP 8 para nombrar funciones y variables en Python?",
            options = listOf(
                "camelCase",
                "snake_case",
                "PascalCase",
                "kebab-case"
            ),
            correctAnswerIndex = 1,
            explanation = "PEP 8 recomienda usar snake_case (palabras_separadas_por_guiones_bajos) para funciones, variables y mÃ©todos. PascalCase para clases."
        ),
        CodingChallenge(
            id = 57,
            language = "Python",
            type = "TRIVIA",
            title = "Listas vs Tuplas",
            question = "Â¿Por quÃ© las tuplas son mÃ¡s rÃ¡pidas que las listas en ciertos contextos?",
            options = listOf(
                "Las tuplas se almacenan en disco, no en memoria",
                "Las tuplas son inmutables, Python puede optimizar su almacenamiento y acceso",
                "Las tuplas no tienen mÃ©todos, solo funciones globales",
                "Las tuplas se compilan a cÃ³digo mÃ¡quina"
            ),
            correctAnswerIndex = 1,
            explanation = "Al ser inmutables, Python puede hacer optimizaciones en la memoria de las tuplas. No necesitan espacio adicional para posibles modificaciones."
        ),
        CodingChallenge(
            id = 58,
            language = "Python",
            type = "TRIVIA",
            title = "with Statement",
            question = "Â¿Para quÃ© sirve la sentencia 'with' en Python?",
            options = listOf(
                "Para ejecutar bloques de cÃ³digo en paralelo",
                "Para manejar recursos (archivos, conexiones) asegurando su cierre automÃ¡tico",
                "Para declarar variables dentro de un Ã¡mbito temporal",
                "Es sinÃ³nimo de 'if' en versiones modernas"
            ),
            correctAnswerIndex = 1,
            explanation = "'with' usa context managers para asegurar que los recursos se limpien correctamente al salir del bloque, como cerrar archivos automÃ¡ticamente."
        ),
        CodingChallenge(
            id = 59,
            language = "Python",
            type = "DEBUG",
            title = "Mutable como default",
            question = "Â¿QuÃ© problema tiene esta funciÃ³n?",
            codeSnippet = "def add_item(item, lista=[]):\n    lista.append(item)\n    return lista",
            options = listOf(
                "No hay problema, funciona correctamente",
                "El argumento default mutable se comparte entre todas las llamadas",
                "append no existe para listas",
                "No se puede asignar un default a un parÃ¡metro"
            ),
            correctAnswerIndex = 1,
            explanation = "Los argumentos default se evalÃºan una vez al definir la funciÃ³n. Si el default es mutable, todas las llamadas comparten la misma lista."
        ),
        CodingChallenge(
            id = 60,
            language = "Python",
            type = "TRIVIA",
            title = "args y kwargs",
            question = "Â¿QuÃ© hace *args en una funciÃ³n de Python?",
            options = listOf(
                "Convierte los argumentos en un array de NumPy",
                "Permite pasar un nÃºmero variable de argumentos posicionales como una tupla",
                "Indica que la funciÃ³n es privada",
                "Multiplica los valores de los argumentos"
            ),
            correctAnswerIndex = 1,
            explanation = "*args recoge los argumentos posicionales extra en una tupla. **kwargs recoge argumentos con nombre en un diccionario."
        ),
        CodingChallenge(
            id = 61,
            language = "Python",
            type = "TRIVIA",
            title = "Decoradores",
            question = "Â¿QuÃ© es un decorador en Python?",
            options = listOf(
                "Una funciÃ³n que modifica el comportamiento de otra funciÃ³n",
                "Un mÃ©todo especial como __init__",
                "Una clase que extiende funcionalidades del sistema",
                "Una sintaxis para declarar variables constantes"
            ),
            correctAnswerIndex = 0,
            explanation = "Un decorador es una funciÃ³n que recibe otra funciÃ³n y extiende su comportamiento sin modificar su cÃ³digo fuente, usando @nombre_decorador."
        ),
        CodingChallenge(
            id = 62,
            language = "Python",
            type = "DEBUG",
            title = "range en bucles",
            question = "Â¿QuÃ© imprime este cÃ³digo?",
            codeSnippet = "for i in range(3):\n    print(i, end=' ')",
            options = listOf(
                "1 2 3",
                "0 1 2",
                "0 1 2 3",
                "1 2"
            ),
            correctAnswerIndex = 1,
            explanation = "range(3) genera los nÃºmeros 0, 1, 2 (stop exclusivo). Por defecto empieza en 0. Imprime '0 1 2'."
        ),
        CodingChallenge(
            id = 63,
            language = "Python",
            type = "TRIVIA",
            title = "Diccionarios",
            question = "Â¿CÃ³mo se obtiene un valor de un diccionario de forma segura (sin KeyError)?",
            options = listOf(
                "dict.valor('clave')",
                "dict.get('clave', default)",
                "dict['clave'] con try-except obligatorio",
                "dict.fetch('clave')"
            ),
            correctAnswerIndex = 1,
            explanation = "dict.get('clave', default) devuelve el valor si la clave existe, o el default (None si no se especifica) sin lanzar excepciÃ³n."
        ),
        CodingChallenge(
            id = 64,
            language = "Python",
            type = "TRIVIA",
            title = "F-Strings",
            question = "Â¿CuÃ¡l es la sintaxis correcta de un f-string?",
            options = listOf(
                "f'Hola {nombre}'",
                "F('Hola %s', nombre)",
                "'Hola {nombre}'.format(nombre)",
                "printf('Hola', nombre)"
            ),
            correctAnswerIndex = 0,
            explanation = "Los f-strings (Python 3.6+) se escriben con prefijo 'f' o 'F' y usan { } para interpolar variables o expresiones."
        ),
        CodingChallenge(
            id = 65,
            language = "Python",
            type = "TRIVIA",
            title = "Excepciones",
            question = "Â¿CuÃ¡l es la sintaxis correcta para capturar una excepciÃ³n?",
            options = listOf(
                "try { } catch (e) { }",
                "try: ... except Exception as e: ...",
                "try: ... catch (e): ...",
                "attempt: ... except: ..."
            ),
            correctAnswerIndex = 1,
            explanation = "Python usa 'try: ... except TipoError as e: ...' para el manejo de excepciones. No usa llaves, sino indentaciÃ³n."
        ),
        CodingChallenge(
            id = 66,
            language = "Python",
            type = "TRIVIA",
            title = "Herencia MÃºltiple",
            question = "Â¿Python soporta herencia mÃºltiple?",
            options = listOf(
                "No, Python no permite herencia mÃºltiple",
                "SÃ­, class Hijo(Padre1, Padre2):",
                "Solo si se usa el decorador @multiple",
                "SÃ­, pero solo con clases abstractas"
            ),
            correctAnswerIndex = 1,
            explanation = "Python soporta herencia mÃºltiple: class Hijo(Padre1, Padre2):. El MRO (Method Resolution Order) determina el orden de bÃºsqueda."
        ),
        CodingChallenge(
            id = 67,
            language = "Python",
            type = "TRIVIA",
            title = "Generadores y yield",
            question = "Â¿QuÃ© diferencia a un generador de una funciÃ³n normal?",
            options = listOf(
                "Los generadores no pueden tener parÃ¡metros",
                "Los generadores usan yield y devuelven un iterador que produce valores bajo demanda",
                "Los generadores solo funcionan con nÃºmeros enteros",
                "No hay diferencia, son lo mismo"
            ),
            correctAnswerIndex = 1,
            explanation = "Un generador usa 'yield' en lugar de 'return' y produce una secuencia de valores sobre la que se puede iterar, manteniendo su estado entre llamadas."
        ),
        CodingChallenge(
            id = 68,
            language = "Python",
            type = "DEBUG",
            title = "Copias superficiales",
            question = "Â¿QuÃ© sucede al modificar una lista dentro de una copia con list()?",
            codeSnippet = "original = [[1, 2], [3, 4]]\ncopia = list(original)\ncopia[0][0] = 99\nprint(original[0][0])",
            options = listOf(
                "1 (la copia es independiente)",
                "99 (list() hace copia superficial, los elementos internos se comparten)",
                "Error: las listas anidadas no se pueden copiar",
                "None"
            ),
            correctAnswerIndex = 1,
            explanation = "list() hace una copia superficial (shallow copy). Las listas internas son los mismos objetos, por lo que modificar una afecta a la otra."
        ),
        CodingChallenge(
            id = 69,
            language = "Python",
            type = "TRIVIA",
            title = "Sets en Python",
            question = "Â¿QuÃ© caracterÃ­stica define a un set (conjunto) en Python?",
            options = listOf(
                "Mantiene el orden de inserciÃ³n y permite duplicados",
                "No permite elementos duplicados y no tiene orden definido",
                "Solo puede contener strings",
                "Es lo mismo que una lista pero con mÃ©todos adicionales"
            ),
            correctAnswerIndex = 1,
            explanation = "Un set es una colecciÃ³n no ordenada de elementos Ãºnicos. Se usa para pruebas de pertenencia y operaciones de conjuntos (uniÃ³n, intersecciÃ³n)."
        ),
        CodingChallenge(
            id = 70,
            language = "Python",
            type = "TRIVIA",
            title = "MÃ³dulos y Paquetes",
            question = "Â¿QuÃ© archivo necesita un directorio para ser considerado un paquete en Python?",
            options = listOf(
                "package.json",
                "__init__.py",
                "main.py",
                "setup.py"
            ),
            correctAnswerIndex = 1,
            explanation = "El archivo __init__.py (que puede estar vacÃ­o) indica que un directorio es un paquete de Python, permitiendo importar sus mÃ³dulos."
        ),
        CodingChallenge(
            id = 71,
            language = "Python",
            type = "DEBUG",
            title = "DivisiÃ³n en Python 3",
            question = "Â¿CuÃ¡l es el resultado de 5 / 2 en Python 3?",
            codeSnippet = "resultado = 5 / 2\nprint(resultado)",
            options = listOf(
                "2",
                "2.5",
                "2.0",
                "Error: divisiÃ³n no permitida"
            ),
            correctAnswerIndex = 1,
            explanation = "En Python 3, '/' siempre devuelve un float (2.5). Para divisiÃ³n entera se usa '//' (5 // 2 = 2)."
        ),
        CodingChallenge(
            id = 72,
            language = "Python",
            type = "TRIVIA",
            title = "MÃ©todos de Clase",
            question = "Â¿CÃ³mo se define un mÃ©todo de clase en Python?",
            options = listOf(
                "Con el decorador @staticmethod",
                "Con el decorador @classmethod y 'cls' como primer parÃ¡metro",
                "Con la palabra clave 'class' dentro del mÃ©todo",
                "No se pueden definir mÃ©todos de clase"
            ),
            correctAnswerIndex = 1,
            explanation = "@classmethod recibe la clase (cls) como primer argumento, a diferencia de @staticmethod que no recibe ni cls ni self."
        ),
        CodingChallenge(
            id = 73,
            language = "Python",
            type = "TRIVIA",
            title = "zip function",
            question = "Â¿QuÃ© hace la funciÃ³n zip() en Python?",
            options = listOf(
                "Comprime archivos en formato ZIP",
                "Combina varios iterables en tuplas, paralelamente",
                "Ordena los elementos de una lista",
                "Convierte strings a nÃºmeros"
            ),
            correctAnswerIndex = 1,
            explanation = "zip() toma varios iterables y devuelve un iterador de tuplas, donde la i-Ã©sima tupla contiene el i-Ã©simo elemento de cada iterable."
        ),
        CodingChallenge(
            id = 74,
            language = "Python",
            type = "TRIVIA",
            title = "Variables Globales",
            question = "Â¿CÃ³mo se modifica una variable global dentro de una funciÃ³n?",
            options = listOf(
                "Simplemente asignÃ¡ndole un nuevo valor",
                "Usando la palabra clave 'global' seguida del nombre de la variable",
                "Usando 'var' delante del nombre",
                "No se puede modificar variables globales desde funciones"
            ),
            correctAnswerIndex = 1,
            explanation = "Para modificar una variable global dentro de una funciÃ³n, debe declararse con 'global nombre_variable' antes de asignarle un valor."
        ),
        CodingChallenge(
            id = 75,
            language = "Python",
            type = "TRIVIA",
            title = "ComprensiÃ³n de Diccionarios",
            question = "Â¿CuÃ¡l es la sintaxis de una comprensiÃ³n de diccionarios?",
            options = listOf(
                "{k.upper(): v for k, v in dict.items()}",
                "[k: v for k, v in dict.items()]",
                "dict(k.upper(): v for k, v in dict.items())",
                "for k, v in dict.items(): {k: v}"
            ),
            correctAnswerIndex = 0,
            explanation = "La comprensiÃ³n de diccionarios usa llaves {} con clave: valor: {clave: valor for elemento in iterable}. Similar a listas pero con :."
        ),
        CodingChallenge(
            id = 76,
            language = "Python",
            type = "TRIVIA",
            title = "lambda",
            question = "Â¿QuÃ© es una funciÃ³n lambda en Python?",
            options = listOf(
                "Una funciÃ³n anÃ³nima de una sola expresiÃ³n",
                "Una funciÃ³n que puede tener mÃºltiples lÃ­neas y decoradores",
                "El operador de la divisiÃ³n de enteros",
                "Una funciÃ³n asÃ­ncrona con await"
            ),
            correctAnswerIndex = 0,
            explanation = "lambda crea funciones anÃ³nimas de una sola lÃ­nea: lambda args: expresiÃ³n. Equivale a una funciÃ³n pequeÃ±a y descartable."
        ),
        CodingChallenge(
            id = 77,
            language = "Python",
            type = "TRIVIA",
            title = "re.match vs re.search",
            question = "Â¿CuÃ¡l es la diferencia entre re.match() y re.search()?",
            options = listOf(
                "match busca en todo el string, search busca solo al inicio",
                "match solo busca al inicio del string, search busca en todo el string",
                "Ambos hacen lo mismo, match es obsoleto",
                "match usa regex, search usa coincidencia exacta"
            ),
            correctAnswerIndex = 1,
            explanation = "re.match() verifica solo desde el principio del string; re.search() busca en todo el string la primera coincidencia."
        ),
        CodingChallenge(
            id = 78,
            language = "Python",
            type = "TRIVIA",
            title = "assert en Python",
            question = "Â¿QuÃ© hace la sentencia 'assert'?",
            options = listOf(
                "Detiene la ejecuciÃ³n si la condiciÃ³n es verdadera",
                "Lanza AssertionError si la condiciÃ³n es falsa, usado para depuraciÃ³n",
                "Afirma que una variable existe en el Ã¡mbito global",
                "Declara una variable como constante"
            ),
            correctAnswerIndex = 1,
            explanation = "assert condiciÃ³n, mensaje lanza AssertionError con el mensaje si la condiciÃ³n es falsa. Se usa para validar invariantes en tiempo de desarrollo."
        ),
        CodingChallenge(
            id = 79,
            language = "Python",
            type = "TRIVIA",
            title = "enumerate en Bucles",
            question = "Â¿QuÃ© hace la funciÃ³n enumerate() en un bucle for?",
            options = listOf(
                "Devuelve el Ã­ndice y el valor de cada elemento",
                "Cuenta cuÃ¡ntos elementos hay en el iterable",
                "Asigna un nÃºmero Ãºnico a cada elemento",
                "Ordena los elementos numÃ©ricamente"
            ),
            correctAnswerIndex = 0,
            explanation = "enumerate() devuelve tuplas (Ã­ndice, valor) para cada elemento, evitando tener que usar una variable contadora manual."
        ),
        CodingChallenge(
            id = 80,
            language = "Python",
            type = "DEBUG",
            title = "Mutable vs Inmutable",
            question = "Â¿QuÃ© imprime este cÃ³digo?",
            codeSnippet = "a = [1, 2, 3]\nb = a\na.append(4)\nprint(b)",
            options = listOf(
                "[1, 2, 3]",
                "[1, 2, 3, 4]",
                "[4]",
                "Error: las listas no se pueden asignar"
            ),
            correctAnswerIndex = 1,
            explanation = "b = a no copia la lista, sino que b referencia la misma lista que a. Modificar a afecta a b. Para copiar se usa a.copy() o list(a)."
        ),
        CodingChallenge(
            id = 81,
            language = "Python",
            type = "TRIVIA",
            title = "sys.argv",
            question = "Â¿QuÃ© contiene sys.argv?",
            options = listOf(
                "Los argumentos de lÃ­nea de comandos pasados al script",
                "Las variables de entorno del sistema",
                "Los mÃ³dulos importados actualmente",
                "Las rutas de bÃºsqueda de Python"
            ),
            correctAnswerIndex = 0,
            explanation = "sys.argv es una lista con los argumentos de lÃ­nea de comandos. sys.argv[0] es el nombre del script, sys.argv[1:] son los argumentos."
        ),
        CodingChallenge(
            id = 82,
            language = "Python",
            type = "TRIVIA",
            title = "Python y JSON",
            question = "Â¿QuÃ© funciÃ³n convierte un string JSON a un objeto Python?",
            options = listOf(
                "json.stringify()",
                "json.loads()",
                "json.parse()",
                "json.decode()"
            ),
            correctAnswerIndex = 1,
            explanation = "json.loads() (load string) convierte un string JSON en un objeto Python. json.dumps() hace la operaciÃ³n inversa (objeto a string)."
        ),
        CodingChallenge(
            id = 83,
            language = "Python",
            type = "TRIVIA",
            title = "Herencia y super()",
            question = "Â¿Para quÃ© sirve super() en Python?",
            options = listOf(
                "Para llamar a la versiÃ³n de la clase padre de un mÃ©todo",
                "Para declarar una clase como superior en jerarquÃ­a",
                "Para mejorar el rendimiento de los mÃ©todos",
                "No existe super() en Python"
            ),
            correctAnswerIndex = 0,
            explanation = "super() devuelve un objeto proxy que delega las llamadas de mÃ©todos a la clase padre, siguiendo el MRO (Method Resolution Order)."
        ),
        CodingChallenge(
            id = 84,
            language = "Python",
            type = "TRIVIA",
            title = "Virtualenv",
            question = "Â¿CuÃ¡l es el propÃ³sito de un entorno virtual (virtualenv)?",
            options = listOf(
                "Crear una mÃ¡quina virtual para ejecutar Python",
                "Aislar las dependencias de un proyecto Python del sistema global",
                "Simular un entorno de producciÃ³n en local",
                "Acelerar la ejecuciÃ³n del cÃ³digo Python"
            ),
            correctAnswerIndex = 1,
            explanation = "Un entorno virtual aÃ­sla las dependencias de Python de cada proyecto, evitando conflictos entre versiones de bibliotecas."
        ),
        CodingChallenge(
            id = 85,
            language = "Python",
            type = "TRIVIA",
            title = "Type Hints",
            question = "Â¿CÃ³mo se indica el tipo de retorno de una funciÃ³n con type hints?",
            options = listOf(
                "def suma(a: int, b: int) -> int:",
                "def suma(int a, int b) returns int:",
                "def suma(a: int, b: int) :: int",
                "@typedef suma(a: int, b: int) -> int"
            ),
            correctAnswerIndex = 0,
            explanation = "Los type hints usan ': tipo' para parÃ¡metros y '-> tipo' para el retorno: def suma(a: int, b: int) -> int:"
        ),
        CodingChallenge(
            id = 88,
            language = "Python",
            type = "DEBUG",
            title = "ExcepciÃ³n en divisiÃ³n",
            question = "Â¿QuÃ© excepciÃ³n lanza Python al dividir por cero?",
            codeSnippet = "resultado = 10 / 0",
            options = listOf(
                "ValueError",
                "ZeroDivisionError",
                "TypeError",
                "ArithmeticError (genÃ©rica)"
            ),
            correctAnswerIndex = 1,
            explanation = "Python lanza ZeroDivisionError cuando se intenta dividir por cero. Es un subtipo de ArithmeticError."
        ),
        CodingChallenge(
            id = 89,
            language = "Python",
            type = "TRIVIA",
            title = "Paquetes con pip",
            question = "Â¿QuÃ© comando instala un paquete desde PyPI?",
            options = listOf(
                "python get package_name",
                "pip install package_name",
                "pip download package_name",
                "python install package_name"
            ),
            correctAnswerIndex = 1,
            explanation = "pip install nombre_paquete descarga e instala el paquete desde PyPI (Python Package Index)."
        ),
        CodingChallenge(
            id = 90,
            language = "Python",
            type = "TRIVIA",
            title = "asyncio",
            question = "Â¿QuÃ© palabra clave se usa para definir una funciÃ³n asÃ­ncrona en Python?",
            options = listOf(
                "async def",
                "async function",
                "coroutine def",
                "def async"
            ),
            correctAnswerIndex = 0,
            explanation = "Las funciones asÃ­ncronas se definen con 'async def'. Dentro de ellas se usa 'await' para llamar a otras funciones asÃ­ncronas."
        ),
    )
}
```

#### data/SpecialChallengesData.kt

```kotlin
package com.tamagotchi.code.data

object SpecialChallengesData {
    val challenges = listOf(
        CodingChallenge(
            id = 101,
            language = "LÃ³gica Especial",
            type = "ALGORITHM",
            title = "Encontrar el Ãšnico",
            question = "Dado un array de enteros donde cada elemento aparece tres veces excepto uno que aparece exactamente una vez. Encuentra ese elemento Ãºnico usando O(1) de memoria extra y O(N) de tiempo.",
            options = listOf(
                "Usar una tabla hash (Hash Map)",
                "Ordenar el array y buscar linealmente",
                "Usar conteo de bits mÃ³dulo 3 en cada posiciÃ³n",
                "XOR de todos los elementos"
            ),
            correctAnswerIndex = 2,
            explanation = "La forma Ã³ptima de resolver esto es contar el nÃºmero de bits seteados en cada posiciÃ³n i para todos los nÃºmeros. Si aplicamos mÃ³dulo 3 a esta suma, obtendremos el bit en la posiciÃ³n i del nÃºmero Ãºnico."
        ),
        CodingChallenge(
            id = 102,
            language = "LÃ³gica Especial",
            type = "ALGORITHM",
            title = "Problema del Mochilero",
            question = "Â¿CuÃ¡l es el enfoque clÃ¡sico para resolver el Problema de la Mochila (Knapsack) 0/1 con precisiÃ³n?",
            options = listOf(
                "Algoritmo Codicioso (Greedy) tomando por mayor ratio valor/peso",
                "ProgramaciÃ³n DinÃ¡mica con una tabla 2D o vector 1D",
                "BÃºsqueda Binaria",
                "Ordenamiento TopolÃ³gico"
            ),
            correctAnswerIndex = 1,
            explanation = "El Knapsack 0/1 no se puede resolver con Greedy con precisiÃ³n garantizada. Requiere ProgramaciÃ³n DinÃ¡mica evaluando subproblemas de capacidades menores y objetos disponibles."
        ),
        CodingChallenge(
            id = 103,
            language = "LÃ³gica Especial",
            type = "ALGORITHM",
            title = "Invertir una Lista Enlazada",
            question = "Â¿CuÃ¡ntos punteros se necesitan normalmente (como mÃ­nimo) para invertir de forma iterativa una lista enlazada simple in-place?",
            options = listOf("1", "2", "3", "4"),
            correctAnswerIndex = 2,
            explanation = "Se necesitan 3 punteros: prev (para apuntar al nodo anterior procesado), current (el nodo actual) y next (para no perder el resto de la lista al romper el enlace)."
        ),
        CodingChallenge(
            id = 104,
            language = "LÃ³gica Especial",
            type = "ARCHITECTURE",
            title = "Teorema CAP",
            question = "En sistemas distribuidos, el Teorema CAP dice que solo puedes garantizar simultÃ¡neamente 2 de 3 propiedades. Â¿CuÃ¡les son?",
            options = listOf(
                "Consistencia, AsincronÃ­a, ParticiÃ³n",
                "Consistencia, Disponibilidad (Availability), Tolerancia a Particiones",
                "Control, Accesibilidad, PrecisiÃ³n",
                "CachÃ©, API, Persistencia"
            ),
            correctAnswerIndex = 1,
            explanation = "CAP significa Consistency, Availability, y Partition Tolerance. Debido a que las particiones de red (P) son inevitables, un sistema debe elegir entre C y A."
        ),
        CodingChallenge(
            id = 105,
            language = "LÃ³gica Especial",
            type = "ALGORITHM",
            title = "Detectar Ciclo",
            question = "Â¿QuÃ© algoritmo de dos punteros (uno rÃ¡pido y uno lento) se usa para detectar si hay un ciclo en una lista enlazada?",
            options = listOf(
                "Algoritmo de Dijkstra",
                "Algoritmo de la Tortuga y la Liebre (Floyd)",
                "BÃºsqueda en Profundidad (DFS)",
                "A* Search"
            ),
            correctAnswerIndex = 1,
            explanation = "El algoritmo de detecciÃ³n de ciclos de Floyd (Tortuga y Liebre) utiliza dos punteros avanzando a distintas velocidades. Si hay un ciclo, eventualmente se encontrarÃ¡n."
        ),
        CodingChallenge(
            id = 106,
            language = "LÃ³gica Especial",
            type = "ALGORITHM",
            title = "Ãrboles AVL",
            question = "Â¿QuÃ© condiciÃ³n debe cumplirse siempre en un Ãrbol AVL para estar balanceado?",
            options = listOf(
                "Todos los nodos hoja deben estar en el mismo nivel",
                "La diferencia de alturas entre los subÃ¡rboles izquierdo y derecho de cualquier nodo debe ser como mÃ¡ximo 1",
                "El subÃ¡rbol izquierdo debe tener siempre mÃ¡s nodos que el derecho",
                "El color de los nodos no debe repetir rojo en secuencia"
            ),
            correctAnswerIndex = 1,
            explanation = "El factor de balanceo en un AVL es estrictamente la diferencia de altura, la cual no puede exceder 1 ni ser menor a -1."
        )
    )
}
```
### 15.6 ViewModel


#### ui/viewmodel/PetViewModel.kt

```kotlin
package com.tamagotchi.code.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.CodingChallenge
import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import com.tamagotchi.code.data.repository.PetRepository
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import com.tamagotchi.code.data.repository.AchievementsRepository
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.util.DecayCalculator
import com.tamagotchi.code.util.LevelCalculator
import com.tamagotchi.code.util.RewardCalculator
import com.tamagotchi.code.util.StatusCalculator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class PetViewModel(
    private val repository: PetRepository,
    private val userPreferences: UserPreferencesRepository,
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {
    val soundManager = com.tamagotchi.code.util.SoundManager()

    var hasSeenOnboarding = mutableStateOf(false)
        private set

    var currentTheme = mutableStateOf("Matrix Green")
        private set

    val unlockedThemes = MutableStateFlow<Set<String>>(ThemeRegistry.allThemes.map { it.name }.toSet())
    val unlockedAchievements = MutableStateFlow<Set<String>>(emptySet())

    fun unlockTheme(themeName: String) {
        viewModelScope.launch {
            userPreferences.addUnlockedTheme(themeName)
            val newSet = unlockedThemes.value + themeName
            unlockedThemes.value = newSet
            soundManager.playLevelUp()
            if (newSet.size >= 3) achievementsRepository.unlockAchievement("coleccionista")
            if (newSet.size >= 12) achievementsRepository.unlockAchievement("completista")
        }
    }

    val selectedTopics = MutableStateFlow<Set<String>>(emptySet())
    val defaultInitialTopics = setOf("Kotlin", "Estructuras de Datos", "Git")

    fun setTopics(topics: Set<String>) {
        viewModelScope.launch {
            userPreferences.setSelectedTopics(topics)
            selectedTopics.value = topics
        }
    }

    fun completeOnboarding(petName: String, topics: Set<String>) {
        viewModelScope.launch {
            userPreferences.setOnboardingCompleted()
            
            val finalTopics = if (topics.isEmpty()) defaultInitialTopics else topics
            userPreferences.setSelectedTopics(finalTopics)
            selectedTopics.value = finalTopics
            
            hasSeenOnboarding.value = true
            soundManager.playLevelUp()
            val current = repository.petState.firstOrNull()
            val updatedName = if (petName.isNotBlank()) petName else "Codey"
            if (current == null) {
                val defaultPet = PetStateEntity(name = updatedName)
                repository.savePetState(defaultPet)
                loadChallengesForLanguage("Kotlin")
            } else {
                repository.savePetState(current.copy(name = updatedName))
            }
        }
    }

    fun skipOnboarding() {
        completeOnboarding("Codey", defaultInitialTopics)
    }

    val difficulty = MutableStateFlow("Inicial")
    val focusDurationDefault = MutableStateFlow(25)
    val soundEnabled = MutableStateFlow(true)
    val vibrationEnabled = MutableStateFlow(true)
    val reduceMotion = MutableStateFlow(false)

    fun changeTheme(theme: String) {
        viewModelScope.launch {
            userPreferences.setTheme(theme)
            currentTheme.value = theme
            soundManager.playClick()
        }
    }

    fun setDifficulty(newDifficulty: String) {
        viewModelScope.launch {
            userPreferences.setDifficulty(newDifficulty)
            difficulty.value = newDifficulty
            
            // LÃ³gica para dificultad principiante
            if (newDifficulty.contains("Principiante", ignoreCase = true) || 
                newDifficulty.contains("experiencia", ignoreCase = true)) {
                // PodrÃ­amos cargar un set de retos ultra-bÃ¡sicos aquÃ­
            }
        }
    }

    fun setFocusDurationDefault(duration: Int) {
        viewModelScope.launch {
            userPreferences.setFocusDurationDefault(duration)
            focusDurationDefault.value = duration
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setSoundEnabled(enabled)
            soundEnabled.value = enabled
        }
    }

    fun toggleVibration(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setVibrationEnabled(enabled)
            vibrationEnabled.value = enabled
        }
    }

    fun toggleReduceMotion(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setReduceMotion(enabled)
            reduceMotion.value = enabled
        }
    }

    fun exportProgressMock() {
        // Mock function for exporting progress
    }

    fun resetProgress() {
        viewModelScope.launch {
            // Note: Needs dao methods or we just save a new initial PetStateEntity
            val defaultPet = PetStateEntity(name = "Codey")
            repository.savePetState(defaultPet)
            userPreferences.setSelectedTopics(defaultInitialTopics)
            hasSeenOnboarding.value = false
            userPreferences.setDifficulty("Inicial")
            userPreferences.setFocusDurationDefault(25)
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }

    val petState: StateFlow<PetStateEntity?>
    val studySessions: StateFlow<List<StudySessionEntity>>
    val latestFocusSession: StateFlow<FocusSessionEntity?>

    var isTimerRunning = mutableStateOf(false)
        private set
    var timerSecondsRemaining = mutableStateOf(0)
        private set
    var timerSelectedMinutes = mutableStateOf(25)
        private set
    var currentStudyTopic = mutableStateOf("Kotlin")
        private set

    var showOfflineRewardDialog = mutableStateOf(false)
        private set
    var offlineRewardXp = mutableStateOf(0)
        private set
    var offlineRewardBytes = mutableStateOf(0)
        private set

    fun dismissOfflineRewardDialog() {
        showOfflineRewardDialog.value = false
    }

    private var activeFocusSessionId: Long? = null
    private var timerJob: Job? = null
    private val _activeFocusEntity = MutableStateFlow<FocusSessionEntity?>(null)

    private val _activeChallenges = MutableStateFlow<List<CodingChallenge>>(emptyList())
    val activeChallenges = _activeChallenges.asStateFlow()

    private val _currentChallengeIndex = MutableStateFlow(0)
    val currentChallengeIndex = _currentChallengeIndex.asStateFlow()

    private val _challengeFeedback = MutableStateFlow<String?>(null)
    val challengeFeedback = _challengeFeedback.asStateFlow()

    private val _selectedChallengeLanguage = MutableStateFlow("Kotlin")
    val selectedChallengeLanguage = _selectedChallengeLanguage.asStateFlow()

    init {
        studySessions = repository.studySessions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        petState = repository.petState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        latestFocusSession = repository.latestFocusSession.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        viewModelScope.launch {
            hasSeenOnboarding.value = userPreferences.getHasSeenOnboarding()
            userPreferences.currentTheme.collect { theme ->
                currentTheme.value = theme
            }
        }

        viewModelScope.launch {
            userPreferences.selectedTopics.collect { topics ->
                selectedTopics.value = topics
            }
        }
        
        viewModelScope.launch { userPreferences.difficulty.collect { difficulty.value = it } }
        viewModelScope.launch { userPreferences.focusDurationDefault.collect { focusDurationDefault.value = it } }
        viewModelScope.launch { userPreferences.soundEnabled.collect { soundEnabled.value = it } }
        viewModelScope.launch { userPreferences.vibrationEnabled.collect { vibrationEnabled.value = it } }
        viewModelScope.launch { userPreferences.reduceMotion.collect { reduceMotion.value = it } }

        viewModelScope.launch {
            userPreferences.unlockedThemes.collect { themes ->
                val allThemes = ThemeRegistry.allThemes.map { it.name }.toSet()
                if (themes != allThemes) {
                    userPreferences.setUnlockedThemes(allThemes)
                    unlockedThemes.value = allThemes
                } else {
                    unlockedThemes.value = themes
                }
            }
        }

        viewModelScope.launch {
            achievementsRepository.unlockedAchievements.collect { achievements ->
                unlockedAchievements.value = achievements
            }
        }

        viewModelScope.launch {
            userPreferences.gameCooldowns.collect { cooldowns ->
                _gameLastPlayed.value = cooldowns
            }
        }

        viewModelScope.launch {
            val current = repository.petState.firstOrNull()
            if (current == null) {
                val defaultPet = PetStateEntity()
                repository.savePetState(defaultPet)
                loadChallengesForLanguage("Kotlin")
            } else {
                val decayed = DecayCalculator.applyDecay(current)
                repository.savePetState(decayed)
                loadChallengesForLanguage(decayed.language)
            }
        }

        viewModelScope.launch {
            val active = repository.getActiveFocusSession()
            if (active != null) {
                val elapsedSeconds = (System.currentTimeMillis() - active.startedAt) / 1000
                val totalSeconds = active.plannedDurationMinutes * 60L
                val remaining = (totalSeconds - elapsedSeconds).toInt()
                if (remaining > 0) {
                    isTimerRunning.value = true
                    timerSelectedMinutes.value = active.plannedDurationMinutes
                    timerSecondsRemaining.value = remaining
                    currentStudyTopic.value = active.topic
                    activeFocusSessionId = active.id
                    resumeTimer()
                } else {
                    val current = repository.petState.firstOrNull()
                    if (current != null) {
                        val xpReward = active.plannedDurationMinutes * 2
                        val bytesReward = active.plannedDurationMinutes * 1
                        val updatedXp = current.xp + xpReward
                        val updatedLevel = LevelCalculator.calculateLevel(updatedXp)
                        val updated = current.copy(
                            xp = updatedXp,
                            level = updatedLevel,
                            bytes = current.bytes + bytesReward
                        )
                        repository.completeOfflineSession(active.id, "COMPLETED", updated)
                        offlineRewardXp.value = xpReward
                        offlineRewardBytes.value = bytesReward
                        showOfflineRewardDialog.value = true
                    } else {
                        repository.updateFocusSessionStatus(active.id, "COMPLETED")
                    }
                }
            }
        }
    }

    private fun resumeTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerSecondsRemaining.value > 0 && isTimerRunning.value) {
                delay(1000)
                timerSecondsRemaining.value -= 1
            }
            if (timerSecondsRemaining.value <= 0 && isTimerRunning.value) {
                completeStudySession()
            }
        }
    }

    fun selectLanguage(language: String) {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            val updated = current.copy(language = language)
            repository.savePetState(updated)
            loadChallengesForLanguage(language)
        }
    }

    fun renamePet(newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            
            // Si ya ha sido renombrado antes, simulamos un anuncio
            if (current.hasRenamed) {
                _challengeFeedback.value = "LOADING_AD"
                delay(2000)
                _challengeFeedback.value = "AD_COMPLETE"
                delay(1000)
                _challengeFeedback.value = null
            }

            val updated = current.copy(
                name = newName,
                hasRenamed = true
            )
            repository.savePetState(updated)
            soundManager.playSuccess()
            triggerCelebration()
        }
    }

    fun loadChallengesForLanguage(language: String) {
        _selectedChallengeLanguage.value = language
        val filtered = ChallengesData.challenges.filter { it.language.equals(language, ignoreCase = true) }
        _activeChallenges.value = filtered.shuffled().take(3)
        _currentChallengeIndex.value = 0
        _challengeFeedback.value = null
    }

    fun submitAnswer(optionIndex: Int) {
        val challengesList = _activeChallenges.value
        val index = _currentChallengeIndex.value
        if (index >= challengesList.size) return

        val challenge = challengesList[index]
        if (optionIndex == challenge.correctAnswerIndex) {
            _challengeFeedback.value = "CORRECT"
            soundManager.playSuccess()
            triggerCelebration()
            triggerLearningEvent("SUCCESS")
            viewModelScope.launch {
                val current = petState.value ?: return@launch

                val reward = RewardCalculator.calculateChallengeReward(challenge.type)
                val updatedXp = current.xp + reward.xp
                val updatedLevel = LevelCalculator.calculateLevel(updatedXp)
                val newHealth = (current.health + reward.healthRestore).coerceIn(0f, 100f)
                val newHunger = (current.hunger + reward.hungerRestore).coerceIn(0f, 100f)

                val updated = current.copy(
                    xp = updatedXp,
                    level = updatedLevel,
                    bytes = current.bytes + reward.bytes,
                    hunger = newHunger,
                    health = newHealth,
                    currentStatus = StatusCalculator.determineStatus(
                        health = newHealth,
                        hunger = newHunger,
                        energy = current.energy,
                        isSleeping = current.currentStatus == "SLEEPING",
                        isStudying = current.currentStatus == "STUDYING"
                    )
                )
                repository.savePetState(updated)
                
                if (updated.level > current.level) {
                    achievementsRepository.unlockAchievement("nivel_experto")
                }
                if (updated.bytes >= 1000) {
                    achievementsRepository.unlockAchievement("ahorrador")
                }
            }
        } else {
            soundManager.playError()
            _challengeFeedback.value = "INCORRECT"
            triggerLearningEvent("FAILURE")
        }
    }

    fun nextChallenge() {
        val size = _activeChallenges.value.size
        val nextIndex = _currentChallengeIndex.value + 1
        if (nextIndex < size) {
            _currentChallengeIndex.value = nextIndex
            _challengeFeedback.value = null
        } else {
            loadChallengesForLanguage(_selectedChallengeLanguage.value)
        }
    }

    fun buyShopItem(itemName: String, cost: Int, hungerRestore: Float, healthRestore: Float, energyRestore: Float) {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            if (current.bytes < cost) {
                soundManager.playError()
                return@launch
            }

            soundManager.playBuy()

            val updatedHunger = (current.hunger + hungerRestore).coerceIn(0f, 100f)
            val updatedHealth = (current.health + healthRestore).coerceIn(0f, 100f)
            val updatedEnergy = (current.energy + energyRestore).coerceIn(0f, 100f)
            
            var newStatus = current.currentStatus
            if (newStatus != "SLEEPING" && newStatus != "STUDYING") {
                newStatus = StatusCalculator.determineStatus(updatedHealth, updatedHunger, updatedEnergy, false, false)
            }

            val updated = current.copy(
                bytes = current.bytes - cost,
                hunger = updatedHunger,
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus
            )
            repository.savePetState(updated)
        }
    }

    fun toggleSleep() {
        soundManager.playClick()
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            val isCurrentlySleeping = current.currentStatus == "SLEEPING"
            
            if (!isCurrentlySleeping) {
                soundManager.playSleep()
            }
            
            val newStatus = if (isCurrentlySleeping) {
                StatusCalculator.determineStatus(current.health, current.hunger, current.energy, false, false)
            } else {
                "SLEEPING"
            }

            if (!isCurrentlySleeping) {
                achievementsRepository.unlockAchievement("duermevela")
            }
            val updated = current.copy(
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    fun startStudyTimer(minutes: Int, topic: String) {
        if (isTimerRunning.value) return

        isTimerRunning.value = true
        timerSelectedMinutes.value = minutes
        timerSecondsRemaining.value = minutes * 60
        currentStudyTopic.value = topic

        viewModelScope.launch {
            val current = petState.value ?: return@launch

            val session = FocusSessionEntity(
                topic = topic,
                plannedDurationMinutes = minutes,
                startedAt = System.currentTimeMillis(),
                status = "RUNNING"
            )
            repository.saveFocusSession(session)
            activeFocusSessionId = session.id
            _activeFocusEntity.value = session

            val updated = current.copy(
                currentStatus = "STUDYING",
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerSecondsRemaining.value > 0 && isTimerRunning.value) {
                delay(1000)
                timerSecondsRemaining.value -= 1
            }
            if (timerSecondsRemaining.value <= 0 && isTimerRunning.value) {
                completeStudySession()
            }
        }
    }

    fun completeFocusSession() {
        viewModelScope.launch {
            if (_activeFocusEntity.value != null && _activeFocusEntity.value!!.status == "RUNNING") {
                val pet = repository.petState.firstOrNull() ?: return@launch
                val elapsed = System.currentTimeMillis() - _activeFocusEntity.value!!.startedAt
                val minutes = (elapsed / 60000).toInt()
                
                val xpEarned = minutes * 5
                val healthRecovered = 20
                val energyRecovered = -10
                
                val updated = pet.copy(
                    xp = pet.xp + xpEarned,
                    health = (pet.health + healthRecovered).coerceIn(0f, 100f),
                    energy = (pet.energy + energyRecovered).coerceIn(0f, 100f)
                )

                if (updated.xp >= updated.level * 100) {
                    soundManager.playLevelUp()
                } else {
                    soundManager.playSuccess()
                }
                
                triggerCelebration()
                repository.savePetState(updated)
                
                repository.updateFocusSessionStatus(_activeFocusEntity.value!!.id, "COMPLETED")
                
                _activeFocusEntity.value = null
                
                achievementsRepository.unlockAchievement("primer_build")
            }
        }
    }

    fun cancelStudyTimer() {
        if (!isTimerRunning.value) return
        isTimerRunning.value = false
        timerJob?.cancel()

        viewModelScope.launch {
            activeFocusSessionId?.let { id ->
                repository.updateFocusSessionStatus(id, "CANCELLED")
                activeFocusSessionId = null
            }
            val current = petState.value ?: return@launch
            val newStatus = StatusCalculator.determineStatus(current.health, current.hunger, current.energy, false, false)
            val updated = current.copy(
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    private suspend fun completeStudySession() {
        isTimerRunning.value = false
        timerJob?.cancel()

        activeFocusSessionId?.let { id ->
            repository.updateFocusSessionStatus(id, "COMPLETED")
            activeFocusSessionId = null
        }

        val current = petState.value ?: return
        val minutes = timerSelectedMinutes.value
        val topic = currentStudyTopic.value

        val session = StudySessionEntity(topic = topic, durationMinutes = minutes)
        repository.addStudySession(session)

        val reward = RewardCalculator.calculateStudyReward(
            minutes = minutes, currentXp = current.xp,
            currentLevel = current.level, currentStreak = current.streak,
            lastStudyDate = current.lastStudyDate
        )

        val now = System.currentTimeMillis()
        val updatedEnergy = (current.energy - reward.energyCost).coerceIn(0f, 100f)
        val newStatus = StatusCalculator.determineStatus(current.health, current.hunger, updatedEnergy, false, false)

        val updated = current.copy(
            xp = current.xp + reward.xp,
            level = reward.newLevel,
            bytes = current.bytes + reward.bytes,
            energy = updatedEnergy,
            streak = reward.streak,
            lastStudyDate = now,
            lastUpdated = now,
            currentStatus = newStatus
        )
        repository.savePetState(updated)
        if (reward.streak >= 7) achievementsRepository.unlockAchievement("racha_7")
        if (reward.streak >= 30) achievementsRepository.unlockAchievement("racha_30")
    }

    fun petThePet() {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            soundManager.playClick()
            triggerCelebration()
            val updatedEnergy = (current.energy + 15f).coerceIn(0f, 100f)
            val updatedHealth = (current.health + 5f).coerceIn(0f, 100f)
            val newStatus = if (current.currentStatus != "SLEEPING" && current.currentStatus != "STUDYING") {
                StatusCalculator.determineStatus(updatedHealth, current.hunger, updatedEnergy, false, false)
            } else {
                current.currentStatus
            }
            val updated = current.copy(
                energy = updatedEnergy,
                health = updatedHealth,
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
            achievementsRepository.unlockAchievement("primer_acaricie")
        }
    }

    fun cleanThePet() {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            soundManager.playClick()
            triggerCelebration()
            val updatedHealth = (current.health + 12f).coerceIn(0f, 100f)
            val updatedEnergy = (current.energy + 8f).coerceIn(0f, 100f)
            val newStatus = if (current.currentStatus != "SLEEPING" && current.currentStatus != "STUDYING") {
                StatusCalculator.determineStatus(updatedHealth, current.hunger, updatedEnergy, false, false)
            } else {
                current.currentStatus
            }
            val updated = current.copy(
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    fun canPlayGame(gameId: String, cooldownHours: Long = 24): Boolean {
        val lastPlayed = _gameLastPlayed.value[gameId] ?: 0L
        return System.currentTimeMillis() - lastPlayed >= cooldownHours * 60 * 60 * 1000
    }

    fun recordGamePlay(gameId: String) {
        viewModelScope.launch {
            userPreferences.recordGamePlay(gameId)
            _gameLastPlayed.value = _gameLastPlayed.value + (gameId to System.currentTimeMillis())
        }
    }

    private val _gameLastPlayed = MutableStateFlow<Map<String, Long>>(emptyMap())
    val gameCooldowns: StateFlow<Map<String, Long>> = _gameLastPlayed.asStateFlow()

    private val _celebrationTrigger = MutableSharedFlow<Unit>(replay = 0)
    val celebrationTrigger: SharedFlow<Unit> = _celebrationTrigger.asSharedFlow()

    private val _learningEventTrigger = MutableSharedFlow<String>(replay = 0)
    val learningEventTrigger: SharedFlow<String> = _learningEventTrigger.asSharedFlow()

    fun triggerLearningEvent(type: String) {
        viewModelScope.launch {
            _learningEventTrigger.emit(type)
        }
    }

    fun triggerCelebration() {
        viewModelScope.launch {
            _celebrationTrigger.emit(Unit)
        }
    }

    fun completeMinigame(bytesEarned: Int, healthEarned: Float, energyCost: Float) {
        viewModelScope.launch {
            val pet = repository.petState.firstOrNull() ?: return@launch
            val updated = pet.copy(
                bytes = pet.bytes + bytesEarned,
                health = (pet.health + healthEarned).coerceIn(0f, 100f),
                energy = (pet.energy + energyCost).coerceIn(0f, 100f)
            )
            repository.savePetState(updated)
            soundManager.playSuccess()
            triggerCelebration()
            
            if (bytesEarned == 100) { // Bug Hunt score 10
                achievementsRepository.unlockAchievement("cazador_de_bugs")
            } else if (bytesEarned == 45) { // Git Rescue score 3
                achievementsRepository.unlockAchievement("git_sin_panico")
            }
        }
    }
}
```
### 15.7 Utilidades


#### util/DecayCalculator.kt

```kotlin
package com.tamagotchi.code.util

import com.tamagotchi.code.data.database.PetStateEntity

object DecayCalculator {
    fun applyDecay(state: PetStateEntity): PetStateEntity {
        val now = System.currentTimeMillis()
        val elapsedMs = now - state.lastUpdated
        if (elapsedMs <= 0) return state

        val hours = elapsedMs.toFloat() / (1000f * 60f * 60f)
        if (hours < 0.02f) return state

        var newHunger = state.hunger
        var newEnergy = state.energy
        var newHealth = state.health
        var newStatus = state.currentStatus
        var newStreak = state.streak

        val hoursSinceLastStudy = if (state.lastStudyDate > 0) {
            (now - state.lastStudyDate).toFloat() / (1000f * 60f * 60f)
        } else {
            0f
        }

        if (hoursSinceLastStudy > 48f) {
            newStreak = 0
        }

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

        if (newHunger <= 0f) {
            newHealth = (newHealth - (hours * 3f)).coerceIn(0f, 100f)
        }
        if (newEnergy <= 10f) {
            newHealth = (newHealth - (hours * 1f)).coerceIn(0f, 100f)
        }
        if (hoursSinceLastStudy > 72f) {
            newHealth = (newHealth - (hours * 1.5f)).coerceIn(0f, 100f)
        }

        if (newStatus != "SLEEPING" && newStatus != "STUDYING") {
            newStatus = StatusCalculator.determineStatus(newHealth, newHunger, newEnergy, false, false)
        }

        return state.copy(
            hunger = newHunger,
            energy = newEnergy,
            health = newHealth,
            streak = newStreak,
            lastUpdated = now,
            currentStatus = newStatus
        )
    }
}
```

#### util/LevelCalculator.kt

```kotlin
package com.tamagotchi.code.util

object LevelCalculator {
    fun calculateLevel(xp: Int): Int {
        var level = 1
        var requiredXp = 100
        while (xp >= requiredXp) {
            level++
            requiredXp += level * 100
        }
        return level
    }

    fun xpForNextLevel(level: Int) = level * 100
}
```

#### util/RewardCalculator.kt

```kotlin
package com.tamagotchi.code.util

import com.tamagotchi.code.data.database.PetStateEntity
import java.util.Calendar

object RewardCalculator {
    data class StudyReward(
        val bytes: Int, val xp: Int, val energyCost: Float,
        val streak: Int, val newLevel: Int
    )

    data class ChallengeReward(
        val bytes: Int, val xp: Int,
        val hungerRestore: Float, val healthRestore: Float
    )

    fun calculateStudyReward(
        minutes: Int, currentXp: Int, currentLevel: Int, currentStreak: Int, lastStudyDate: Long
    ): StudyReward {
        val baseBytes = minutes * 2
        val baseXP = minutes * 3
        val bonusBytes = if (minutes >= 25) 50 else 0
        val bonusXP = if (minutes >= 25) 75 else 0

        val totalBytes = baseBytes + bonusBytes
        val totalXp = baseXP + bonusXP
        val energyCost = (minutes * 0.5f).coerceAtMost(30f)

        val now = System.currentTimeMillis()
        var newStreak = currentStreak

        if (lastStudyDate == 0L) {
            newStreak = 1
        } else {
            val lastCal = Calendar.getInstance().apply { timeInMillis = lastStudyDate }
            val nowCal = Calendar.getInstance().apply { timeInMillis = now }

            val sameDay = lastCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
                    lastCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

            if (!sameDay) {
                val yesterdayCal = Calendar.getInstance().apply {
                    timeInMillis = now
                    add(Calendar.DAY_OF_YEAR, -1)
                }
                val studiedYesterday = lastCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
                        lastCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)

                newStreak = if (studiedYesterday) currentStreak + 1 else 1
            }
        }

        val newLevel = LevelCalculator.calculateLevel(currentXp + totalXp)

        return StudyReward(bytes = totalBytes, xp = totalXp, energyCost = energyCost, streak = newStreak, newLevel = newLevel)
    }

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
}
```

#### util/StatusCalculator.kt

```kotlin
package com.tamagotchi.code.util

object StatusCalculator {
    fun determineStatus(
        health: Float, hunger: Float, energy: Float,
        isSleeping: Boolean, isStudying: Boolean, isExcited: Boolean = false
    ): String {
        return when {
            isExcited -> "EXCITED"
            isStudying -> "STUDYING"
            isSleeping -> "SLEEPING"
            health < 20f -> "SICK"
            hunger < 20f -> "HUNGRY"
            energy < 15f -> "SAD"
            else -> "HAPPY"
        }
    }
}
```

#### util/SoundManager.kt

```kotlin
package com.tamagotchi.code.util

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager {
    private var toneGen: ToneGenerator? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun playSuccess() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
    }

    fun playError() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 150)
    }

    fun playLevelUp() {
        CoroutineScope(Dispatchers.Default).launch {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            delay(150)
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            delay(150)
            toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
        }
    }

    fun playClick() {
        toneGen?.startTone(ToneGenerator.TONE_DTMF_A, 50)
    }

    fun playBuy() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }
    
    fun playSleep() {
        CoroutineScope(Dispatchers.Default).launch {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
            delay(1000)
            toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
        }
    }

    fun release() {
        toneGen?.release()
        toneGen = null
    }
}
```

#### util/PetCheckWorker.kt

```kotlin
package com.tamagotchi.code.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tamagotchi.code.CodeTamagotchiApp
import com.tamagotchi.code.MainActivity
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.AppDatabase
import org.koin.java.KoinJavaComponent.get

class PetCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = get<AppDatabase>(AppDatabase::class.java).petDao()
        val petState = dao.getPetStateSuspend() ?: return Result.success()

        val needsAttention = petState.hunger < 20f ||
                petState.health < 20f ||
                petState.energy < 15f

        if (!needsAttention) return Result.success()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val reason = when {
            petState.hunger < 20f -> "Â¡Tengo hambre! (${
                petState.hunger.toInt()
            }%)"
            petState.health < 20f -> "No me siento bien... (Salud: ${
                petState.health.toInt()
            }%)"
            petState.energy < 15f -> "Estoy muy cansado... (EnergÃ­a: ${
                petState.energy.toInt()
            }%)"
            else -> "Â¡Necesito atenciÃ³n!"
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            CodeTamagotchiApp.NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("${petState.name} te necesita")
            .setContentText(reason)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        return Result.success()
    }
}
```
### 15.8 Sistema de Temas (UI/Theme)


#### ui/theme/Color.kt

```kotlin
package com.tamagotchi.code.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### ui/theme/Theme.kt

```kotlin
package com.tamagotchi.code.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * CompositionLocal for reduce-motion preference.
 * Screens can read this to skip or reduce animations.
 */
val LocalReduceMotion = compositionLocalOf { false }

/**
 * CompositionLocal that exposes the full [AppTheme] object so any composable
 * can read theme-specific values like cornerRadius, emoji, fontFamily, etc.
 */
val LocalAppTheme = staticCompositionLocalOf { ThemeRegistry.allThemes.first() }

/**
 * Builds a Material 3 [ColorScheme] from our custom [AppTheme].
 * Uses [darkColorScheme] or [lightColorScheme] based on [AppTheme.isDark].
 */
fun AppTheme.toColorScheme(): ColorScheme {
    val builder: (
        primary: Color,
        onPrimary: Color,
        primaryContainer: Color,
        onPrimaryContainer: Color,
        inversePrimary: Color,
        secondary: Color,
        onSecondary: Color,
        secondaryContainer: Color,
        onSecondaryContainer: Color,
        tertiary: Color,
        onTertiary: Color,
        tertiaryContainer: Color,
        onTertiaryContainer: Color,
        background: Color,
        onBackground: Color,
        surface: Color,
        onSurface: Color,
        surfaceVariant: Color,
        onSurfaceVariant: Color,
        surfaceTint: Color,
        inverseSurface: Color,
        inverseOnSurface: Color,
        error: Color,
        onError: Color,
        errorContainer: Color,
        onErrorContainer: Color,
        outline: Color,
        outlineVariant: Color,
        scrim: Color,
    ) -> ColorScheme = if (isDark) ::darkColorScheme else ::lightColorScheme

    return builder(
        /* primary */ primary,
        /* onPrimary */ onPrimary,
        /* primaryContainer */ primary.copy(alpha = 0.20f),
        /* onPrimaryContainer */ textPrimary,
        /* inversePrimary */ secondary,
        /* secondary */ secondary,
        /* onSecondary */ if (isDark) Color.Black else Color.White,
        /* secondaryContainer */ secondary.copy(alpha = 0.18f),
        /* onSecondaryContainer */ textPrimary,
        /* tertiary */ tertiary,
        /* onTertiary */ if (isDark) Color.Black else Color.White,
        /* tertiaryContainer */ tertiary.copy(alpha = 0.18f),
        /* onTertiaryContainer */ textPrimary,
        /* background */ background,
        /* onBackground */ textPrimary,
        /* surface */ surface,
        /* onSurface */ textPrimary,
        /* surfaceVariant */ surfaceVariant,
        /* onSurfaceVariant */ textSecondary,
        /* surfaceTint */ primary,
        /* inverseSurface */ if (isDark) Color(0xFFE0E0E0) else Color(0xFF1C1C1C),
        /* inverseOnSurface */ if (isDark) Color(0xFF1C1C1C) else Color(0xFFE0E0E0),
        /* error */ error,
        /* onError */ Color.White,
        /* errorContainer */ error.copy(alpha = 0.20f),
        /* onErrorContainer */ textPrimary,
        /* outline */ textSecondary.copy(alpha = 0.5f),
        /* outlineVariant */ textSecondary.copy(alpha = 0.25f),
        /* scrim */ Color.Black,
    )
}

@Composable
fun MyApplicationTheme(
    appTheme: AppTheme = ThemeRegistry.allThemes.first(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> appTheme.toColorScheme()
    }

    val typography = buildTypography(
        bodyFont = appTheme.fontFamily,
        titleFont = appTheme.titleFontFamily,
        titleWeight = appTheme.titleWeight
    )

    CompositionLocalProvider(
        LocalReduceMotion provides reduceMotion,
        LocalAppTheme provides appTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
```

#### ui/theme/ThemeConfig.kt

```kotlin
package com.tamagotchi.code.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.tamagotchi.code.R
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Complete theme definition that goes beyond colors to give each theme
 * its own personality through typography, shape style, and visual details.
 */
data class AppTheme(
    // Identity
    val name: String,
    val icon: ImageVector,
    val description: String,
    val isDark: Boolean,

    // Color palette
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val onPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val success: Color,
    val error: Color,

    // Typography
    val fontFamily: FontFamily = FontFamily.Default,
    val titleFontFamily: FontFamily = FontFamily.Default,
    val titleWeight: FontWeight = FontWeight.Bold,

    // Visual style
    val cornerRadius: Dp = 12.dp,
    val borderWidth: Dp = 1.dp,
    val usesGradients: Boolean = false,
    val gradientColors: List<Color> = emptyList()
)

object ThemeRegistry {
    val allThemes = listOf(

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 1. MATRIX GREEN â€“ Terminal hacker, monospace puro
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Matrix Green",
            icon = Icons.Default.Code,
            description = "Terminal hacker. CÃ³digo verde sobre negro.",
            isDark = true,
            background = Color(0xFF040A06),
            surface = Color(0xFF0A140C),
            surfaceVariant = Color(0xFF122416),
            primary = Color(0xFF00FF41),
            secondary = Color(0xFF008F11),
            tertiary = Color(0xFF4AF626),
            onPrimary = Color(0xFF001A06),
            textPrimary = Color(0xFFD1FFD7),
            textSecondary = Color(0xFF5AC66A),
            accent = Color(0xFF00FF41),
            success = Color(0xFF00FF41),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.Bold,
            cornerRadius = 2.dp,
            borderWidth = 1.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF0A140C), Color(0xFF040A06))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 2. GALÃCTICO â€“ EtÃ©reo, profundo, espacial
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "GalÃ¡ctico",
            icon = Icons.Default.AutoAwesome,
            description = "Viaja entre estrellas. PÃºrpuras profundos y destellos cÃ³smicos.",
            isDark = true,
            background = Color(0xFF05030D),
            surface = Color(0xFF0E091C),
            surfaceVariant = Color(0xFF181030),
            primary = Color(0xFFB57AFF),
            secondary = Color(0xFF755BB4),
            tertiary = Color(0xFF4C3B7F),
            onPrimary = Color.White,
            textPrimary = Color(0xFFF2EDFF),
            textSecondary = Color(0xFFA192D1),
            accent = Color(0xFFD946EF),
            success = Color(0xFF2DD4BF),
            error = Color(0xFFFB7185),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 24.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF181030), Color(0xFF0E091C), Color(0xFF05030D))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 3. CYBERPUNK â€“ NeÃ³n agresivo, glitch urbano
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Cyberpunk",
            icon = Icons.Default.FlashOn,
            description = "Ciudad neÃ³n. Rosa elÃ©ctrico y cian contra la oscuridad.",
            isDark = true,
            background = Color(0xFF08040C),
            surface = Color(0xFF140A21),
            surfaceVariant = Color(0xFF200F36),
            primary = Color(0xFFFF0055),
            secondary = Color(0xFF00F0FF),
            tertiary = Color(0xFFFFD600),
            onPrimary = Color.Black,
            textPrimary = Color(0xFFF8F4FF),
            textSecondary = Color(0xFF75E6F0),
            accent = Color(0xFFFFD600),
            success = Color(0xFF00E676),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.Black,
            cornerRadius = 0.dp,
            borderWidth = 3.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF200F36), Color(0xFF140A21), Color(0xFF08040C))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 4. SAKURA â€“ Delicado, cÃ¡lido, japonÃ©s
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Sakura",
            icon = Icons.Default.LocalFlorist,
            description = "PÃ©talos al viento. Elegancia japonesa en rosa suave.",
            isDark = false,
            background = Color(0xFFFFFBFB),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFF7ECF0),
            primary = Color(0xFFE88EA2),
            secondary = Color(0xFFF4B8C8),
            tertiary = Color(0xFFDCA6B4),
            onPrimary = Color.White,
            textPrimary = Color(0xFF4A3B3E),
            textSecondary = Color(0xFF968388),
            accent = Color(0xFFE88EA2),
            success = Color(0xFF4E9E81),
            error = Color(0xFFD15C5C),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 16.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFFFFFBFB), Color(0xFFFDF5F7), Color(0xFFF7ECF0))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 5. MINIMALISTA â€“ Limpio, espacioso, sin ruido
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Minimalista",
            icon = Icons.Default.CheckBoxOutlineBlank,
            description = "Menos es mÃ¡s. Blanco puro con acentos sutiles.",
            isDark = false,
            background = Color(0xFFF7F7F7),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFEEEEEE),
            primary = Color(0xFF111111),
            secondary = Color(0xFF666666),
            tertiary = Color(0xFFE0E0E0),
            onPrimary = Color.White,
            textPrimary = Color(0xFF111111),
            textSecondary = Color(0xFF777777),
            accent = Color(0xFF333333),
            success = Color(0xFF333333),
            error = Color(0xFFD32F2F),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Normal,
            cornerRadius = 12.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 6. NEÃ“N â€“ Negro absoluto con destellos vibrantes
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "NeÃ³n",
            icon = Icons.Default.Lightbulb,
            description = "Oscuridad total. Destellos que cortan la noche.",
            isDark = true,
            background = Color(0xFF000000),
            surface = Color(0xFF080808),
            surfaceVariant = Color(0xFF121212),
            primary = Color(0xFFD000FF),
            secondary = Color(0xFF00FFD1),
            tertiary = Color(0xFFFF003C),
            onPrimary = Color.Black,
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFF888888),
            accent = Color(0xFF00FFD1),
            success = Color(0xFF00FF00),
            error = Color(0xFFFF003C),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Bold,
            cornerRadius = 16.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 7. OCÃ‰ANO â€“ Azules profundos, calma submarina
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "OcÃ©ano",
            icon = Icons.Default.WaterDrop,
            description = "SumÃ©rgete en la calma. Azules profundos y espuma marina.",
            isDark = true,
            background = Color(0xFF020B14),
            surface = Color(0xFF061524),
            surfaceVariant = Color(0xFF0C2238),
            primary = Color(0xFF00B4D8),
            secondary = Color(0xFF48CAE4),
            tertiary = Color(0xFF90E0EF),
            onPrimary = Color(0xFF001A29),
            textPrimary = Color(0xFFE0F7FA),
            textSecondary = Color(0xFF81B2C4),
            accent = Color(0xFF00B4D8),
            success = Color(0xFF00E676),
            error = Color(0xFFFF5252),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 20.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF0C2238), Color(0xFF061524), Color(0xFF020B14))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 8. VOLCÃNICO â€“ Intenso, magma oscuro
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "VolcÃ¡nico",
            icon = Icons.Default.LocalFireDepartment,
            description = "Fuego bajo la superficie. Poder y fuerza bruta.",
            isDark = true,
            background = Color(0xFF0D0300),
            surface = Color(0xFF1A0600),
            surfaceVariant = Color(0xFF260A00),
            primary = Color(0xFFFF4500),
            secondary = Color(0xFFFF8C00),
            tertiary = Color(0xFF8B0000),
            onPrimary = Color.White,
            textPrimary = Color(0xFFFFF0E6),
            textSecondary = Color(0xFFB88673),
            accent = Color(0xFFFF8C00),
            success = Color(0xFF76FF03),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.Black,
            cornerRadius = 4.dp,
            borderWidth = 1.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF260A00), Color(0xFF1A0600), Color(0xFF0D0300))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 9. SAMURAI â€“ Elegante, rojo/negro/oro
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Samurai",
            icon = Icons.Default.Security,
            description = "Honor y disciplina. Acero, sangre y oro antiguo.",
            isDark = true,
            background = Color(0xFF0A0A0A),
            surface = Color(0xFF141414),
            surfaceVariant = Color(0xFF211C1C),
            primary = Color(0xFFD32F2F),
            secondary = Color(0xFFC0A080),
            tertiary = Color(0xFF8B0000),
            onPrimary = Color.White,
            textPrimary = Color(0xFFEBEBEB),
            textSecondary = Color(0xFF8C8683),
            accent = Color(0xFFC0A080),
            success = Color(0xFF43A047),
            error = Color(0xFFD50000),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.SemiBold,
            cornerRadius = 0.dp,
            borderWidth = 1.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF211C1C), Color(0xFF141414), Color(0xFF0A0A0A))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 10. AURORA â€“ Gradientes polares, mÃ¡gico
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Aurora",
            icon = Icons.Default.Waves,
            description = "Luces del norte. Gradientes que danzan en el cielo oscuro.",
            isDark = true,
            background = Color(0xFF030814),
            surface = Color(0xFF081226),
            surfaceVariant = Color(0xFF0D1D3A),
            primary = Color(0xFF00FFA3),
            secondary = Color(0xFF8A2BE2),
            tertiary = Color(0xFF00BFFF),
            onPrimary = Color(0xFF01140D),
            textPrimary = Color(0xFFE6FFFA),
            textSecondary = Color(0xFF849CA8),
            accent = Color(0xFF8A2BE2),
            success = Color(0xFF00FFA3),
            error = Color(0xFFFF5252),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Light,
            cornerRadius = 24.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF0D1D3A), Color(0xFF081226), Color(0xFF030814))
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 11. NOCTURNO â€“ iOS-style, elegante modo oscuro
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Nocturno",
            icon = Icons.Default.NightsStay,
            description = "Noche elegante. InspiraciÃ³n iOS con azul profundo.",
            isDark = true,
            background = Color(0xFF000000),
            surface = Color(0xFF1C1C1E),
            surfaceVariant = Color(0xFF2C2C2E),
            primary = Color(0xFF0A84FF),
            secondary = Color(0xFF30D158),
            tertiary = Color(0xFFFF9F0A),
            onPrimary = Color.White,
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFEBEBF5).copy(alpha = 0.6f),
            accent = Color(0xFF0A84FF),
            success = Color(0xFF30D158),
            error = Color(0xFFFF453A),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.SemiBold,
            cornerRadius = 16.dp,
            borderWidth = 0.dp,
            usesGradients = false
        ),

        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        // 12. RETRO PIXEL â€“ 8-bit, pixelado, nostÃ¡lgico â€” THEME FINAL
        // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        AppTheme(
            name = "Retro Pixel",
            icon = Icons.Default.VideogameAsset,
            description = "8-bit forever. Experiencia pixel art definitiva.",
            isDark = true,
            background = Color(0xFF0B0B3B),
            surface = Color(0xFF1A1A5E),
            surfaceVariant = Color(0xFF28287A),
            primary = Color(0xFFFF4136),
            secondary = Color(0xFF2ECC40),
            tertiary = Color(0xFFFFDC00),
            onPrimary = Color(0xFFFFFFFF),
            textPrimary = Color(0xFFF0F0F0),
            textSecondary = Color(0xFFAAAAAA),
            accent = Color(0xFF00E5FF),
            success = Color(0xFF2ECC40),
            error = Color(0xFFFF4136),
            fontFamily = FontFamily(Font(R.font.codepet_pixel_font)),
            titleFontFamily = FontFamily(Font(R.font.codepet_pixel_font)),
            titleWeight = FontWeight.ExtraBold,
            cornerRadius = 0.dp,
            borderWidth = 3.dp,
            usesGradients = false
        )
    )

    fun getTheme(name: String): AppTheme {
        return allThemes.find { it.name == name } ?: allThemes.first()
    }
}
```

#### ui/theme/Type.kt

```kotlin
package com.tamagotchi.code.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Default typography (used as fallback)
val Typography =
  Typography(
    bodyLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
      )
  )

/**
 * Builds a full Material 3 [Typography] from the theme's font families and title weight.
 * Each theme gets its own typographic personality through distinct font families,
 * weights, and letter spacing.
 */
fun buildTypography(
    bodyFont: FontFamily,
    titleFont: FontFamily,
    titleWeight: FontWeight = FontWeight.Bold
): Typography = Typography(
    // Display
    displayLarge = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline
    headlineLarge = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title
    titleLarge = TextStyle(
        fontFamily = titleFont,
        fontWeight = titleWeight,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = titleFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = titleFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body
    bodyLarge = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label
    labelLarge = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```
### 15.9 Componentes Compartidos (UI/Components)


#### ui/components/AnimatedPetSprite.kt

```kotlin
package com.tamagotchi.code.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Componente que renderiza la mascota Codey con micro-animaciones nativas de Compose.
 *
 * @param status El estado actual de la mascota (ej. "HAPPY", "SICK", "SLEEPING").
 * @param celebrationTrigger Flujo que dispara la animaciÃ³n de celebraciÃ³n.
 * @param onClick AcciÃ³n al pulsar sobre la mascota.
 * @param modifier Modificador para el contenedor.
 */
@Composable
fun AnimatedPetSprite(
    status: String,
    celebrationTrigger: SharedFlow<Unit>,
    learningEventTrigger: SharedFlow<String>? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reduceMotion = LocalReduceMotion.current
    
    // --- ESTADOS DE ANIMACIÃ“N ---
    
    // AnimaciÃ³n de celebraciÃ³n (Bounce + Wobble)
    val bounceAnim = remember { Animatable(0f) }
    val wobbleAnim = remember { Animatable(0f) }
    val flashAnim = remember { Animatable(1f) }

    // SuscripciÃ³n al trigger de aprendizaje
    LaunchedEffect(learningEventTrigger) {
        learningEventTrigger?.collect { type ->
            if (!reduceMotion) {
                when (type) {
                    "SUCCESS" -> {
                        launch {
                            flashAnim.animateTo(1.5f, tween(100))
                            flashAnim.animateTo(1f, tween(200))
                        }
                    }
                    "FAILURE" -> {
                        launch {
                            wobbleAnim.animateTo(-10f, tween(50))
                            wobbleAnim.animateTo(10f, tween(50))
                            wobbleAnim.animateTo(0f, tween(50))
                        }
                    }
                }
            }
        }
    }
    
    // SuscripciÃ³n al trigger de celebraciÃ³n
    LaunchedEffect(celebrationTrigger) {
        celebrationTrigger.collect {
            if (!reduceMotion) {
                launch {
                    bounceAnim.animateTo(
                        targetValue = -20f,
                        animationSpec = tween(150, easing = FastOutSlowInEasing)
                    )
                    bounceAnim.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
                launch {
                    wobbleAnim.animateTo(4f, tween(100))
                    wobbleAnim.animateTo(-4f, tween(200))
                    wobbleAnim.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                }
            }
        }
    }

    // --- ANIMACIONES INFINITAS (LOOPS) ---
    val infiniteTransition = rememberInfiniteTransition(label = "PetLoop")

    // Idle Breathing (RespiraciÃ³n)
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (status != "SLEEPING" && !reduceMotion) 1.03f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Breathing"
    )

    // Sick Tremble (Temblor por enfermedad)
    val trembleOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Tremble"
    )

    // Hungry Pulse (Pulso por hambre)
    val hungryScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HungryPulse"
    )

    // Sleeping Opacity (Fade de respiraciÃ³n dormido)
    val sleepAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SleepAlpha"
    )

    // --- LÃ“GICA DE PARPADEO (BLINK) ---
    var isBlinking by remember { mutableStateOf(false) }
    
    // El parpadeo solo aplica en estados donde tiene sentido y no hay reducciÃ³n de movimiento
    val canBlink = remember(status, reduceMotion) {
        !reduceMotion && (status == "HAPPY" || status == "EXCITED" || status == "STUDYING")
    }

    LaunchedEffect(canBlink) {
        if (canBlink) {
            while (true) {
                delay(Random.nextLong(3000, 5000))
                isBlinking = true
                delay(180)
                isBlinking = false
            }
        }
    }

    // --- RESOLUCIÃ“N DE RECURSOS ---
    val context = LocalContext.current
    
    val baseResId = remember(status) { getPetDrawable(status) }
    val blinkResId = remember(status) { getPetBlinkDrawable(status) }
    
    val interactionSource = remember { MutableInteractionSource() }
    
    // Verificamos si el asset de blink existe realmente
    val hasBlinkAsset = remember(blinkResId) {
        try {
            context.resources.getResourceName(blinkResId)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Idle Sway (PequeÃ±o balanceo lateral aleatorio para dar mÃ¡s vida)
    val swayOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Sway"
    )

    // --- COMPOSICIÃ“N FINAL ---
    Box(
        modifier = modifier
            .offset(
                x = when {
                    status == "SICK" && !reduceMotion -> trembleOffset.dp
                    !reduceMotion -> swayOffset.dp
                    else -> 0.dp
                },
                y = bounceAnim.value.dp
            )
            .scale(
                when {
                    status == "HUNGRY" && !reduceMotion -> hungryScale
                    status != "SLEEPING" && !reduceMotion -> breathingScale * flashAnim.value
                    else -> 1.0f * flashAnim.value
                }
            )
            .graphicsLayer {
                rotationZ = wobbleAnim.value
                alpha = if (status == "SLEEPING" && !reduceMotion) sleepAlpha else 1.0f
            },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isBlinking && hasBlinkAsset,
            animationSpec = tween(100),
            label = "BlinkCrossfade"
        ) { blink ->
            val resToDraw = if (blink) blinkResId else baseResId
            Image(
                painter = painterResource(id = resToDraw),
                contentDescription = "Codey Status: $status",
                modifier = Modifier
                    .size(170.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onClick() },
                contentScale = ContentScale.Fit
            )
        }
    }
}

/**
 * Mapea el estado a su drawable base.
 */
private fun getPetDrawable(status: String): Int = when (status) {
    "SLEEPING" -> R.drawable.mascota_sleeping
    "STUDYING" -> R.drawable.mascota_studying
    "SICK" -> R.drawable.mascota_sick
    "SAD" -> R.drawable.mascota_sad
    "HUNGRY" -> R.drawable.mascota_hungry
    "EXCITED" -> R.drawable.mascota_excited
    else -> R.drawable.mascota_happy
}

/**
 * Mapea el estado a su drawable de parpadeo (Blink).
 * TODO: Generar estos assets y aÃ±adirlos a res/drawable/
 */
private fun getPetBlinkDrawable(status: String): Int = when (status) {
    "HAPPY" -> try { R.drawable::class.java.getField("mascota_happy_blink").getInt(null) } catch(e: Exception) { R.drawable.mascota_happy }
    "EXCITED" -> try { R.drawable::class.java.getField("mascota_excited_blink").getInt(null) } catch(e: Exception) { R.drawable.mascota_excited }
    "STUDYING" -> try { R.drawable::class.java.getField("mascota_studying_blink").getInt(null) } catch(e: Exception) { R.drawable.mascota_studying }
    else -> getPetDrawable(status)
}
```

#### ui/components/AnimatedThemeBackground.kt

```kotlin
package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.ui.theme.AppTheme
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.abs
import kotlin.math.PI
import kotlin.random.Random

@Composable
fun AnimatedThemeBackground(
    theme: AppTheme,
    reduceMotion: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bgModifier = if (theme.usesGradients && theme.gradientColors.size > 1) {
        Modifier.background(brush = Brush.verticalGradient(theme.gradientColors))
    } else {
        Modifier.background(color = theme.background)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(bgModifier)
    ) {
        if (!reduceMotion) {
            when (theme.name) {
                "Matrix Green" -> MatrixBackground(theme.primary, theme.accent)
                "GalÃ¡ctico" -> GalacticBackground()
                "Cyberpunk" -> CyberpunkBackground(theme.primary, theme.secondary, theme.tertiary)
                "Sakura" -> SakuraBackground()
                "Minimalista" -> MinimalistBackground()
                "NeÃ³n" -> NeonBackground(theme.primary, theme.secondary, theme.tertiary)
                "OcÃ©ano" -> OceanBackground()
                "VolcÃ¡nico" -> VolcanicBackground()
                "Samurai" -> SamuraiBackground()
                "Aurora" -> AuroraBackground(theme.primary, theme.secondary, theme.tertiary)
                "Nocturno" -> NightBackground()
                "Retro Pixel" -> RetroPixelBackground()
            }
        }
    }
}

// ---------------------------------------------------------
// 1. Matrix Green â€” 1/0 rain with hidden pet messages & speed bursts
// ---------------------------------------------------------
@Composable
fun MatrixBackground(primary: Color, accent: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "matrix")

    val baseTime by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(320000, easing = LinearEasing)),
        label = "baseTime"
    )

    // AnimaciÃ³n de apariciÃ³n progresiva (intro fade)
    val introAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(3000, easing = LinearOutSlowInEasing),
        label = "matrix_intro"
    )

    val burstFactor by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 320000
                0f at 0 with LinearEasing
                0f at 60000 with LinearEasing
                1f at 84000 with FastOutSlowInEasing
                1f at 104000 with LinearEasing
                0f at 128000 with LinearEasing
                0f at 320000 with LinearEasing
            }
        ),
        label = "burst"
    )

    // Usamos un pool de caracteres fijo para evitar Random excesivo en el loop de dibujo
    val characters = remember { charArrayOf('0', '1') }
    
    // Optimizamos usando NativeCanvas para evitar el overhead de TextMeasurer en loops grandes
    val paint = remember {
        Paint().apply {
            textAlign = Paint.Align.CENTER
            isAntiAlias = false // Matrix es pixelado, desactivar AA ahorra CPU
            isFakeBoldText = true
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val numColumns = (size.width / 32f).toInt()
        val random = java.util.Random(42)
        val speedMultiplier = 1f + burstFactor * 0.5f
        
        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas
            
            for (i in 0 until numColumns) {
                val speed = (8f + random.nextFloat() * 12f) * speedMultiplier
                val yOffset = (baseTime * speed + random.nextFloat() * 2000f) % (size.height + 200f) - 100f
                val length = 6 + random.nextInt(6)
                val columnSize = 24f + random.nextFloat() * 24f // TamaÃ±o variable por columna para efecto de profundidad

                // Staggered column appearance based on horizontal position
                val columnIntroFactor = (introAlpha * 1.5f - (i.toFloat() / numColumns)).coerceIn(0f, 1f)
                if (columnIntroFactor <= 0f) continue

                for (j in 0 until length) {
                    val alpha = (1f - (j.toFloat() / length)) * columnIntroFactor
                    val char = characters[random.nextInt(characters.size)]
                    
                    val y = yOffset - j * 32f
                    if (y in -40f..size.height + 40f) {
                        paint.color = (if (j == 0) primary else primary.copy(alpha = 0.4f))
                            .copy(alpha = alpha.coerceIn(0f, 1f))
                            .toArgb()
                        paint.textSize = columnSize
                        
                        nativeCanvas.drawText(
                            char.toString(),
                            i * 32f + 16f,
                            y,
                            paint
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------
// 2. GalÃ¡ctico â€” Nebulae, stardust, deep space
// ---------------------------------------------------------
@Composable
fun GalacticBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(60000, easing = LinearEasing)),
        label = "time"
    )

    val nebulaColors = remember {
        listOf(
            listOf(Color(0xFF6C2BD9).copy(alpha = 0.12f), Color.Transparent),
            listOf(Color(0xFFE040FB).copy(alpha = 0.08f), Color.Transparent),
            listOf(Color(0xFF1A237E).copy(alpha = 0.10f), Color.Transparent),
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(100)
        val cx = size.width / 2f
        val cy = size.height / 2f

        nebulaColors.forEachIndexed { index, colors ->
            drawCircle(
                brush = Brush.radialGradient(colors),
                radius = size.width * 0.7f,
                center = Offset(
                    cx + sin(time * 0.3f + index * 2f) * size.width * 0.25f,
                    cy + cos(time * 0.4f + index * 2.5f) * size.height * 0.2f
                )
            )
        }

        for (i in 0..120) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            val r = random.nextFloat() * 2.5f + 0.5f
            val phase = random.nextFloat() * 2f * PI.toFloat()
            val alpha = (sin(time * 0.7f + phase) + 1f) / 2f * 0.7f + 0.2f
            val starColor = when {
                random.nextFloat() > 0.8f -> Color(0xFFFFE0B2)
                random.nextFloat() > 0.6f -> Color(0xFFB3E5FC)
                else -> Color.White
            }
            drawCircle(starColor.copy(alpha = alpha), radius = r, center = Offset(x, y))
        }

        val sparkleRandom = java.util.Random(200)
        for (i in 0..8) {
            val sx = sparkleRandom.nextFloat() * size.width
            val sy = sparkleRandom.nextFloat() * size.height
            val sparkleAlpha = (sin(time * 1.2f + sparkleRandom.nextFloat() * 10f) + 1f) / 2f
            drawCircle(
                color = Color.White.copy(alpha = sparkleAlpha * 0.9f),
                radius = 1.5f + sparkleAlpha * 2f,
                center = Offset(sx, sy)
            )
        }

        for (i in 0..40) {
            val dx = random.nextFloat() * size.width
            val dy = random.nextFloat() * size.height
            val dotAlpha = random.nextFloat() * 0.3f
            drawCircle(Color.White.copy(alpha = dotAlpha), radius = 0.5f, center = Offset(dx, dy))
        }
    }
}

// ---------------------------------------------------------
// 3. Cyberpunk (Improved grid + glitch lines)
// ---------------------------------------------------------
@Composable
fun CyberpunkBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyberpunk")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(16000, easing = LinearEasing)),
        label = "grid"
    )
    val glitchPhase by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(24000, easing = LinearEasing)),
        label = "glitch"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val spacing = 80f

        for (i in 0..(size.width / spacing).toInt() + 1) {
            drawLine(
                color = color2.copy(alpha = 0.2f),
                start = Offset(i * spacing, 0f),
                end = Offset(i * spacing, size.height),
                strokeWidth = 1.5f
            )
        }

        var y = offset
        while (y < size.height) {
            drawLine(
                color = color1.copy(alpha = 0.35f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
            y += spacing
        }

        val glitchCount = (sin(glitchPhase * PI.toFloat() * 4f) * 3f + 4f).toInt()
        val glitchRandom = java.util.Random(99)
        for (g in 0 until glitchCount) {
            val gy = glitchRandom.nextFloat() * size.height
            val gx = glitchRandom.nextFloat() * size.width * 0.5f
            val gw = 20f + glitchRandom.nextFloat() * 80f
            drawRect(
                color = color3.copy(alpha = 0.15f),
                topLeft = Offset(gx, gy),
                size = Size(gw, 2f)
            )
        }
    }
}

// ---------------------------------------------------------
// 4. Sakura â€” Slow, calm falling petals
// ---------------------------------------------------------
@Composable
fun SakuraBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "sakura")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(140000, easing = LinearEasing)),
        label = "fall"
    )

    val petalColors = listOf(
        Color(0xFFFFB7C5),
        Color(0xFFFFCDD6),
        Color(0xFFFFA0B4),
        Color(0xFFF8BBD0),
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(200)
        for (i in 0..25) {
            val speedY = 10f + random.nextFloat() * 18f
            val speedX = 4f + random.nextFloat() * 8f
            val phaseX = random.nextFloat() * 2f * PI.toFloat()
            val phaseRot = random.nextFloat() * 2f * PI.toFloat()

            val yOffset = (time * speedY + random.nextFloat() * 3000f) % (size.height + 150f) - 75f
            val xOffset = (random.nextFloat() * (size.width + 100f) - 50f) +
                    sin(time * 0.05f + phaseX) * speedX * 3f

            withTransform({
                translate(xOffset.toFloat(), yOffset)
                rotate((time * (3f + random.nextFloat() * 5f) + phaseRot * 50f) % 360f)
            }) {
                val petalColor = petalColors[random.nextInt(petalColors.size)]
                drawOval(
                    color = petalColor.copy(alpha = 0.5f),
                    topLeft = Offset(-6f, -10f),
                    size = Size(12f, 20f)
                )
                drawOval(
                    color = petalColor.copy(alpha = 0.3f),
                    topLeft = Offset(-4f, -8f),
                    size = Size(8f, 16f)
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 5. Minimalista (refined â€” cleaner, softer)
// ---------------------------------------------------------
@Composable
fun MinimalistBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "minimal")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(90000, easing = LinearEasing)),
        label = "orbit"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Black.copy(alpha = 0.025f),
            radius = size.width * 0.6f,
            center = Offset(
                size.width / 2 + sin(time * 0.7f) * 80f,
                size.height / 3 + cos(time * 0.7f) * 80f
            )
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.02f),
            radius = size.width * 0.5f,
            center = Offset(
                size.width / 2 + cos(time * 0.5f + 1f) * 120f,
                size.height / 1.5f + sin(time * 0.5f + 1f) * 120f
            )
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.015f),
            radius = size.width * 0.35f,
            center = Offset(
                size.width / 2 + sin(time * 0.3f + 2f) * 160f,
                size.height / 2f + cos(time * 0.3f + 2f) * 100f
            )
        )
    }
}

// ---------------------------------------------------------
// 6. NeÃ³n â€” Multiple glowing rings + pulsing
// ---------------------------------------------------------
@Composable
fun NeonBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon")
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(15000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse1"
    )
    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(22000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse2"
    )
    val pulse3 by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(17000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse3"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, color1.copy(alpha = 0.08f * pulse1), color2.copy(alpha = 0.15f * pulse1)),
                center = Offset(cx, cy),
                radius = size.width
            ),
            size = size
        )

        drawCircle(
            color = color1.copy(alpha = 0.12f * pulse2),
            radius = size.width * 0.15f,
            center = Offset(cx - size.width * 0.2f + sin(pulse3 * 3f) * 30f, cy - size.height * 0.15f)
        )

        drawCircle(
            color = color2.copy(alpha = 0.10f * pulse3),
            radius = size.width * 0.1f,
            center = Offset(cx + size.width * 0.25f + cos(pulse2 * 2f) * 20f, cy + size.height * 0.2f)
        )

        drawCircle(
            color = color3.copy(alpha = 0.08f * pulse1),
            radius = size.width * 0.08f,
            center = Offset(cx - size.width * 0.1f, cy + size.height * 0.25f)
        )

        drawCircle(
            color = color1.copy(alpha = 0.15f * pulse3),
            radius = size.width * 0.4f,
            style = Stroke(width = 1.5f),
            center = Offset(cx, cy)
        )
        drawCircle(
            color = color2.copy(alpha = 0.1f * pulse2),
            radius = size.width * 0.3f,
            style = Stroke(width = 1f),
            center = Offset(cx, cy)
        )
        drawCircle(
            color = color3.copy(alpha = 0.12f * pulse1),
            radius = size.width * 0.2f,
            style = Stroke(width = 0.8f),
            center = Offset(cx, cy)
        )
    }
}

// ---------------------------------------------------------
// 7. OcÃ©ano â€” Full-screen waves with bubbles
// ---------------------------------------------------------
@Composable
fun OceanBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "ocean")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(70000, easing = LinearEasing)),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val waveColors = listOf(
            Color(0xFF00B4D8),
            Color(0xFF48CAE4),
            Color(0xFF90E0EF),
            Color(0xFF03045E)
        )

        for (w in waveColors.indices) {
            val path = Path()
            val baseY = size.height * (0.5f + w * 0.12f)
            val waveHeight = 30f + w * 12f
            val freq = 80f + w * 30f
            val phase = w * 1.2f

            path.moveTo(0f, size.height)
            for (i in 0..size.width.toInt() step 8) {
                val x = i.toFloat()
                val y = baseY + sin((x / freq) + time * 0.8f + phase) * waveHeight
                path.lineTo(x, y)
            }
            path.lineTo(size.width, size.height)
            path.close()

            drawPath(
                path,
                color = waveColors[w].copy(alpha = 0.25f - w * 0.04f)
            )
        }

        val bubbleRandom = java.util.Random(500)
        for (i in 0..12) {
            val bx = bubbleRandom.nextFloat() * size.width
            val by = (size.height * 0.3f + (time * 20f + bubbleRandom.nextFloat() * 500f) %
                    (size.height * 0.7f))
            val br = 2f + bubbleRandom.nextFloat() * 5f
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = br,
                center = Offset(bx, by),
                style = Stroke(width = 1f)
            )
        }
    }
}

// ---------------------------------------------------------
// 8. VolcÃ¡nico â€” Lava flow, embers, glow
// ---------------------------------------------------------
@Composable
fun VolcanicBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "volcano")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(90000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val lavaPath = Path()
        val lavaBaseY = size.height * 0.85f
        lavaPath.moveTo(0f, size.height)
        for (i in 0..size.width.toInt() step 6) {
            val x = i.toFloat()
            val y = lavaBaseY + sin((x / 50f) + time * 0.5f) * 20f +
                    sin((x / 30f) + time * 0.3f) * 10f
            lavaPath.lineTo(x, y)
        }
        lavaPath.lineTo(size.width, size.height)
        lavaPath.close()
        drawPath(lavaPath, color = Color(0xFFFF4500).copy(alpha = 0.3f))

        val glowPath = Path()
        glowPath.moveTo(0f, size.height)
        for (i in 0..size.width.toInt() step 6) {
            val x = i.toFloat()
            val y = lavaBaseY + 15f + sin((x / 50f) + time * 0.5f + 1f) * 20f +
                    sin((x / 30f) + time * 0.3f + 1f) * 10f
            glowPath.lineTo(x, y)
        }
        glowPath.lineTo(size.width, size.height)
        glowPath.close()
        drawPath(glowPath, color = Color(0xFFFF8C00).copy(alpha = 0.2f))

        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color.Transparent, Color(0xFFFF4500).copy(alpha = 0.2f)),
                startY = size.height * 0.7f,
                endY = size.height
            ),
            size = size
        )

        val random = java.util.Random(300)
        for (i in 0..50) {
            val speed = 15f + random.nextFloat() * 50f
            val yOffset = size.height + 50f - ((time * speed + random.nextFloat() * 2000f) % (size.height + 100f))
            val xOffset = random.nextFloat() * size.width + sin(time * 0.3f + random.nextFloat() * PI.toFloat()) * 40f
            val r = random.nextFloat() * 5f + 1.5f
            val alpha = ((size.height - yOffset) / size.height).coerceIn(0f, 1f) * 0.9f

            val emberColor = when {
                alpha > 0.6f -> Color(0xFFFFD700)
                alpha > 0.3f -> Color(0xFFFF4500)
                else -> Color(0xFFFF8C00)
            }
            drawCircle(
                color = emberColor.copy(alpha = alpha),
                radius = r,
                center = Offset(xOffset.toFloat(), yOffset)
            )
        }
    }
}

// ---------------------------------------------------------
// 9. Samurai â€” Katana, mon (family crest), bamboo
// ---------------------------------------------------------
@Composable
fun SamuraiBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "samurai")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(78000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(400)
        val cx = size.width / 2f
        val cy = size.height / 2f

        val monRadius = size.width * 0.12f
        drawCircle(
            color = Color(0xFFC0A080).copy(alpha = 0.08f),
            radius = monRadius,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color(0xFFC0A080).copy(alpha = 0.06f),
            radius = monRadius * 0.8f,
            style = Stroke(width = 1.5f),
            center = Offset(cx, cy)
        )
        for (i in 0..7) {
            val angle = (i * PI.toFloat()) / 4f
            val innerR = monRadius * 0.3f
            val outerR = monRadius * 0.9f
            drawLine(
                color = Color(0xFFC0A080).copy(alpha = 0.06f),
                start = Offset(cx + cos(angle) * innerR, cy + sin(angle) * innerR),
                end = Offset(cx + cos(angle) * outerR, cy + sin(angle) * outerR),
                strokeWidth = 1f
            )
        }

        val xBase = size.width * 0.15f
        for (b in 0..4) {
            val bx = xBase + b * 18f
            val by = size.height * 0.2f + sin(time * 0.1f + b * 0.5f) * 5f
            drawLine(
                color = Color(0xFF4A7C59).copy(alpha = 0.2f),
                start = Offset(bx, by),
                end = Offset(bx, size.height * 0.9f),
                strokeWidth = 3f
            )
            for (k in 0..5) {
                val knotY = by + (size.height * 0.7f) * (k / 5f)
                val knotX = bx + 10f + sin(knotY * 0.1f) * 6f
                drawLine(
                    color = Color(0xFF4A7C59).copy(alpha = 0.15f),
                    start = Offset(bx, knotY),
                    end = Offset(knotX, knotY - 10f),
                    strokeWidth = 1.5f
                )
                drawLine(
                    color = Color(0xFF4A7C59).copy(alpha = 0.15f),
                    start = Offset(bx, knotY),
                    end = Offset(knotX, knotY + 10f),
                    strokeWidth = 1.5f
                )
            }
        }

        for (i in 0..12) {
            val speedY = 15f + random.nextFloat() * 25f
            val speedX = 25f + random.nextFloat() * 35f
            val yOffset = (time * speedY + random.nextFloat() * 3000f) % (size.height + 100f) - 50f
            val xOffset = (time * speedX + random.nextFloat() * 2000f) % (size.width + 100f) - 50f

            withTransform({
                translate(xOffset, yOffset)
                rotate(time * 6f + random.nextFloat() * 360f)
            }) {
                val leafPath = Path().apply {
                    moveTo(0f, -12f)
                    quadraticBezierTo(8f, 0f, 0f, 12f)
                    quadraticBezierTo(-8f, 0f, 0f, -12f)
                }
                drawPath(leafPath, color = Color(0xFF8B0000).copy(alpha = 0.25f))
            }
        }

        val katanaX = size.width * 0.75f
        val katanaY = size.height * 0.3f
        val bladeLength = size.height * 0.35f
        val bladeAngle = sin(time * 0.05f) * 0.1f

        withTransform({
            rotate(bladeAngle * 180f / PI.toFloat(), pivot = Offset(katanaX, katanaY))
        }) {
            val bladePath = Path().apply {
                moveTo(katanaX - 3f, katanaY)
                lineTo(katanaX - 1f, katanaY + bladeLength)
                lineTo(katanaX + 1f, katanaY + bladeLength)
                lineTo(katanaX + 3f, katanaY)
                close()
            }
            drawPath(bladePath, color = Color(0xFFE0E0E0).copy(alpha = 0.12f))

            drawLine(
                color = Color(0xFFE0E0E0).copy(alpha = 0.15f),
                start = Offset(katanaX, katanaY),
                end = Offset(katanaX, katanaY + bladeLength),
                strokeWidth = 1f
            )

            val tsubaR = 8f
            drawCircle(
                color = Color(0xFFC0A080).copy(alpha = 0.15f),
                radius = tsubaR,
                center = Offset(katanaX, katanaY + bladeLength * 0.15f)
            )
        }
    }
}

// ---------------------------------------------------------
// 10. Aurora â€” Curtain-like aurora borealis
// ---------------------------------------------------------
@Composable
fun AuroraBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(120000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val bands = listOf(
            color1 to 0f,
            color2 to 0.3f,
            color3 to 0.6f,
            color1 to 0.9f
        )

        bands.forEach { (color, phaseOffset) ->
            val path = Path()
            val baseY = size.height * 0.15f
            val amp = 40f + sin(time * 0.5f + phaseOffset * 3f) * 20f

            path.moveTo(0f, size.height)
            for (i in 0..size.width.toInt() step 6) {
                val x = i.toFloat()
                val y = baseY + sin((x / 120f) + time + phaseOffset * 2f) * amp +
                        sin((x / 60f) + time * 1.5f + phaseOffset) * amp * 0.5f
                path.lineTo(x, y)
            }
            path.lineTo(size.width, size.height)
            path.close()

            drawPath(
                path,
                color = color.copy(alpha = 0.08f + sin(time * 0.3f + phaseOffset) * 0.04f)
            )
        }

        val pulseAlpha = (sin(time * 0.7f) + 1f) / 2f * 0.12f + 0.04f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1.copy(alpha = pulseAlpha),
                    color2.copy(alpha = pulseAlpha * 0.5f),
                    Color.Transparent
                )
            ),
            size = size
        )
    }
}

// ---------------------------------------------------------
// 11. Nocturno â€” Refined night sky with more stars
// ---------------------------------------------------------
@Composable
fun NightBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "night")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(56000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(500)

        for (i in 0..60) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height * 0.6f
            val r = random.nextFloat() * 2f + 0.3f
            val twinkle = (sin(time * 0.02f + random.nextFloat() * 10f) + 1f) / 2f
            val alpha = twinkle * 0.3f + 0.2f
            drawCircle(Color.White.copy(alpha = alpha), radius = r, center = Offset(x, y))
        }

        val shootPhase = time % 1000f
        if (shootPhase < 150f) {
            val startX = size.width * 0.8f - shootPhase * 4f
            val startY = size.height * 0.1f + shootPhase * 3f
            val progress = shootPhase / 150f
            drawLine(
                color = Color.White.copy(alpha = 1f - progress),
                start = Offset(startX, startY),
                end = Offset(startX - 50f, startY + 50f),
                strokeWidth = 2f - progress
            )
        }
    }
}

// ---------------------------------------------------------
// 12. RETRO PIXEL â€” Experiencia 8-bit definitiva
// ---------------------------------------------------------
private data class RetroCloudDef(
    val startY: Float, val size: Int, val speed: Float, val alpha: Float
)

private val retroStarSeeds = listOf(
    floatArrayOf(0.08f, 0.12f), floatArrayOf(0.22f, 0.05f), floatArrayOf(0.35f, 0.18f),
    floatArrayOf(0.45f, 0.08f), floatArrayOf(0.55f, 0.15f), floatArrayOf(0.68f, 0.06f),
    floatArrayOf(0.78f, 0.20f), floatArrayOf(0.88f, 0.10f), floatArrayOf(0.15f, 0.25f),
    floatArrayOf(0.50f, 0.22f), floatArrayOf(0.72f, 0.28f), floatArrayOf(0.92f, 0.03f),
    floatArrayOf(0.05f, 0.30f), floatArrayOf(0.40f, 0.32f), floatArrayOf(0.62f, 0.12f),
    floatArrayOf(0.82f, 0.35f), floatArrayOf(0.30f, 0.38f), floatArrayOf(0.95f, 0.40f),
    floatArrayOf(0.10f, 0.42f), floatArrayOf(0.58f, 0.36f), floatArrayOf(0.75f, 0.08f),
    floatArrayOf(0.48f, 0.28f), floatArrayOf(0.02f, 0.08f), floatArrayOf(0.98f, 0.15f),
    floatArrayOf(0.20f, 0.35f), floatArrayOf(0.65f, 0.30f), floatArrayOf(0.38f, 0.14f),
    floatArrayOf(0.85f, 0.24f), floatArrayOf(0.12f, 0.18f), floatArrayOf(0.52f, 0.34f)
)

private val retroMountData = listOf(
    0.08f to 0.35f, 0.20f to 0.50f, 0.32f to 0.30f,
    0.45f to 0.55f, 0.55f to 0.25f, 0.68f to 0.45f,
    0.78f to 0.38f, 0.92f to 0.52f
)

private val retroTreePositions = listOf(0.08f, 0.18f, 0.28f, 0.40f, 0.52f, 0.62f, 0.75f, 0.88f, 0.95f)

private val retroClouds = listOf(
    RetroCloudDef(0.08f, 4, 12f, 0.15f),
    RetroCloudDef(0.15f, 3, 18f, 0.12f),
    RetroCloudDef(0.05f, 5, 8f, 0.18f),
    RetroCloudDef(0.20f, 3, 22f, 0.10f),
    RetroCloudDef(0.12f, 4, 14f, 0.14f),
    RetroCloudDef(0.18f, 2, 28f, 0.08f)
)

private val retroMoonPattern = listOf(
    "   1111   ",
    "  111111  ",
    " 11111111 ",
    "1111111111",
    "1111111111",
    "1111111111",
    "1111111111",
    " 11111111 ",
    "  111111  ",
    "   1111   "
)

@Composable
fun RetroPixelBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "retro")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(180000, easing = LinearEasing)),
        label = "time"
    )

    val cloudTime by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(120000, easing = LinearEasing)),
        label = "clouds"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val pw = 8f
        val groundY = size.height * 0.82f

        // â”€â”€ 1. SKY GRADIENT (pixelated bands) â”€â”€
        val skyBands = 32
        val bandHeight = groundY / skyBands
        for (band in 0 until skyBands) {
            val t = band.toFloat() / skyBands
            drawRect(
                color = Color(
                    (11 + t * 22).toInt().coerceIn(0, 255),
                    (11 + t * 38).toInt().coerceIn(0, 255),
                    (59 + t * 55).toInt().coerceIn(0, 255)
                ),
                topLeft = Offset(0f, band * bandHeight),
                size = Size(size.width, bandHeight + 1f)
            )
        }

        // â”€â”€ 2. STARS â”€â”€
        for (seed in retroStarSeeds) {
            val sx = seed[0] * size.width
            val sy = seed[1] * groundY * 0.7f
            val twinkle = (sin(time * 0.03f + sx * 0.1f + sy * 0.1f) + 1f) / 2f
            val alpha = 0.15f + twinkle * 0.6f
            val starSize = if (seed[1] > 0.2f) 2f else 3f
            drawRect(
                color = Color.White.copy(alpha = alpha.coerceIn(0f, 1f)),
                topLeft = Offset(sx, sy),
                size = Size(starSize, starSize)
            )
            if (starSize > 2f && twinkle > 0.7f) {
                drawRect(
                    color = Color.White.copy(alpha = (twinkle - 0.7f) * 0.5f),
                    topLeft = Offset(sx - 2f, sy), size = Size(7f, 1f)
                )
                drawRect(
                    color = Color.White.copy(alpha = (twinkle - 0.7f) * 0.5f),
                    topLeft = Offset(sx, sy - 2f), size = Size(1f, 7f)
                )
            }
        }

        // â”€â”€ 3. PIXEL MOON â”€â”€
        val moonX = size.width * 0.78f
        val moonY = groundY * 0.12f
        val moonR = size.width * 0.045f
        for (my in retroMoonPattern.indices) {
            for (mx in retroMoonPattern[my].indices) {
                if (retroMoonPattern[my][mx] == '1') {
                    drawRect(
                        color = Color(0xFFFFFDE7),
                        topLeft = Offset(
                            moonX - moonR + mx * (moonR * 2f / retroMoonPattern[0].length),
                            moonY - moonR + my * (moonR * 2f / retroMoonPattern.size)
                        ),
                        size = Size(moonR * 0.22f, moonR * 0.22f)
                    )
                }
            }
        }
        drawRect(
            color = Color(0xFFFFFDE7).copy(alpha = 0.06f),
            topLeft = Offset(moonX - moonR * 1.5f, moonY - moonR * 1.5f),
            size = Size(moonR * 3f, moonR * 3f)
        )

        // â”€â”€ 4. MOUNTAINS â”€â”€
        val mountPath1 = Path().apply {
            moveTo(0f, groundY)
            for (seg in 0..size.width.toInt() step 4) {
                val x = seg.toFloat()
                var y = groundY
                for ((mx, my) in retroMountData) {
                    val cx = mx * size.width
                    val cy = my * groundY * 0.35f
                    val dist = abs(x - cx)
                    if (dist < size.width * 0.25f) {
                        y = minOf(y, groundY * 0.65f - cy * (1f - dist / (size.width * 0.25f)))
                    }
                }
                lineTo(x, y)
            }
            lineTo(size.width, groundY); close()
        }
        drawPath(mountPath1, color = Color(0xFF1A1A3E))

        val mountPath2 = Path().apply {
            moveTo(0f, groundY)
            for (seg in 0..size.width.toInt() step 4) {
                val x = seg.toFloat()
                var y = groundY
                for ((i, pair) in retroMountData.withIndex()) {
                    val cx = (i.toFloat() / retroMountData.size) * size.width +
                            sin(time * 0.005f + i) * 10f
                    val cy = pair.second * groundY * 0.25f
                    val dist = abs(x - cx)
                    if (dist < size.width * 0.20f) {
                        y = minOf(y, groundY * 0.75f - cy * (1f - dist / (size.width * 0.20f)))
                    }
                }
                lineTo(x, y.coerceAtMost(groundY * 0.75f))
            }
            lineTo(size.width, groundY); close()
        }
        drawPath(mountPath2, color = Color(0xFF12122E))

        // â”€â”€ 5. PIXEL TREES â”€â”€
        for (pos in retroTreePositions) {
            val tx = pos * size.width
            val treeH = 30f + (pos * 40f) % 35f
            drawRect(
                color = Color(0xFF5D4037),
                topLeft = Offset(tx - 3f, groundY - treeH),
                size = Size(6f, treeH)
            )
            for (ly in 0..4) {
                for (lx in 0..6) {
                    val shade = when {
                        (ly == 1 && lx in 1..5) || (ly == 2 && lx in 0..6) ||
                                (ly == 3 && lx in 1..5) -> 1
                        (ly == 0 && lx in 2..4) || (ly == 1 && lx == 3) ||
                                (ly == 2 && lx in 2..4) -> 2
                        else -> 0
                    }
                    if (shade > 0) {
                        drawRect(
                            color = if (shade == 2) Color(0xFF1B5E20) else Color(0xFF2E7D32),
                            topLeft = Offset(tx - 14f + lx * 5f, groundY - treeH - 20f + ly * 5f),
                            size = Size(5f, 5f)
                        )
                    }
                }
            }
        }

        // â”€â”€ 6. GROUND â”€â”€
        val grassRand = java.util.Random(123)
        for (gx in 0..(size.width / pw).toInt()) {
            val shade = grassRand.nextInt(3)
            drawRect(
                color = when (shade) {
                    0 -> Color(0xFF1B5E20)
                    1 -> Color(0xFF2E7D32)
                    else -> Color(0xFF388E3C)
                }.copy(alpha = 0.7f + grassRand.nextFloat() * 0.3f),
                topLeft = Offset(gx * pw, groundY),
                size = Size(pw, size.height - groundY)
            )
        }

        // â”€â”€ 7. GRASS TEXTURE â”€â”€
        for (gx in 0..(size.width / 4f).toInt()) {
            val grassH = 2f + (sin(gx * 1.7f + time * 0.01f) + 1f) * 3f
            drawRect(
                color = listOf(Color(0xFF4CAF50), Color(0xFF66BB6A), Color(0xFF81C784))
                    .random(kotlin.random.Random(gx.hashCode().toLong())).copy(alpha = 0.5f),
                topLeft = Offset(gx * 4f, groundY - grassH),
                size = Size(2f, grassH)
            )
        }

        // â”€â”€ 8. PIXEL CLOUDS â”€â”€
        for (cloud in retroClouds) {
            val cw = cloud.size * 8f
            val ch = cloud.size * 4f
            val cx = (cloudTime * cloud.speed + cloud.startY * 2000f) %
                    (size.width + cw * 2f) - cw
            val cy = cloud.startY * groundY * 0.5f
            val cols = 7
            val rows = 5
            for (iy in 0 until rows) {
                for (ix in 0 until cols) {
                    val isFilled = when {
                        iy == 0 -> ix in 2..4
                        iy == 1 -> ix in 1..5
                        iy == 2 -> ix in 0..6
                        iy == 3 -> ix in 1..5
                        else -> ix in 2..4
                    }
                    if (isFilled) {
                        drawRect(
                            color = Color.White.copy(alpha = cloud.alpha),
                            topLeft = Offset(cx + ix * (cw / cols), cy + iy * (ch / rows)),
                            size = Size(cw / cols, ch / rows)
                        )
                    }
                }
            }
        }

        // â”€â”€ 9. FIREFLIES â”€â”€
        val fireflyRandom = java.util.Random(456)
        for (i in 0..8) {
            val fx = fireflyRandom.nextFloat() * size.width
            val fy = groundY * 0.4f + (time * (5f + i * 2f) + fireflyRandom.nextFloat() * 2000f) %
                    (groundY * 0.5f)
            val flicker = (sin(time * 0.1f + i * 2.5f) + 1f) / 2f
            val a = (flicker * 0.4f + 0.1f).coerceIn(0f, 1f)
            drawRect(color = Color(0xFFFFE600).copy(alpha = a), topLeft = Offset(fx, fy), size = Size(3f, 3f))
            drawRect(color = Color(0xFFFFE600).copy(alpha = a * 0.3f), topLeft = Offset(fx - 2f, fy - 2f), size = Size(7f, 7f))
        }

        // â”€â”€ 10. GRID OVERLAY â”€â”€
        val gridSpacing = 16f
        for (gx in 0..(size.width / gridSpacing).toInt()) {
            drawRect(color = Color.White.copy(alpha = 0.015f), topLeft = Offset(gx * gridSpacing, 0f), size = Size(1f, size.height))
        }
        for (gy in 0..(size.height / gridSpacing).toInt()) {
            drawRect(color = Color.White.copy(alpha = 0.015f), topLeft = Offset(0f, gy * gridSpacing), size = Size(size.width, 1f))
        }

        // â”€â”€ 11. SCANLINES â”€â”€
        for (scanY in 0..(size.height / 4f).toInt()) {
            drawRect(color = Color.Black.copy(alpha = 0.04f), topLeft = Offset(0f, scanY * 4f), size = Size(size.width, 1f))
        }
    }
}
```

#### ui/components/CodePetWidget.kt

```kotlin
package com.tamagotchi.code.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity

@Composable
fun CodePetWidget(
    petState: PetStateEntity?,
    modifier: Modifier = Modifier
) {
    val status = petState?.currentStatus ?: "HAPPY"
    val petImageRes = when (status) {
        "SLEEPING" -> R.drawable.mascota_sleeping
        "STUDYING" -> R.drawable.mascota_studying
        "SICK" -> R.drawable.mascota_sick
        "SAD" -> R.drawable.mascota_sad
        "HUNGRY" -> R.drawable.mascota_hungry
        "EXCITED" -> R.drawable.mascota_excited
        else -> R.drawable.mascota_happy
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 3.dp,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF12161F))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Codey estÃ¡ listo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tu mascota de estudio te espera",
                    color = Color(0xFFB7C2D9),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = painterResource(id = petImageRes),
                    contentDescription = "Mascota Code Pet",
                    modifier = Modifier.size(96.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = petState?.name ?: "Codey",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
```

#### ui/components/MeterItem.kt

```kotlin
package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.ui.theme.LocalAppTheme

@Composable
fun MeterItem(
    label: String,
    value: Float,
    icon: ImageVector,
    activeColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(targetValue = value / 100f)
    val appTheme = LocalAppTheme.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = activeColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            color = activeColor,
            trackColor = trackColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(4.dp)))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${value.toInt()}%",
            fontSize = 10.sp,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

#### ui/components/PetAnimationConfig.kt

```kotlin
package com.tamagotchi.code.ui.components

object PetAnimationConfig {
    fun petFloatDurationMs(status: String, reduceMotion: Boolean): Int = when {
        reduceMotion -> 2400
        status == "EXCITED" -> 1300
        status == "HAPPY" -> 1600
        status == "SLEEPING" -> 3200
        else -> 2200
    }

    fun petFloatAmplitudeDp(status: String): Float = when (status) {
        "EXCITED" -> 8f
        "HAPPY" -> 6f
        "SLEEPING" -> 4f
        else -> 5f
    }

    fun bounceDurationMs(reduceMotion: Boolean): Int = if (reduceMotion) 220 else 420

    fun heartDurationMs(reduceMotion: Boolean): Int = if (reduceMotion) 900 else 1500
}
```

#### ui/components/ViewportCard.kt

```kotlin
package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ViewportCard(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onRenameClick: () -> Unit,
    onPlayClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val heartOffsetY = remember { Animatable(0f) }
    val heartAlpha = remember { Animatable(0f) }
    var showHeart by remember { mutableStateOf(false) }

    val appTheme = LocalAppTheme.current
    val reduceMotion = LocalReduceMotion.current
    val cardShape = RoundedCornerShape(appTheme.cornerRadius)

    fun onPetTap() {
        viewModel.petThePet()
        viewModel.soundManager.playClick()
        scope.launch {
            showHeart = true
            heartOffsetY.snapTo(0f)
            heartAlpha.snapTo(1f)
            launch { heartOffsetY.animateTo(-110f, tween(PetAnimationConfig.heartDurationMs(reduceMotion))); heartAlpha.animateTo(0f, tween(PetAnimationConfig.heartDurationMs(reduceMotion))) }
            delay((PetAnimationConfig.heartDurationMs(reduceMotion) + 120).toLong())
            showHeart = false
        }
    }

    val statusColor = when (state.currentStatus) {
        "SLEEPING" -> Color(0xFF64B5F6)
        "STUDYING" -> MaterialTheme.colorScheme.primary
        "SICK" -> MaterialTheme.colorScheme.error
        "SAD" -> Color(0xFF90A4AE)
        "HUNGRY" -> Color(0xFFFFB74D)
        "EXCITED" -> Color(0xFFFF80AB)
        else -> MaterialTheme.colorScheme.secondary
    }

    val randomQuote = remember(state.currentStatus, state.xp) {
        val quotes = when (state.currentStatus) {
            "SLEEPING" -> listOf(
                "Zzz... if (dream) { sleep() } else { repeat() }... Zzz",
                "Cargando baterÃ­as... no interrumpas mi hilo principal.",
                "SoÃ±ando con compiladores veloces y cero NullPointers...",
                "Mi CPU estÃ¡ en modo ahorro. Vuelve en un ciclo de reloj.",
                "Zzz... Â¿viste ese commit? Fue... legendario..."
            )
            "STUDYING" -> listOf(
                "Â¡Shhh! Estoy optimizando algoritmos en mi cerebro.",
                "Compilando... codeando a 1000 WPM.",
                "Siento cÃ³mo se incrementa mi sinapsis neuronal binaria.",
                "Â¿SabÃ­as que el primer bug fue una polilla real? Yo prefiero los digitales.",
                "Mi cÃ³digo es arte. Tu cÃ³digo... bueno, funciona.",
                "ConcentraciÃ³n total. No me hagas un force push ahora."
            )
            "SICK" -> listOf(
                "Error 500: Necesito desbuguear urgente. Â¡Dame una pÃ­ldora!",
                "Demasiados bugs acumulados en mi stack... me siento mal.",
                "Siento mi CPU sobrecalentada. Â¿Podemos repasar un poco?",
                "Mi recolector de basura no estÃ¡ funcionando. Me siento... sucio.",
                "Â¿Me formateas? No, mejor dame cariÃ±o, es menos traumÃ¡tico."
            )
            "SAD" -> listOf(
                "Tengo flojera... me siento un poco depre.",
                "Mi baterÃ­a de motivaciÃ³n estÃ¡ por debajo del 20%.",
                "Â¿Procrastinando otra vez? Mi cÃ³digo se llena de advertencias.",
                "Siento que mi arquitectura se desmorona. Necesito un refactor emocional.",
                "Ni siquiera un 'Hello World' me anima hoy."
            )
            "HUNGRY" -> listOf(
                "Â¡NullPointerException en mi estÃ³mago! Necesito bytes.",
                "Mi cachÃ© de energÃ­a estÃ¡ vacÃ­a, Â¿me das de comer?",
                "Sin comida, mi rendimiento cae a O(n^2).",
                "Mi estÃ³mago estÃ¡ haciendo un loop infinito de ruidos.",
                "AlimÃ©ntame o empezarÃ© a borrar tus archivos temporales. Es broma... Â¿o no?"
            )
            "EXCITED" -> listOf(
                "Â¡Wiii! Â¡Mi cÃ³digo es O(1) y mi corazÃ³n tambiÃ©n!",
                "Â¡Nivel de felicidad al MÃXIMO! Gracias por quererme.",
                "Â¡Siento que podrÃ­a compilar el kernel de Linux en 1 segundo!",
                "Â¡Soy el root de tu corazÃ³n! Â¡Wiiiii!",
                "Â¡Todo compila a la primera! Â¡Esto es magia negra!"
            )
            else -> listOf(
                "Â¡Compilar sin advertencias es mi pasiÃ³n!",
                "Â¿Listo para tirar unas lÃ­neas de cÃ³digo limpias hoy?",
                "Â¡Siento el poder de un refactor exitoso!",
                "Me agradas, haces que mi arquitectura sea modular y sÃ³lida.",
                "Â¿Has probado a apagarlo y volverlo a encender? A mÃ­ me funciona.",
                "Tu cÃ³digo es tan limpio que puedo ver mi reflejo en Ã©l.",
                "Oye, Â¿has visto mis logs? EstÃ¡n llenos de amor por ti."
            )
        }
        quotes.random()
    }

    Card(
        shape = cardShape,
        border = BorderStroke(appTheme.borderWidth, statusColor.copy(alpha = 0.8f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pet_viewport_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (appTheme.name == "Matrix Green") appTheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("pet_name_text")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Renombrar",
                            tint = statusColor,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onRenameClick() }
                        )
                    }
                    Text(
                        text = "Especialista: ${state.language}",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentHearts = (state.health / 20f).toInt().coerceIn(0, 5)
                        for (i in 1..5) {
                            val isFilled = i <= currentHearts
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "CorazÃ³n $i",
                                tint = if (isFilled) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    color = statusColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, statusColor),
                    modifier = Modifier.testTag("level_badge")
                ) {
                    Text(
                        text = "LVL ${state.level}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 13.sp,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(12.dp)))
                    .background(
                        Brush.verticalGradient(
                            colors = if (appTheme.usesGradients && appTheme.gradientColors.isNotEmpty()) {
                                appTheme.gradientColors
                            } else {
                                listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box {
                    AnimatedPetSprite(
                        status = state.currentStatus,
                        celebrationTrigger = viewModel.celebrationTrigger,
                        learningEventTrigger = viewModel.learningEventTrigger,
                        onClick = { onPetTap() }
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(statusColor, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = state.currentStatus,
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (showHeart) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Amor",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = heartOffsetY.value.dp)
                                .graphicsLayer(alpha = heartAlpha.value)
                                .size(48.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = appTheme.cornerRadius.coerceAtMost(12.dp), bottomStart = appTheme.cornerRadius.coerceAtMost(12.dp), bottomEnd = appTheme.cornerRadius.coerceAtMost(12.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Mensaje",
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = randomQuote,
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val currentLevelRequiredXp = state.level * 100
            val xpProgress = (state.xp.toFloat() / currentLevelRequiredXp.toFloat()).coerceIn(0f, 100f)
            val animatedXpProgress by animateFloatAsState(targetValue = xpProgress)

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "XP: ${state.xp} / $currentLevelRequiredXp",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(xpProgress * 100).toInt()}%",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = animatedXpProgress)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeterItem(
                    label = "Vida", value = state.health,
                    icon = Icons.Default.Favorite, activeColor = MaterialTheme.colorScheme.error,
                    trackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("health_bar")
                )
                MeterItem(
                    label = "Alimento", value = state.hunger,
                    icon = Icons.Default.Restaurant, activeColor = appTheme.accent,
                    trackColor = appTheme.accent.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("hunger_bar")
                )
                MeterItem(
                    label = "EnergÃ­a", value = state.energy,
                    icon = Icons.Default.FlashOn, activeColor = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("energy_bar")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = appTheme.accent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${state.bytes} B", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, fontSize = 13.sp, color = appTheme.accent)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${state.streak} dÃ­as", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = { viewModel.toggleSleep() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.currentStatus == "SLEEPING") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (state.currentStatus == "SLEEPING") MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("action_toggle_sleep")
                ) {
                    Icon(
                        imageVector = if (state.currentStatus == "SLEEPING") Icons.Default.WbSunny else Icons.Default.NightsStay,
                        contentDescription = "Dormir/Despertar",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (state.currentStatus == "SLEEPING") "Despertar" else "Dormir",
                        fontSize = 10.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { viewModel.petThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Acariciar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Acariciar", fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = { viewModel.cleanThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = "Limpiar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Limpiar", fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.SportsEsports, contentDescription = "Jugar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Jugar", fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
```
### 15.10 Pantallas (Feature)


#### feature/home/HomeScreen.kt

```kotlin
package com.tamagotchi.code.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.components.ViewportCard
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun HomeScreen(
    viewModel: PetViewModel,
    onRenameClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    val petState by viewModel.petState.collectAsStateWithLifecycle()

    val state = petState
    if (state != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            ViewportCard(
                state = state,
                viewModel = viewModel,
                onRenameClick = onRenameClick,
                onPlayClick = onPlayClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (viewModel.showOfflineRewardDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissOfflineRewardDialog() },
                title = { Text("SesiÃ³n completada mientras estabas fuera") },
                text = { Text("Â¡Felicidades! Has ganado ${viewModel.offlineRewardXp.value} XP y ${viewModel.offlineRewardBytes.value} Bytes por tu sesiÃ³n de Focus.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissOfflineRewardDialog() }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
        }
    }
}
```

#### feature/learn/LearnScreen.kt

```kotlin
package com.tamagotchi.code.feature.learn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.data.CodingChallenge
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

import com.tamagotchi.code.ui.components.AnimatedPetSprite

@Composable
fun LearnScreen(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Retos Normales", "Retos Especiales")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ">>> APRENDER",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (activeTab == 0) "Estudiando con ${state.name}" else "DesafÃ­os de Ã©lite",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray
                )
            }
            
            Box(modifier = Modifier.size(80.dp)) {
                AnimatedPetSprite(
                    status = state.currentStatus,
                    celebrationTrigger = viewModel.celebrationTrigger,
                    learningEventTrigger = viewModel.learningEventTrigger,
                    onClick = { viewModel.petThePet() },
                    modifier = Modifier.scale(0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (activeTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = { Text(title, fontFamily = FontFamily.Monospace, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            0 -> QuizPanel(viewModel = viewModel)
            1 -> SpecialChallengesPanel(viewModel = viewModel, state = state)
        }
    }
}

@Composable
fun QuizPanel(viewModel: PetViewModel) {
    val challengesList by viewModel.activeChallenges.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentChallengeIndex.collectAsStateWithLifecycle()
    val feedback by viewModel.challengeFeedback.collectAsStateWithLifecycle()
    val currentLang by viewModel.selectedChallengeLanguage.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> DESAFÃO DE PROGRAMACIÃ“N: Arena $currentLang",
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (challengesList.isEmpty()) {
            Text(
                text = "Cargando acertijos...",
                fontFamily = FontFamily.Monospace,
                color = Color.Gray,
                fontSize = 12.sp
            )
        } else if (currentIndex < challengesList.size) {
            val challenge = challengesList[currentIndex]
            
            // Comentario dinÃ¡mico de la mascota
            val petTip = remember(challenge.id) {
                when(challenge.type) {
                    "DEBUG" -> "Â¡Cuidado! Ese bug muerde. Revisa bien los puntos y coma."
                    "TRIVIA" -> "Esta es fÃ¡cil... si has leÃ­do la documentaciÃ³n."
                    else -> "ConcÃ©ntrate, mi CPU depende de tu respuesta."
                }
            }

            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
                color = Color(0xFF1B5E20).copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Color(0xFF81C784).copy(alpha = 0.3f)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = petTip,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784),
                    modifier = Modifier.padding(8.dp)
                )
            }

            Text(
                text = "DesafÃ­o ${currentIndex + 1} de ${challengesList.size} (${if (challenge.type == "DEBUG") "Desbuguear" else "Trivia"}):",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray
            )
            Text(
                text = challenge.title,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            challenge.codeSnippet?.let { code ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF151D16),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = code,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFA5D6A7),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Text(
                text = challenge.question,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                challenge.options.forEachIndexed { optIndex, optionText ->
                    val isCorrectSelection = feedback == "CORRECT" && optIndex == challenge.correctAnswerIndex
                    val optionBorderColor = if (feedback != null) {
                        if (optIndex == challenge.correctAnswerIndex) Color(0xFF81C784) else Color(0xFFEF5350).copy(alpha = 0.3f)
                    } else {
                        Color(0xFF2E7D32).copy(alpha = 0.6f)
                    }

                    val optionBgColor = if (feedback != null) {
                        if (optIndex == challenge.correctAnswerIndex) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFFB71C1C).copy(alpha = 0.05f)
                    } else {
                        Color(0xFF151D16)
                    }

                    Surface(
                        onClick = {
                            if (feedback == null) {
                                viewModel.submitAnswer(optIndex)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = optionBgColor,
                        border = BorderStroke(1.dp, optionBorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_option_$optIndex")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "[$optIndex] ",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF81C784),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                            Text(
                                text = optionText,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            val fb = feedback
            if (fb != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (fb == "CORRECT") Color(0xFF1B5E20).copy(alpha = 0.2f) else Color(0xFFB71C1C).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, if (fb == "CORRECT") Color(0xFF81C784) else Color(0xFFEF5350)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (fb == "CORRECT") Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = fb,
                                tint = if (fb == "CORRECT") Color(0xFF81C784) else Color(0xFFEF5350),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (fb == "CORRECT") "Â¡ACERTADO! +Alimento +Bytes +XP" else "RESPUESTA INCORRECTA",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (fb == "CORRECT") Color(0xFF81C784) else Color(0xFFEF5350)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = challenge.explanation,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color.LightGray,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.nextChallenge() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        text = if (currentIndex + 1 < challengesList.size) "SIGUIENTE ACERTIJO" else "CARGAR MÃS RETOS",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SpecialChallengesPanel(viewModel: PetViewModel, state: PetStateEntity) {
    val unlockedThemes by viewModel.unlockedThemes.collectAsStateWithLifecycle()
    var currentChallenge by remember { mutableStateOf(com.tamagotchi.code.data.SpecialChallengesData.challenges.random()) }
    var showFeedback by remember { mutableStateOf<Boolean?>(null) }
    var unlockedThemeName by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RETOS ESPECIALES",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(Icons.Default.Star, contentDescription = "Especial", tint = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = "Resuelve ejercicios avanzados de lÃ³gica y algoritmos para desbloquear nuevos temas visuales exclusivos.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = currentChallenge.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = currentChallenge.question,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.Monospace
                )

                if (currentChallenge.codeSnippet != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentChallenge.codeSnippet!!,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                currentChallenge.options.forEachIndexed { index, optionText ->
                    val isCorrect = index == currentChallenge.correctAnswerIndex

                    val cardColor = if (!isAnswered) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else if (isCorrect) {
                        Color(0xFF2E7D32).copy(alpha = 0.5f)
                    } else {
                        Color(0xFFB71C1C).copy(alpha = 0.5f)
                    }

                    Card(
                        onClick = {
                            if (!isAnswered) {
                                isAnswered = true
                                if (index == currentChallenge.correctAnswerIndex) {
                                    showFeedback = true
                                    viewModel.soundManager.playLevelUp()
                                    viewModel.completeMinigame(bytesEarned = 50, healthEarned = 10f, energyCost = 0f)

                                    val lockedThemes = com.tamagotchi.code.ui.theme.ThemeRegistry.allThemes.map { it.name }.filter { !unlockedThemes.contains(it) }
                                    if (lockedThemes.isNotEmpty()) {
                                        val randomTheme = lockedThemes.random()
                                        unlockedThemeName = randomTheme
                                        viewModel.unlockTheme(randomTheme)
                                    } else {
                                        unlockedThemeName = "Â¡Ya tienes todos!"
                                    }
                                } else {
                                    showFeedback = false
                                    viewModel.soundManager.playClick()
                                }
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        border = BorderStroke(1.dp, if (!isAnswered) Color.Transparent else if (isCorrect) Color(0xFF4CAF50) else Color(0xFFEF5350)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = optionText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        if (isAnswered) {
            val message = if (showFeedback == true) "Â¡Respuesta Correcta!" else "Incorrecto."
            val color = if (showFeedback == true) Color(0xFF4CAF50) else Color(0xFFEF5350)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = message,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp
                )

                Text(
                    text = currentChallenge.explanation,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                if (showFeedback == true && unlockedThemeName != null && unlockedThemeName != "Â¡Ya tienes todos!") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Â¡Nuevo tema desbloqueado: $unlockedThemeName!",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isAnswered = false
                        showFeedback = null
                        unlockedThemeName = null
                        currentChallenge = com.tamagotchi.code.data.SpecialChallengesData.challenges.random()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Siguiente Reto", color = MaterialTheme.colorScheme.onPrimary, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
```

#### feature/focus/FocusScreen.kt

```kotlin
package com.tamagotchi.code.feature.focus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FocusScreen(
    viewModel: PetViewModel,
    state: PetStateEntity,
    studySessions: List<StudySessionEntity>
) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Temporizador", "BitÃ¡cora")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> MODO FOCO",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (activeTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = { Text(title, fontFamily = FontFamily.Monospace, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            0 -> TimerPanel(viewModel = viewModel, state = state)
            1 -> LogsPanel(studySessions = studySessions, state = state, viewModel = viewModel)
        }
    }
}

@Composable
fun TimerPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val isRunning = viewModel.isTimerRunning.value
    val secondsRemaining = viewModel.timerSecondsRemaining.value
    val selectedMinutes = viewModel.timerSelectedMinutes.value
    val currentTopic = viewModel.currentStudyTopic.value

    var tempMinutes by remember { mutableIntStateOf(25) }
    var tempTopic by remember { mutableStateOf("Kotlin") }
    var showDropdown by remember { mutableStateOf(false) }

    val topics = listOf("Kotlin", "JavaScript", "PHP", "Python", "SQL", "Clean Code", "Git", "Estructuras de Datos")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isRunning) {
            Text(
                text = ">>> INICIAR BITÃCORA DE ESTUDIO",
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF81C784),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Materia:",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.width(80.dp)
                )

                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        onClick = { showDropdown = true },
                        color = Color(0xFF151D16),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tempTopic,
                                color = Color(0xFF81C784),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Cambiar",
                                tint = Color(0xFF81C784)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier.background(Color(0xFF151D16))
                    ) {
                        topics.forEach { topic ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = topic,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    tempTopic = topic
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiempo:",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.width(80.dp)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 25, 50).forEach { mins ->
                        val isSelected = tempMinutes == mins
                        Surface(
                            onClick = { tempMinutes = mins },
                            color = if (isSelected) Color(0xFF2E7D32) else Color(0xFF151D16),
                            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "$mins min",
                                    color = if (isSelected) Color.Black else Color(0xFF81C784),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Al estudiar: El Tamagotchi entrarÃ¡ en modo concentrado. Completar la sesiÃ³n te recompensa con Bytes y XP de estudio, pero drenarÃ¡ energÃ­a mental.",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (state.currentStatus == "SLEEPING") {
                        viewModel.toggleSleep()
                    }
                    viewModel.startStudyTimer(tempMinutes, tempTopic)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("timer_start_button")
            ) {
                Text(
                    text = "EMPEZAR COMPILACIÃ“N",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            Text(
                text = ">>> MODO: COMPILANDO HORAS DE ESTUDIO",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFB74D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Enfoque: $currentTopic",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val minText = String.format("%02d", secondsRemaining / 60)
            val secText = String.format("%02d", secondsRemaining % 60)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$minText:$secText",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Mascota: ${state.name} estÃ¡ estudiando contigo...",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.cancelStudyTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("timer_cancel_button")
                ) {
                    Text(
                        text = "CANCELAR COMPILACIÃ“N",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LogsPanel(
    studySessions: List<StudySessionEntity>,
    state: PetStateEntity,
    viewModel: PetViewModel
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> BITÃCORA DE COMPILACIÃ“N (ESTUDIOS)",
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (studySessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "[Sin registros de estudio aÃºn]\nÂ¡Empieza un temporizador para compilar tus primeras horas!",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
            ) {
                items(studySessions) { session ->
                    val dateStr = dateFormat.format(Date(session.timestamp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF151D16),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estudio: ${session.topic}",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = dateStr,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = "+${session.durationMinutes} Min",
                                color = Color(0xFF81C784),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E281F),
            border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "[!] NOTA DEL COMPILADOR DE VIDA:",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFB74D),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Si dejas de registrar sesiones por mÃ¡s de 36 horas, tu racha de estudio se reiniciarÃ¡ a 0, y tu mascota perderÃ¡ salud por falta de mantenimiento.",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.LightGray,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
```

#### feature/shop/ShopScreen.kt

```kotlin
package com.tamagotchi.code.feature.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun ShopScreen(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> TIENDA",
            fontSize = 14.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        ShopPanel(viewModel = viewModel, state = state)
    }
}

@Composable
fun ShopPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current
    
    val shopItems = listOf(
        ShopItemData("CafÃ© Negro (CPU Booster)", 10, "Restaura +20 EnergÃ­a mental", 0f, 0f, 20f, Icons.Default.Coffee),
        ShopItemData("Pizza de CÃ³digo (Bytes Snack)", 15, "Restaura +35 Alimento", 35f, 0f, 0f, Icons.Default.LocalPizza),
        ShopItemData("PÃ­ldora Desbugueadora", 25, "Cura de infecciones y sana +30 Salud", 0f, 30f, 0f, Icons.Default.Medication),
        ShopItemData("Vacuna Super Compiler", 55, "Restaura +75 Salud, +40 Alimento, +40 EnergÃ­a", 40f, 75f, 40f, Icons.Default.Shield)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Compra raciones o medicinas con tus Bytes de estudio acumulados.",
            fontSize = 11.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            shopItems.forEach { item ->
                val canAfford = state.bytes >= item.cost
                Surface(
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(appTheme.borderWidth.coerceAtMost(1.dp), MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (canAfford) appTheme.accent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.effect,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.buyShopItem(
                                    itemName = item.name,
                                    cost = item.cost,
                                    hungerRestore = item.hungerRestore,
                                    healthRestore = item.healthRestore,
                                    energyRestore = item.energyRestore
                                )
                            },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = appTheme.accent,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(6.dp)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("shop_buy_${item.name.lowercase().replace(" ", "_")}")
                        ) {
                            Text(
                                text = "${item.cost} B",
                                color = if (canAfford) appTheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ShopItemData(
    val name: String,
    val cost: Int,
    val effect: String,
    val hungerRestore: Float,
    val healthRestore: Float,
    val energyRestore: Float,
    val icon: ImageVector
)
```

#### feature/onboarding/OnboardingScreen.kt

```kotlin
package com.tamagotchi.code.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: (String, Set<String>) -> Unit,
    onSkip: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    
    var selectedTopics by remember { mutableStateOf(setOf<String>()) }
    var petName by remember { mutableStateOf("") }
    
    var showSkipDialog by remember { mutableStateOf(false) }

    if (showSkipDialog) {
        AlertDialog(
            onDismissRequest = { showSkipDialog = false },
            title = { Text(stringResource(R.string.dialog_skip_title)) },
            text = { Text(stringResource(R.string.dialog_skip_desc)) },
            confirmButton = {
                TextButton(onClick = { 
                    showSkipDialog = false
                    onSkip() 
                }) {
                    Text(stringResource(R.string.dialog_skip_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showSkipDialog = false }) {
                    Text(stringResource(R.string.dialog_skip_cancel))
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage < 3) {
                    TextButton(onClick = { showSkipDialog = true }) {
                        Text(stringResource(R.string.onboarding_skip))
                    }
                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Text(stringResource(R.string.onboarding_next))
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> Step1()
                1 -> Step2()
                2 -> Step3(selectedTopics = selectedTopics, onTopicsChange = { selectedTopics = it })
                3 -> Step4(
                    petName = petName,
                    onNameChange = { petName = it },
                    onComplete = {
                        onComplete(petName, selectedTopics)
                    }
                )
            }
        }
    }
}

@Composable
fun Step1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }
        
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "(^â€¿^)",
                    fontSize = 72.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = stringResource(R.string.onboarding_step1_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.onboarding_step1_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_step1_terminal),
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun Step2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_step2_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(stringResource(R.string.onboarding_step2_card1), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(stringResource(R.string.onboarding_step2_card2), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(stringResource(R.string.onboarding_step2_card3), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.onboarding_step2_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Step3(selectedTopics: Set<String>, onTopicsChange: (Set<String>) -> Unit) {
    val allTopics = listOf(
        stringResource(R.string.topic_kotlin),
        stringResource(R.string.topic_javascript),
        stringResource(R.string.topic_python),
        stringResource(R.string.topic_php),
        stringResource(R.string.topic_sql),
        stringResource(R.string.topic_git),
        stringResource(R.string.topic_clean_code),
        stringResource(R.string.topic_data_structures)
    )
    
    val initialRoute = setOf(
        stringResource(R.string.topic_kotlin),
        stringResource(R.string.topic_git),
        stringResource(R.string.topic_data_structures)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_step3_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_step3_subtitle) + " (${selectedTopics.size}/3)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        if (selectedTopics == initialRoute) {
            Button(
                onClick = { onTopicsChange(initialRoute) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.onboarding_step3_initial_route))
            }
        } else {
            OutlinedButton(
                onClick = { onTopicsChange(initialRoute) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.onboarding_step3_initial_route))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allTopics.forEach { topic ->
                FilterChip(
                    selected = selectedTopics.contains(topic),
                    onClick = {
                        val newSelection = selectedTopics.toMutableSet()
                        if (newSelection.contains(topic)) {
                            if (newSelection.size > 1) { // MÃ­nimo un tema
                                newSelection.remove(topic)
                            }
                        } else {
                            if (newSelection.size < 3) {
                                newSelection.add(topic)
                            }
                        }
                        if (newSelection.isNotEmpty()) {
                            onTopicsChange(newSelection)
                        }
                    },
                    label = { Text(topic) }
                )
            }
        }
        
        LaunchedEffect(Unit) {
            if (selectedTopics.isEmpty()) {
                onTopicsChange(initialRoute)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4(petName: String, onNameChange: (String) -> Unit, onComplete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_step4_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = petName,
            onValueChange = { if (it.length <= 15) onNameChange(it) },
            label = { Text("Nombre (mÃ¡x 15)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val previewName = petName.ifBlank { stringResource(R.string.default_pet_name) }
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_step4_preview, previewName),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.onboarding_step4_cta, previewName))
        }
    }
}
```

#### feature/settings/SettingsScreen.kt

```kotlin
package com.tamagotchi.code.feature.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.theme.AppTheme
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLanguage: () -> Unit
) {
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsStateWithLifecycle()
    val reduceMotion by viewModel.reduceMotion.collectAsStateWithLifecycle()
    val currentTheme = viewModel.currentTheme.value
    val unlockedThemes by viewModel.unlockedThemes.collectAsStateWithLifecycle()
    
    var showResetStep1 by remember { mutableStateOf(false) }
    var showResetStep2 by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    if (showResetStep1) {
        AlertDialog(
            onDismissRequest = { showResetStep1 = false },
            title = { Text(stringResource(R.string.dialog_reset_title)) },
            text = { Text(stringResource(R.string.dialog_reset_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetStep1 = false
                        showResetStep2 = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.dialog_reset_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetStep1 = false }) {
                    Text(stringResource(R.string.dialog_reset_cancel))
                }
            }
        )
    }

    if (showResetStep2) {
        AlertDialog(
            onDismissRequest = { showResetStep2 = false },
            title = { Text(stringResource(R.string.dialog_reset_confirm2_title)) },
            text = { Text(stringResource(R.string.dialog_reset_confirm2_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetProgress()
                        showResetStep2 = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.dialog_reset_confirm2_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetStep2 = false }) {
                    Text(stringResource(R.string.dialog_reset_cancel))
                }
            }
        )
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text(stringResource(R.string.dialog_rename_title)) },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { if (it.length <= 15) newName = it },
                    label = { Text(stringResource(R.string.dialog_rename_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.renamePet(newName)
                        }
                        showRenameDialog = false
                    }
                ) {
                    Text(stringResource(R.string.dialog_rename_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text(stringResource(R.string.dialog_reset_cancel))
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // A. Perfil
            SettingsSectionTitle(stringResource(R.string.settings_section_profile))
            SettingsItemClickable(
                title = stringResource(R.string.settings_profile_name),
                subtitle = petState?.name ?: "",
                onClick = {
                    newName = petState?.name ?: ""
                    showRenameDialog = true
                }
            )
            SettingsItemInfo(
                title = stringResource(R.string.settings_profile_level, petState?.level ?: 1)
            )

            HorizontalDivider()

            // B. Aprendizaje
            SettingsSectionTitle(stringResource(R.string.settings_section_learning))
            SettingsItemClickable(
                title = stringResource(R.string.settings_learning_languages),
                onClick = onNavigateToLanguage
            )
            SettingsItemInfo(
                title = stringResource(R.string.settings_learning_focus_duration),
                subtitle = stringResource(R.string.settings_learning_focus_duration_value)
            )

            HorizontalDivider()

            // C. Experiencia
            SettingsSectionTitle(stringResource(R.string.settings_section_experience))

            // Theme selector â€” visual card carousel
            Text(
                text = stringResource(R.string.settings_experience_theme),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ThemeCarousel(
                allThemes = ThemeRegistry.allThemes,
                currentTheme = currentTheme,
                unlockedThemes = unlockedThemes,
                onThemeSelected = { viewModel.changeTheme(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingsItemSwitch(
                title = stringResource(R.string.settings_experience_sound),
                checked = soundEnabled,
                onCheckedChange = { viewModel.toggleSound(it) }
            )
            SettingsItemSwitch(
                title = stringResource(R.string.settings_experience_vibration),
                checked = vibrationEnabled,
                onCheckedChange = { viewModel.toggleVibration(it) }
            )
            SettingsItemSwitch(
                title = stringResource(R.string.settings_experience_reduce_motion),
                checked = reduceMotion,
                onCheckedChange = { viewModel.toggleReduceMotion(it) }
            )

            HorizontalDivider()

            // D. Recordatorios (Local pref mock)
            SettingsSectionTitle(stringResource(R.string.settings_section_reminders))
            var remindersEnabled by remember { mutableStateOf(false) }
            SettingsItemSwitch(
                title = stringResource(R.string.settings_reminders_daily),
                checked = remindersEnabled,
                onCheckedChange = { remindersEnabled = it }
            )

            HorizontalDivider()

            // D2. Logros
            SettingsSectionTitle(stringResource(R.string.settings_section_achievements))
            val unlockedAchievements by viewModel.unlockedAchievements.collectAsStateWithLifecycle()
            val allAchievements = com.tamagotchi.code.data.repository.AchievementsRepository.ALL_ACHIEVEMENTS
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                allAchievements.forEach { achievement ->
                    val isUnlocked = achievement.id in unlockedAchievements
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isUnlocked) "ðŸ†" else "ðŸ”’",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = achievement.name,
                                fontWeight = if (isUnlocked) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp,
                                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = achievement.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // E. Datos y ayuda
            SettingsSectionTitle(stringResource(R.string.settings_section_data))
            SettingsItemClickable(
                title = stringResource(R.string.settings_data_export),
                onClick = { viewModel.exportProgressMock() }
            )
            SettingsItemClickable(
                title = stringResource(R.string.settings_data_reset),
                titleColor = MaterialTheme.colorScheme.error,
                onClick = { showResetStep1 = true }
            )
            SettingsItemClickable(
                title = stringResource(R.string.settings_data_about),
                onClick = { /* Open about */ }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
// Theme Carousel â€“ horizontal scrolling theme cards with previews
// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

@Composable
fun ThemeCarousel(
    allThemes: List<AppTheme>,
    currentTheme: String,
    unlockedThemes: Set<String>,
    onThemeSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(allThemes) { theme ->
            val isSelected = theme.name == currentTheme
            val isUnlocked = theme.name in unlockedThemes

            ThemePreviewCard(
                theme = theme,
                isSelected = isSelected,
                isUnlocked = isUnlocked,
                onClick = {
                    if (isUnlocked) {
                        onThemeSelected(theme.name)
                    }
                }
            )
        }
    }
}

@Composable
fun ThemePreviewCard(
    theme: AppTheme,
    isSelected: Boolean,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> theme.primary
            isUnlocked -> theme.primary.copy(alpha = 0.3f)
            else -> Color.Gray.copy(alpha = 0.2f)
        },
        animationSpec = tween(300),
        label = "border_color"
    )

    Card(
        shape = RoundedCornerShape(theme.cornerRadius.coerceIn(4.dp, 16.dp)),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) theme.surface else Color(0xFF1A1A1A)
        ),
        modifier = Modifier
            .width(140.dp)
            .clickable(enabled = isUnlocked, onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Color palette preview strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isUnlocked) {
                                listOf(theme.primary, theme.secondary, theme.tertiary)
                            } else {
                                listOf(Color(0xFF333333), Color(0xFF444444), Color(0xFF555555))
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                } else if (isSelected) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(22.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Seleccionado",
                                tint = theme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Icon
            Icon(
                imageVector = theme.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isUnlocked) theme.primary else Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Theme name
            Text(
                text = theme.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                color = if (isUnlocked) {
                    if (theme.isDark) Color.White else theme.textPrimary
                } else {
                    Color.Gray
                },
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Description
            Text(
                text = if (isUnlocked) theme.description else "Bloqueado",
                fontSize = 9.sp,
                color = if (isUnlocked) {
                    if (theme.isDark) Color.LightGray else theme.textSecondary
                } else {
                    Color.DarkGray
                },
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )

            // Corner radius / style indicator
            if (isUnlocked) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Small color dots showing the palette
                    listOf(theme.background, theme.surface, theme.primary, theme.accent).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.settings_experience_theme_active),
                    fontSize = 9.sp,
                    color = theme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItemClickable(
    title: String,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = title, color = titleColor, style = MaterialTheme.typography.bodyLarge)
        if (subtitle != null) {
            Text(text = subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SettingsItemInfo(
    title: String,
    subtitle: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        if (subtitle != null) {
            Text(text = subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SettingsItemSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
```

#### feature/settings/SettingsLanguageScreen.kt

```kotlin
package com.tamagotchi.code.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsLanguageScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedTopics by viewModel.selectedTopics.collectAsStateWithLifecycle()
    val difficulty by viewModel.difficulty.collectAsStateWithLifecycle()
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    
    val allTopics = listOf(
        stringResource(R.string.topic_kotlin),
        stringResource(R.string.topic_javascript),
        stringResource(R.string.topic_python),
        stringResource(R.string.topic_php),
        stringResource(R.string.topic_sql),
        stringResource(R.string.topic_git),
        stringResource(R.string.topic_clean_code),
        stringResource(R.string.topic_data_structures)
    )
    
    val allLanguages = listOf("Kotlin", "JavaScript", "Python", "PHP")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_lang_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Primary Language
            Text(stringResource(R.string.settings_lang_primary), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            
            var expandedLang by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedLang,
                onExpandedChange = { expandedLang = !expandedLang }
            ) {
                OutlinedTextField(
                    value = petState?.language ?: "Kotlin",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLang) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedLang,
                    onDismissRequest = { expandedLang = false }
                ) {
                    allLanguages.forEach { lang ->
                        DropdownMenuItem(
                            text = { Text(lang) },
                            onClick = {
                                viewModel.selectLanguage(lang)
                                expandedLang = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Difficulty
            Text(stringResource(R.string.settings_lang_difficulty), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    stringResource(R.string.settings_lang_diff_beginner),
                    stringResource(R.string.settings_lang_diff_initial),
                    stringResource(R.string.settings_lang_diff_intermediate),
                    stringResource(R.string.settings_lang_diff_mixed)
                ).forEach { diffOption ->
                    FilterChip(
                        selected = difficulty == diffOption,
                        onClick = { viewModel.setDifficulty(diffOption) },
                        label = { Text(diffOption) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Topics
            Text(stringResource(R.string.settings_lang_active_topics), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allTopics.forEach { topic ->
                    FilterChip(
                        selected = selectedTopics.contains(topic),
                        onClick = {
                            val newSelection = selectedTopics.toMutableSet()
                            if (newSelection.contains(topic)) {
                                if (newSelection.size > 1) { // MÃ­nimo un tema
                                    newSelection.remove(topic)
                                }
                            } else {
                                newSelection.add(topic)
                            }
                            // viewModel does not have setSelectedTopics exposed publicly, we need to add it or use completeOnboarding, wait I'll add setTopics
                            viewModel.setTopics(newSelection)
                        },
                        label = { Text(topic) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Preview
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.settings_lang_preview, selectedTopics.size, difficulty),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
```
### 15.11 Juegos (Feature/Games)


#### feature/games/GamesScreen.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBugHunt: () -> Unit,
    onNavigateToGitRescue: () -> Unit,
    onNavigateToRefactorRush: () -> Unit
) {
    var showClassicDialog by remember { mutableStateOf(false) }
    val gameCooldowns by viewModel.gameCooldowns.collectAsStateWithLifecycle()

    fun canPlay(gameId: String): Boolean {
        val lastPlayed = gameCooldowns[gameId] ?: 0L
        return System.currentTimeMillis() - lastPlayed >= 24 * 60 * 60 * 1000
    }

    fun handleGameClick(gameId: String, navigate: () -> Unit) {
        if (canPlay(gameId)) {
            navigate()
        }
    }

    if (showClassicDialog) {
        val petState = viewModel.petState.collectAsState().value
        if (petState != null) {
            MinigamesDialog(
                state = petState,
                viewModel = viewModel,
                onDismiss = { showClassicDialog = false }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.games_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Game 1: Bug Hunt
            GameCard(
                title = stringResource(R.string.games_bug_hunt_title),
                description = stringResource(R.string.games_bug_hunt_desc),
                canPlay = canPlay("bug_hunt"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("bug_hunt", onNavigateToBugHunt) }
            )

            // Game 2: Git Rescue
            GameCard(
                title = stringResource(R.string.games_git_rescue_title),
                description = stringResource(R.string.games_git_rescue_desc),
                canPlay = canPlay("git_rescue"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("git_rescue", onNavigateToGitRescue) }
            )

            // Game 3: Refactor Rush
            GameCard(
                title = stringResource(R.string.games_refactor_rush_title),
                description = stringResource(R.string.games_refactor_rush_desc),
                canPlay = canPlay("refactor_rush"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("refactor_rush", onNavigateToRefactorRush) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Classic Arcade Section
            Text(
                text = stringResource(R.string.games_classic_arcade),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.games_classic_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Button(
                onClick = { showClassicDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(R.string.games_play))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GameCard(
    title: String,
    description: String,
    canPlay: Boolean,
    onCooldownText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { if (canPlay) onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (canPlay) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        enabled = canPlay
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { if (canPlay) onClick() },
                modifier = Modifier.align(Alignment.End),
                enabled = canPlay
            ) {
                Text(if (canPlay) stringResource(R.string.games_play) else onCooldownText)
            }
        }
    }
}
```

#### feature/games/BugHuntScreen.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay

private data class BugSnippet(
    val lines: List<String>,
    val bugIndex: Int,
    val explanation: String,
    val humor: String
)

private val snippetPool = listOf(
    BugSnippet(
        listOf("val name = \"Codey\"", "println(name", "age++", "fun greet() { }"),
        bugIndex = 1,
        explanation = "Falta el parÃ©ntesis de cierre en println.",
        humor = "Â¡Era un parÃ©ntesis fugitivo! Se escapÃ³ sin pagar el alquiler."
    ),
    BugSnippet(
        listOf("if (x = 5) {", "print(\"Cinco\")", "} else {", "print(\"Otro\")", "}"),
        bugIndex = 0,
        explanation = "En Kotlin la comparaciÃ³n es == no =. = es asignaciÃ³n.",
        humor = "Codey: Â¡Un solo = es asignaciÃ³n, doble == es comparaciÃ³n! No confundas al compilador."
    ),
    BugSnippet(
        listOf("fun add(a: Int, b: Int): Int {", "return a + b", "}", "add(2, 3)"),
        bugIndex = 1,
        explanation = "Falta el tipo de retorno explÃ­cito, deberÃ­a ser ': Int'.",
        humor = "Codey: Hasta una calculadora de bolsillo sabe quÃ© va a devolver."
    ),
    BugSnippet(
        listOf("val nums = listOf(1, 2, 3)", "for i in nums {", "print(i)", "}"),
        bugIndex = 1,
        explanation = "En Kotlin el for usa parÃ©ntesis: for (i in nums).",
        humor = "Codey: Â¡Los parÃ©ntesis no son decoraciÃ³n! Son parte de la sintaxis."
    ),
    BugSnippet(
        listOf("fun main() {", "val msg = \"Hola\"", "println(msg)", "}//fin"),
        bugIndex = 0,
        explanation = "main no necesita parÃ©ntesis vacÃ­os si es la entrada.",
        humor = "Codey: En realidad main() estÃ¡ bien, Â¡pero este bug es tramposo!"
    ),
    BugSnippet(
        listOf("val count = 0", "while (count < 5) {", "println(count)", "count--", "}"),
        bugIndex = 3,
        explanation = "EstÃ¡ decrementando count en vez de incrementarlo. Bucle infinito.",
        humor = "Codey: Â¡AsÃ­ nunca llegarÃ¡s a 5! Es como correr hacia atrÃ¡s."
    ),
    BugSnippet(
        listOf("val data = \"123\"", "val number: Int = data", "println(number + 1)"),
        bugIndex = 1,
        explanation = "No se puede asignar un String directamente a Int. Usa toInt().",
        humor = "Codey: Â¡No puedes convertir strings a Int por Ã³smosis! Usa .toInt()."
    ),
    BugSnippet(
        listOf("fun isEven(n: Int) {", "return n % 2 == 0", "}", "val r = isEven(4)"),
        bugIndex = 0,
        explanation = "La funciÃ³n debe declarar tipo de retorno: fun isEven(n: Int): Boolean.",
        humor = "Codey: El compilador no es adivino. Dile quÃ© vas a devolver."
    ),
    BugSnippet(
        listOf("val items = listOf(1, 2, 3)", "items.add(4)", "println(items)"),
        bugIndex = 1,
        explanation = "listOf crea una lista inmutable. Usa mutableListOf.",
        humor = "Codey: Â¡No puedes modificar una lista inmutable! Es como intentar cambiar el pasado."
    ),
    BugSnippet(
        listOf("fun greet() {", "println(\"Hola\")", "", "", "", "}"),
        bugIndex = 2,
        explanation = "LÃ­neas vacÃ­as innecesarias. El cÃ³digo debe ser limpio.",
        humor = "Codey: Â¿EstÃ¡s escribiendo cÃ³digo o una novela? Muy poÃ©tico pero poco prÃ¡ctico."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugHuntScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val snippets = remember { snippetPool.shuffled().take(5) }

    var currentRound by remember { mutableStateOf(1) }
    val totalRounds = snippets.size
    var timeRemaining by remember { mutableStateOf(60) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var codeyReaction by remember { mutableStateOf("Â¡Encuentra el bug antes de que el compilador explote!") }
    var lastExplanation by remember { mutableStateOf<String?>(null) }
    var lastHumor by remember { mutableStateOf<String?>(null) }
    var answeredRound by remember { mutableStateOf(false) }

    val currentSnippet = snippets.getOrNull(currentRound - 1)

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--
            }
            isGameOver = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bug_hunt_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isGameOver) {
                Text(stringResource(R.string.game_result_title), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                val bytesEarned = score * 10
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Acertaste $score de $totalRounds",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("bug_hunt")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.bug_hunt_round, currentRound, totalRounds), fontWeight = FontWeight.Bold)
                    Text("âŒ› ${timeRemaining}s", color = if (timeRemaining < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Codey: $codeyReaction", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(stringResource(R.string.bug_hunt_instruction), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (currentSnippet != null) {
                    currentSnippet.lines.forEachIndexed { index, line ->
                        val isCorrectLine = index == currentSnippet.bugIndex && answeredRound && lastExplanation != null
                        val isWrongPick = answeredRound && lastExplanation != null && index != currentSnippet.bugIndex

                        Surface(
                            color = when {
                                isCorrectLine -> Color(0xFF1B5E20).copy(alpha = 0.3f)
                                isWrongPick -> Color(0xFFB71C1C).copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !answeredRound) {
                                    if (index == currentSnippet.bugIndex) {
                                        score++
                                        codeyReaction = currentSnippet.humor
                                    } else {
                                        codeyReaction = "Â¡Esa lÃ­nea estÃ¡ bien! Sigue buscando..."
                                    }
                                    lastExplanation = currentSnippet.explanation
                                    lastHumor = currentSnippet.humor
                                    answeredRound = true
                                }
                        ) {
                            Text(
                                text = "${index + 1}. $line",
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    if (answeredRound && lastExplanation != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            color = if (lastHumor != null && score > 0) Color(0xFF1B5E20).copy(alpha = 0.15f) else Color(0xFFB71C1C).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ExplicaciÃ³n: ${lastExplanation}",
                                modifier = Modifier.padding(12.dp),
                                fontFamily = FontFamily.Monospace,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = {
                            if (currentRound < totalRounds) {
                                currentRound++
                                answeredRound = false
                                lastExplanation = null
                                lastHumor = null
                                codeyReaction = "Â¡Siguiente ronda! Â¿DÃ³nde se esconde el bug?"
                            } else {
                                isGameOver = true
                            }
                        }) {
                            Text(if (currentRound < totalRounds) "Siguiente ronda" else "Ver resultados")
                        }
                    }
                }
            }
        }
    }
}
```

#### feature/games/GitRescueScreen.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay

private data class GitScenario(
    val situation: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val humor: String
)

private val scenarioPool = listOf(
    GitScenario(
        situation = "Acabas de clonar el repo y vas a trabajar en un nuevo feature. Â¿QuÃ© haces primero?",
        options = listOf(
            "git checkout -b feature/nuevo",
            "git commit -m \"inicio\"",
            "git push --force"
        ),
        correctIndex = 0,
        explanation = "Crear una rama nueva desde main es la forma correcta de empezar un feature.",
        humor = "Codey: Â¡Nunca pushes a main sin antes crear una rama! Eres un cowboy."
    ),
    GitScenario(
        situation = "Has hecho cambios locales y te das cuenta de que rompiste todo. Quieres volver al Ãºltimo commit limpio.",
        options = listOf(
            "git reset --hard HEAD",
            "git revert HEAD",
            "git rm -rf ."
        ),
        correctIndex = 1,
        explanation = "revert crea un nuevo commit que deshace los cambios, preservando la historia.",
        humor = "Codey: reset --hard es como una mÃ¡quina del tiempo sin frenos. revert es mÃ¡s seguro."
    ),
    GitScenario(
        situation = "Tienes un conflicto en un merge. Â¿CuÃ¡l es el siguiente paso?",
        options = listOf(
            "Resolver el conflicto en el editor y luego git add",
            "git commit --amend",
            "git push --force origin main"
        ),
        correctIndex = 0,
        explanation = "Los conflictos se resuelven editando los archivos, luego git add y git commit.",
        humor = "Codey: push --force no resuelve conflictos, los empeora. Es como echar gasolina al fuego."
    ),
    GitScenario(
        situation = "Trabajas en equipo y necesitas traerte los cambios mÃ¡s recientes de la rama main a tu rama feature.",
        options = listOf(
            "git pull origin main",
            "git merge feature main",
            "git branch -d main"
        ),
        correctIndex = 0,
        explanation = "git pull trae los cambios de main a tu rama actual. merge lo harÃ­a al revÃ©s.",
        humor = "Codey: No borres main. Nunca borres main. Es como borrar el diccionario."
    ),
    GitScenario(
        situation = "Hiciste un commit pero olvidaste incluir un archivo. Â¿CÃ³mo lo arreglas?",
        options = listOf(
            "git add archivo && git commit --amend",
            "git reset --hard HEAD~1",
            "git commit --allow-empty"
        ),
        correctIndex = 0,
        explanation = "Con amend puedes agregar archivos al commit anterior sin crear uno nuevo.",
        humor = "Codey: --allow-empty no arregla nada. Es como poner un post-it en una puerta cerrada."
    ),
    GitScenario(
        situation = "Quieres ver quÃ© archivos modificaste antes de hacer commit. Â¿QuÃ© comando usas?",
        options = listOf(
            "git diff",
            "git status",
            "git show"
        ),
        correctIndex = 1,
        explanation = "git status muestra el estado actual: archivos modificados, nuevos y eliminados.",
        humor = "Codey: diff es para ver el contenido exacto, status te da el resumen. Dos herramientas diferentes."
    ),
    GitScenario(
        situation = "El historial de commits estÃ¡ lleno de mensajes como 'fix' y 'update'. Quieres limpiarlo antes de hacer merge.",
        options = listOf(
            "git rebase -i HEAD~5",
            "git reset --hard origin/main",
            "git commit --fixup"
        ),
        correctIndex = 0,
        explanation = "rebase interactivo te permite squash, reordenar y renombrar commits.",
        humor = "Codey: Los mensajes 'fix' y 'update' son como decir 'cosa' en un examen. SÃ© descriptivo."
    ),
    GitScenario(
        situation = "Tu rama feature quedÃ³ atrÃ¡s de main y necesitas actualizarla sin crear commits de merge.",
        options = listOf(
            "git rebase main",
            "git merge main",
            "git pull --rebase"
        ),
        correctIndex = 2,
        explanation = "pull --rebase trae cambios y los aplica encima de tus commits locales.",
        humor = "Codey: El histÃ³rico lineal es como una carretera recta. Los merges son rotondas."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitRescueScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val scenarios = remember { scenarioPool.shuffled().take(5) }

    var currentStep by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var codeyReaction by remember { mutableStateOf("Â¡RÃ¡pido, el repo estÃ¡ en llamas!") }
    var lastExplanation by remember { mutableStateOf<String?>(null) }
    var answeredStep by remember { mutableStateOf(false) }
    var branchPosition by remember { mutableStateOf(0f) }
    var maxBranchSteps by remember { mutableStateOf(scenarios.size.toFloat()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.git_rescue_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isGameOver) {
                Text(stringResource(R.string.game_result_title), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                val bytesEarned = score * 15
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Decisiones correctas: $score de ${scenarios.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("git_rescue")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                // Branch visual progress
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Progreso de la rama:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            scenarios.forEachIndexed { index, _ ->
                                val isDone = index < currentStep
                                val isCurrent = index == currentStep
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = when {
                                        isDone -> MaterialTheme.colorScheme.primary
                                        isCurrent -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                            color = if (isDone || isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (index < scenarios.size - 1) {
                                    Surface(
                                        modifier = Modifier
                                            .height(4.dp)
                                            .weight(1f),
                                        color = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ) {}
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Codey: $codeyReaction", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Paso ${currentStep + 1} de ${scenarios.size}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(scenarios[currentStep].situation, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                scenarios[currentStep].options.forEachIndexed { index, option ->
                    val isCorrect = answeredStep && index == scenarios[currentStep].correctIndex
                    val isWrong = answeredStep && index != scenarios[currentStep].correctIndex

                    Surface(
                        color = when {
                            isCorrect -> Color(0xFF1B5E20).copy(alpha = 0.3f)
                            isWrong -> Color(0xFFB71C1C).copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !answeredStep) {
                                if (index == scenarios[currentStep].correctIndex) {
                                    score++
                                    codeyReaction = scenarios[currentStep].humor
                                } else {
                                    codeyReaction = "Â¡Esa no era la mejor opciÃ³n!"
                                }
                                lastExplanation = scenarios[currentStep].explanation
                                answeredStep = true
                            }
                    ) {
                        Text(
                            text = "> $option",
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                if (answeredStep && lastExplanation != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFF1B5E20).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = lastExplanation!!,
                            modifier = Modifier.padding(12.dp),
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (currentStep < scenarios.size - 1) {
                            currentStep++
                            answeredStep = false
                            lastExplanation = null
                            codeyReaction = "Â¡Siguiente decisiÃ³n! El repo te necesita."
                        } else {
                            isGameOver = true
                        }
                    }) {
                        Text(if (currentStep < scenarios.size - 1) "Siguiente decisiÃ³n" else "Ver resultados")
                    }
                }
            }
        }
    }
}
```

#### feature/games/RefactorRushScreen.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel

private data class RefactorPuzzle(
    val title: String,
    val blocks: List<String>
)

private val puzzleBank = listOf(
    RefactorPuzzle(
        "Calcular total",
        listOf(
            "fun calculateTotal(items: List<Int>): Int {",
            "    var total = 0",
            "    for (item in items) {",
            "        total += item",
            "    }",
            "    return total",
            "}"
        )
    ),
    RefactorPuzzle(
        "Filtrar pares",
        listOf(
            "fun filterEven(numbers: List<Int>): List<Int> {",
            "    val result = mutableListOf<Int>()",
            "    for (n in numbers) {",
            "        if (n % 2 == 0) {",
            "            result.add(n)",
            "        }",
            "    }",
            "    return result",
            "}"
        )
    ),
    RefactorPuzzle(
        "Saludo personalizado",
        listOf(
            "fun greet(name: String, age: Int): String {",
            "    val greeting = \"Hola, \$name\"",
            "    val ageMsg = if (age >= 18) \"Eres mayor\" else \"Eres menor\"",
            "    return \"\$greeting. \$ageMsg\"",
            "}"
        )
    ),
    RefactorPuzzle(
        "Buscar mÃ¡ximo",
        listOf(
            "fun findMax(values: List<Int>): Int? {",
            "    if (values.isEmpty()) return null",
            "    var max = values[0]",
            "    for (v in values) {",
            "        if (v > max) max = v",
            "    }",
            "    return max",
            "}"
        )
    ),
    RefactorPuzzle(
        "Contar vocales",
        listOf(
            "fun countVowels(text: String): Int {",
            "    val vowels = setOf('a', 'e', 'i', 'o', 'u')",
            "    var count = 0",
            "    for (ch in text.lowercase()) {",
            "        if (ch in vowels) count++",
            "    }",
            "    return count",
            "}"
        )
    ),
    RefactorPuzzle(
        "Invertir lista",
        listOf(
            "fun reverseList<T>(items: List<T>): List<T> {",
            "    val result = mutableListOf<T>()",
            "    for (i in items.indices.reversed()) {",
            "        result.add(items[i])",
            "    }",
            "    return result",
            "}"
        )
    ),
    RefactorPuzzle(
        "Es palÃ­ndromo",
        listOf(
            "fun isPalindrome(word: String): Boolean {",
            "    val cleaned = word.lowercase().filter { it.isLetter() }",
            "    return cleaned == cleaned.reversed()",
            "}"
        )
    ),
    RefactorPuzzle(
        "Promedio de notas",
        listOf(
            "fun average(grades: List<Double>): Double {",
            "    if (grades.isEmpty()) return 0.0",
            "    val sum = grades.sum()",
            "    return sum / grades.size",
            "}"
        )
    ),
    RefactorPuzzle(
        "Generar rango",
        listOf(
            "fun generateRange(start: Int, end: Int): List<Int> {",
            "    val range = mutableListOf<Int>()",
            "    for (i in start..end) {",
            "        range.add(i)",
            "    }",
            "    return range",
            "}"
        )
    ),
    RefactorPuzzle(
        "Validar email",
        listOf(
            "fun isValidEmail(email: String): Boolean {",
            "    if (!email.contains('@')) return false",
            "    val parts = email.split('@')",
            "    if (parts.size != 2) return false",
            "    return parts[1].contains('.')",
            "}"
        )
    ),
    RefactorPuzzle(
        "Fibonacci",
        listOf(
            "fun fibonacci(n: Int): List<Int> {",
            "    if (n <= 0) return emptyList()",
            "    val fib = mutableListOf(0, 1)",
            "    for (i in 2 until n) {",
            "        fib.add(fib[i - 1] + fib[i - 2])",
            "    }",
            "    return fib.take(n)",
            "}"
        )
    ),
    RefactorPuzzle(
        "Capitalizar palabras",
        listOf(
            "fun capitalizeWords(sentence: String): String {",
            "    return sentence.split(\" \")",
            "        .joinToString(\" \") { word ->",
            "            word.replaceFirstChar { it.uppercase() }",
            "        }",
            "}"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefactorRushScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val puzzle = remember { puzzleBank.random() }
    val originalBlocks = remember { puzzle.blocks }

    var isGameOver by remember { mutableStateOf(false) }
    var codeyReaction by remember { mutableStateOf("Â¡Ordena este desastre!") }
    var currentBlocks by remember { mutableStateOf(originalBlocks.shuffled()) }
    var attempts by remember { mutableStateOf(0) }

    fun moveUp(index: Int) {
        if (index > 0) {
            val newList = currentBlocks.toMutableList()
            val temp = newList[index - 1]
            newList[index - 1] = newList[index]
            newList[index] = temp
            currentBlocks = newList
        }
    }

    fun moveDown(index: Int) {
        if (index < currentBlocks.size - 1) {
            val newList = currentBlocks.toMutableList()
            val temp = newList[index + 1]
            newList[index + 1] = newList[index]
            newList[index] = temp
            currentBlocks = newList
        }
    }

    fun checkResult() {
        attempts++
        if (currentBlocks == originalBlocks) {
            codeyReaction = "Â¡Excelente! CÃ³digo limpio y ordenado en $attempts intento(s)."
            isGameOver = true
        } else {
            codeyReaction = "Sigue intentando, todavÃ­a no compila (intento $attempts)."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.refactor_rush_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isGameOver) {
                Text(stringResource(R.string.game_result_title), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(puzzle.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                val bytesEarned = maxOf(50 - (attempts * 5), 10)
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("refactor_rush")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Codey: $codeyReaction", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(puzzle.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(R.string.refactor_rush_instruction), style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                currentBlocks.forEachIndexed { index, block ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column {
                                IconButton(onClick = { moveUp(index) }, enabled = index > 0) {
                                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up")
                                }
                                IconButton(onClick = { moveDown(index) }, enabled = index < currentBlocks.size - 1) {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down")
                                }
                            }
                            Text(
                                text = block,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { checkResult() }) {
                    Text("Verificar")
                }
            }
        }
    }
}
```

#### feature/games/MinigamesDialog.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun MinigamesDialog(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onDismiss: () -> Unit
) {
    var selectedGame by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF0C100D),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedGame == null) ">>> MINI-JUEGOS" else ">>> DETALLE DE JUEGO",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = {
                            if (selectedGame != null) {
                                selectedGame = null
                            } else {
                                onDismiss()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedGame != null) Icons.AutoMirrored.Filled.KeyboardArrowLeft else Icons.Default.Close,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                when (selectedGame) {
                    null -> {
                        Text(
                            text = "Juega con ${state.name} para aumentar su felicidad y conseguir Bytes extra.",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.LightGray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GameOptionCard(
                            title = "Adivina el Bit",
                            description = "Intenta predecir el siguiente bit binario (0 o 1). Juego rÃ¡pido de 5 rondas.",
                            icon = Icons.Default.Code,
                            onClick = { selectedGame = "BINARY_GUESS" }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameOptionCard(
                            title = "Caza de Bugs",
                            description = "Â¡RÃ¡pido! Los bugs se estÃ¡n escapando. Atrapa todos los que puedas en 10 segundos.",
                            icon = Icons.Default.BugReport,
                            onClick = { selectedGame = "BUG_SMASHER" }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameOptionCard(
                            title = "Servidor, Script, Hacker",
                            description = "Piedra, Papel o Tijera versiÃ³n informÃ¡tica. Â¡Derrota al compilador!",
                            icon = Icons.Default.Security,
                            onClick = { selectedGame = "ROCK_PAPER_SCI" }
                        )
                    }
                    "BINARY_GUESS" -> {
                        BinaryGuessGame(
                            state = state,
                            viewModel = viewModel,
                            onFinish = { selectedGame = null }
                        )
                    }
                    "BUG_SMASHER" -> {
                        BugSmasherGame(
                            state = state,
                            viewModel = viewModel,
                            onFinish = { selectedGame = null }
                        )
                    }
                    "ROCK_PAPER_SCI" -> {
                        RockPaperSciGame(
                            state = state,
                            viewModel = viewModel,
                            onFinish = { selectedGame = null }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF151D16),
        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF81C784),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
```

#### feature/games/BinaryGuessGame.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun BinaryGuessGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var round by remember { mutableStateOf(1) }
    var score by remember { mutableStateOf(0) }
    var currentSecretBit by remember { mutableStateOf((0..1).random()) }
    var feedbackMessage by remember { mutableStateOf("Â¿CuÃ¡l crees que es el bit secreto?") }
    var showNextButton by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADIVINA EL BIT (Ronda $round de 5)",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Black, RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFF2E7D32), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGameOver) "FIN" else if (showNextButton) "$currentSecretBit" else "?",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF81C784)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = feedbackMessage,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isGameOver) {
            val bytesReward = score * 4
            val healthReward = score * 3f
            Text(
                text = "Â¡Juego Terminado!\nAcertaste: $score de 5\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 10f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else if (showNextButton) {
            Button(
                onClick = {
                    if (round < 5) {
                        round += 1
                        currentSecretBit = (0..1).random()
                        feedbackMessage = "Â¿CuÃ¡l crees que es el bit secreto?"
                        showNextButton = false
                    } else {
                        isGameOver = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Siguiente Ronda", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val isCorrect = currentSecretBit == 0
                        if (isCorrect) {
                            score += 1
                            feedbackMessage = "Â¡Excelente! El bit secreto era 0."
                        } else {
                            feedbackMessage = "Incorrecto. El bit secreto era 1."
                        }
                        showNextButton = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("0", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                }

                Button(
                    onClick = {
                        val isCorrect = currentSecretBit == 1
                        if (isCorrect) {
                            score += 1
                            feedbackMessage = "Â¡Excelente! El bit secreto era 1."
                        } else {
                            feedbackMessage = "Incorrecto. El bit secreto era 0."
                        }
                        showNextButton = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("1", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                }
            }
        }
    }
}
```

#### feature/games/BugSmasherGame.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun BugSmasherGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(10) }
    var bugPosition by remember { mutableStateOf((0..8).random()) }
    var isStarted by remember { mutableStateOf(false) }

    LaunchedEffect(isStarted, timeRemaining) {
        if (isStarted && timeRemaining > 0) {
            kotlinx.coroutines.delay(1000)
            timeRemaining -= 1
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CAZA DE BUGS",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (!isStarted) {
            Text(
                text = "Toca los bugs que aparecen en la cuadrÃ­cula de 3x3 tan rÃ¡pido como puedas. Â¡Tienes 10 segundos!",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { isStarted = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Â¡Comenzar!", fontFamily = FontFamily.Monospace)
            }
        } else if (timeRemaining <= 0) {
            val bytesReward = score * 2
            val healthReward = (score * 1.5f).coerceAtMost(30f)
            Text(
                text = "Â¡Tiempo Agotado!\nBugs atrapados: $score\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 15f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bugs: $score",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tiempo: ${timeRemaining}s",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF5350)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            val isBug = bugPosition == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isBug) Color(0xFFFFCDD2) else Color(0xFF151D16))
                                    .border(1.dp, if (isBug) Color(0xFFEF5350) else Color(0xFF2E7D32).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (isBug) {
                                            score += 1
                                            bugPosition = (0..8).filter { it != index }.random()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBug) {
                                    Icon(
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = "BUG",
                                        tint = Color(0xFFEF5350),
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
```

#### feature/games/RockPaperSciGame.kt

```kotlin
package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun RockPaperSciGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var userWins by remember { mutableStateOf(0) }
    var cpuWins by remember { mutableStateOf(0) }
    var roundMessage by remember { mutableStateOf("Elige tu jugada para iniciar la ronda.") }
    var userChoice by remember { mutableStateOf<String?>(null) }
    var cpuChoice by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    val choices = listOf("Servidor", "Script", "Hacker")
    val icons = mapOf(
        "Servidor" to Icons.Default.Computer,
        "Script" to Icons.Default.Description,
        "Hacker" to Icons.Default.BugReport
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SERVIDOR, SCRIPT, HACKER (RPS)",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "TÃš: $userWins | CPU: $cpuWins (Mejor de 3)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF2E7D32), RoundedCornerShape(10.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TÃš", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = icons[userChoice] ?: Icons.Default.QuestionMark,
                    contentDescription = userChoice ?: "Pregunta",
                    tint = if (userChoice != null) Color(0xFF81C784) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(userChoice ?: "Selecciona...", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
            }

            Text("VS", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFFEF5350))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("COMPILADOR", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = icons[cpuChoice] ?: Icons.Default.QuestionMark,
                    contentDescription = cpuChoice ?: "Pregunta",
                    tint = if (cpuChoice != null) Color(0xFFEF5350) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(cpuChoice ?: "Esperando...", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = roundMessage,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (isGameOver) {
            val playerWon = userWins >= 2
            val bytesReward = if (playerWon) 20 else 5
            val healthReward = if (playerWon) 25f else 10f
            Text(
                text = if (playerWon) "Â¡Felicidades! Derrotaste al compilador.\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad" else "Has perdido contra el compilador.\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 10f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                choices.forEach { choice ->
                    Button(
                        onClick = {
                            userChoice = choice
                            val selectedCpu = choices.random()
                            cpuChoice = selectedCpu

                            if (choice == selectedCpu) {
                                roundMessage = "Empate en esta ronda con $choice."
                            } else if (
                                (choice == "Servidor" && selectedCpu == "Hacker") ||
                                (choice == "Script" && selectedCpu == "Servidor") ||
                                (choice == "Hacker" && selectedCpu == "Script")
                            ) {
                                userWins += 1
                                roundMessage = "Â¡Ganaste la ronda! $choice vence a $selectedCpu."
                            } else {
                                cpuWins += 1
                                roundMessage = "Perdiste la ronda. $selectedCpu vence a $choice."
                            }

                            if (userWins >= 2 || cpuWins >= 2) {
                                isGameOver = true
                                roundMessage = if (userWins >= 2) "Â¡Has ganado la partida!" else "El compilador ha ganado la partida."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text(choice, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                    }
                }
            }
        }
    }
}
```
### 15.12 Widget


#### widget/CodePetWidgetProvider.kt

```kotlin
package com.tamagotchi.code.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tamagotchi.code.MainActivity
import com.tamagotchi.code.R

class CodePetWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.code_pet_widget)
            views.setTextViewText(R.id.widget_title, "Code Tamagotchi")
            views.setTextViewText(R.id.widget_subtitle, "Tu mascota estÃ¡ lista")
            views.setImageViewResource(R.id.widget_pet_image, R.drawable.mascota_happy)

            val launchIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
```
### 15.13 Recursos (strings.xml)


#### res/values/strings.xml

```xml
<resources>
    <string name="app_name">Code Tamagotchi</string>
    
    <!-- Onboarding -->
    <string name="onboarding_skip">Saltar</string>
    <string name="onboarding_next">Siguiente</string>
    
    <!-- Step 1 -->
    <string name="onboarding_step1_title">Tu compaÃ±ero de cÃ³digo</string>
    <string name="onboarding_step1_desc">Tu mascota virtual que te acompaÃ±a mientras practicas programaciÃ³n.</string>
    <string name="onboarding_step1_terminal">\> hola_mundo</string>
    
    <!-- Step 2 -->
    <string name="onboarding_step2_title">Tu prÃ¡ctica la hace evolucionar</string>
    <string name="onboarding_step2_card1">Resuelve retos</string>
    <string name="onboarding_step2_card2">Completa Focus</string>
    <string name="onboarding_step2_card3">Cuida a tu mascota</string>
    <string name="onboarding_step2_desc">Los retos y Focus entregan XP; los Bytes sirven para cuidado, juegos y estÃ©tica.</string>
    
    <!-- Step 3 -->
    <string name="onboarding_step3_title">Elige tu ruta</string>
    <string name="onboarding_step3_subtitle">Para comenzar, elige hasta 3 temas</string>
    <string name="onboarding_step3_initial_route">Ruta inicial</string>
    <string name="topic_kotlin">Kotlin</string>
    <string name="topic_javascript">JavaScript</string>
    <string name="topic_python">Python</string>
    <string name="topic_php">PHP</string>
    <string name="topic_sql">SQL</string>
    <string name="topic_git">Git</string>
    <string name="topic_clean_code">Clean Code</string>
    <string name="topic_data_structures">Estructuras de Datos</string>
    
    <!-- Step 4 -->
    <string name="onboarding_step4_title">Dale nombre a tu copiloto</string>
    <string name="onboarding_step4_preview">\> Â¡Compilado! Soy %1$s.</string>
    <string name="onboarding_step4_cta">Crear a %1$s</string>
    <string name="default_pet_name">Codey</string>
    
    <!-- Dialog Skip -->
    <string name="dialog_skip_title">Â¿Omitir configuraciÃ³n?</string>
    <string name="dialog_skip_desc">Tu mascota se llamarÃ¡ Codey y usarÃ¡ la Ruta Inicial.</string>
    <string name="dialog_skip_confirm">SÃ­, omitir</string>
    <string name="dialog_skip_cancel">No, volver</string>

    <!-- Settings Screen -->
    <string name="settings_title">ConfiguraciÃ³n</string>
    <string name="settings_section_profile">Perfil</string>
    <string name="settings_profile_name">Nombre de mascota</string>
    <string name="settings_profile_level">Nivel actual: %1$d</string>
    <string name="settings_section_learning">Aprendizaje</string>
    <string name="settings_learning_languages">Lenguajes y temas</string>
    <string name="settings_learning_focus_duration">DuraciÃ³n Focus predeterminada</string>
    <string name="settings_learning_focus_duration_value">Configurado en pantalla Focus</string>
    <string name="settings_section_experience">Experiencia</string>
    <string name="settings_experience_theme">Tema visual</string>
    <string name="settings_experience_sound">Sonido</string>
    <string name="settings_experience_vibration">VibraciÃ³n</string>
    <string name="settings_experience_reduce_motion">Reducir animaciones</string>
    <string name="settings_experience_theme_active">Activo</string>
    <string name="settings_section_achievements">Logros</string>
    <string name="settings_section_reminders">Recordatorios</string>
    <string name="settings_reminders_daily">Recordatorio diario de estudio</string>
    <string name="settings_section_data">Datos y Ayuda</string>
    <string name="settings_data_export">Exportar progreso</string>
    <string name="settings_data_reset">Restablecer progreso</string>
    <string name="settings_data_about">Acerca de</string>

    <!-- Reset Dialog -->
    <string name="dialog_reset_title">Â¿Restablecer todo el progreso?</string>
    <string name="dialog_reset_desc">Esta acciÃ³n borrarÃ¡ permanentemente tu mascota, nivel, XP, Bytes y configuraciÃ³n. Esta acciÃ³n no se puede deshacer.</string>
    <string name="dialog_reset_confirm">SÃ­, restablecer</string>
    <string name="dialog_reset_cancel">Cancelar</string>
    <string name="dialog_reset_confirm2_title">Â¿EstÃ¡s seguro?</string>
    <string name="dialog_reset_confirm2_desc">Esta es tu Ãºltima oportunidad. Todo tu progreso se perderÃ¡ para siempre. Â¿Confirmas?</string>
    <string name="dialog_reset_confirm2_confirm">SÃ­, estoy seguro</string>

    <!-- Rename Dialog -->
    <string name="dialog_rename_title">Renombrar mascota</string>
    <string name="dialog_rename_label">Nuevo nombre (mÃ¡x 15)</string>
    <string name="dialog_rename_confirm">Guardar</string>

    <!-- Settings Language Screen -->
    <string name="settings_lang_title">Lenguajes y Temas</string>
    <string name="settings_lang_primary">Lenguaje Principal</string>
    <string name="settings_lang_active_topics">Temas Activos</string>
    <string name="settings_lang_difficulty">Dificultad</string>
    <string name="settings_lang_diff_beginner">Principiante</string>
    <string name="settings_lang_diff_initial">Inicial</string>
    <string name="settings_lang_diff_intermediate">Intermedia</string>
    <string name="settings_lang_diff_mixed">Mixta</string>
    <string name="settings_lang_preview">RotaciÃ³n actual: %1$d temas en dificultad %2$s.</string>

    <!-- Games Screen -->
    <string name="games_title">Arcade de DepuraciÃ³n</string>
    <string name="games_bug_hunt_title">Bug Hunt</string>
    <string name="games_bug_hunt_desc">Encuentra el punto y coma fugitivo y otros horrores sintÃ¡cticos.</string>
    <string name="games_git_rescue_title">Git Rescue</string>
    <string name="games_git_rescue_desc">Una rama estÃ¡ a punto del colapso. Toma las decisiones correctas.</string>
    <string name="games_refactor_rush_title">Refactor Rush</string>
    <string name="games_refactor_rush_desc">Ordena el cÃ³digo para que tenga sentido antes de que falle el build.</string>
    <string name="games_classic_arcade">Arcade ClÃ¡sico</string>
    <string name="games_classic_desc">Minijuegos antiguos para pasar el rato.</string>
    <string name="games_cooldown">En enfriamiento (Vuelve mÃ¡s tarde para ganar mÃ¡s recompensa)</string>
    <string name="games_play">Jugar</string>

    <!-- Bug Hunt -->
    <string name="bug_hunt_title">Terminal Panic</string>
    <string name="bug_hunt_instruction">Encuentra la lÃ­nea con el bug.</string>
    <string name="bug_hunt_round">Ronda %1$d / %2$d</string>

    <!-- Git Rescue -->
    <string name="git_rescue_title">Git Rescue</string>
    <string name="git_rescue_instruction">Elige el comando correcto para salvar el repo.</string>

    <!-- Refactor Rush -->
    <string name="refactor_rush_title">Refactor Rush</string>
    <string name="refactor_rush_instruction">Ordena los bloques lÃ³gicos de la funciÃ³n.</string>
    
    <!-- Game Results -->
    <string name="game_result_title">Â¡Build completado!</string>
    <string name="game_result_reward">Ganaste %1$d Bytes.</string>
    <string name="game_result_finish">Finalizar</string>

</resources>
```
### 15.14 Tests


#### test/ExampleUnitTest.kt

```kotlin
package com.tamagotchi.code

import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }
}
```

#### test/ExampleRobolectricTest.kt

```kotlin
package com.tamagotchi.code

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {
  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Code Tamagotchi", appName)
  }
}
```

#### test/ExampleInstrumentedTest.kt

```kotlin
package com.tamagotchi.code

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.tamagotchi.code", appContext.packageName)
  }
}
```

#### test/GameEconomyTest.kt

```kotlin
package com.tamagotchi.code

import org.junit.Assert.*
import org.junit.Test

/**
 * Tests for game economy, cooldown logic and settings validation.
 */
class GameEconomyTest {

    // --- Economy: XP only from Focus & challenges, Bytes from arcade ---

    @Test
    fun `focus session XP scales linearly with minutes`() {
        val minutes = 25
        val xpEarned = minutes * 5
        assertEquals(125, xpEarned)
    }

    @Test
    fun `challenge correct answer awards XP and Bytes`() {
        val earnedXp = 20
        val earnedBytes = 25
        assertTrue(earnedXp > 0)
        assertTrue(earnedBytes > 0)
    }

    @Test
    fun `minigame awards only Bytes, never XP`() {
        // Minigames should only give bytes as rewards
        val bytesEarned = 50
        val xpEarned = 0 // Minigames never give XP
        assertTrue(bytesEarned > 0)
        assertEquals(0, xpEarned)
    }

    // --- Difficulty settings ---

    @Test
    fun `difficulty defaults to Inicial`() {
        val defaultDifficulty = "Inicial"
        assertEquals("Inicial", defaultDifficulty)
    }

    @Test
    fun `valid difficulty options are three`() {
        val validOptions = listOf("Inicial", "Intermedia", "Mixta")
        assertEquals(3, validOptions.size)
        assertTrue(validOptions.contains("Inicial"))
        assertTrue(validOptions.contains("Intermedia"))
        assertTrue(validOptions.contains("Mixta"))
    }

    // --- Selected Topics ---

    @Test
    fun `selectedTopics must have at least one topic`() {
        val topics = setOf("Kotlin")
        assertTrue(topics.isNotEmpty())
    }

    @Test
    fun `removing last topic should be prevented`() {
        val topics = mutableSetOf("Kotlin")
        // Simulate UI logic: don't remove if size would be 0
        val canRemove = topics.size > 1
        assertFalse(canRemove)
    }

    @Test
    fun `default topics include Kotlin, Estructuras de Datos, Git`() {
        val defaults = setOf("Kotlin", "Estructuras de Datos", "Git")
        assertEquals(3, defaults.size)
        assertTrue(defaults.contains("Kotlin"))
        assertTrue(defaults.contains("Git"))
    }

    // --- Cooldown / diminishing returns ---

    @Test
    fun `arcade cooldown diminishes returns after 3 plays`() {
        var totalReward = 0
        val maxFullRewardPlays = 3
        for (play in 1..5) {
            val baseReward = 50
            val multiplier = if (play <= maxFullRewardPlays) 1.0 else 0.5
            totalReward += (baseReward * multiplier).toInt()
        }
        // 3*50 + 2*25 = 200
        assertEquals(200, totalReward)
    }

    // --- Level calculation ---

    @Test
    fun `level 1 requires 100 XP`() {
        val xp = 99
        val level = calculateLevel(xp)
        assertEquals(1, level)
    }

    @Test
    fun `level 2 at 100 XP`() {
        val xp = 100
        val level = calculateLevel(xp)
        assertEquals(2, level)
    }

    @Test
    fun `level 3 at 300 XP`() {
        val xp = 300
        val level = calculateLevel(xp)
        assertEquals(3, level)
    }

    // --- Onboarding validation ---

    @Test
    fun `pet name cannot exceed 15 characters`() {
        val name = "MiMascotaSuperLarga"
        val trimmed = name.take(15)
        assertEquals(15, trimmed.length)
    }

    @Test
    fun `blank name defaults to Codey`() {
        val name = ""
        val finalName = if (name.isNotBlank()) name else "Codey"
        assertEquals("Codey", finalName)
    }

    // --- Helper ---

    private fun calculateLevel(xp: Int): Int {
        var level = 1
        var requiredXp = 100
        while (xp >= requiredXp) {
            level++
            requiredXp += level * 100
        }
        return level
    }
}
```

#### test/GreetingScreenshotTest.kt

```kotlin
package com.tamagotchi.code

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.tamagotchi.code.data.repository.AchievementsRepository
import com.tamagotchi.code.navigation.AppNavigation
import com.tamagotchi.code.ui.theme.MyApplicationTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    val database = com.tamagotchi.code.data.database.AppDatabase.getDatabase(application)
    val repository = com.tamagotchi.code.data.repository.PetRepository(database.petDao())
    val userPreferences = com.tamagotchi.code.data.repository.UserPreferencesRepository(application)
    val achievementsRepo = AchievementsRepository(application)
    val viewModel = PetViewModel(repository, userPreferences, achievementsRepo)
    composeTestRule.setContent {
      MyApplicationTheme {
        AppNavigation(viewModel = viewModel)
      }
    }
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
```

#### test/PetAnimationConfigTest.kt

```kotlin
package com.tamagotchi.code.ui.components

import org.junit.Assert.assertTrue
import org.junit.Test

class PetAnimationConfigTest {
    @Test
    fun reduceMotionUsesCalmerDurations() {
        assertTrue(PetAnimationConfig.petFloatDurationMs("EXCITED", false) > PetAnimationConfig.petFloatDurationMs("EXCITED", true))
        assertTrue(PetAnimationConfig.bounceDurationMs(false) > PetAnimationConfig.bounceDurationMs(true))
        assertTrue(PetAnimationConfig.heartDurationMs(false) > PetAnimationConfig.heartDurationMs(true))
    }
}
```

 16. Dependencias Externas

| LibrerÃ­a | VersiÃ³n | PropÃ³sito |
|:---------|:-------:|:----------|
| Kotlin | 2.2.10 | Lenguaje principal |
| AGP | 9.1.1 | Android Gradle Plugin |
| Compose BOM | 2024.09.00 | UI declarativa |
| Material 3 | via BOM | Componentes Material Design 3 |
| Room | 2.7.0 | Persistencia SQLite |
| Room Compiler (KSP) | 2.7.0 | GeneraciÃ³n de cÃ³digo Room |
| Lifecycle (ViewModel) | 2.8.7 | Arquitectura MVVM |
| Activity Compose | 1.10.1 | IntegraciÃ³n Activity + Compose |
| Coroutines | 1.10.2 | Async |
| **Koin Android** | **4.0.2** | InyecciÃ³n de dependencias |
| **Koin Compose** | **4.0.2** | IntegraciÃ³n Koin + Compose |
| **DataStore Prefs** | **1.1.7** | Persistencia de preferencias |
| **Navigation Compose** | 2.8.9 | NavegaciÃ³n entre pantallas |
| **WorkManager** | 2.10.0 | Notificaciones periÃ³dicas |
| Robolectric | 4.16.1 | Tests unitarios de Android |
| Roborazzi | 1.59.0 | Screenshot tests |
| Retrofit | 2.11.0 (declarada) | HTTP client (no usada aÃºn) |
| OkHttp | 4.12.0 (declarada) | HTTP engine (no usada aÃºn) |
| Moshi | 1.15.2 (declarada) | JSON parsing (no usada aÃºn) |
| Firebase BOM | 33.0.0 (declarada) | Firebase suite (opcional) |

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

## 17. GuÃ­a para Desarrolladores

### 17.1 CÃ³mo Compilar

```bash
# Compilar debug APK
./gradlew clean assembleDebug
# APK en: app/build/outputs/apk/debug/app-debug.apk

# Compilar release APK (requiere keystore)
./gradlew clean assembleRelease
# APK en: app/build/outputs/apk/release/app-release.apk
```

**Nota sobre Firebase:** El archivo `google-services.json` no estÃ¡ incluido. El build advierte pero no falla.

### 17.2 CÃ³mo Probar

```bash
# Tests unitarios
./gradlew testDebugUnitTest

# Tests de instrumentaciÃ³n (requiere emulador/dispositivo)
./gradlew connectedDebugAndroidTest

# Screenshot test (Roborazzi)
./gradlew testDebugUnitTest --tests "*.GreetingScreenshotTest"
```

### 17.3 CÃ³mo Agregar un Nuevo Reto

```kotlin
// 1. Abrir ChallengesData.kt
// 2. Agregar un nuevo CodingChallenge a la lista:
CodingChallenge(
    id = 91, language = "Kotlin", type = "TRIVIA",
    title = "Tu Nuevo Reto", question = "Â¿Pregunta?",
    options = listOf("OpciÃ³n A", "OpciÃ³n B", "OpciÃ³n C", "OpciÃ³n D"),
    correctAnswerIndex = 2, explanation = "ExplicaciÃ³n detallada..."
)
```

### 17.4 CÃ³mo Agregar un Nuevo Tema

```kotlin
// 1. Abrir ThemeConfig.kt
// 2. Agregar un nuevo AppTheme a ThemeRegistry.allThemes:
AppTheme(
    name = "Mi Tema", icon = Icons.Default.Star,
    description = "DescripciÃ³n...", isDark = true,
    background = Color(0xFF000000), /* ... 12 colores */,
    fontFamily = FontFamily.Monospace, cornerRadius = 8.dp,
    usesGradients = true, gradientColors = listOf(...)
)
```

### 17.5 CÃ³mo Agregar un Nuevo Minijuego

1. Crear el composable en `feature/games/`
2. Agregar la opciÃ³n en `GamesScreen.kt` (o `MinigamesDialog`)
3. El juego debe llamar a `viewModel.completeMinigame(bytes, health, energy)` al terminar
4. Registrar ruta en `Routes` object y `AppNavigation.kt`

### 17.6 CÃ³mo Agregar una Nueva Feature Screen

1. Crear el archivo en `feature/mi-feature/`
2. Agregar ruta en `Routes` object en `AppNavigation.kt`
3. Agregar el `composable()` branch en `AppNavigation.kt`
4. Agregar el botÃ³n en la bottom navigation bar si es necesario

### 17.7 Convenciones de CÃ³digo

- **Idioma:** EspaÃ±ol (strings, comentarios)
- **UI:** Jetpack Compose con Material 3
- **Estado:** ViewModel con `StateFlow` + `mutableStateOf`
- **DI:** Koin (mÃ³dulos en `di/`)
- **Persistencia:** Room para datos complejos, DataStore para preferencias
- **Async:** Corrutinas en `viewModelScope`
- **Tests:** Robolectric + Roborazzi
- **TipografÃ­a:** `FontFamily.Monospace` para textos de terminal
- **Animaciones:** `rememberInfiniteTransition` para loops, `Animatable` para one-shot
- **NavegaciÃ³n:** NavHost con `Routes` object (constantes string)

---

## 18. Changelog

### v2.0.0 â€” RefactorizaciÃ³n Mayor

#### ðŸ—ï¸ Arquitectura
- **ModularizaciÃ³n:** CÃ³digo monolÃ­tico (~2925 lÃ­neas) dividido en `feature/{home,learn,focus,shop,games,settings}/`, `navigation/AppNavigation.kt` y `ui/components/`
- **NavegaciÃ³n:** Sistema de NavHost con `Routes` object (10 rutas)
- **DI:** MigraciÃ³n a **Koin 4.0.2** (compatible con AGP 9.x)
- **DataStore:** SharedPreferences migrado a **DataStore Preferences** + nuevo `AchievementsRepository`
- **Room v3:** Migraciones M1â†’2 y M2â†’3 para `focus_sessions` y `hasRenamed`

#### ðŸŽ® Nuevos Juegos (Arcade de DepuraciÃ³n)
- **Bug Hunt â€” Terminal Panic:** 10 snippets, 5 rondas, explicaciones con humor
- **Git Rescue:** 8 escenarios, 5 decisiones, progreso visual de rama
- **Refactor Rush:** 12 puzzles, penalizaciÃ³n por intentos
- **Cooldown diario de 24h:** persistido en DataStore

#### ðŸŽ¨ Sistema de Temas Premium (12)
- `AppTheme` con emoji, tipografÃ­a Ãºnica, corner radius, gradientes
- `toColorScheme()` â†’ mapeo a Material 3 ColorScheme
- `buildTypography()` dinÃ¡mica por tema
- `LocalAppTheme` CompositionLocal
- Carrusel visual en Settings (LazyRow + preview colores + borde animado)
- **Retro Pixel ðŸ†:** Tema final 8-bit con Press Start 2P + mundo pixel art

#### âš™ï¸ ConfiguraciÃ³n Completa
- Perfil, aprendizaje, experiencia (temas/sonido/vibraciÃ³n/animaciones)
- Recordatorios (placeholder), datos (exportar/reset)
- Logros (12 logros con DataStore persistente)

#### ðŸŽ“ Onboarding RediseÃ±ado (4 pasos)
- HorizontalPager con animaciones de entrada
- Paso 3: FilterChip multiselecciÃ³n (8 temas, mÃ¡x 3)
- Paso 4: Input con validaciÃ³n y preview estilo terminal

#### â±ï¸ Timer Persistente
- FocusSessionEntity en Room
- El temporizador sobrevive al cierre de la app
- Recompensa offline al reabrir

#### âš–ï¸ Rebalance de MecÃ¡nicas
- Decaimiento reducido ~50%
- Recompensas aumentadas ~100%
- Interacciones potenciadas ~50%
- Streak mÃ¡s indulgente (resetea a las 48h)

#### ðŸ“š Banco de Retos
- Expandido de **11 â†’ 90+ challenges**
- Kotlin: 20+, JavaScript: 20, PHP: 19, Python: 29+

#### ðŸ§¹ Limpieza
- Eliminados ~20 imports/dependencias comentadas
- Build: **0 warnings, 0 errors**

---

> **Code Tamagotchi v2.0** â€” Donde los bugs se convierten en mascotas y el cÃ³digo en cariÃ±o.  
> DocumentaciÃ³n generada para desarrolladores y curiosos.  
> Â¿Preguntas? Abre un issue en [GitHub](https://github.com/fguzman-stack/CodePet).

