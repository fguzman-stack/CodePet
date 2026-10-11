# Persistence

[Back to README](../README.md) · [Docs index](README.md)

## Room

The database is `code_tamagotchi_db` in `data/database/AppDatabase.kt`. The current Room schema version is **7**.

Explicit migrations are registered for:

| Migration | Purpose |
|:--|:--|
| 1 -> 2 | Adds focus sessions. |
| 2 -> 3 | Adds rename state. |
| 3 -> 4 | Adds dead-state support. |
| 4 -> 5 | Adds cards, quests, owned items, language progress and logs. |
| 5 -> 6 | Adds newer gameplay persistence fields. |
| 6 -> 7 | Adds latest persisted state fields. |

## Entities

The schema includes pet state, study/focus sessions, code cards, quests, owned items, language progress and activity logs.

The pet is stored as a singleton row (`id = 1`) and exposed reactively through DAO flows.

## DataStore

DataStore-backed repositories keep preferences and lightweight progression state outside Room. This covers theme selection, onboarding, settings, cooldowns and several live systems that do not need relational queries.

## Migration rule

Any future Room entity or column change should increase the database version and add an explicit migration. `exportSchema` is currently disabled, so there is no schema directory to update.
