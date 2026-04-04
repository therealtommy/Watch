package com.example.watch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.MovieRepository
import com.example.watch.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _watchlist = MutableStateFlow<List<Movie>>(emptyList())
    val watchlist = _watchlist.asStateFlow()

    private val _selectedIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllWatchlist().collect { movies ->
                _watchlist.value = movies
            }
        }
    }

    fun toggleSelection(imdbID: String) {
        val current = _selectedIds.value.toMutableSet()
        if (current.contains(imdbID)) current.remove(imdbID)
        else current.add(imdbID)
        _selectedIds.value = current
    }

    fun deleteSelected() {
        viewModelScope.launch {
            repository.removeMoviesByIds(_selectedIds.value.toList())
            _selectedIds.value = emptySet()
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            repository.addMovie(movie)
        }
    }
}