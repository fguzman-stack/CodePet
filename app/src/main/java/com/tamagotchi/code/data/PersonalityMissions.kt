package com.tamagotchi.code.data

// Display text for quests is resolved by enum name in the UI layer (R.string.quest_*)
enum class QuestType(val rewardXp: Int, val rewardBytes: Int) {
    STUDY_KOTLIN(100, 30),
    GO_SHOP(50, 20),
    PLAY_BUG_HUNT(80, 40),
    BUY_PIZZA(60, 0),
    ACARICIAR(40, 15),
    LIMPIAR(50, 10),
    FOCUS_25(120, 50),
    COMPLETE_3_CHALLENGES(150, 60)
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
