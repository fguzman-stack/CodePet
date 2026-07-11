package com.tamagotchi.code.data.repository

import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetDao
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    val petState: Flow<PetStateEntity?> = petDao.getPetState()
    val studySessions: Flow<List<StudySessionEntity>> = petDao.getAllStudySessions()
    val latestFocusSession: Flow<FocusSessionEntity?> = petDao.getLatestFocusSession()
    val allFocusSessions: Flow<List<FocusSessionEntity>> = petDao.getAllFocusSessions()

    suspend fun savePetState(state: PetStateEntity) {
        petDao.insertOrUpdatePetState(state)
    }

    suspend fun addStudySession(session: StudySessionEntity) {
        petDao.insertStudySession(session)
    }

    suspend fun getActiveFocusSession(): FocusSessionEntity? {
        return petDao.getActiveFocusSession()
    }

    suspend fun saveFocusSession(session: FocusSessionEntity): Long {
        petDao.insertFocusSession(session)
        return session.id
    }

    suspend fun updateFocusSessionStatus(sessionId: Long, status: String) {
        petDao.updateFocusSessionStatus(sessionId, status)
    }

    suspend fun completeOfflineSession(sessionId: Long, status: String, petState: PetStateEntity) {
        petDao.completeOfflineSession(sessionId, status, petState)
    }
}
