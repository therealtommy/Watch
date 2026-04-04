package com.example.watch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.watch.model.Movie

@Database(entities = [Movie::class], version = 2, exportSchema = false)  // увеличили version
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "watch_database"
                ).fallbackToDestructiveMigration()  // разрешаем пересоздание при смене схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}