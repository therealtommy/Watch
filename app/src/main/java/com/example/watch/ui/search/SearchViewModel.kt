package com.example.watch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.domain.model.Movie
import com.example.watch.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Search -> search(intent.query, intent.year)
            is SearchIntent.SelectMovie -> selectMovie(intent.movie)
        }
    }

    private fun search(query: String, year: String?) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, movies = emptyList()) }
            try {
                val movies = searchMoviesUseCase(query, year)
                _state.update { it.copy(isLoading = false, movies = movies) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun selectMovie(movie: Movie) {
        viewModelScope.launch {
            _effect.emit(SearchEffect.NavigateToAdd(movie))
        }
    }
}

data class SearchState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

sealed class SearchIntent {
    data class Search(val query: String, val year: String?) : SearchIntent()
    data class SelectMovie(val movie: Movie) : SearchIntent()
}

sealed class SearchEffect {
    data class NavigateToAdd(val movie: Movie) : SearchEffect()
}