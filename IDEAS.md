# 🎮 CodePet — Plan de Expansión (v2, tecnologías actualizadas)

## Estado actual
- [x] Easter eggs ocultos (toques rápidos → animación secreta)
- [x] Recordatorios con personalidad (notificaciones humorísticas)
- [x] Codey escribe commits (resumen diario)
- [x] Cartas de código (datos curiosos/chistes)
- [x] Misiones de personaje (Codey pide actividades)
- [x] Evolución visual por nivel (5, 10, 25, 50)
- [x] Colección de sombreros en la tienda
- [x] Code Review (minijuego)
- [x] Insignias por lenguaje (50%, 75%, 100%)
- [x] Editor de mascota (sliders RGB)
- [x] Música de fondo chiptune/lo-fi (MusicManager)
- [x] GitHub Stats Sync
- [x] Modo No molestar (detección DND)
- [x] Hackatón semanal
- [x] Pair Programming (amigo virtual Buggy)
- [x] Skill Tree / Pase de temporada / Misiones semanales
- [x] Moodlet system
- [x] Weekly Missions

---

## Stack tecnológico recomendado (2026)

| Necesidad | Tecnología antigua | Tecnología recomendada ahora |
|---|---|---|
| Persistencia simple | DataStore Preferences | DataStore (Preferences) + Proto DataStore para datos tipados |
| Datos relacionales (insignias, misiones, cartas) | — | Room con Flow |
| Secretos (token GitHub) | EncryptedSharedPreferences | DataStore + Jetpack Tink / Android Keystore (ESP deprecado desde security-crypto 1.1.0-alpha07) |
| Audio/música | MediaPlayer | Media3 ExoPlayer |
| Tareas periódicas | WorkManager | WorkManager con CoroutineWorker |
| Llamadas a GitHub API | Retrofit clásico | Retrofit + Kotlinx Serialization o Ktor Client |
| Inyección de dependencias | Manual | Hilt |
| Árbol de habilidades / gráficos custom | Compose básico | Compose Canvas + Compose Animation API |
| Detección de Modo Foco del sistema | AccessibilityService | NotificationManager.ACCESS_NOTIFICATION_POLICY |

---

## 1. Easter eggs ocultos
**Estado:** ✅ Implementado
**Archivo:** `ViewportCard.kt`
**Mecánica:** Si tocas a Codey 5 veces en menos de 2 segundos, aparece una animación secreta con mensaje humorístico ("404: Personalidad no encontrada"), con escala y rotación.

**Mejoras técnicas:**
- Migrar la animación a `Animatable` de Compose para transiciones más fluidas y cancelables.
- Usar `SoundPool` (no MediaPlayer) para el sonido especial, óptimo para efectos cortos de baja latencia.
- Guardar el progreso de "easter eggs descubiertos" en Room en vez de una lista plana en DataStore.

**Ideas adicionales:**
- Diferentes easter eggs aleatorios (breakdance, gafas de sol, emoji de fuego)
- Logro secreto por descubrir todos los easter eggs

---

## 2. Recordatorios con personalidad
**Archivo:** `PetCheckWorker.kt`
**Mecánica:** Las notificaciones no son genéricas — Codey se queja con humor según el contexto.

**Mensajes propuestos:**
- Hambre baja: "Llevo 4 horas sin comer, ¿crees que soy un microservicio?"
- Salud baja: "Me duele hasta el último bit... ¿puedes revisarme?"
- Energía baja: "Mi batería está en rojo. Y no es una metáfora de código."
- Crítico: "Si no vienes ahora, voy a escribir un commit con mensaje 'arreglado' y sin descripción."
- Muerte: "Me he ido al otro lado del compilador... ¿me rescatas?"
- General: "Oye, ¿has visto mis logs? Están llenos de NullPointerException existenciales."

