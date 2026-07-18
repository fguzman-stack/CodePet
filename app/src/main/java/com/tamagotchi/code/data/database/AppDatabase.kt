package com.tamagotchi.code.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PetStateEntity::class,
        StudySessionEntity::class,
        FocusSessionEntity::class,
        CodeCardEntity::class,
        QuestEntity::class,
        OwnedItemEntity::class,
        LanguageProgressEntity::class,
        ActivityLogEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `focus_sessions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `topic` TEXT NOT NULL, 
                        `plannedDurationMinutes` INTEGER NOT NULL, 
                        `startedAt` INTEGER NOT NULL, 
                        `status` TEXT NOT NULL
                    )
                    """
                )
            }
        }

        val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pet_state ADD COLUMN hasRenamed INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : androidx.room.migration.Migration(3, 4) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pet_state ADD COLUMN isDead INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pet_state ADD COLUMN equippedHat TEXT")
                db.execSQL("CREATE TABLE IF NOT EXISTS `code_cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `type` TEXT NOT NULL, `shown` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `quests` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `targetType` TEXT NOT NULL, `targetValue` INTEGER NOT NULL, `currentValue` INTEGER NOT NULL, `rewardBytes` INTEGER NOT NULL, `rewardXp` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `expiresAt` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `owned_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `itemId` TEXT NOT NULL, `type` TEXT NOT NULL, `isEquipped` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `language_progress` (`languageId` TEXT PRIMARY KEY NOT NULL, `challengesCompleted` INTEGER NOT NULL, `totalChallenges` INTEGER NOT NULL, `badgeLevel` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `activity_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `activityType` TEXT NOT NULL, `description` TEXT NOT NULL)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "code_tamagotchi_db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
