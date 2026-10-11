# Gameplay

[Back to README](../README.md) · [Docs index](README.md)

## Core loop

```text
Study -> solve challenges -> earn XP and Bytes -> care for Codey -> unlock more
```

CodePet turns study consistency into a pet-care loop. Pomodoro sessions and programming challenges feed progression; care actions keep health, satiety and energy under control.

## Codey

Codey tracks health, satiety, energy, XP, level, streak and Bytes. The current state changes how the pet looks and moves.

| State | Trigger |
|:--|:--|
| `HAPPY` | Balanced stats |
| `SLEEPING` | Rest mode is enabled |
| `STUDYING` | A focus session is active |
| `SICK` | Health is low or critical |
| `HUNGRY` | Satiety is low |
| `SAD` | Energy is low |
| `EXCITED` | Happiness peaks during rewards or interactions |
| `DEAD` | The rendered mood used when the persisted dead flag is active |

## Learning content

The current static challenge bank contains **117 regular challenges** plus **6 special challenges**.

| Language | Regular challenges |
|:--|--:|
| Python | 35 |
| Kotlin | 19 |
| JavaScript | 18 |
| PHP | 18 |
| C# | 3 |
| C++ | 3 |
| Dart | 3 |
| Go | 3 |
| Java | 3 |
| Rust | 3 |
| SQL | 3 |
| Swift | 3 |
| TypeScript | 3 |

There are also code review snippets and trivia cards for extra practice.

## Focus mode

The Pomodoro screen supports 15, 25 and 50 minute sessions. Completed sessions award XP and Bytes, and longer sessions add bonuses.

## Arcade

The game modes include Bug Hunt, Git Rescue, Refactor Rush, Code Review, Hackathon and classic arcade-style minigames. Cooldowns prevent farming rewards without studying.

## Themes and progression

The app includes 12 visual themes, several progression systems and unlockable rewards:

- Daily rewards and streak progress.
- Skill Tree with 3 branches and 9 nodes.
- Season Pass with 20 levels.
- Weekly Missions reset on Monday.
- Moodlets and the Buggy pair-programming buddy.
- Theme unlocks through special challenges.
