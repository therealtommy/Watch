package com.example.watch.domain.repository

import com.example.watch.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getWatchlist(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovies(ids: List<String>)
    suspend fun searchMovies(query: String, year: String?): List<Movie>
}