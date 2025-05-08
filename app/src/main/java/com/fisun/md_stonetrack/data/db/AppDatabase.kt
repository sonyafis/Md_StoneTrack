package com.fisun.md_stonetrack.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fisun.md_stonetrack.data.db.Dao.UserDao
import com.fisun.md_stonetrack.data.db.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 2, // Увеличена версия
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Миграция с версии 1 на 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создаем новую таблицу с обновленной структурой
                database.execSQL("""
                    CREATE TABLE new_user_table (
                        id INTEGER PRIMARY KEY NOT NULL,
                        accessToken TEXT NOT NULL,
                        refreshToken TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                """)

                // Копируем данные из старой таблицы
                database.execSQL("""
                    INSERT INTO new_user_table (id, accessToken, refreshToken, timestamp)
                    SELECT id, access, refresh, ${System.currentTimeMillis()} 
                    FROM user_table
                """)

                // Удаляем старую таблицу
                database.execSQL("DROP TABLE user_table")

                // Переименовываем новую таблицу
                database.execSQL("ALTER TABLE new_user_table RENAME TO user_table")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}