package com.example.watch.domain.usecase

import com.example.watch.domain.repository.MovieRepository

class DeleteMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(ids: List<String>) = repository.deleteMovies(ids)
}