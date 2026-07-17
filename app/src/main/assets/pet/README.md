# 🐾 Code Tamagotchi Pet Assets

Este directorio contiene los recursos visuales de la mascota virtual, organizados por estilo y estado emocional.

---

## 🏗️ Organización de Carpetas

La arquitectura de archivos permite una escalabilidad sencilla para nuevos temas visuales.

| Carpeta | Estilo | Descripción |
| :--- | :--- | :--- |
| `normal/` | **Default 2D** | Estilo ilustrado moderno, resolución base 512px. |
| `pixel_art/` | **Retro 8-bit** | Estilo pixelado clásico para desbloqueables premium. |

Dentro de cada estilo encontrarás:
- `static/`: Imágenes en formato PNG de alta calidad.
- `animations/`: Archivos GIF/WebP para movimiento fluido.

---

## 🎭 Matriz de Estados

Cada estado representa una necesidad o emoción de la mascota:

| Icono | Estado | Archivo | Significado Visual |
| :---: | :--- | :--- | :--- |
| <img src="normal/static/mascota_happy.png" width="32"> | **HAPPY** | `mascota_happy.png` | Flotando, con rubor y sonrisa. |
| <img src="normal/static/mascota_sleeping.png" width="32"> | **SLEEPING** | `mascota_sleeping.png` | Burbuja de sueño (Zzz) y ojos cerrados. |
| <img src="normal/static/mascota_studying.png" width="32"> | **STUDYING** | `mascota_studying.png` | Usando gafas o con un libro/laptop. |
| <img src="normal/static/mascota_sick.png" width="32"> | **SICK** | `mascota_sick.png` | Tono verdoso o termómetro. |
| <img src="normal/static/mascota_sad.png" width="32"> | **SAD** | `mascota_sad.png` | Mirada baja y lagrimitas. |
| <img src="normal/static/mascota_hungry.png" width="32"> | **HUNGRY** | `mascota_hungry.png` | Pensando en comida o con tenedor/cuchillo. |
| <img src="normal/static/mascota_excited.png" width="32"> | **EXCITED** | `mascota_excited.png` | Saltando con estrellas en los ojos. |

---

## ⚙️ Guía de Contribución

Si deseas agregar un nuevo estilo (ej. *Cyberpunk* o *Kawaii*):
1. Crea una nueva subcarpeta en `pet/`.
2. Mantén la estructura `static/` y `animations/`.
3. Asegúrate de incluir los 7 estados básicos con los nombres de archivo exactos.
4. Las imágenes deben tener el fondo transparente (Alpha channel).

---
> 🚀 **Tip:** Usa el estado `EXCITED` para las animaciones de recompensa tras completar un desafío de código.
