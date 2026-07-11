# 🐾 Pet Assets — Code Tamagotchi

Estructura organizada de los diseños de la mascota.

## 📁 Estructura

```
pet/
├── normal/                          ← Estilo por defecto (2D ilustrado)
│   ├── static/                      ← Imágenes estáticas .png
│   │   ├── happy.png
│   │   ├── sleeping.png
│   │   ├── studying.png
│   │   ├── sick.png
│   │   ├── sad.png
│   │   ├── hungry.png
│   │   └── excited.png
│   └── animations/                  ← Animaciones .gif / .webp
│       ├── happy.gif
│       ├── sleeping.gif
│       ├── studying.gif
│       ├── sick.gif
│       ├── sad.gif
│       ├── hungry.gif
│       └── excited.gif
│
└── pixel_art/                       ← Estilo premium / secreto (pixel art retro)
    ├── static/                      ← Imágenes estáticas .png
    │   ├── happy.png
    │   ├── sleeping.png
    │   ├── studying.png
    │   ├── sick.png
    │   ├── sad.png
    │   ├── hungry.png
    │   └── excited.png
    └── animations/                  ← Animaciones .gif / .webp
        ├── happy.gif
        ├── sleeping.gif
        ├── studying.gif
        ├── sick.gif
        ├── sad.gif
        ├── hungry.gif
        └── excited.gif
```

## 🎨 Estilos

| Carpeta | Estilo | Propósito |
|:--------|:-------|:----------|
| `normal/` | 2D ilustrado vectorial | Apariencia por defecto de la mascota |
| `pixel_art/` | Pixel art retro 8-bit | Recompensa secreta / avatar premium |

## 🎭 Estados por imagen

| Archivo | Estado | Descripción |
|:--------|:-------|:------------|
| `happy.png` | HAPPY | Feliz, flotando suavemente, cola brillante |
| `sleeping.png` | SLEEPING | Durmiendo, acurrucado, Zzz |
| `studying.png` | STUDYING | Concentrado, monitor del pecho con código |
| `sick.png` | SICK | Enfermo, temblando, ojos 0xBAD |
| `sad.png` | SAD | Triste, orejas caídas, lágrima pixel |
| `hungry.png` | HUNGRY | Hambriento, pupilas $ |
| `excited.png` | EXCITED | Emocionado, saltando, cola con ondas |

## ⚙️ Carga desde código (Android)

Para cargar estos assets desde Kotlin:

```kotlin
// Desde static normal
val bitmap = context.assets.open("pet/normal/static/happy.png")

// Desde pixel art animado
val gif = context.assets.open("pet/pixel_art/animations/excited.gif")
```
