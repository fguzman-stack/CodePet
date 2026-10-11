package com.tamagotchi.code.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tamagotchi.code.data.repository.FocusSessionCompletion
import com.tamagotchi.code.util.DecayCalculator
import com.tamagotchi.code.util.LevelCalculator
import com.tamagotchi.code.util.RewardCalculator
import com.tamagotchi.code.util.StatusCalculator
import com.tamagotchi.code.util.ProgrammingLanguages
import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.PetWardrobe
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    fun getPetState(): Flow<PetStateEntity?>

    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    suspend fun getPetStateSuspend(): PetStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePetState(state: PetStateEntity)

    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySessionEntity)

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC LIMIT 1")
    fun getLatestFocusSession(): Flow<FocusSessionEntity?>

    @Query("SELECT * FROM focus_sessions WHERE status = 'RUNNING' ORDER BY id DESC LIMIT 1")
    suspend fun getActiveFocusSession(): FocusSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSessionEntity): Long

    @Query("SELECT * FROM focus_sessions WHERE id = :sessionId")
    suspend fun getFocusSession(sessionId: Long): FocusSessionEntity?

    @Query("UPDATE focus_sessions SET status = :status WHERE id = :sessionId AND status = 'RUNNING'")
    suspend fun updateFocusSessionStatus(sessionId: Long, status: String): Int

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC")
    fun getAllFocusSessions(): Flow<List<FocusSessionEntity>>

    @Transaction
    suspend fun startFocusSession(session: FocusSessionEntity): FocusSessionEntity? {
        require(session.plannedDurationMinutes in 1..180)
        require(session.xpMultiplier == 1f || session.xpMultiplier == 1.5f)
        getActiveFocusSession()?.let { return it }
        val current = getPetStateSuspend() ?: return null
        val pet = DecayCalculator.applyDecay(current, session.startedAt)
        if (pet.health <= 0f) {
            insertOrUpdatePetState(pet)
            return null
        }
        val saved = session.copy(id = insertFocusSession(session))
        insertOrUpdatePetState(pet.copy(currentStatus = "STUDYING"))
        return saved
    }

    @Transaction
    suspend fun cancelFocusSession(sessionId: Long, now: Long): Boolean {
        val session = getFocusSession(sessionId) ?: return false
        if (session.status != "RUNNING") return false
        val current = requireNotNull(getPetStateSuspend()) { "Active focus session has no pet" }
        val pet = DecayCalculator.applyDecay(current, now)
        if (updateFocusSessionStatus(sessionId, "CANCELLED") != 1) return false
        insertOrUpdatePetState(pet.copy(
            currentStatus = StatusCalculator.determineStatus(
                pet.health, pet.hunger, pet.energy,
                isSleeping = pet.currentStatus == "SLEEPING", isStudying = false,
                isDead = pet.isDead
            )
        ))
        return true
    }

    @Transaction
    suspend fun completeFocusSession(sessionId: Long, now: Long): FocusSessionCompletion? {
        val session = getFocusSession(sessionId) ?: return null
        if (session.status != "RUNNING" || now < session.endsAt) return null
        val current = requireNotNull(getPetStateSuspend()) { "Active focus session has no pet" }
        // Settle the session at its deadline, before applying any subsequent absence.
        val pet = DecayCalculator.applyDecay(current, session.endsAt)
        val studyDate = maxOf(pet.lastStudyDate, session.endsAt)
        val reward = RewardCalculator.calculateStudyReward(
            session.plannedDurationMinutes, pet.xp, pet.level, pet.streak,
            pet.lastStudyDate, now = studyDate
        )
        val xp = (reward.xp * session.xpMultiplier).toInt()
        val energy = (pet.energy - reward.energyCost).coerceIn(0f, 100f)
        val updated = pet.copy(
            xp = pet.xp + xp,
            level = LevelCalculator.calculateLevel(pet.xp + xp),
            bytes = pet.bytes + reward.bytes,
            energy = energy,
            streak = reward.streak,
            lastStudyDate = studyDate,
            currentStatus = StatusCalculator.determineStatus(
                pet.health, pet.hunger, energy,
                isSleeping = pet.currentStatus == "SLEEPING", isStudying = false,
                isDead = pet.isDead
            )
        )
        if (updateFocusSessionStatus(sessionId, "COMPLETED") != 1) return null
        insertStudySession(StudySessionEntity(
            topic = session.topic, durationMinutes = session.plannedDurationMinutes,
            timestamp = session.endsAt
        ))
        val settled = DecayCalculator.applyDecay(updated, now)
        insertOrUpdatePetState(settled)
        return FocusSessionCompletion(session.copy(status = "COMPLETED"), settled, xp, reward.bytes)
    }

    @Transaction
    suspend fun applyPetDecay(now: Long): PetStateEntity? {
        val current = getPetStateSuspend() ?: return null
        val updated = DecayCalculator.applyDecay(current, now)
        insertOrUpdatePetState(updated)
        return updated
    }

    // New methods for Expansion
    @Query("SELECT * FROM code_cards WHERE shown = 0 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomUnshownCard(): CodeCardEntity?

    @Query("UPDATE code_cards SET shown = 1 WHERE id = :cardId")
    suspend fun markCardAsShown(cardId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCodeCards(cards: List<CodeCardEntity>)

    @Query("SELECT * FROM quests WHERE isCompleted = 0 AND expiresAt > :now")
    fun getActiveQuests(now: Long): Flow<List<QuestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: QuestEntity)

    @Query("SELECT * FROM owned_items")
    fun getOwnedItems(): Flow<List<OwnedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnedItem(item: OwnedItemEntity)

    @Query("UPDATE owned_items SET isEquipped = 0 WHERE type = :type")
    suspend fun unequipAllOfType(type: String)

    @Query("UPDATE owned_items SET isEquipped = 1 WHERE itemId = :itemId")
    suspend fun equipItem(itemId: String)

    @Query("SELECT * FROM owned_items WHERE itemId = :itemId LIMIT 1")
    suspend fun getOwnedItem(itemId: String): OwnedItemEntity?

    @Query("SELECT * FROM owned_items")
    suspend fun getOwnedItemsSnapshot(): List<OwnedItemEntity>

    /** Price and unlock source are resolved here, never trusted from UI input. */
    @Transaction
    suspend fun buyCosmetic(itemId: String): Boolean {
        val item = PetWardrobe.find(itemId) ?: return false
        if (item.language != null || getOwnedItem(itemId) != null) return false
        val pet = getPetStateSuspend() ?: return false
        if (pet.bytes < item.cost) return false
        insertOrUpdatePetState(pet.copy(bytes = pet.bytes - item.cost))
        insertOwnedItem(OwnedItemEntity(itemId = item.id, type = item.slot))
        unequipAllOfType(item.slot)
        equipItem(item.id)
        return true
    }

    @Transaction
    suspend fun equipCosmetic(itemId: String, equip: Boolean): Boolean {
        val item = PetWardrobe.find(itemId) ?: return false
        val owned = getOwnedItem(itemId) ?: return false
        if (!equip && !owned.isEquipped) return false
        unequipAllOfType(item.slot)
        if (equip) equipItem(itemId)
        return true
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSolvedChallenge(challenge: SolvedChallengeEntity): Long

    @Query("SELECT challengeId FROM solved_challenges WHERE language = :language")
    suspend fun getSolvedChallengeIds(language: String): List<Int>

    @Query("SELECT * FROM language_progress WHERE languageId = :languageId LIMIT 1")
    suspend fun getLanguageProgress(languageId: String): LanguageProgressEntity?

    @Transaction
    suspend fun recordChallengeMastery(challengeId: Int) {
        val challenge = ChallengesData.challenges.firstOrNull { it.id == challengeId } ?: return
        insertSolvedChallenge(SolvedChallengeEntity(challenge.id, challenge.language))
        refreshLanguageMastery(challenge.language)
    }

    @Transaction
    suspend fun refreshLanguageMastery(language: String) {
        val challenges = ChallengesData.challenges.filter { it.language == language }
        if (challenges.isEmpty()) return
        val languageId = ProgrammingLanguages.progressKey(language)
        val solvedIds = getSolvedChallengeIds(language).toSet()
        val count = challenges.count { it.id in solvedIds }
        val previous = getLanguageProgress(languageId)
        // Preserve a previously earned gold badge on upgrades.
        val mastered = count == challenges.size || previous?.badgeLevel == 3
        val badge = when {
            mastered -> 3
            count >= challenges.size * 0.75f -> 2
            count >= challenges.size * 0.5f -> 1
            else -> 0
        }
        updateLanguageProgress(LanguageProgressEntity(languageId,
            if (mastered) challenges.size else count, challenges.size, badge))
        if (mastered) {
            PetWardrobe.items.filter { it.language == language }.forEach { item ->
                if (getOwnedItem(item.id) == null) insertOwnedItem(OwnedItemEntity(itemId = item.id, type = item.slot))
            }
        }
    }

    @Query("SELECT * FROM language_progress")
    fun getAllLanguageProgress(): Flow<List<LanguageProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLanguageProgress(progress: LanguageProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogEntity)

    @Query("SELECT * FROM activity_logs WHERE timestamp > :since ORDER BY timestamp DESC")
    suspend fun getActivityLogsSince(since: Long): List<ActivityLogEntity>
}
