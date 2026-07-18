package com.tamagotchi.code.data

enum class QuestType(val displayName: String, val description: String, val rewardXp: Int, val rewardBytes: Int) {
    STUDY_KOTLIN("Estudia Kotlin", "Hoy quiero estudiar Kotlin!", 100, 30),
    GO_SHOP("Visita la tienda", "Llevame a la tienda a comprar algo!", 50, 20),
    PLAY_BUG_HUNT("Juega Bug Hunt", "Juguemos Bug Hunt, hace tiempo que no!", 80, 40),
    BUY_PIZZA("Compra pizza", "Tengo hambre, comprame una pizza!", 60, 0),
    ACARICIAR("Acariciame 3 veces", "Hoy necesito mucho cariño!", 40, 15),
    LIMPIAR("Limpia mi habitat", "Mi casa esta sucia, necesito que me limpies!", 50, 10),
    FOCUS_25("Foco 25 minutos", "Pongamos musica de focus y trabajemos 25 minutos!", 120, 50),
    COMPLETE_3_CHALLENGES("Completa 3 retos", "Hagamos ejercicios mentales!" , 150, 60)
}

data class ActiveQuest(
    val type: QuestType,
    val progress: Int,
    val target: Int,
    val expiresAt: Long,
    val startedAt: Long
)

fun getTargetForQuest(type: QuestType): Int = when (type) {
    QuestType.STUDY_KOTLIN -> 1
    QuestType.GO_SHOP -> 1
    QuestType.PLAY_BUG_HUNT -> 1
    QuestType.BUY_PIZZA -> 1
    QuestType.ACARICIAR -> 3
    QuestType.LIMPIAR -> 1
    QuestType.FOCUS_25 -> 1
    QuestType.COMPLETE_3_CHALLENGES -> 3
}
