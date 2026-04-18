package com.example.watch.data.repository

import com.example.watch.data.mapper.MovieMapper
import com.example.watch.data.source.LocalDataSource
import com.example.watch.data.source.RemoteDataSource
import com.example.watch.domain.model.Movie
import com.example.watch.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : MovieRepository {

    override fun getWatchlist(): Flow<List<Movie>> =
        localDataSource.getAllMovies()
            .map { entities -> entities.map { MovieMapper.toDomain(it) } }

    override suspend fun addMovie(movie: Movie) {
        localDataSource.addMovie(MovieMapper.toEntity(movie))
    }

    override suspend fun deleteMovies(ids: List<String>) {
        localDataSource.deleteMovies(ids)
    }

    override suspend fun searchMovies(query: String, year: String?): List<Movie> {
        val networkMovies = remoteDataSource.searchMovies(query, year)
        return networkMovies.map { MovieMapper.fromNetwork(it) }
    }
}