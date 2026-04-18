package com.example.watch.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.watch.data.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM watchlist ORDER BY title ASC")
    fun getAll(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("DELETE FROM watchlist WHERE imdbID IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)
}