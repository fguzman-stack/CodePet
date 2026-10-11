package com.tamagotchi.code

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import com.tamagotchi.code.data.database.AppDatabase
import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.repository.PetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [35])
class FocusSessionPersistenceTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: PetRepository
    private val startedAt = 1_800_000_000_000L

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository = PetRepository(database.petDao())
        runBlocking { repository.savePetState(PetStateEntity(lastUpdated = startedAt)) }
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun start(minutes: Int = 25, multiplier: Float = 1f): FocusSessionEntity =
        requireNotNull(repository.startFocusSession(FocusSessionEntity(
            topic = "Kotlin", plannedDurationMinutes = minutes,
            startedAt = startedAt, xpMultiplier = multiplier
        )))

    @Test
    fun cancelUsesGeneratedIdAndCannotRewardAfterRecovery() = runBlocking {
        val session = start()
        assertTrue(session.id > 0)
        assertTrue(repository.cancelFocusSession(session.id, startedAt + 10_000L))

        val restored = PetRepository(database.petDao())
        assertNull(restored.getActiveFocusSession())
        assertNull(restored.completeFocusSession(session.id, session.endsAt))
        assertEquals("CANCELLED", database.petDao().getFocusSession(session.id)?.status)
        assertEquals(0, restored.petState.first()?.xp)
        assertTrue(restored.studySessions.first().isEmpty())
        assertNotEquals("STUDYING", restored.petState.first()?.currentStatus)
    }

    @Test
    fun startingTwiceKeepsOneActiveSession() = runBlocking {
        val first = start()
        val second = start(minutes = 50)
        assertEquals(first, second)
        assertEquals(1, repository.allFocusSessions.first().size)
    }

    @Test
    fun completionUpdatesRewardHistoryLevelAndStudyDateTogether() = runBlocking {
        val session = start()
        assertNull(repository.completeFocusSession(session.id, session.endsAt - 1))
        val result = requireNotNull(repository.completeFocusSession(session.id, session.endsAt))

        assertEquals(150, result.xp)
        assertEquals(100, result.bytes)
        assertEquals(150, result.pet.xp)
        assertEquals(150, result.pet.bytes)
        assertEquals(2, result.pet.level)
        assertEquals(1, result.pet.streak)
        assertEquals(session.endsAt, result.pet.lastStudyDate)
        assertEquals("COMPLETED", database.petDao().getFocusSession(session.id)?.status)
        assertNotEquals("STUDYING", result.pet.currentStatus)
        val history = repository.studySessions.first().single()
        assertEquals(session.topic, history.topic)
        assertEquals(session.plannedDurationMinutes, history.durationMinutes)
        assertEquals(session.endsAt, history.timestamp)
        assertTrue(result.pet.energy < 100f - 12.5f)
    }

    @Test
    fun concurrentCompletionGrantsExactlyOneReward() = runBlocking {
        val session = start()
        val results = (1..4).map {
            async(Dispatchers.IO) { repository.completeFocusSession(session.id, session.endsAt) }
        }.awaitAll()

        assertEquals(1, results.count { it != null })
        assertEquals(150, repository.petState.first()?.xp)
        assertEquals(150, repository.petState.first()?.bytes)
        assertEquals(1, repository.studySessions.first().size)
    }

    @Test
    fun recoveredSessionUsesPersistedBonusAndItsOriginalDeadline() = runBlocking {
        val session = start(multiplier = 1.5f)
        val restored = PetRepository(database.petDao())
        val recoveredSession = requireNotNull(restored.getActiveFocusSession())
        assertEquals(1.5f, recoveredSession.xpMultiplier, 0f)
        val result = requireNotNull(restored.completeFocusSession(session.id, session.endsAt + 86_400_000L))

        assertEquals(225, result.xp)
        assertEquals(100, result.bytes)
        assertEquals(session.endsAt, result.pet.lastStudyDate)
        assertEquals(session.endsAt, restored.studySessions.first().single().timestamp)
        val beforeAbsence = result.pet.health
        val afterAbsence = requireNotNull(restored.applyPetDecay(session.endsAt + 2 * 86_400_000L))
        assertTrue(afterAbsence.health < beforeAbsence)
    }

    @Test
    fun historyFailureRollsBackSessionAndReward() = runBlocking {
        val session = start()
        val before = repository.petState.first()
        database.openHelper.writableDatabase.execSQL("""
            CREATE TRIGGER reject_study BEFORE INSERT ON study_sessions
            BEGIN SELECT RAISE(ABORT, 'injected history failure'); END
        """.trimIndent())

        try {
            repository.completeFocusSession(session.id, session.endsAt)
            fail("Expected the transaction to fail")
        } catch (_: android.database.sqlite.SQLiteException) {
            // Verify the persisted state, not just the thrown exception.
        }
        assertEquals("RUNNING", database.petDao().getFocusSession(session.id)?.status)
        assertEquals(before, repository.petState.first())
        assertTrue(repository.studySessions.first().isEmpty())
    }

    @Test
    fun completionAndOfflineDecayLeaveExhaustedPetRecoverableButUnableToFocus() = runBlocking {
        repository.savePetState(PetStateEntity(health = 0.1f, lastUpdated = startedAt))
        val session = start()
        val result = requireNotNull(repository.completeFocusSession(session.id, session.endsAt))
        assertFalse(result.pet.isDead)
        assertEquals(0.1f, result.pet.health, 0f)
        assertEquals("SICK", result.pet.currentStatus)
        val afterAbsence = requireNotNull(repository.applyPetDecay(session.endsAt + 4 * 86_400_000L))
        assertFalse(afterAbsence.isDead)
        assertEquals(0f, afterAbsence.health, 0f)
        assertEquals("SICK", afterAbsence.currentStatus)
        assertNull(repository.startFocusSession(FocusSessionEntity(
            topic = "Git", plannedDurationMinutes = 15, startedAt = afterAbsence.lastUpdated
        )))
    }

    @Test
    fun countdownUsesDeadlineAndNeverShowsNegativeOrExtraTime() {
        val session = FocusSessionEntity(topic = "Git", plannedDurationMinutes = 1, startedAt = startedAt)
        assertEquals(60, session.remainingSeconds(startedAt - 10_000L))
        assertEquals(60, session.remainingSeconds(startedAt + 1))
        assertEquals(1, session.remainingSeconds(session.endsAt - 1))
        assertEquals(0, session.remainingSeconds(session.endsAt))
        assertEquals(0, session.remainingSeconds(session.endsAt + 60_000L))
    }

    @Test
    fun migrationPreservesExistingSessionAndAssignsNeutralBonus() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .callback(object : SupportSQLiteOpenHelper.Callback(5) {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL("""
                            CREATE TABLE focus_sessions (
                                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                topic TEXT NOT NULL, plannedDurationMinutes INTEGER NOT NULL,
                                startedAt INTEGER NOT NULL, status TEXT NOT NULL
                            )
                        """.trimIndent())
                        db.execSQL("INSERT INTO focus_sessions VALUES (7, 'Python', 25, 1000, 'RUNNING')")
                    }
                    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
                }).build()
        )
        helper.use {
            AppDatabase.MIGRATION_5_6.migrate(it.writableDatabase)
            it.writableDatabase.query("SELECT * FROM focus_sessions WHERE id = 7").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("Python", cursor.getString(cursor.getColumnIndexOrThrow("topic")))
                assertEquals("RUNNING", cursor.getString(cursor.getColumnIndexOrThrow("status")))
                assertEquals(1f, cursor.getFloat(cursor.getColumnIndexOrThrow("xpMultiplier")), 0f)
            }
        }
    }

    @Test
    fun roomValidatesMigratedDatabaseAndKeepsPetAndSession() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val name = "focus-migration-${java.util.UUID.randomUUID()}"
        val pet = PetStateEntity(name = "Migration Codey", xp = 300, level = 3, lastUpdated = startedAt)
        try {
            val legacy = Room.databaseBuilder(context, AppDatabase::class.java, name).build()
            val sessionId: Long
            try {
                legacy.petDao().insertOrUpdatePetState(pet)
                sessionId = legacy.petDao().insertFocusSession(FocusSessionEntity(
                    topic = "Python", plannedDurationMinutes = 25, startedAt = startedAt
                ))
                // Recreate the v5 focus schema while keeping every other production table.
                legacy.openHelper.writableDatabase.execSQL("ALTER TABLE focus_sessions DROP COLUMN xpMultiplier")
                legacy.openHelper.writableDatabase.version = 5
            } finally {
                legacy.close()
            }
            val upgraded = Room.databaseBuilder(context, AppDatabase::class.java, name)
                .addMigrations(AppDatabase.MIGRATION_5_6, AppDatabase.MIGRATION_6_7).build()
            try {
                assertEquals(pet, upgraded.petDao().getPetStateSuspend())
                val session = requireNotNull(upgraded.petDao().getFocusSession(sessionId))
                assertEquals("RUNNING", session.status)
                assertEquals("Python", session.topic)
                assertEquals(1f, session.xpMultiplier, 0f)
                assertEquals(7, upgraded.openHelper.writableDatabase.version)
            } finally {
                upgraded.close()
            }
        } finally {
            context.deleteDatabase(name)
        }
    }
}
