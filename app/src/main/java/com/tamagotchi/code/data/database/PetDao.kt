package com.tamagotchi.code.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    suspend fun insertFocusSession(session: FocusSessionEntity)

    @Query("UPDATE focus_sessions SET status = :status WHERE id = :sessionId")
    suspend fun updateFocusSessionStatus(sessionId: Long, status: String)

    @Query("SELECT * FROM focus_sessions ORDER BY id DESC")
    fun getAllFocusSessions(): Flow<List<FocusSessionEntity>>

    @androidx.room.Transaction
    suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
        updateFocusSessionStatus(sessionId, status)
        insertOrUpdatePetState(petState)
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

    @Query("SELECT * FROM language_progress")
    fun getAllLanguageProgress(): Flow<List<LanguageProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLanguageProgress(progress: LanguageProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogEntity)

    @Query("SELECT * FROM activity_logs WHERE timestamp > :since ORDER BY timestamp DESC")
    suspend fun getActivityLogsSince(since: Long): List<ActivityLogEntity>
}