**Mejoras técnicas:**
- Reescribir `PetCheckWorker` como `CoroutineWorker` para llamar directamente a funciones suspend del repositorio.
- Usar `NotificationChannelGroup` para separar canales por tipo de alerta (hambre, salud, energía, crítico).
- Aplicar `NotificationCompat.MessagingStyle` para dar formato de conversación a los mensajes de Codey.

---

## 3. Codey escribe commits
**Archivo:** `PetViewModel.kt` + nuevo `DailyCommitGenerator.kt`
**Mecánica:** Al cerrar la app o al final del día, Codey genera un "commit message" gracioso resumiendo lo que pasó.

**Mensajes según actividad:**
- Estudió: "feat: aprendí cosas nuevas hoy"
- Compró: "chore: gasté bytes en cosas innecesarias"
- Jugó: "refactor: me distraje un rato"
- Durmió: "fix: pausa activa para recargar baterías"
- Sin actividad: "docs: hoy no hice nada productivo"
- Acarició: "style: recibí cariño y eso mejora el código"
- Varias actividades: "feat: hoy fue un día completo, hasta hice merge"

**Mejoras técnicas:**
- Guardar el `dailyActivityLog` en Room (tabla `activity_log`) en lugar de una lista en DataStore.
- Disparar la generación del commit con un `CoroutineWorker` programado a medianoche, no solo al cerrar la app.
- Mostrar el resultado con un `AlertDialog` de Compose con estilo terminal (fondo negro, texto verde monospace, cursor parpadeante).

---

## 4. Cartas de código
**Archivo nuevo:** `data/CodeCards.kt` + `ui/components/CodeCardDialog.kt`
**Mecánica:** Cada cierto tiempo (al completar retos, al abrir la app, etc.) aparece una carta con un dato curioso de programación, un chiste nerd o un tip de productividad.

**Datos de ejemplo:**
- "El primer bug de la historia fue una polilla real atrapada en un relé del Harvard Mark II en 1947."
- "¿Sabías que el lenguaje de programación más caro del mundo es APL? Se necesitaba un teclado especial."
- "Tip: Usa nombres descriptivos para tus variables. Tu yo del futuro te lo agradecerá."
- "El término 'debugging' viene de quitar polillas (bugs) de los ordenadores."
- "¿Cuál es el lenguaje de programación favorito de los gatos? Scratch."
- "En 1960 existían más de 2000 lenguajes de programación. Hoy sobreviven unos 250."
- "El primer compilador fue escrito por Grace Hopper en 1952. Ella también popularizó el término 'bug'."
- "Tip: Un buen código no necesita comentarios. Un excelente código los tiene claros."
- "¿Sabías que Python debe su nombre a los Monty Python? No a la serpiente."
- "El símbolo {} se llama 'llave' en español, 'curly brace' en inglés, y 'accolade' en francés."

**Mejoras técnicas:**
- Guardar el mazo en Room con un campo `shown: Boolean` para evitar repetir cartas antes de agotar el mazo completo.
- Animar la carta con `graphicsLayer` (flip 3D en Compose).
- Considerar traer cartas dinámicas desde Firebase Remote Config para actualizar contenido sin publicar nueva versión.

---

## 5. Misiones de personaje
**Archivo:** `PetViewModel.kt` + `ViewportCard.kt`
**Mecánica:** Codey ocasionalmente pide algo específico. Si lo haces, te da bonus de afinidad.

**Tipos de misiones:**
- "Hoy quiero estudiar Kotlin" → Estudia con tema Kotlin → bonus XP
- "Llévame a la tienda" → Abre la tienda → bonus bytes
- "Juguemos Bug Hunt" → Juega Bug Hunt → bonus doble
- "Tengo hambre, cómprame pizza" → Compra pizza → bonus salud
- "Háblame en código" → Completa 3 retos → bonus XP
- "Acaríciame 3 veces" → Acaricia 3 veces → bonus energía
- "Limpia mi habitación" → Limpia → bonus salud
- "Modo foco: 25 min" → Estudia 25 min → bonus doble XP

