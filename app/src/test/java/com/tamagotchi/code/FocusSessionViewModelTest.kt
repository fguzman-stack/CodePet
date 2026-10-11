package com.tamagotchi.code

import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tamagotchi.code.data.database.AppDatabase
import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.repository.AchievementsRepository
import com.tamagotchi.code.data.repository.PetRepository
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [35])
class FocusSessionViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var database: AppDatabase
    private lateinit var repository: PetRepository
    private lateinit var context: Context
    private val stores = mutableListOf<ViewModelStore>()
    private var now = 1_800_000_000_000L

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .setQueryCoroutineContext(dispatcher)
            .build()
        repository = PetRepository(database.petDao())
    }

    private fun createViewModel(): PetViewModel = PetViewModel(
        repository, UserPreferencesRepository(context), AchievementsRepository(context),
        focusClock = { now }
    ).also { model ->
        model.soundEnabled.value = false
        stores += ViewModelStore().apply { put("pet", model) }
    }

    @After
    fun tearDown() {
        stores.forEach { it.clear() }
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun timerFinalizesWithoutCancellingItsOwnPersistenceJob() = runTest(dispatcher) {
        repository.savePetState(PetStateEntity(lastUpdated = now))
        val model = createViewModel()
        runCurrent()
        assertFalse(model.isFocusBusy.value)

        model.startStudyTimer(1, "Git")
        runCurrent()
        assertTrue(model.isTimerRunning.value)
        val session = requireNotNull(repository.getActiveFocusSession())

        now = session.endsAt
        advanceTimeBy(1000)
        runCurrent()

        assertNull(model.focusError.value)
        assertFalse(model.isTimerRunning.value)
        assertFalse(model.isFocusBusy.value)
        assertEquals("COMPLETED", database.petDao().getFocusSession(session.id)?.status)
        assertEquals(3, repository.petState.first()?.xp)
        assertEquals(1, repository.studySessions.first().size)
        assertEquals(3, model.focusCompletion.value?.xp)
    }

    @Test
    fun reopeningCompletesOnceBeforeApplyingDaysOfHeartDecay() = runTest(dispatcher) {
        val startedAt = now
        repository.savePetState(PetStateEntity(lastUpdated = startedAt))
        val session = requireNotNull(repository.startFocusSession(FocusSessionEntity(
            topic = "Python", plannedDurationMinutes = 25,
            startedAt = startedAt, xpMultiplier = 1.5f
        )))
        now = session.endsAt + 4 * 86_400_000L

        val model = createViewModel()
        runCurrent()
        assertNull(model.focusError.value)
        assertTrue(model.showOfflineRewardDialog.value)
        assertEquals(225, model.offlineRewardXp.value)
        assertEquals(100, model.offlineRewardBytes.value)
        assertFalse(model.isTimerRunning.value)
        val pet = requireNotNull(repository.petState.first())
        assertFalse(pet.isDead)
        assertEquals(0f, pet.health, 0f)
        assertEquals("SICK", pet.currentStatus)

        model.retryFocusSession()
        runCurrent()
        assertEquals(225, repository.petState.first()?.xp)
        assertEquals(1, repository.studySessions.first().size)
        assertFalse(model.showDeathDialog.value)
    }

    @Test
    fun cancelDoesNotLeaveAnActiveSessionForTheNextLaunch() = runTest(dispatcher) {
        repository.savePetState(PetStateEntity(lastUpdated = now))
        val model = createViewModel()
        runCurrent()
        model.startStudyTimer(25, "Kotlin")
        runCurrent()
        model.cancelStudyTimer()
        runCurrent()

        assertFalse(model.isTimerRunning.value)
        assertNull(repository.getActiveFocusSession())
        now += 86_400_000L
        val reopened = createViewModel()
        runCurrent()
        assertFalse(reopened.showOfflineRewardDialog.value)
        assertEquals(0, repository.petState.first()?.xp)
        assertTrue(repository.studySessions.first().isEmpty())
    }
}
