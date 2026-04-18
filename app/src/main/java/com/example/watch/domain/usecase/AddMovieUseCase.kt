package com.example.watch.domain.usecase

import com.example.watch.domain.model.Movie
import com.example.watch.domain.repository.MovieRepository

class AddMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) = repository.addMovie(movie)
}