package com.example.watch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.domain.repository.MovieRepository
import com.example.watch.model.OmdbMovie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

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
                val response = repository.searchMovies(query, year)
                if (response.response == "True") {
                    _state.update { it.copy(isLoading = false, movies = response.search ?: emptyList()) }
                } else {
                    _state.update { it.copy(isLoading = false, error = response.error ?: "Unknown error") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun selectMovie(movie: OmdbMovie) {
        viewModelScope.launch {
            _effect.emit(SearchEffect.NavigateToAdd(movie))
        }
    }
}

sealed class SearchEffect {
    data class NavigateToAdd(val movie: OmdbMovie) : SearchEffect()
}