package com.example.watch.data.source

import com.example.watch.data.model.MovieEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllMovies(): Flow<List<MovieEntity>>
    suspend fun addMovie(movie: MovieEntity)
    suspend fun deleteMovies(ids: List<String>)
}

class LocalDataSourceImpl(
    private val movieDao: MovieDao
) : LocalDataSource {
    override fun getAllMovies(): Flow<List<MovieEntity>> = movieDao.getAll()
    override suspend fun addMovie(movie: MovieEntity) = movieDao.insert(movie)
    override suspend fun deleteMovies(ids: List<String>) = movieDao.deleteByIds(ids)
}