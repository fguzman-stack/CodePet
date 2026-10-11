package com.tamagotchi.code

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.PetWardrobe
import com.tamagotchi.code.data.database.AppDatabase
import com.tamagotchi.code.data.database.PetStateEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
class WardrobePersistenceTest {
    private lateinit var database: AppDatabase
    private val dao get() = database.petDao()

    @Before fun setUp() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java).build()
        runBlocking { dao.insertOrUpdatePetState(PetStateEntity(bytes = 500)) }
    }

    @After fun tearDown() { database.close() }

    @Test fun duplicatePurchasesChargeOnceAndSlotsAreIndependent() = runBlocking {
        val results = List(4) { async { dao.buyCosmetic("beanie") } }.awaitAll()
        assertEquals(1, results.count { it })
        assertEquals(420, dao.getPetStateSuspend()!!.bytes)
        assertTrue(dao.buyCosmetic("scarf"))
        assertTrue(dao.buyCosmetic("top_hat"))
        val equipped = dao.getOwnedItemsSnapshot().filter { it.isEquipped }
        assertEquals(setOf("scarf", "top_hat"), equipped.map { it.itemId }.toSet())
        assertTrue(dao.equipCosmetic("top_hat", false))
        assertTrue(dao.getOwnedItem("scarf")!!.isEquipped)
        assertFalse(dao.equipCosmetic("kotlin_beret", true))
    }

    @Test fun insufficientBytesAndMasteryRewardsCannotBePurchased() = runBlocking {
        dao.insertOrUpdatePetState(PetStateEntity(bytes = 10))
        assertFalse(dao.buyCosmetic("beanie"))
        assertFalse(dao.buyCosmetic("python_hood"))
        assertFalse(dao.buyCosmetic("unknown"))
        assertEquals(10, dao.getPetStateSuspend()!!.bytes)
        assertTrue(dao.getOwnedItemsSnapshot().isEmpty())
    }

    @Test fun eachLanguageUnlocksOnlyAfterAllUniqueChallengesAndNeverCharges() = runBlocking {
        PetWardrobe.items.filter { it.language != null }.forEach { item ->
            val challenges = ChallengesData.challenges.filter { it.language == item.language }
            repeat(5) { dao.recordChallengeMastery(challenges.first().id) }
            assertEquals(1, dao.getSolvedChallengeIds(item.language!!).size)
            assertNull(dao.getOwnedItem(item.id))
            challenges.dropLast(1).forEach { dao.recordChallengeMastery(it.id) }
            assertNull(dao.getOwnedItem(item.id))
            dao.recordChallengeMastery(challenges.last().id)
            dao.recordChallengeMastery(challenges.last().id)
            assertNotNull(dao.getOwnedItem(item.id))
            assertFalse(dao.getOwnedItem(item.id)!!.isEquipped)
            assertEquals(1, dao.getOwnedItemsSnapshot().count { it.itemId == item.id })
            val key = if (item.language == "JavaScript") "JS" else item.language.uppercase()
            assertEquals(3, dao.getLanguageProgress(key)!!.badgeLevel)
        }
        assertEquals(500, dao.getPetStateSuspend()!!.bytes)
    }

    @Test fun migrationKeepsPurchasedOutfitAndProgressAcrossReopening() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val name = "wardrobe-migration-${java.util.UUID.randomUUID()}"
        try {
            val legacy = Room.databaseBuilder(context, AppDatabase::class.java, name).build()
            try {
                legacy.petDao().insertOrUpdatePetState(PetStateEntity(bytes = 500))
                legacy.petDao().buyCosmetic("beanie")
                legacy.openHelper.writableDatabase.execSQL("DROP TABLE solved_challenges")
                legacy.openHelper.writableDatabase.version = 6
            } finally { legacy.close() }
            val upgraded = Room.databaseBuilder(context, AppDatabase::class.java, name)
                .addMigrations(AppDatabase.MIGRATION_6_7).build()
            try {
                    assertEquals(420, upgraded.petDao().getPetStateSuspend()!!.bytes)
                    assertTrue(upgraded.petDao().getOwnedItem("beanie")!!.isEquipped)
                    upgraded.petDao().recordChallengeMastery(ChallengesData.challenges.first().id)
            } finally { upgraded.close() }
            val reopened = Room.databaseBuilder(context, AppDatabase::class.java, name).build()
            try {
                assertTrue(reopened.petDao().getOwnedItem("beanie")!!.isEquipped)
                assertEquals(1, reopened.petDao().getSolvedChallengeIds("Kotlin").size)
            } finally { reopened.close() }
        } finally { context.deleteDatabase(name) }
    }
}
