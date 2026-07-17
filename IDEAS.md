# Ideas para CodePet — Plan de Expansión

## Estado actual
- [x] Easter eggs ocultos (toques rápidos → animación secreta)
- [ ] Recordatorios con personalidad (notificaciones humorísticas)
- [ ] Codey escribe commits (resumen diario)
- [ ] Cartas de código (datos curiosos/chistes)
- [ ] Misiones de personaje (Codey pide actividades)
- [ ] Evolución visual por nivel (5, 10, 25, 50)
- [ ] Colección de sombreros en la tienda
- [ ] Code Review (minijuego)
- [ ] Insignias por lenguaje (50%, 75%, 100%)
- [ ] Editor de mascota (sliders RGB)
- [ ] Música de fondo chiptune/lo-fi
- [ ] GitHub Stats Sync
- [ ] Modo No molestar
- [ ] Hackatón semanal
- [ ] Pair Programming (amigo virtual)
- [ ] Skill Tree / Pase de temporada / Misiones semanales

---

# 🎮 CodePet — Plan de Expansión

## 1. Easter eggs ocultos
**Estado:** ✅ Implementado
**Archivo:** `ViewportCard.kt`
**Mecánica:** Si tocas a Codey 5 veces en menos de 2 segundos, aparece una animación secreta con un mensaje humorístico ("404: Personalidad no encontrada") con escala y rotación.
**Ideas adicionales:**
- Diferentes easter eggs aleatorios (breakdance, gafas de sol, emoji de fuego)
- Sonido especial al activarse
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

---

## 3. Codey escribe commits
**Archivo:** `PetViewModel.kt` + nuevo método
**Mecánica:** Al cerrar la app o al final del día, Codey genera un "commit message" gracioso resumiendo lo que pasó.
**Implementación:**
- Guardar un "commit log" diario en DataStore
- Al cerrar app, generar un mensaje basado en las actividades del día
- Mostrar en un dialog al abrir la app al día siguiente
- Mensajes según actividad:
  - Estudió: "feat: aprendí cosas nuevas hoy"
  - Compró: "chore: gasté bytes en cosas innecesarias"
  - Jugó: "refactor: me distraje un rato"
  - Durmió: "fix: pausa activa para recargar baterías"
  - Sin actividad: "docs: hoy no hice nada productivo"

---

## 3. Cartas de código
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

---

## 4. Misiones de personaje
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

**Implementación:**
- DataStore para guardar misión activa
- Timer que asigna nueva misión cada X horas
- ViewModel: `activeQuest`, `questProgress`, `questReward`
- UI: Badge en ViewportCard indicando misión activa
- Al completar: dialog con celebración y recompensa extra

---

## 5. Evolución visual de Codey por nivel
**Archivo:** `AnimatedPetSprite.kt` + recursos drawable
**Mecánica:** Codey cambia de sprite según el nivel:
- Nivel 1-4: Huevo / Cachorro (actual)
- Nivel 5-9: Cría / Adolescente
- Nivel 10-24: Adulto
- Nivel 25-49: Veterano
- Nivel 50+: Legendario

**Implementación:**
- Nuevos drawables: `mascota_happy_lv5`, `mascota_sleeping_lv5`, etc.
- O usar overlay/efectos visuales (brillo, aura, tamaño) según nivel
- ViewModel expone `evolutionStage` basado en nivel
- AnimatedPetSprite recibe `evolutionStage` y renderiza diferente

---

## 5. Colección de sombreros en la tienda
**Archivo:** `ShopScreen.kt` + `PetStateEntity.kt` + `AnimatedPetSprite.kt`
**Mecánica:** La tienda vende sombreros que Codey puede usar.
**Sombreros:**
- Gorro de programador (50 B)
- Birrete de graduación (100 B)
- Casco VR (150 B)
- Sombrero de chef (80 B)
- Corona de rey del código (500 B)
- Gorro de navidad (estacional, 30 B)

**Implementación:**
- `PetStateEntity` nuevo campo: `hat: String = "none"`
- ShopScreen: nueva sección de sombreros
- AnimatedPetSprite: dibujar overlay del sombrero según `hat`
- DataStore para persistir

---

