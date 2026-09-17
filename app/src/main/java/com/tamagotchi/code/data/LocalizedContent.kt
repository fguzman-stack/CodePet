package com.tamagotchi.code.data

import java.util.Locale

data class ChallengeTextsEn(
    val language: String,
    val title: String,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val explanation: String
)

fun isEnglishContent(): Boolean = Locale.getDefault().language.equals("en", ignoreCase = true)

fun CodingChallenge.localized(): CodingChallenge {
    if (!isEnglishContent()) return this
    val en = ChallengesDataEn.translations[id]
        ?: SpecialChallengesDataEn.translations[id]
        ?: return this
    return copy(
        language = en.language,
        title = en.title,
        question = en.question,
        codeSnippet = en.codeSnippet ?: codeSnippet,
        options = en.options,
        explanation = en.explanation
    )
}

data class ReviewTextsEn(val code: String, val explanation: String)

fun CodeReviewSnippet.localized(): CodeReviewSnippet {
    if (!isEnglishContent()) return this
    val en = CodeReviewDataEn.translations[id] ?: return this
    return copy(code = en.code, explanation = en.explanation)
}

fun CodeCard.localized(): CodeCard {
    if (!isEnglishContent()) return this
    val en = CodeCardsEn.texts[id] ?: return this
    return copy(text = en)
}
