package com.example.watch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.watch.data.model.MovieEntity
import com.example.watch.data.source.MovieDao

@Database(entities = [MovieEntity::class], version = 2, exportSchema = false)
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
                ).fallbackToDestructiveMigration() // для разработки
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}