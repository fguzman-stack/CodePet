# 🐾 Code Tamagotchi Pet Assets

Este directorio ya **no contiene imágenes de la mascota**. Codey ahora se dibuja 100% por código
(Jetpack Compose `Canvas`) desde `app/src/main/java/com/tamagotchi/code/ui/components/CodeySprite.kt`,
parametrizado por la etapa de evolución (`PetEvolutionStage`: Egg, Child, Adult, Veteran, Legendary)
derivada del nivel y el ánimo (`currentStatus` + `isDead` de `PetStateEntity`, en `PetViewModel`).
El widget de pantalla de inicio reutiliza el mismo renderizador vía
`CodeyBitmap.kt` (`RemoteViews.setImageViewBitmap`), por lo que tampoco necesita PNG.

---

## 🎭 Matriz de estados (referencia de diseño)

La apariencia de cada ánimo se define en el código, no en archivos de imagen:

| Estado | Acento | Señal visual |
| :--- | :--- | :--- |
| **HAPPY** | `#4DE0C4` | Ojos y boca en arco, rebote suave. |
| **SLEEPING** | `#4D8FD1` | Ojos y boca como líneas, brillo del núcleo bajo. |
| **STUDYING** | `#52E07A` | Ojos `<` concentrados. |
| **SICK** | `#EF5D5D` | Ojos `X`, boca ondulada, gota verde flotando. |
| **HUNGRY** | `#F0B84C` | Ojos circulares, anillo pulsante alrededor del cuerpo. |
| **SAD** | `#4D7EA8` | Ojos caídos, lagrimita azul, postura inclinada. |
| **EXCITED** | `#F4C94D` | Ojos estrella, chispas orbitando, rebote rápido. |
| **DEAD** | `#3C454D` | Núcleo apagado, ojos `X` grises, cuerpo tumbado junto a una lápida, sin animación. |

---

## ⚙️ Guía de Contribución

1. **No agregar PNG de la mascota**: cualquier cambio visual de Codey se hace editando
   `CodeySprite.kt` (diseño) y `CodeyRendererTest.kt` (verificación de las 40 combinaciones
   etapa/ánimo, que no se recorte y que DEAD esté congelado).
2. Esta carpeta queda reservada únicamente para assets experimentales o de referencia; las
   subcarpetas `static/` y `animations/` se conservan vacías con `.gitkeep` por compatibilidad
   histórica.
3. Las capturas de referencia del renderizado procedural viven en
   `app/src/test/screenshots/codey.png` (generada por `CodeyRendererTest`).
