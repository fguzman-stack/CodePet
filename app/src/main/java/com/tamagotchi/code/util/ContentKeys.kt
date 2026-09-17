package com.tamagotchi.code.util

import com.tamagotchi.code.R

// Canonical, locale-independent keys for user preferences that used to be stored as
// localized display text. Legacy localized values are normalized on read so existing
// installs keep working after switching device language.
object TopicKey {
    const val KOTLIN = "Kotlin"
    const val JAVASCRIPT = "JavaScript"
    const val PYTHON = "Python"
    const val PHP = "PHP"
    const val SQL = "SQL"
    const val GIT = "Git"
    const val CLEAN_CODE = "Clean Code"
    const val DATA_STRUCTURES = "Data Structures"

    val ALL = listOf(KOTLIN, JAVASCRIPT, PYTHON, PHP, SQL, GIT, CLEAN_CODE, DATA_STRUCTURES)

    fun normalize(raw: String): String = when (raw.trim().lowercase()) {
        "kotlin" -> KOTLIN
        "javascript" -> JAVASCRIPT
        "python" -> PYTHON
        "php" -> PHP
        "sql" -> SQL
        "git" -> GIT
        "clean code", "clean_code" -> CLEAN_CODE
        "data structures", "estructuras de datos", "data_structures" -> DATA_STRUCTURES
        else -> raw.trim()
    }

    fun displayRes(key: String): Int = when (normalize(key)) {
        KOTLIN -> R.string.topic_kotlin
        JAVASCRIPT -> R.string.topic_javascript
        PYTHON -> R.string.topic_python
        PHP -> R.string.topic_php
        SQL -> R.string.topic_sql
        GIT -> R.string.topic_git
        CLEAN_CODE -> R.string.topic_clean_code
        DATA_STRUCTURES -> R.string.topic_data_structures
        else -> R.string.topic_kotlin
    }
}

object DifficultyKey {
    const val BEGINNER = "BEGINNER"
    const val INITIAL = "INITIAL"
    const val INTERMEDIATE = "INTERMEDIATE"
    const val MIXED = "MIXED"

    val ALL = listOf(BEGINNER, INITIAL, INTERMEDIATE, MIXED)

    fun normalize(raw: String): String = when (raw.trim().lowercase()) {
        BEGINNER.lowercase(), "principiante" -> BEGINNER
        INITIAL.lowercase(), "inicial" -> INITIAL
        INTERMEDIATE.lowercase(), "intermedia" -> INTERMEDIATE
        MIXED.lowercase(), "mixta" -> MIXED
        else -> raw.trim()
    }

    fun displayRes(key: String): Int = when (normalize(key)) {
        BEGINNER -> R.string.settings_lang_diff_beginner
        INITIAL -> R.string.settings_lang_diff_initial
        INTERMEDIATE -> R.string.settings_lang_diff_intermediate
        MIXED -> R.string.settings_lang_diff_mixed
        else -> R.string.settings_lang_diff_initial
    }
}
