# Mascota de CodePet

La fuente del dibujo es `gato-programador.svg`, en la raíz del proyecto. Incluye
cinco gatos; sus tarjetas, flechas y rótulos solo sirven como referencia y no se
dibujan en la aplicación.

## Actualizar el arte

1. Editar el SVG original conservando los cinco grupos de mascotas.
2. Ejecutar desde la raíz: `python tools/generate_cat_art.py`.
3. El generador conserva los trazos, degradados y accesorios en
   `ProgrammerCatArt.kt`, y deriva los iconos vectoriales de la cabeza del gatito.
4. No editar a mano el archivo de arte generado ni agregar PNG de mascotas.

## Renderizado y animación

- `CatSvgRenderer.kt` interpreta los elementos del SVG y guarda las capas como
  `Picture` de Android. El SVG se procesa una sola vez, no en cada fotograma.
- `ProgrammerCatRenderer.kt` coloca las capas, adapta ojos y boca al estado, anima
  la cola y los accesorios, y dibuja la ropa sobre el torso detrás de la cabeza.
- `CodeySprite.kt` conecta los eventos de aprendizaje, caricias y evolución. Con
  movimiento reducido se usa una pose estática; la muerte siempre está congelada.
- `CodeyBitmap.kt` usa el mismo renderizador para el widget.
- `CodeyPixelArt.kt` aplica una paleta del gato y escala por vecino más próximo.
  Su caché acotada conserva las poses discretas y el efecto de evolución.

Las cinco etapas se desbloquean en los niveles de experiencia 1, 5, 10, 25 y 50.

## Verificación

`ProgrammerCatArtworkTest` verifica la pose original, los umbrales, el parpadeo,
los límites de dibujo y la muerte estática en ambos modos. Las galerías para
revisión se generan en `app/build/reports/cat/normal.png` y `retro.png`.
`WardrobeRendererTest` revisa todas las prendas y genera galerías en
`app/build/reports/wardrobe/`.

Esta carpeta queda como referencia histórica; no se cargan imágenes de mascota
desde ella.
