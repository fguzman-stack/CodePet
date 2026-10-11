<div align="center">
  <img src="docs/assets/codey-hero.svg" width="1200" alt="Code Tamagotchi: Codey, a programmer cat, inside a terminal-inspired Android app"/>

  <h1>Code Tamagotchi</h1>

  <p><strong>Raise Codey while you learn to code.</strong></p>
  <p>Pomodoro sessions, coding challenges, arcade games and pet-care systems in a local-first Android app.</p>

  <p>
    <a href="https://github.com/fguzman-stack/CodePet/releases/latest"><img src="https://img.shields.io/badge/Download_APK-81C784?style=for-the-badge&logo=android&logoColor=white" alt="Download APK"/></a>
    <a href="#screenshots"><img src="https://img.shields.io/badge/View_screenshots-7C4DFF?style=for-the-badge" alt="View screenshots"/></a>
  </p>

  <p>
    <a href="https://github.com/fguzman-stack/CodePet/releases"><img src="https://img.shields.io/github/v/release/fguzman-stack/CodePet?style=flat-square&color=7C4DFF" alt="Latest release"/></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-81C784?style=flat-square" alt="MIT License"/></a>
    <img src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android 7.0+"/>
    <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin 2.2.10"/>
    <img src="https://img.shields.io/badge/Jetpack_Compose-UI-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  </p>

  <p><strong>English</strong> · <a href="README.es.md">Español</a></p>
</div>

---

<div align="center">
  <a href="#screenshots">Screenshots</a> ·
  <a href="#features">Features</a> ·
  <a href="#install">Install</a> ·
  <a href="#docs">Docs</a> ·
  <a href="#contribute">Contribute</a>
</div>

---

## What It Is

Code Tamagotchi turns programming practice into a pet-care loop. Study with Pomodoro, solve coding challenges, earn XP and Bytes, then use those rewards to keep Codey healthy, rested and evolving.

```text
study -> solve -> earn -> care -> unlock -> repeat
```

The app is built for Android with Kotlin, Jetpack Compose, Room, DataStore, Koin and WorkManager.

<a id="screenshots"></a>
## Screenshots

The project screenshots live in [`capturas_de_pantalla/`](capturas_de_pantalla/) and are shown here in their folder order.

<div align="center">
<table>
<tr>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_1.jpeg"><img src="capturas_de_pantalla/captura_1.jpeg" width="220" alt="Codey home screen with health, satiety, energy, XP and care actions"/></a><br/><sub><strong>01</strong> · Home and care</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_2.jpeg"><img src="capturas_de_pantalla/captura_2.jpeg" width="220" alt="Normal programming challenge screen"/></a><br/><sub><strong>02</strong> · Coding challenges</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_3.jpeg"><img src="capturas_de_pantalla/captura_3.jpeg" width="220" alt="Special algorithm challenge screen"/></a><br/><sub><strong>03</strong> · Special challenges</sub></td>
</tr>
<tr>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_4.jpeg"><img src="capturas_de_pantalla/captura_4.jpeg" width="220" alt="Language badges and progress screen"/></a><br/><sub><strong>04</strong> · Language badges</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_5.jpeg"><img src="capturas_de_pantalla/captura_5.jpeg" width="220" alt="Focus mode with Pomodoro length selection"/></a><br/><sub><strong>05</strong> · Focus mode</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_6.jpeg"><img src="capturas_de_pantalla/captura_6.jpeg" width="220" alt="Shop and Codey wardrobe screen"/></a><br/><sub><strong>06</strong> · Shop and wardrobe</sub></td>
</tr>
</table>

<a href="capturas_de_pantalla/captura_7.jpeg"><img src="capturas_de_pantalla/captura_7.jpeg" width="260" alt="Matrix Green theme on the Codey home screen"/></a><br/>
<sub><strong>07</strong> · Matrix Green theme</sub>
</div>

<a id="features"></a>
## Features

<table>
<tr>
<td width="50%" valign="top">

### Pet Care

- Dynamic states for happy, sleeping, studying, sick, hungry, sad, excited and critical moments.
- Health, satiety, energy, XP, Bytes and streak tracking.
- Sleep, clean, pet and shop actions.
- Moodlets, daily rewards, weekly missions and pair-programming events.

</td>
<td width="50%" valign="top">

### Learn By Coding

- 117 regular challenges across 13 languages.
- 6 special algorithm and architecture challenges.
- Code review snippets, trivia cards and educational feedback.
- Language badge progress for completion milestones.

</td>
</tr>
<tr>
<td width="50%" valign="top">

### Focus And Rewards

- Pomodoro sessions of 15, 25 or 50 minutes.
- XP and Bytes awarded for study time.
- Bonus rewards for longer sessions.
- DND-aware focus mode.

</td>
<td width="50%" valign="top">

### Android Systems

- Room database with explicit migrations.
- DataStore preferences and cooldown state.
- WorkManager checks and launcher widget updates.
- Procedural Codey rendering with Compose Canvas.

</td>
</tr>
</table>

## Project Stats

| Item | Current value |
|:--|:--|
| App version | 3.2.0 |
| Android minimum | 7.0 / API 24 |
| Regular challenges | 117 |
| Special challenges | 6 |
| Challenge languages | 13 |
| Visual themes | 12 plus default |
| Room schema | version 7 |

<a id="install"></a>
## Install

### I Just Want To Play

1. Download the APK from [Releases](https://github.com/fguzman-stack/CodePet/releases/latest).
2. Install it on Android 7.0 or newer.
3. Name Codey and start studying.

### I Want To Build It

```bash
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet
./gradlew :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

On Windows PowerShell, use `./gradlew.bat`.

<a id="docs"></a>
## Documentation

The long README content was split into focused docs:

| Guide | Use it for |
|:--|:--|
| [Getting Started](docs/GETTING_STARTED.md) | Build, install and run commands. |
| [Gameplay](docs/GAMEPLAY.md) | Pet loop, economy, themes and rewards. |
| [Architecture](docs/ARCHITECTURE.md) | Stack, package map and app wiring. |
| [Persistence](docs/PERSISTENCE.md) | Room, migrations and DataStore. |
| [Rendering](docs/RENDERING.md) | Codey, themes and widget rendering. |
| [Testing](docs/TESTING.md) | Unit, Robolectric, lint and screenshot tests. |
| [Localization](docs/LOCALIZATION.md) | Spanish/English resources and content. |
| [Roadmap](docs/ROADMAP.md) | Shipped features and next ideas. |

The older Spanish full reference is still available in [`documentacion.md`](documentacion.md).

<a id="contribute"></a>
## Contribute

The easiest contribution is adding or improving learning content:

- Add challenges for languages with smaller banks.
- Improve explanations in Codey's voice.
- Keep Spanish and English challenge entries synchronized.
- Report UI bugs with screenshots and device details.
- Suggest themes, wardrobe items or study rewards.

See [CONTRIBUTING.md](CONTRIBUTING.md) for the full contributor guide.

## License

MIT. Use it, fork it, adapt it and ship it; keep the copyright notice. See [LICENSE](LICENSE).

<div align="center">
  <img src="docs/assets/codey-footer.svg" width="1200" alt="Codey footer illustration"/>
  <p><strong>Code Tamagotchi</strong> · where bugs become pets and code becomes care.</p>
  <p><sub>Made from Chile by <a href="https://github.com/fguzman-stack">fguzman-stack</a>.</sub></p>
</div>
