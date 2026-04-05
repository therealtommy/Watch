package com.example.watch.data

import com.example.watch.db.MovieDao
import com.example.watch.model.Movie
import com.example.watch.model.OmdbSearchResponse
import com.example.watch.network.OmdbApi
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val movieDao: MovieDao,
    private val omdbApi: OmdbApi,
    private val apiKey: String
) {
    fun getAllWatchlist(): Flow<List<Movie>> = movieDao.getAll()

    suspend fun addMovie(movie: Movie) = movieDao.insert(movie)

    suspend fun removeMoviesByIds(ids: List<String>) = movieDao.deleteByIds(ids)

    suspend fun searchMovies(query: String, year: String?): OmdbSearchResponse {
        return omdbApi.searchMovies(apiKey, query, year)
    }
}