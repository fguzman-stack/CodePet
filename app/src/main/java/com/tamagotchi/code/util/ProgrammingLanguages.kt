package com.tamagotchi.code.util

import java.util.Locale

object ProgrammingLanguages {
    val ALL = listOf(
        "Kotlin",
        "JavaScript",
        "Python",
        "PHP",
        "Java",
        "TypeScript",
        "C#",
        "SQL",
        "C++",
        "Dart",
        "Swift",
        "Go",
        "Rust"
    )

    fun progressKey(language: String): String = when (language) {
        "JavaScript" -> "JS"
        "TypeScript" -> "TS"
        "C#" -> "CSHARP"
        "C++" -> "CPP"
        else -> language.uppercase(Locale.ROOT)
    }
}