**Mejoras técnicas:**
- Guardar `activeQuest` en Proto DataStore con campos tipados `id`, `progress`, `reward`, `expiresAt`.
- Usar `CoroutineWorker` con `PeriodicWorkRequest` para asignar nueva misión cada X horas, con constraint de batería no baja.
- Badge de misión activa en `ViewportCard` con `Modifier.animateContentSize()`.

---

## 6. Evolución visual de Codey por nivel
**Archivo:** `AnimatedPetSprite.kt` + recursos drawable
**Mecánica:** Codey cambia de sprite según el nivel:
- Nivel 1-4: Huevo / Cachorro (actual)
- Nivel 5-9: Cría / Adolescente
- Nivel 10-24: Adulto
- Nivel 25-49: Veterano
- Nivel 50+: Legendario

**Mejoras técnicas:**
- Usar **Lottie for Android** con capas condicionales en vez de multiplicar drawables por estado x nivel, reduciendo el peso del APK.
- `evolutionStage` como `sealed class` en el ViewModel para exhaustividad en el `when` de Compose.
- Efecto de aura con `Canvas` + `RadialGradient` shader para niveles altos, sin assets extra.

---

## 7. Colección de sombreros en la tienda
**Archivo:** `ShopScreen.kt` + `PetStateEntity.kt` + `AnimatedPetSprite.kt`
**Mecánica:** La tienda vende sombreros que Codey puede usar.

**Sombreros:**
- Gorro de programador (50 B)
- Birrete de graduación (100 B)
- Casco VR (150 B)
- Sombrero de chef (80 B)
- Corona de rey del código (500 B)
- Gorro de navidad (estacional, 30 B)

**Mejoras técnicas:**
- Modelar `hat` como entidad relacional en Room (`owned_items` con `itemId`, `type`, `equipped`).
- Renderizar el overlay del sombrero con `Modifier.drawWithContent` para composición eficiente.
- Precargar imágenes con **Coil** si se planea agregar contenido remoto.

---

## 8. Code Review (minijuego)
**Archivo nuevo:** `feature/games/CodeReviewScreen.kt`
**Mecánica:** Aparecen fragmentos de código con bugs y tienes que decidir si "Aprobar" o "Solicitar cambios".

**Reglas:**
- 5 rondas por partida
- Cada ronda muestra un snippet con o sin bug
- Si aciertas, ganas puntos; si fallas, Codey se pone triste y pierdes puntos
- Puntuación determina recompensa

**Datos:** 20 snippets con bugs y 10 limpios, mezclados aleatoriamente.

**Mejoras técnicas:**
- `LazyColumn` con `key()` estable por snippet para evitar recomposiciones innecesarias.
- Resaltado de sintaxis con **Sora Editor** o `AnnotatedString` custom con regex para colorear palabras clave.
- Guardar historial de partidas en Room para estadísticas de % de aciertos por lenguaje.

---

## 9. Insignias por lenguaje
**Archivo:** `AchievementsRepository.kt` + `LearnScreen.kt`
**Mecánica:** Por cada lenguaje (Kotlin, JS, PHP, Python), desbloqueas insignias al completar:
- 50% de los retos → Insignia Bronce
- 75% → Insignia Plata
- 100% → Insignia Oro

**Mejoras técnicas:**
- `languageProgress` como tabla Room con relación uno-a-muchos (`Language` → `CompletedChallenge`).
- Insignias con `Badge` de Material 3 y animación de "unlock" usando `AnimatedVisibility` con `scaleIn + fadeIn`.
- Logros: `linguista_kotlin`, `linguista_js`, `linguista_php`, `linguista_python`

---

## 10. Editor de mascota (sliders RGB)
**Archivo:** Nuevo dialog o sección en `ShopScreen.kt`
**Mecánica:** Sliders para cambiar el color primario de Codey, su brillo, o su patrón.

**Mejoras técnicas:**
- `Slider` de Material 3 con preview en vivo del sprite.
- Guardar `accentColor` en DataStore Preferences, validando rango de color para no romper legibilidad.
- Costo: 30 bytes por cambio; considerar paletas preestablecidas además de sliders libres.