## 6. Code Review (nuevo minijuego)
**Archivo nuevo:** `feature/games/CodeReviewScreen.kt`
**Mecánica:** Aparecen fragmentos de código con bugs y tienes que decidir si "Aprobar" o "Solicitar cambios".
**Reglas:**
- 5 rondas por partida
- Cada ronda muestra un snippet con o sin bug
- Si aciertas (detectas bug o código limpio), ganas puntos
- Si fallas, Codey se pone triste y pierdes puntos
- Puntuación determina recompensa

**Datos:** 20 snippets con bugs y 10 limpios, mezclados aleatoriamente.

---

## 7. Insignias por lenguaje
**Archivo:** `AchievementsRepository.kt` + `LearnScreen.kt`
**Mecánica:** Por cada lenguaje (Kotlin, JS, PHP, Python), desbloqueas insignias al completar:
- 50% de los retos → Insignia Bronce
- 75% → Insignia Plata
- 100% → Insignia Oro

**Implementación:**
- DataStore: `languageProgress: Map<String, Int>` (retos completados por lenguaje)
- ViewModel: `getLanguageProgress()` calcula porcentaje
- LearnScreen: mostrar insignias en la cabecera
- Logros: `linguista_kotlin`, `linguista_js`, `linguista_php`, `linguista_python`

---

## 8. Editor de mascota (sliders RGB)
**Archivo:** Nuevo dialog o sección en ShopScreen
**Mecánica:** Sliders para cambiar el color primario de Codey, su brillo, o su patrón.
**Implementación:**
- `PetStateEntity` nuevo campo: `accentColor: Long = defaultColor`
- Dialog con 3 sliders (R, G, B) + preview
- AnimatedPetSprite usa el color para tintar la mascota
- Costo: 30 bytes por cambio

---

## 9. Música de fondo chiptune/lo-fi
**Archivo:** `SoundManager.kt` → `MusicManager.kt`
**Mecánica:** Pistas chiptune / lo-fi que se desbloquean con cada tema visual.
**Implementación:**
- Usar `MediaPlayer` con archivos .mp3 en res/raw
- Cada tema visual tiene su propia pista
- Control de volumen en settings
- Loop infinito mientras la app está en foreground
- Pausa al salir de la app

---

## 10. GitHub Stats Sync
**Archivo:** Nuevo: `data/github/GitHubApi.kt`
**Mecánica:** Conecta tu cuenta de GitHub y Codey gana XP extra según tus contribuciones del día.
**Implementación:**
- Pantalla de configuración con input de token/username
- Llamada a GitHub API (contributions today)
- XP bonus: commits * 5 + PRs * 10 + issues * 3
- Sincronización cada hora via WorkManager
- Almacenar token de forma segura (EncryptedSharedPreferences)

---

## 11. Modo "No molestar"
**Archivo:** `FocusScreen.kt` + `PetViewModel.kt`
**Mecánica:** Si activas el modo foco en el teléfono (o el modo estudio en la app), Codey automáticamente entra en modo estudio y gana XP pasivo.
**Implementación:**
- Detectar `NotificationListenerService` o `AccessibilityService` para DND
- Alternativa más simple: al iniciar FocusScreen, Codey gana XP pasivo cada 5 min
- Bonus: si el teléfono está en DND, XP x1.5

---

## 12. Hackatón semanal
**Archivo:** Nuevo: `data/WeeklyHackathon.kt`
**Mecánica:** Cada fin de semana (sábado-domingo) un desafío especial de 24h.
**Reglas:**
- Desafío único de algoritmo (ej: "Ordena este array sin usar sort()")
- 3 intentos máximos
- Recompensa: skin exclusiva temporal + 500 bytes + 200 XP
- Leaderboard local (mejor tiempo / menos intentos)

---

## 13. Pair Programming (amigo virtual)
**Archivo:** Nuevo: `ui/components/PairBuddy.kt`
**Mecánica:** A veces aparece un segundo personaje (un amigo de Codey) y resuelven retos juntos.
**Implementación:**
- Personaje secundario: "Buggy" (una polilla amigable)
- Aparece aleatoriamente al completar retos
- Mientras está presente, los retos dan 1.5x XP
- Desaparece después de 3 retos o al cerrar la app
- Tiene su propio sprite y animaciones

---

