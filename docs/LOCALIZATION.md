# Localization

[Back to README](../README.md) · [Docs index](README.md)

CodePet supports Spanish and English.

## UI strings

Android resources live under `app/src/main/res/values*`. New user-visible strings should be added to resources instead of being hardcoded in UI code.

## Static learning content

Spanish source data lives in files such as:

- `ChallengesData.kt`
- `SpecialChallengesData.kt`
- `CodeReviewData.kt`
- `CodeCards.kt`

English mirrors live beside them:

- `ChallengesDataEn.kt`
- `SpecialChallengesDataEn.kt`
- `CodeReviewDataEn.kt`
- `CodeCardsEn.kt`

The app resolves translations at display time through stable IDs. The database stores progress, not translated text.

## Notes

- Notification channel labels are created by Android the first time channels are registered.
- Theme names are persisted by name, so renaming a theme needs care.
- `LocalizedContentTest` checks Spanish/English parity for learning content.
