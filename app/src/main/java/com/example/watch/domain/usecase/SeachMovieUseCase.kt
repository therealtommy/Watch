package com.example.watch.domain.usecase

import com.example.watch.domain.model.Movie
import com.example.watch.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String, year: String?): List<Movie> =
        repository.searchMovies(query, year)
}