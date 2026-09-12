# Contributing to CodePet

First off, thanks for taking the time to contribute! CodePet is a gamified coding-learning app, and the best way to grow it is with the community.

[Español abajo](#-contribuyendo-en-español)

## Ways to contribute

### 🧩 Add coding challenges (easiest way to start!)

The app ships with 88+ challenges in Kotlin, JavaScript, PHP and Python. We'd love to grow to **Ruby, Go, Rust, Swift, SQL and C**.

1. Open `app/src/main/java/com/tamagotchi/code/data/ChallengesData.kt`
2. Study the format of existing challenges (trivia, debugging, code output)
3. Add 5–10 well-explained challenges in a new language
4. Submit a PR — one language per PR keeps reviews fast

**Rules for good challenges:**
- The explanation teaches a concept, it doesn't just say "right/wrong"
- No trick questions; ambiguity is a bug
- Code snippets must actually compile/run in their language

### 🎨 Themes, pets and art
- New visual themes: implement an `AppTheme` in `ui/theme/ThemeConfig.kt` and its animated background in `ui/components/AnimatedThemeBackground.kt`
- Pixel-art pet skins go in `app/src/main/assets/pet/`

### 🐛 Bug fixes
Search [issues](https://github.com/fguzman-stack/CodePet/issues) first. `good first issue` labels are beginner-friendly.

### 🌍 Translations
The UI strings live in `app/src/main/res/values/`. A new language = one `values-<lang>/strings.xml`.

## Getting set up

```bash
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet
./gradlew assembleDebug          # build the APK
./gradlew testDebugUnitTest      # run tests
```

**Requirements:** JDK 17+, Android SDK (API 36), Android Studio Hedgehog+.

> The app is 100% offline — no API keys, accounts or secrets needed. All game data lives in local Room/DataStore.

## Pull Request process

1. Fork → branch from `master` (`git checkout -b feat/rust-challenges`)
2. Keep diffs focused: one feature or fix per PR
3. Run `./gradlew assembleDebug testDebugUnitTest` locally — CI must be green
4. For UI changes, attach a screenshot or short GIF to the PR
5. Conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`, `chore:`

## Code style

- Kotlin official style, 2-space indent (match surrounding code)
- Compose: stateless composables + hoisted state where practical
- Room schema changes require a migration in `AppDatabase.kt` (check current `version`)
- Never commit `local.properties`, keystores, or API keys

## Community

Be kind. We follow the [Code of Conduct](CODE_OF_CONDUCT.md) (Contributor Covenant). Questions? Open an issue or start a discussion.

---

## 🤝 Contribuyendo (en español)

### Cómo empezar
La forma más fácil de contribuir es **agregar retos de programación** en lenguajes que aún no tenemos: Ruby, Go, Rust, Swift, SQL o C. Todo está en `app/src/main/java/com/tamagotchi/code/data/ChallengesData.kt`. Copia el formato de los retos existentes y envía un PR por lenguaje.

### Reglas de oro
- Cada explicación debe enseñar un concepto, no solo decir "correcto/incorrecto"
- Nada de preguntas trampa; la ambigüedad es un bug
- Los fragmentos de código deben compilar/ejecutarse de verdad

### Requisitos
JDK 17+, Android SDK (API 36) y Android Studio Hedgehog+. Compila con `./gradlew assembleDebug` y prueba con `./gradlew testDebugUnitTest`.

### Proceso
1. Fork y rama desde `master`
2. Un PR = una funcionalidad o fix
3. Capturas o GIF si tocaste UI
4. Commits convencionales (`feat:`, `fix:`, `docs:`...)
