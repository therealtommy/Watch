package com.example.watch.data

import com.example.watch.db.MovieDao
import com.example.watch.model.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {
    fun getAllWatchlist(): Flow<List<Movie>> = movieDao.getAll()

    suspend fun addMovie(movie: Movie) = movieDao.insert(movie)

    suspend fun removeMovie(movie: Movie) = movieDao.delete(movie)

    suspend fun removeMoviesByIds(ids: List<String>) = movieDao.deleteByIds(ids)
}