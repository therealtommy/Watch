package com.example.watch.db

import androidx.room.*
import com.example.watch.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM watchlist ORDER BY title ASC")
    fun getAll(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("DELETE FROM watchlist WHERE imdbID IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)
}