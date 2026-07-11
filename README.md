<div align="center">

<!-- Hero animado -->
<img
  src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=26&duration=3200&pause=900&color=81C784&center=true&vCenter=true&width=760&lines=%F0%9F%90%BE+Code+Tamagotchi;%3E+Tu+mascota+virtual+para+aprender+a+programar;%3E+Estudia.+Resuelve+retos.+Evoluciona."
  alt="Code Tamagotchi - Tu mascota virtual para aprender a programar"
/>

<br/>

<img src="app/src/main/res/drawable/img_pet_happy_1783677319374.jpg" width="150" alt="Codey, la mascota virtual de Code Tamagotchi"/>

# 🐾 Code Tamagotchi

### `Tu compañero de estudio que evoluciona mientras programas`

<p>
  <a href="https://github.com/fguzman-stack/CodePet/releases">
    <img src="https://img.shields.io/github/v/release/fguzman-stack/CodePet?style=for-the-badge&logo=github&logoColor=white&color=7C4DFF" alt="Última versión"/>
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/Licencia-MIT-81C784?style=for-the-badge&logo=opensourceinitiative&logoColor=white" alt="Licencia MIT"/>
  </a>
  <a href="https://github.com/fguzman-stack/CodePet/actions">
    <img src="https://img.shields.io/badge/Build-passing-00C853?style=for-the-badge&logo=githubactions&logoColor=white" alt="Estado del build"/>
  </a>
</p>

<p>
  <img src="https://img.shields.io/badge/Android-24%2B-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android API 24 o superior"/>
  <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-UI-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Material_3-Material_You-6750A4?style=flat-square&logo=materialdesign&logoColor=white" alt="Material 3"/>
  <img src="https://img.shields.io/badge/Room-SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white" alt="Room"/>
</p>

<br/>

> `./code-tamagotchi --status`
>
> **Status:** `ACTIVE` · **Platform:** `ANDROID` · **Language:** `KOTLIN / COMPOSE`

**Code Tamagotchi** transforma el estudio de programación en una experiencia de cuidado, progreso y recompensas.  
Resuelve desafíos, completa sesiones Pomodoro, gana Bytes y mantén a **Codey** feliz, saludable y listo para compilar.

<a href="#-instalación">
  <img src="https://img.shields.io/badge/▶_Empezar_a_jugar-81C784?style=for-the-badge&logo=android&logoColor=white" alt="Empezar a jugar"/>
</a>
<a href="#-características">
  <img src="https://img.shields.io/badge/✨_Explorar_funciones-7C4DFF?style=for-the-badge&logo=github&logoColor=white" alt="Explorar funciones"/>
</a>

</div>

---

## 🧭 Navegación

<div align="center">