---

## 11. Música de fondo chiptune/lo-fi
**Archivo:** `SoundManager.kt` → `MusicManager.kt`
**Mecánica:** Pistas chiptune / lo-fi que se desbloquean con cada tema visual.

**Mejoras técnicas:**
- Reemplazar `MediaPlayer` por **Media3 ExoPlayer**, que maneja mejor ciclo de vida, buffering y foco de audio.
- Usar `MediaSession` de Media3 para exponer controles de música en segundo plano.
- Loop nativo con `player.repeatMode = Player.REPEAT_MODE_ONE`.
- Pausa automática al salir de la app.

---

## 12. GitHub Stats Sync
**Archivo:** Nuevo: `data/github/GitHubApi.kt`
**Mecánica:** Conecta tu cuenta de GitHub y Codey gana XP extra según tus contribuciones del día.

**Implementación:**
- Retrofit + **Kotlinx Serialization** para parsear la respuesta de la API de GitHub.
- XP bonus: commits * 5 + PRs * 10 + issues * 3
- Sincronización cada hora vía `CoroutineWorker` + `PeriodicWorkRequest` con constraint `NetworkType.CONNECTED`.
- **Importante:** el token NO debe guardarse con EncryptedSharedPreferences (deprecado); usar DataStore + Tink/Android Keystore.

---

## 13. Modo "No molestar"
**Archivo:** `FocusScreen.kt` + `PetViewModel.kt`
**Mecánica:** Si activas el modo foco del teléfono (o el modo estudio en la app), Codey entra en modo estudio y gana XP pasivo.

**Mejoras técnicas:**
- Evitar `AccessibilityService` (invasivo, sujeto a políticas estrictas de Play Store); usar `NotificationManager.getCurrentInterruptionFilter()` con permiso `ACCESS_NOTIFICATION_POLICY`.
- XP pasivo calculado con `CoroutineWorker` cada 5 min mientras la app está en foreground.
- Bonus: si el teléfono está en DND, XP x1.5

---

## 14. Hackatón semanal
**Archivo:** Nuevo: `data/WeeklyHackathon.kt`
**Mecánica:** Cada fin de semana (sábado-domingo) un desafío especial de 24h.

**Reglas:**
- Desafío único de algoritmo (ej: "Ordena este array sin usar sort()")
- 3 intentos máximos
- Recompensa: skin exclusiva temporal + 500 bytes + 200 XP
- Leaderboard local (mejor tiempo / menos intentos)

**Mejoras técnicas:**
- Leaderboard local con Room ordenado por `attempts ASC, timeMs ASC`.
- Programar activación/cierre con `CoroutineWorker` anclado a `Calendar`, resistente a reinicios del dispositivo.

---

## 15. Pair Programming (amigo virtual)
**Archivo:** Nuevo: `ui/components/PairBuddy.kt`
**Mecánica:** A veces aparece un segundo personaje (un amigo de Codey) y resuelven retos juntos.

**Implementación:**
- Personaje secundario: "Buggy" (una polilla amigable)
- Aparece aleatoriamente al completar retos
- Mientras está presente, los retos dan 1.5x XP
- Desaparece después de 3 retos o al cerrar la app

**Mejoras técnicas:**
- Sprite de Buggy en Lottie para animaciones ligeras (vuelo, parpadeo).
- Estado gestionado con `StateFlow<PairBuddyState>`, consumido con `collectAsStateWithLifecycle()`.

---

## 16. Skill Tree
**Archivo:** Nuevo: `feature/skills/SkillTreeScreen.kt`
**Mecánica:** Árbol de habilidades pasivas que se desbloquean con puntos de habilidad (ganados al subir de nivel).

**Habilidades:**
- Doble XP los domingos (3 niveles: 1.5x, 2x, 3x)
- Decaimiento -20% más lento (3 niveles: -10%, -20%, -30%)
- Descuento en tienda (3 niveles: 5%, 10%, 15%)
- Bonus en minijuegos (3 niveles: +10%, +20%, +30%)
- XP pasivo mientras offline (3 niveles: 1h, 2h, 4h de XP)
- Corazón extra (máximo 6 corazones en vez de 5)

