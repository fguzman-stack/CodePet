package com.tamagotchi.code.util

import kotlin.random.Random

object DailyCommitGenerator {

    fun generateCommit(activities: Set<String>, petName: String): String {
        if (activities.isEmpty()) {
            return Random.nextCommitMsg(
                listOf(
                    "docs: hoy no hice nada productivo",
                    "chore: día de procrastinación técnica",
                    "fix: sobreviví otro día sin crashear",
                    "style: no toqué nada pero me veo bien",
                    "refactor: moví archivos de lugar sin motivo aparente"
                )
            )
        }

        val hasStudied = activities.any { it.contains("study", ignoreCase = true) }
        val hasShopped = activities.any { it.contains("shop", ignoreCase = true) || it.contains("buy", ignoreCase = true) }
        val hasPlayed = activities.any { it.contains("play", ignoreCase = true) || it.contains("game", ignoreCase = true) }
        val hasSlept = activities.any { it.contains("sleep", ignoreCase = true) }
        val hasPetted = activities.any { it.contains("pet", ignoreCase = true) || it.contains("acarici", ignoreCase = true) }
        val hasClean = activities.any { it.contains("clean", ignoreCase = true) || it.contains("limpi", ignoreCase = true) }
        val hasChallenge = activities.any { it.contains("challenge", ignoreCase = true) || it.contains("reto", ignoreCase = true) }

        val activeCount = listOf(hasStudied, hasShopped, hasPlayed, hasSlept, hasPetted, hasClean, hasChallenge).count { it }

        return when {
            activeCount >= 3 -> Random.nextCommitMsg(
                listOf(
                    "feat: hoy fue un día completo, hasta hice merge",
                    "feat: commit masivo con varias funcionalidades",
                    "chore: $petName tuvo un día ocupado"
                )
            )
            hasStudied && hasPetted -> "feat: aprendí cosas nuevas mientras recibía cariño"
            hasStudied -> Random.nextCommitMsg(
                listOf(
                    "feat: aprendí cosas nuevas hoy",
                    "feat: $petName estudió y ganó XP",
                    "docs: sesión de estudio completada"
                )
            )
            hasShopped -> Random.nextCommitMsg(
                listOf(
                    "chore: gasté bytes en cosas innecesarias",
                    "chore: compras impulsivas en la tienda",
                    "feat: nuevos items adquiridos"
                )
            )
            hasPlayed -> Random.nextCommitMsg(
                listOf(
                    "refactor: me distraje un rato",
                    "feat: sesión de juegos completada",
                    "test: probé mis reflejos en los minijuegos"
                )
            )
            hasSlept -> Random.nextCommitMsg(
                listOf(
                    "fix: pausa activa para recargar baterías",
                    "fix: $petName durmió y recuperó energía",
                    "chore: modo ahorro de energía activado"
                )
            )
            hasPetted -> Random.nextCommitMsg(
                listOf(
                    "style: recibí cariño y eso mejora el código",
                    "feat: ++felicidad, --bugs",
                    "chore: mantenimiento emocional completado"
                )
            )
            hasClean -> "refactor: limpieza profunda del sistema"
            hasChallenge -> Random.nextCommitMsg(
                listOf(
                    "feat: desafío de código completado",
                    "fix: bugs eliminados con éxito",
                    "test: retos de programación superados"
                )
            )
            else -> Random.nextCommitMsg(
                listOf(
                    "chore: día normal, nada especial",
                    "docs: $petName estuvo tranquilo hoy",
                    "style: sin cambios aparentes"
                )
            )
        }
    }

    private fun Random.nextCommitMsg(messages: List<String>): String {
        return messages[this.nextInt(messages.size)]
    }
}