[✨ Características](#-características) ·
[📸 Capturas](#-capturas) ·
[🛠️ Tecnologías](#️-tecnologías) ·
[🏗️ Arquitectura](#️-arquitectura) ·
[🚀 Instalación](#-instalación) ·
[🎮 Cómo jugar](#-cómo-jugar) ·
[🎨 Temas](#-temas-visuales) ·
[🗺️ Roadmap](#️-roadmap) ·
[🤝 Contribuir](#-contribuir)

</div>

---

## ✨ Características

<table>
<tr>
<td width="50%" valign="top">

### 🐾 Mascota virtual

- Estados emocionales dinámicos
- Salud, hambre y energía en tiempo real
- Animaciones según el estado
- Sistema de nivel, XP y Bytes
- Racha diaria de estudio
- Decaimiento progresivo cuando no la cuidas

</td>
<td width="50%" valign="top">

### 💻 Aprende programando

- Retos de **Kotlin**, **JavaScript**, **PHP** y **Python**
- Preguntas tipo trivia y debugging
- Retos especiales de algoritmos
- Feedback educativo en cada respuesta
- Recompensas por progreso y precisión

</td>
</tr>
<tr>
<td width="50%" valign="top">

### ⏱️ Pomodoro integrado

- Sesiones de 15, 25 o 50 minutos
- Bitácora persistente de estudio
- XP y Bytes por cada minuto
- Bonificación para sesiones de 25+ minutos
- Temas: Git, SQL, Clean Code y más

</td>
<td width="50%" valign="top">

### 🎮 Minijuegos

- 🤖 **Adivina el Bit**
- 🐛 **Caza de Bugs**
- 🔒 **Servidor, Script, Hacker**
- Premios en Bytes y salud
- Jugabilidad breve para descansar entre sesiones

</td>
</tr>
</table>

---

## ⚡ Bucle de progreso

```text
   ESTUDIA             RESUELVE              GANA
┌────────────┐     ┌────────────┐      ┌────────────┐
│ Pomodoro   │ ──► │ Retos dev  │ ───► │ XP + Bytes │
└────────────┘     └────────────┘      └─────┬──────┘
                                             │
                                             ▼
                                      ┌────────────┐
                                      │ CUIDA A    │
                                      │   CODEY    │
                                      └─────┬──────┘
                                            │
                                            ▼
                                      DESBLOQUEA TEMAS
```

> Cada sesión terminada fortalece a tu mascota. Cada reto correcto acelera su evolución.

---

## 🧠 Estados de Codey

| Estado | Condición principal | Comportamiento |
|:--|:--|:--|
| 😊 `HAPPY` | Valores equilibrados | Flotación suave |
| 😴 `SLEEPING` | El usuario activa descanso | Recupera energía |
| 📚 `STUDYING` | Pomodoro en curso | Concentrado en aprender |
| 🤒 `SICK` | Salud menor a 30% | Tiembla y necesita cuidado |
| 🍽️ `HUNGRY` | Hambre menor a 30% | Pulso de alerta |
| 😢 `SAD` | Energía menor a 20% | Baja actividad |
| 🤩 `EXCITED` | Felicidad máxima | Animación rápida |

<details>
<summary><strong>🖼️ Ver recursos de mascota</strong></summary>

<br/>

| Archivo | Estado | Uso |
|:--|:--|:--|
| `img_pet_happy_1783728781307.jpg` | 😊 HAPPY | Estado predeterminado |
| `img_pet_happy_1783677319374.jpg` | 🎉 ONBOARDING | Bienvenida |
| `img_pet_sleep_1783728828159.jpg` | 😴 SLEEPING | Descanso |
| `img_pet_study_1783728839262.jpg` | 📚 STUDYING | Sesión activa |
| `img_pet_sick_1783728805525.jpg` | 🤒 SICK | Salud baja |
| `img_pet_sad_1783728794177.jpg` | 😢 SAD | Energía baja |
| `img_pet_hungry_1783728816746.jpg` | 🍽️ HUNGRY | Hambre baja |
| `img_pet_excited_1783728850905.jpg` | 🤩 EXCITED | Felicidad máxima |

</details>

---

## 📸 Capturas

<div align="center">

| Onboarding | Próximamente: Home | Próximamente: Retos |
|:--:|:--:|:--:|
| <img src="app/src/test/screenshots/greeting.png" width="210" alt="Pantalla de bienvenida de Code Tamagotchi"/> | `Añade captura` | `Añade captura` |

</div>

> Guarda las imágenes recomendadas en `docs/screenshots/` para mantener el repositorio ordenado y usarlas con rutas relativas.

---

## 🛠️ Tecnologías

<div align="center">

| Tecnología | Uso dentro de Code Tamagotchi |
|:--|:--|
| **Kotlin 2.2.10** | Lenguaje principal |
| **Jetpack Compose** | Interfaz declarativa |
| **Material 3** | Componentes y sistema visual |
| **Room** | Persistencia local con SQLite |
| **ViewModel + StateFlow** | Estado reactivo y ciclo de vida |
| **Coroutines + Flow** | Procesos asíncronos |
| **KSP** | Procesamiento de anotaciones |
| **Roborazzi** | Pruebas visuales por capturas |
| **Retrofit + OkHttp + Moshi** | Cliente HTTP |
| **Firebase** | App Check, ReCaptcha e IA opcional |

</div>

---

## 🏗️ Arquitectura

```text
┌──────────────────────────────────────────────────────────┐
│                    🎨 PRESENTACIÓN                        │
│   Compose Screens · Material 3 · StateFlow               │
├──────────────────────────────────────────────────────────┤
│                    🧠 DOMINIO                             │
│   PetViewModel · Retos · Cuidados · Temporizador         │
├──────────────────────────────────────────────────────────┤
│                    📦 DATOS                               │
│   PetRepository · Room DAO · SharedPreferences           │
├──────────────────────────────────────────────────────────┤
│                    💾 PERSISTENCIA                        │
│   SQLite / Room · PetState · StudySession                │
└──────────────────────────────────────────────────────────┘
```

```text
Usuario → Composable → ViewModel → Repository → DAO → SQLite
              ↑            │
              └─ StateFlow ┘
```

---

## 📁 Estructura

```text
CodePet/
├── app/
│   ├── src/main/
│   │   ├── java/com/tamagotchi/code/
│   │   │   ├── data/
│   │   │   │   ├── database/
│   │   │   │   └── repository/
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   └── theme/
│   │   │   ├── util/
│   │   │   ├── viewmodel/
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       ├── drawable/
│   │       ├── mipmap-*/
│   │       └── values/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── .env.example
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Instalación

### Requisitos

- Android Studio Hedgehog o superior
- Android SDK API 24+
- JDK 17+
- Gradle 8.x, incluido mediante Gradle Wrapper

### Compilar localmente

```bash
# Clonar el repositorio
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet

# Compilar APK de desarrollo
./gradlew clean assembleDebug
```

El APK se generará en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Instalar mediante ADB

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

> [!NOTE]
> Firebase es opcional. Si `google-services.json` no existe, el proyecto puede mostrar una advertencia según tu configuración local.

---

## 🎮 Cómo jugar

| Paso | Acción | Recompensa |
|:--:|:--|:--|
| `01` | Nombra a tu mascota durante el onboarding | Comienzo de la aventura |
| `02` | Completa retos en **LEARN** | XP, Bytes, salud y alimento |
| `03` | Inicia un Pomodoro en **STUDY** | XP y Bytes por minuto |
| `04` | Compra recursos en **SHOP** | Recupera estadísticas |
| `05` | Juega minijuegos | Bytes y bonificaciones |
| `06` | Mantén la racha | Progreso constante |

### Economía rápida

| Actividad | Bytes | XP |
|:--|:--:|:--:|
| Reto normal | +20 a +25 | +15 a +20 |
| Reto especial | +50 | — |
| Estudio por minuto | +1 | +2 |
| Bonus de sesión 25+ min | +25 | +50 |
| Minijuego ganado | Hasta +20 | Variable |

---

## 🎨 Temas visuales

Code Tamagotchi incluye **25 temas** desbloqueables para personalizar la experiencia.

```text
Matrix Green · Galáctico · Cyberpunk · Bosque Encantado · Sakura
Minimalista · Neón · Océano · Volcánico · Ártico · Vaporwave
Café · Retro · Pixel Art · Samurai · Medieval · Desierto · Aurora
Cristal · Nocturno · Tropical · Otoño · Hacker · Magma · Fantasma
```

Cada tema configura:

```kotlin
background
surface
primary
secondary
onPrimary
textPrimary
textSecondary
success
error
```

> Resuelve desafíos especiales en **LEARN** para desbloquear nuevos estilos.

---

## 🗺️ Roadmap

- [ ] 🔔 Notificaciones para recordar cuidados y sesiones
- [ ] 🌐 Retos en Ruby, Go, Rust y Swift
- [ ] 🏆 Sistema de logros desbloqueables
- [ ] 🧬 Evolución visual de la mascota por nivel
- [ ] 📱 Widgets para pantalla de inicio
- [ ] ☁️ Sincronización con Firebase Firestore
- [ ] 👥 Funcionalidad multijugador y rankings
- [ ] 🕹️ Nuevos minijuegos: Memory Match y Typing Race

---

## 🤝 Contribuir

Las contribuciones, ideas y mejoras son bienvenidas.

```bash
# 1. Crea una rama
git checkout -b feature/nueva-caracteristica

# 2. Realiza tus cambios y confirma
git commit -m "feat: añade nueva característica"

# 3. Sube la rama
git push origin feature/nueva-caracteristica
```

Después, abre un [Pull Request](https://github.com/fguzman-stack/CodePet/pulls).

### Convenciones del proyecto

- Mantén la convención oficial de Kotlin
- Añade `testTag` a componentes interactivos
- Utiliza `FontFamily.Monospace` para la estética terminal
- Escribe comentarios y documentación en español
- Incluye pruebas cuando modifiques lógica crítica

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia [MIT](LICENSE).

```text
Copyright (c) 2026 Francisco Guzmán
```

---

<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&size=17&duration=3500&pause=1000&color=81C784&center=true&vCenter=true&width=620&lines=%3E+while+(learning)+%7B+code%2C+care%2C+evolve+%7D;%3E+Hecho+con+%E2%9D%A4%EF%B8%8F%2C+%E2%98%95+y+muchos+Bytes." alt="Hecho con amor, café y muchos Bytes"/>

<br/><br/>

<a href="https://github.com/fguzman-stack/CodePet/stargazers">
  <img src="https://img.shields.io/github/stars/fguzman-stack/CodePet?style=for-the-badge&logo=github&color=FFD54F" alt="Estrellas del repositorio"/>
</a>
<a href="https://github.com/fguzman-stack/CodePet/fork">
  <img src="https://img.shields.io/github/forks/fguzman-stack/CodePet?style=for-the-badge&logo=github&color=64B5F6" alt="Forks del repositorio"/>
</a>

<br/><br/>

**Code Tamagotchi** · `Estudia` · `Cuida` · `Evoluciona`

</div>