## 14. Skill Tree
**Archivo:** Nuevo: `feature/skills/SkillTreeScreen.kt`
**Mecánica:** Árbol de habilidades pasivas que se desbloquean con puntos de habilidad (ganados al subir de nivel).
**Habilidades:**
- Doble XP los domingos (3 niveles: 1.5x, 2x, 3x)
- Decaimiento -20% más lento (3 niveles: -10%, -20%, -30%)
- Descuento en tienda (3 niveles: 5%, 10%, 15%)
- Bonus en minijuegos (3 niveles: +10%, +20%, +30%)
- XP pasivo mientras offline (3 niveles: 1h, 2h, 4h de XP)
- Corazón extra (máximo 6 corazones en vez de 5)

---

## 14. Pase de temporada
**Archivo:** Nuevo: `data/SeasonPass.kt`
**Mecánica:** Cada mes un pase con 20 niveles, recompensas gratis y premium.
**Recompensas gratis:** Bytes, XP, sombreros básicos, cartas de código
**Recompensas premium (compra única):** Skins exclusivas, sombreros raros, XP boost permanente
**Implementación:**
- DataStore: `seasonPassLevel`, `seasonPassXp`, `seasonPassPremium`
- XP del pase se gana con actividades normales
- 100 XP del pase por nivel, 20 niveles = 2000 XP total
- Cada 30 días se reinicia

---

## 15. Moodlet system
**Archivo:** `PetViewModel.kt` + `StatusCalculator.kt`
**Mecánica:** Eventos aleatorios que afectan el humor por horas.
**Eventos:**
- "Encontró un bug en producción" → triste 2h
- "Te vio usar tabs" → feliz 1h
- "Te vio usar espacios" → confundido 1h
- "Commit sin mensaje descriptivo" → decepcionado 2h
- "Código limpio detectado" → emocionado 3h
- "Café derramado sobre el teclado" → estresado 1h

**Implementación:**
- Timer cada 30 min para evento aleatorio (5% de probabilidad)
- DataStore: `activeMoodlet: String?`, `moodletExpiry: Long`
- ViewportCard muestra el moodlet como badge

---

## 16. Misiones semanales
**Archivo:** Nuevo: `data/WeeklyMissions.kt`
**Mecánica:** 3 misiones rotativas cada lunes.
**Ejemplos:**
- "Completa 5 retos de código" → 100 XP
- "Estudia 2 horas en modo foco" → 200 XP + 50 B
- "Gana 3 partidas de Bug Hunt" → 150 XP
- "Acaricia a Codey 10 veces" → 50 XP
- "Compra 3 items en la tienda" → 100 XP
- "Mantén a Codey feliz todo el día" → 250 XP

**Implementación:**
- DataStore: `weeklyMissions: List<Mission>`, `weeklyMissionsCompleted: Set<Int>`
- Reset cada lunes
- Bonus por completar las 3: 500 XP + sombrero exclusivo

---

## 17. Codey escribe commits
**Archivo:** `PetViewModel.kt` + nuevo dialog
**Mecánica:** Al cerrar la app o al final del día, Codey genera un "commit message" gracioso resumiendo lo que pasó.
**Mensajes según actividad:**
- Estudió: "feat: aprendí cosas nuevas hoy"
- Compró: "chore: gasté bytes en cosas innecesarias"
- Jugó: "refactor: me distraje un rato"
- Durmió: "fix: pausa activa para recargar baterías"
- Sin actividad: "docs: hoy no hice nada productivo"
- Acarició: "style: recibí cariño y eso mejora el código"
- Varias actividades: "feat: hoy fue un día completo, hasta hice merge"

**Implementación:**
- DataStore: `dailyActivityLog: List<String>` (actividades del día)
- ViewModel: `generateDailyCommit()` al cerrar app
- Mostrar en dialog al abrir la app al día siguiente
- Commit messages aleatorios con formato git

---

## 18. Skill Tree (detallado)
**Ver sección 14 arriba.** Implementación con UI de árbol visual usando Compose Canvas.

---

## 19. Pase de temporada (detallado)
**Ver sección 15 arriba.** Implementación con barra de progreso y 20 niveles.

---

## 20. Misiones semanales (detallado)
**Ver sección 16 arriba.** Implementación con 3 misiones rotativas.

---

## Notas técnicas generales
- Todas las features nuevas deben seguir el patrón MVVM existente
- Usar DataStore para persistencia de nuevas features
- Usar WorkManager para tareas periódicas (hackatón, misiones semanales)
- Mantener compatibilidad con los 13 temas visuales
- Todas las UI deben estar en español
- Seguir el estilo monospace/terminal existente
- Usar `FontFamily.Monospace` para textos principales
- Mantener soporte para reduce-motion
