package com.example.watch.domain.usecase

import com.example.watch.domain.model.Movie
import com.example.watch.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getWatchlist()
}