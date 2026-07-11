<div align="center">

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
<img src="https://img.shields.io/badge/Room-412991?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room"/>
<img src="https://img.shields.io/badge/Material%20You-0061A4?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material You"/>

<br/>

<!-- Pet banner -->
<img src="https://raw.githubusercontent.com/fguzman-stack/CodePet/main/app/src/main/res/drawable/img_pet_happy_1783677319374.jpg" width="120" height="120" style="border-radius: 20px;" alt="CodePet mascot"/>

# 🐾 Code Tamagotchi

### _Tu mascota virtual que evoluciona mientras programas_

> Una aplicación Android interactiva donde cuidas una mascota virtual resolviendo acertijos de programación, registrando sesiones de estudio y ganando recompensas en bytes. Cada línea de código que estudias la hace más fuerte.

<br/>

[![GitHub release](https://img.shields.io/github/v/release/fguzman-stack/CodePet?style=flat-square&logo=github&color=2E7D32)](https://github.com/fguzman-stack/CodePet/releases)
[![License](https://img.shields.io/badge/license-MIT-2E7D32?style=flat-square)](LICENSE)
[![API](https://img.shields.io/badge/API-24%2B-2E7D32?style=flat-square)](build.gradle.kts)
[![Build](https://img.shields.io/badge/build-passing-4CAF50?style=flat-square&logo=githubactions)](https://github.com/fguzman-stack/CodePet/actions)

</div>

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características](#-características)
- [Capturas de Pantalla](#-capturas-de-pantalla)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Estructura del Código](#-estructura-del-código)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Cómo Jugar](#-cómo-jugar)
- [Sistema de Temas](#-sistema-de-temas)
- [API de Datos](#-api-de-datos)
- [Roadmap](#-roadmap)
- [Contribuciones](#-contribuciones)
- [Licencia](#-licencia)

---

## 🎯 Descripción General

**Code Tamagotchi** es una mascota virtual para programadores. Combina la nostalgia de los Tamagotchi clásicos con la productividad del desarrollo de software:

- **Resuelve retos** de programación en Kotlin, JavaScript, PHP y Python
- **Estudia con temporizador** tipo Pomodoro y gana experiencia
- **Desbloquea** más de 20 temas visuales como Matrix, Cyberpunk, Hacker y Sakura
- **Juega minijuegos** como Adivina el Bit, Caza de Bugs y Servidor-Script-Hacker
- **La mascota decae** con el tiempo si no la cuidas (hambre, energía, salud)

---

## ✨ Características

### 🎮 Mascota Virtual
- **Estado emocional dinámico**: Feliz, Triste, Enferma, Hambrienta, Durmiendo, Estudiando, Emocionada
- **Animaciones fluidas**: La mascota se mueve, escala y tiembla según su estado
- **Diálogos contextuales**: Frases graciosas y técnicas según el estado de ánimo
- **Sistema de niveles y XP**: Sube de nivel acumulando experiencia
- **Racha de estudio**: Mantén la constancia para streaks más largos
- **Decaimiento por inactividad**: La mascota necesita cuidado constante

### 💻 Retos de Programación
- **4 lenguajes**: Kotlin, JavaScript, PHP, Python
- **Tipos de retos**: Trivia (conocimiento general) y Debug (corregir código)
- **6 retos especiales** de algoritmos y lógica (Knapsack, CAP Theorem, Floyd's Cycle...)
- **Feedback inmediato**: Explicación detallada de cada respuesta

### ⏱️ Temporizador de Estudio (Pomodoros)
- **Duración**: 15, 25 o 50 minutos
- **Temas de estudio**: Kotlin, JavaScript, PHP, Python, SQL, Clean Code, Git, Estructuras de Datos
- **Recompensas**: Bytes + XP al completar sesiones
- **Bonus por constancia**: +25 Bytes y +50 XP por sesiones de ≥25 min
- **Bitácora de sesiones**: Historial completo de estudio

### 🏪 Tienda y Cuidados
- **Alimentos**: Manzana Binaria, Pizza de Bytes, Sushi de Datos, Café Espresso
- **Medicinas**: Café Negro, Píldora Desbugueadora, Vacuna Super Compiler
- **Cuidados**: Acariciar (+10% energía), Limpiar (+6% salud, +5% energía)
- **Moneda**: "Bytes" ganados programando y estudiando

### 🎯 Minijuegos
- **Adivina el Bit**: 5 rondas de adivinanza binaria
- **Caza de Bugs**: Atrapa bugs en una cuadrícula 3×3 en 10 segundos
- **Servidor, Script, Hacker**: Piedra, Papel o Tijera versión informática (al mejor de 3)

### 🎨 Sistema de Temas (23 temas)
Matrix Green, Galáctico, Cyberpunk, Bosque Encantado, Sakura, Minimalista, Neón, Océano, Volcánico, Ártico, Vaporwave, Café, Retro, Pixel Art, Samurai, Medieval, Desierto, Aurora, Cristal, Nocturno, Tropical, Otoño, Hacker, Magma, Fantasma

### 🔊 Efectos de Sonido
- Generados mediante `ToneGenerator` del sistema Android
- Sonidos para: éxito, error, subida de nivel, clics, compras, dormir

---

## 📸 Capturas de Pantalla

<div align="center">

| Pantalla | Descripción |
|:--------:|:------------|
| <img src="app/src/test/screenshots/greeting.png" width="200"/> | Pantalla principal con la mascota y barras de estado |
| — | Panel de retos de programación estilo terminal |
| — | Temporizador de estudio con tema y duración |
| — | Tienda de recursos con efectos curativos |

</div>

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Propósito |
|:-----------|:----------|
| [Kotlin](https://kotlinlang.org/) 2.2.10 | Lenguaje principal |
| [Jetpack Compose](https://developer.android.com/jetpack/compose) BOM 2025.01 | UI declarativa |
| [Material 3](https://m3.material.io/) | Componentes y diseño |
| [Room](https://developer.android.com/training/data-storage/room) | Base de datos local SQLite |
| [KSP](https://kotlinlang.org/docs/ksp-overview.html) | Procesamiento de anotaciones en tiempo de compilación |
| [Android ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) | Manejo de estado y ciclo de vida |
| [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) + Flow | Programación asíncrona |
| [Roborazzi](https://github.com/takahirom/roborazzi) | Pruebas de captura de pantalla |
| [Firebase](https://firebase.google.com/) | App Check, ReCaptcha, AI (opcional) |
| [Retrofit](https://square.github.io/retrofit/) + OkHttp + Moshi | Cliente HTTP |
| [Secrets Gradle Plugin](https://github.com/google/secrets-gradle-plugin) | Variables de entorno seguras |

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue la arquitectura MVVM (Model-View-ViewModel) con Jetpack Compose:

```
┌──────────────────────┐
│       View           │  → Composables (CodeTamagotchiScreen, OnboardingScreen)
│   (Jetpack Compose)  │     Observan StateFlow del ViewModel
├──────────────────────┤
│    ViewModel         │  → PetViewModel.kt
│  (AndroidViewModel)  │     Maneja lógica de negocio, temporizador, retos
├──────────────────────┤
│    Repository        │  → PetRepository.kt
│   (Patrón Repositorio)│     Abstracción entre ViewModel y DAO
├──────────────────────┤
│      Data            │  → Room Database
│   (Room + DAO)       │     PetDao.kt, AppDatabase.kt
├──────────────────────┤
│   Entidades          │  → PetStateEntity.kt, StudySessionEntity.kt
│   (Room Entities)    │     Modelos de datos persistentes
└──────────────────────┘
```

### Diagrama de Flujo de Datos

```
Usuario interactúa
      ↓
Composable (UI)
      ↓  observa StateFlow
ViewModel
      ↓  llama
Repository
      ↓  ejecuta
DAO (Room)
      ↓  persiste
SQLite Database
```

---

## 📁 Estructura del Código

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/tamagotchi/code/
│   │   │   ├── MainActivity.kt                    # Entry point, edge-to-edge
│   │   │   ├── data/
│   │   │   │   ├── ChallengesData.kt               # 11 retos de programación
│   │   │   │   ├── SpecialChallengesData.kt        # 6 retos de algoritmos/lógica
│   │   │   │   ├── database/
│   │   │   │   │   ├── AppDatabase.kt              # Room DB (singleton)
│   │   │   │   │   ├── PetDao.kt                   # Acceso a datos
│   │   │   │   │   ├── PetStateEntity.kt           # Estado de la mascota
│   │   │   │   │   └── StudySessionEntity.kt       # Sesiones de estudio
│   │   │   │   └── repository/
│   │   │   │       └── PetRepository.kt            # Capa de repositorio
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   ├── CodeTamagotchiScreen.kt     # UI principal (2776 líneas)
│   │   │   │   │   └── OnboardingScreen.kt         # Pantalla de bienvenida
│   │   │   │   ├── theme/
│   │   │   │   │   ├── Color.kt                    # Colores base
│   │   │   │   │   ├── Theme.kt                    # Tema Material 3 + dinámico
│   │   │   │   │   ├── ThemeConfig.kt              # 23 temas personalizados
│   │   │   │   │   └── Type.kt                     # Tipografía
│   │   │   │   └── viewmodel/
│   │   │   │       └── PetViewModel.kt             # Lógica central (526 líneas)
│   │   │   └── util/
│   │   │       └── SoundManager.kt                 # Efectos de sonido
│   │   ├── res/
│   │   │   ├── drawable/                           # 11 imágenes de mascota + launcher
│   │   │   ├── values/                             # Strings, colors, themes
│   │   │   └── xml/                                # Backup/data extraction rules
│   │   └── AndroidManifest.xml
│   ├── test/java/com/tamagotchi/code/
│   │   ├── ExampleUnitTest.kt                      # Test unitario básico
│   │   ├── ExampleRobolectricTest.kt               # Test con Robolectric
│   │   └── GreetingScreenshotTest.kt               # Captura de pantalla (Roborazzi)
│   └── androidTest/java/com/tamagotchi/code/
│       └── ExampleInstrumentedTest.kt              # Test de instrumentación
├── build.gradle.kts                                # Dependencias y configuración
└── proguard-rules.pro                              # Reglas ProGuard
```

---

## 🚀 Instalación y Configuración

### Prerrequisitos

- [Android Studio](https://developer.android.com/studio) Hedgehog o superior
- SDK de Android 24+
- JDK 17+
- Gradle 8.x (incluido en el wrapper)

### Pasos para compilar

```bash
# 1. Clonar el repositorio
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet

# 2. Configurar variables de entorno (opcional, para signing)
#    KEYSTORE_PATH, STORE_PASSWORD, KEY_PASSWORD

# 3. Limpiar y compilar
./gradlew clean
./gradlew assembleDebug

# 4. El APK estará en:
#    app/build/outputs/apk/debug/app-debug.apk
```

### En Android Studio

1. **File → Open** → Selecciona el directorio `CodePet`
2. Espera a que Gradle sincronice las dependencias
3. **Run → Run 'app'** o presiona `Shift+F10`

> ⚠️ Si ves `google-services.json is missing`, es normal. Firebase está configurado como opcional. El build continuará con una advertencia.

---

## 🎮 Cómo Jugar

### 1. Onboarding
Al iniciar la app por primera vez:
1. Ponle nombre a tu mascota (ej: "Codey", "Pyto", "Buggy")
2. Presiona **"Compilar y Empezar"**
3. ¡Tu mascota aparecerá con estado "Happy"!

### 2. Pantalla Principal (HOME)
- **Mascota animada**: Se mueve, tiembla o flota según su estado
- **Barras de estado**: Vida ❤️, Alimento 🍕, Energía ⚡
- **Bytes y Racha**: Tu moneda y constancia de estudio
- **Nivel y XP**: Barra de progreso hacia el siguiente nivel
- **Cuidados**: Acariciar, Limpiar, Alimentar
- **Dormir/Despertar**: La mascota recupera energía mientras duerme

### 3. Aprender (LEARN)
- **Retos Normales**: Preguntas de programación por lenguaje
- **Retos Especiales**: Algoritmos avanzados que desbloquean temas visuales
- Cada respuesta correcta da: Bytes, XP, Alimento y Salud

### 4. Estudio (STUDY)
- **Temporizador**: Elige tema y duración (15/25/50 min)
- **Bitácora**: Historial de todas tus sesiones de estudio
- Al completar: ganas XP, Bytes y mantienes tu racha
- Bonus por sesiones ≥ 25 min

### 5. Tienda (SHOP)
- Compra alimentos, medicinas y mejoras con tus Bytes
- Productos: Café, Pizza, Píldora, Vacuna, Comidas varias

### 6. Minijuegos 🎯
- **Adivina el Bit**: 5 rondas, adivina 0 o 1
- **Caza de Bugs**: Atrapa bugs en 10 segundos
- **Servidor, Script, Hacker**: Piedra, Papel o Tijera (mejor de 3)

---

## 🎨 Sistema de Temas

Code Tamagotchi incluye **23 temas visuales** personalizados. Los temas se aplican a todos los componentes de la UI:

| # | Tema | Estilo | ¿Desbloqueable? |
|:-:|:-----|:-------|:---------------:|
| 1 | Matrix Green | Oscuro, verde/neón | ✅ Inicial |
| 2 | Galáctico | Oscuro, púrpura | ❌ Retos especiales |
| 3 | Cyberpunk | Oscuro, rosa/cian | ❌ Retos especiales |
| 4 | Bosque Encantado | Oscuro, verde/bosque | ❌ Retos especiales |
| 5 | Sakura | Claro, rosa | ❌ Retos especiales |
| 6 | Minimalista | Claro, grises | ❌ Retos especiales |
| 7 | Neón | Oscuro, verde neón/magenta | ❌ Retos especiales |
| 8 | Océano | Oscuro, azul profundo | ❌ Retos especiales |
| ... | ... | ... | ... |
| 23 | Fantasma | Claro, gris suave | ❌ Retos especiales |

Cada tema define: `background`, `surface`, `primary`, `secondary`, `onPrimary`, `textPrimary`, `textSecondary`, `success` y `error`.

---

## 📊 API de Datos

### PetStateEntity (Room)

```kotlin
@Entity(tableName = "pet_state")
data class PetStateEntity(
    val id: Int = 1,              // Siempre 1 (singleton)
    val name: String = "Codey",    // Nombre de la mascota
    val language: String = "Kotlin", // Lenguaje de especialidad
    val level: Int = 1,            // Nivel actual
    val xp: Int = 0,               // Experiencia acumulada
    val hunger: Float = 100f,      // Hambre (0-100)
    val health: Float = 100f,      // Salud (0-100)
    val energy: Float = 100f,      // Energía (0-100)
    val bytes: Int = 50,           // Moneda virtual
    val streak: Int = 0,           // Racha de estudio (días)
    val currentStatus: String = "HAPPY" // Estado emocional
)
```

### StudySessionEntity (Room)

```kotlin
@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    val topic: String,             // Tema estudiado
    val durationMinutes: Int,       // Duración en minutos
    val timestamp: Long             // Fecha Unix
)
```

### Estados de la Mascota

| Estado | Condición | Efecto Visual |
|:-------|:----------|:--------------|
| `HAPPY` | health ≥ 30, hunger ≥ 30, energy ≥ 20, no durmiendo/estudiando | Flotación suave |
| `SLEEPING` | Usuario activa sueño | Flotación lenta, fondo azul |
| `STUDYING` | Temporizador activo | Concentrada |
| `SICK` | health < 30 | Temblor horizontal |
| `HUNGRY` | hunger < 30 | Escalado pulsante |
| `SAD` | energy < 20 | Inmóvil, color gris |
| `EXCITED` | Felicidad máxima | Movimiento rápido |

### Sistema de Decaimiento

Cuando la app se abre, calcula el tiempo transcurrido desde la última actividad:

- **Cada hora despierto**: -4% hambre, -3% energía, -3.64% salud base
- **Cada hora durmiendo**: +15% energía, -1.5% hambre
- **Hambre en 0**: -5% salud extra por hora
- **Energía < 10%**: -2% salud extra por hora
- **Sin estudio > 36h**: Racha se reinicia a 0
- **Sin estudio > 48h**: -3% salud extra por hora

### Cálculo de Nivel

```kotlin
private fun calculateLevel(xp: Int, currentLevel: Int): Int {
    var level = 1
    var requiredXp = 100
    while (xp >= requiredXp) {
        level++
        requiredXp += level * 100
    }
    return level
}
```

---

## 🗺️ Roadmap

- [ ] **Notificaciones**: Recordatorios para cuidar la mascota
- [ ] **Más lenguajes**: Ruby, Go, Rust, Swift retos
- [ ] **Multijugador**: Comparar niveles y rachas con amigos
- [ ] **Widgets**: Mascota en la pantalla de inicio
- [ ] **Logros**: Sistema de achievements desbloqueables
- [ ] **Mascotas evolutivas**: Cambio de apariencia al subir de nivel
- [ ] **Sincronización en la nube**: Firebase Firestore

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor sigue estos pasos:

1. **Fork** el proyecto
2. Crea tu **rama de feature**: `git checkout -b feature/nueva-caracteristica`
3. **Commit** tus cambios: `git commit -m 'feat: añade nueva característica'`
4. **Push** a la rama: `git push origin feature/nueva-caracteristica`
5. Abre un **Pull Request**

### Guía de estilo

- Sigue la convención de código de Kotlin
- Usa `FontFamily.Monospace` para textos en terminal
- Agrega `testTag` a los elementos interactivos
- Comenta en español (el público objetivo es hispanohablante)

---

## 📄 Licencia

```
MIT License

Copyright (c) 2026 Francisco Guzmán

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

**Code Tamagotchi** — _Hecho con ❤️ y café para developers_

[![GitHub stars](https://img.shields.io/github/stars/fguzman-stack/CodePet?style=social)](https://github.com/fguzman-stack/CodePet)
[![Twitter](https://img.shields.io/twitter/follow/fguzman_stack?style=social)](https://twitter.com/fguzman_stack)

</div>