**Mejoras técnicas:**
- Dibujar el árbol con `Canvas` de Compose (`drawLine` para conexiones, `drawCircle`/`Image` para nodos, `detectTapGestures` para selección).
- Guardar en Room (`skill_id`, `tier`, `unlocked`) para permitir prerequisitos entre nodos.

---

## 17. Pase de temporada
**Archivo:** Nuevo: `data/SeasonPass.kt`
**Mecánica:** Cada mes un pase con 20 niveles, recompensas gratis y premium.

**Recompensas gratis:** Bytes, XP, sombreros básicos, cartas de código
**Recompensas premium (compra única):** Skins exclusivas, sombreros raros, XP boost permanente

**Implementación:**
- DataStore: `seasonPassLevel`, `seasonPassXp`, `seasonPassPremium`
- 100 XP del pase por nivel, 20 niveles = 2000 XP total
- Cada 30 días se reinicia

**Mejoras técnicas:**
- Usar **Play Billing Library** si se planea monetizar el pase premium.
- `seasonPassXp` en Proto DataStore tipado, con reinicio automático vía `CoroutineWorker` anclado a fecha calendario.

---

## 18. Moodlet system
**Archivo:** `PetViewModel.kt` + `StatusCalculator.kt`
**Mecánica:** Eventos aleatorios que afectan el humor por horas.

**Eventos:**
- "Encontró un bug en producción" → triste 2h
- "Te vio usar tabs" → feliz 1h
- "Te vio usar espacios" → confundido 1h
- "Commit sin mensaje descriptivo" → decepcionado 2h
- "Código limpio detectado" → emocionado 3h
- "Café derramado sobre el teclado" → estresado 1h

**Mejoras técnicas:**
- Reemplazar el timer manual por `CoroutineWorker` con `PeriodicWorkRequest` de 30 min (5% de probabilidad de evento).
- `activeMoodlet` y `moodletExpiry` en DataStore Preferences.
- ViewportCard muestra el moodlet como badge.

---

## 19. Misiones semanales
**Archivo:** Nuevo: `data/WeeklyMissions.kt`
**Mecánica:** 3 misiones rotativas cada lunes.

**Ejemplos:**
- "Completa 5 retos de código" → 100 XP
- "Estudia 2 horas en modo foco" → 200 XP + 50 B
- "Gana 3 partidas de Bug Hunt" → 150 XP
- "Acaricia a Codey 10 veces" → 50 XP
- "Compra 3 items en la tienda" → 100 XP
- "Mantén a Codey feliz todo el día" → 250 XP

**Mejoras técnicas:**
- Room para `weeklyMissions` y `weeklyMissionsCompleted`, permitiendo histórico de misiones pasadas.
- Reset programado con `CoroutineWorker` anclado a lunes 00:00.
- Bonus por completar las 3: 500 XP + sombrero exclusivo.

---

## Notas técnicas generales (actualizadas)
- Todas las features nuevas deben seguir el patrón **MVVM + Hilt** para inyección de dependencias.
- Usar **Room** para datos relacionales/históricos y **DataStore (Preferences o Proto)** solo para configuraciones simples o valores únicos.
- Usar **WorkManager con CoroutineWorker** para tareas periódicas.
- **No usar EncryptedSharedPreferences** para nuevos secretos (token GitHub); migrar a DataStore + Tink/Android Keystore.
- Reemplazar `MediaPlayer` por **Media3 ExoPlayer** para toda reproducción de audio.
- Mantener compatibilidad con los 13 temas visuales y soporte para reduce-motion.
- Todas las UI deben estar en español, con `FontFamily.Monospace` para textos principales y estilo terminal.
- Considerar **Compose Canvas** para todo componente gráfico custom (skill tree, auras de evolución, gráficos de progreso).
