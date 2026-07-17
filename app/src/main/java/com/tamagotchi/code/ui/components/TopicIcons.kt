package com.tamagotchi.code.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.tamagotchi.code.R

fun topicIconRes(topic: String): Int = when {
    topic.contains("Kotlin", ignoreCase = true) -> R.drawable.ic_lang_kotlin
    topic.contains("JavaScript", ignoreCase = true) -> R.drawable.ic_lang_javascript
    topic.contains("Python", ignoreCase = true) -> R.drawable.ic_lang_python
    topic.contains("PHP", ignoreCase = true) -> R.drawable.ic_lang_php
    topic.contains("SQL", ignoreCase = true) -> R.drawable.ic_lang_sql
    topic.contains("Git", ignoreCase = true) -> R.drawable.ic_lang_git
    topic.contains("Clean Code", ignoreCase = true) || topic.contains("clean_code", ignoreCase = true) -> R.drawable.ic_lang_cleancode
    topic.contains("Estructura", ignoreCase = true) || topic.contains("Data", ignoreCase = true) || topic.contains("data_structures", ignoreCase = true) -> R.drawable.ic_lang_dataestructura
    else -> R.drawable.ic_lang_kotlin
}

@Composable
fun TopicIcon(topic: String, modifier: Modifier = Modifier) {
    val res = topicIconRes(topic)
    Icon(
        painter = painterResource(id = res),
        contentDescription = null,
        modifier = modifier
    )
}
