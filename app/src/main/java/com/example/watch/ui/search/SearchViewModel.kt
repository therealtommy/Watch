package com.example.watch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.MovieRepository
import com.example.watch.model.OmdbMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    fun search(query: String, year: String?) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val response = repository.searchMovies(query, year)
                if (response.response == "True") {
                    _state.value = SearchState.Success(response.search ?: emptyList())
                } else {
                    _state.value = SearchState.Error(response.error ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Network error")
            }
        }
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val movies: List<OmdbMovie>) : SearchState()
    data class Error(val message: String) : SearchState()
}