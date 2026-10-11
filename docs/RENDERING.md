# Rendering

[Back to README](../README.md) · [Docs index](README.md)

## Codey

Codey is drawn procedurally. The project does not rely on pet PNG sprites for the in-app pet.

Main rendering files:

- `ui/components/CodeySprite.kt`
- `ui/components/ProgrammerCatRenderer.kt`
- `ui/components/CodeyPixelArt.kt`
- `ui/components/CodeyBitmap.kt`

The rendered mood maps from persisted status strings into `PetMood` values such as `HAPPY`, `SLEEPING`, `STUDYING`, `SICK`, `HUNGRY`, `SAD`, `EXCITED` and `DEAD`.

## Themes

`ui/theme/ThemeConfig.kt` defines the visual themes. `ui/components/AnimatedThemeBackground.kt` renders animated backgrounds such as Matrix Green, Cyberpunk, Sakura and Retro Pixel.

Theme names currently include:

`Default`, `Matrix Green`, `Galáctico`, `Cyberpunk`, `Sakura`, `Minimalista`, `Neón`, `Océano`, `Volcánico`, `Japonés`, `Aurora`, `Nocturno`, `Retro Pixel`.

## Widget

The Android launcher widget cannot run Compose directly, so it renders Codey into a bitmap via `CodeyBitmap.kt` and places it in `RemoteViews`. The widget shows pet status and meters without opening the app.
