# Pet Assets — Code Tamagotchi

Estructura organizada de los diseños de la mascota.

## Estructura

```
pet/
├── normal/                          ← Estilo por defecto (2D ilustrado)
│   ├── static/                      ← Imágenes estáticas .png
│   │   ├── mascota_happy.png
│   │   ├── mascota_sleeping.png
│   │   ├── mascota_studying.png
│   │   ├── mascota_sick.png
│   │   ├── mascota_sad.png
│   │   ├── mascota_hungry.png
│   │   └── mascota_excited.png
│   └── animations/                  ← Animaciones .gif / .webp
│       ├── mascota_happy.gif
│       ├── mascota_sleeping.gif
│       ├── mascota_studying.gif
│       ├── mascota_sick.gif
│       ├── mascota_sad.gif
│       ├── mascota_hungry.gif
│       └── mascota_excited.gif
│
└── pixel_art/                       ← Estilo premium / secreto (pixel art retro)
    ├── static/                      ← Imágenes estáticas .png
    │   ├── mascota_happy.png
    │   ├── mascota_sleeping.png
    │   ├── mascota_studying.png
    │   ├── mascota_sick.png
    │   ├── mascota_sad.png
    │   ├── mascota_hungry.png
    │   └── mascota_excited.png
    └── animations/                  ← Animaciones .gif / .webp
        ├── mascota_happy.gif
        ├── mascota_sleeping.gif
        ├── mascota_studying.gif
        ├── mascota_sick.gif
        ├── mascota_sad.gif
        ├── mascota_hungry.gif
        └── mascota_excited.gif
```

## Estados

| Archivo | Estado | Descripción |
|:--------|:-------|:------------|
| `mascota_happy.png` | HAPPY | Feliz, flotando suavemente |
| `mascota_sleeping.png` | SLEEPING | Durmiendo, acurrucado |
| `mascota_studying.png` | STUDYING | Concentrado estudiando |
| `mascota_sick.png` | SICK | Enfermo, temblando |
| `mascota_sad.png` | SAD | Triste, baja energía |
| `mascota_hungry.png` | HUNGRY | Hambriento |
| `mascota_excited.png` | EXCITED | Emocionado, saltando |
