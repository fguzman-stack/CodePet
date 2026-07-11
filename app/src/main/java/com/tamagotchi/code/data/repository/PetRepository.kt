package com.tamagotchi.code.data.repository

import com.tamagotchi.code.data.database.PetDao
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    val petState: Flow<PetStateEntity?> = petDao.getPetState()
    val studySessions: Flow<List<StudySessionEntity>> = petDao.getAllStudySessions()

    suspend fun savePetState(state: PetStateEntity) {
        petDao.insertOrUpdatePetState(state)
    }

    suspend fun addStudySession(session: StudySessionEntity) {
        petDao.insertStudySession(session)
    }
}
