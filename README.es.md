<div align="center">
  <img src="docs/assets/codey-hero.svg" width="1200" alt="Code Tamagotchi: Codey, un gato programador, dentro de una app Android con estilo terminal"/>

  <h1>Code Tamagotchi</h1>

  <p><strong>Cría a Codey mientras aprendes a programar.</strong></p>
  <p>Pomodoro, retos de código, juegos arcade y cuidado de mascota en una app Android local-first.</p>

  <p>
    <a href="https://github.com/fguzman-stack/CodePet/releases/latest"><img src="docs/assets/readme/cta-apk.svg" width="260" alt="Descargar APK"/></a>
    <a href="#capturas"><img src="docs/assets/readme/cta-screenshots.svg" width="260" alt="Ver capturas"/></a>
  </p>

  <img src="docs/assets/readme/status-pills.svg" width="760" alt="APK listo, licencia MIT, Android 7+, Kotlin y Compose UI"/>

  <p><a href="README.md">English</a> · <strong>Español</strong></p>
</div>

---

<div align="center">
  <a href="#capturas">Capturas</a> ·
  <a href="#funciones">Funciones</a> ·
  <a href="#instalacion">Instalación</a> ·
  <a href="#documentacion">Documentación</a> ·
  <a href="#contribuir">Contribuir</a>
</div>

---

## Qué Es

Code Tamagotchi convierte la práctica de programación en un ciclo de cuidado. Estudia con Pomodoro, resuelve retos de código, gana XP y Bytes, y usa esas recompensas para mantener a Codey sano, descansado y evolucionando.

<div align="center">
  <img src="docs/assets/readme/loop.svg" width="900" alt="Ciclo de estudiar, resolver, ganar, cuidar y desbloquear"/>
</div>

La app está construida para Android con Kotlin, Jetpack Compose, Room, DataStore, Koin y WorkManager, con Codey renderizado de forma procedural en vez de importado como sprites PNG.

<a id="capturas"></a>
## Capturas

Las capturas del proyecto viven en [`capturas_de_pantalla/`](capturas_de_pantalla/) y se muestran aquí en el mismo orden de la carpeta.

<div align="center">
<table>
<tr>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_1.jpeg"><img src="capturas_de_pantalla/captura_1.jpeg" width="220" alt="Pantalla principal de Codey con vida, saciedad, energía, XP y cuidados"/></a><br/><sub><strong>01</strong> · Inicio y cuidados</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_2.jpeg"><img src="capturas_de_pantalla/captura_2.jpeg" width="220" alt="Pantalla de reto normal de programación"/></a><br/><sub><strong>02</strong> · Retos de código</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_3.jpeg"><img src="capturas_de_pantalla/captura_3.jpeg" width="220" alt="Pantalla de reto especial de algoritmos"/></a><br/><sub><strong>03</strong> · Retos especiales</sub></td>
</tr>
<tr>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_4.jpeg"><img src="capturas_de_pantalla/captura_4.jpeg" width="220" alt="Pantalla de insignias por lenguaje y progreso"/></a><br/><sub><strong>04</strong> · Insignias por lenguaje</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_5.jpeg"><img src="capturas_de_pantalla/captura_5.jpeg" width="220" alt="Modo foco con selección de duración Pomodoro"/></a><br/><sub><strong>05</strong> · Modo foco</sub></td>
<td align="center" width="33%"><a href="capturas_de_pantalla/captura_6.jpeg"><img src="capturas_de_pantalla/captura_6.jpeg" width="220" alt="Pantalla de tienda y vestuario de Codey"/></a><br/><sub><strong>06</strong> · Tienda y vestuario</sub></td>
</tr>
</table>

<a href="capturas_de_pantalla/captura_7.jpeg"><img src="capturas_de_pantalla/captura_7.jpeg" width="260" alt="Tema Matrix Green en la pantalla principal de Codey"/></a><br/>
<sub><strong>07</strong> · Tema Matrix Green</sub>
</div>

<a id="funciones"></a>
## Funciones

<div align="center">
<table>
<tr>
<td width="50%"><img src="docs/assets/readme/card-pet-care.svg" width="420" alt="Cuidado de mascota: vida, saciedad, energía, XP, moodlets, cuidados y revivir"/></td>
<td width="50%"><img src="docs/assets/readme/card-learn.svg" width="420" alt="Aprende programando: 117 retos, 13 lenguajes, algoritmos y code review"/></td>
</tr>
<tr>
<td width="50%"><img src="docs/assets/readme/card-focus.svg" width="420" alt="Foco y recompensas: Pomodoro, XP, Bytes y modo No Molestar"/></td>
<td width="50%"><img src="docs/assets/readme/card-android.svg" width="420" alt="Sistemas Android: Room, DataStore, WorkManager, Compose Canvas y widget"/></td>
</tr>
</table>
</div>

## Datos Del Proyecto

<div align="center">
  <img src="docs/assets/readme/stats-panel.svg" width="900" alt="Estadísticas de CodePet: version 3.2.0, Android API 24+, 123 retos, 13 lenguajes, 12 temas y Room 7"/>
</div>

<a id="instalacion"></a>
## Instalación

### Solo Quiero Jugar

1. Descarga el APK desde [Releases](https://github.com/fguzman-stack/CodePet/releases/latest).
2. Instálalo en Android 7.0 o superior.
3. Nombra a Codey y empieza a estudiar.

### Quiero Compilarlo

```bash
git clone https://github.com/fguzman-stack/CodePet.git
cd CodePet
./gradlew :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

En Windows PowerShell, usa `./gradlew.bat`.

<a id="documentacion"></a>
## Documentación

El contenido largo del README fue dividido en guías enfocadas:

| Guía | Para qué sirve |
|:--|:--|
| [Primeros pasos](docs/GETTING_STARTED.md) | Compilar, instalar y ejecutar comandos. |
| [Gameplay](docs/GAMEPLAY.md) | Ciclo de juego, economía, temas y recompensas. |
| [Arquitectura](docs/ARCHITECTURE.md) | Stack, paquetes y cableado de la app. |
| [Persistencia](docs/PERSISTENCE.md) | Room, migraciones y DataStore. |
| [Renderizado](docs/RENDERING.md) | Codey, temas y widget. |
| [Pruebas](docs/TESTING.md) | Unit, Robolectric, lint y screenshots. |
| [Localización](docs/LOCALIZATION.md) | Recursos y contenido ES/EN. |
| [Roadmap](docs/ROADMAP.md) | Features listas e ideas próximas. |

La referencia histórica extensa en español sigue disponible en [`documentacion.md`](documentacion.md).

<a id="contribuir"></a>
## Contribuir

La forma más directa de ayudar es mejorar el contenido educativo:

- Agrega retos para lenguajes con bancos pequeños.
- Mejora explicaciones con la voz de Codey.
- Mantén sincronizadas las entradas en español e inglés.
- Reporta bugs de UI con capturas y detalles del dispositivo.
- Sugiere temas, prendas o recompensas de estudio.

Consulta [CONTRIBUTING.md](CONTRIBUTING.md) para la guía completa.

## Licencia

MIT. Úsalo, forkealo, adáptalo y publícalo; conserva el aviso de copyright. Ver [LICENSE](LICENSE).

<div align="center">
  <img src="docs/assets/codey-footer.svg" width="1200" alt="Ilustración final de Codey"/>
  <p><strong>Code Tamagotchi</strong> · donde los bugs se vuelven mascotas y el código se vuelve cuidado.</p>
  <p><sub>Hecho desde Chile por <a href="https://github.com/fguzman-stack">fguzman-stack</a>.</sub></p>
</div>
