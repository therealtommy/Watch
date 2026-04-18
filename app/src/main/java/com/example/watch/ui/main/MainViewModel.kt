package com.example.watch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.MovieRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    fun processIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.LoadWatchlist -> loadWatchlist()
            is MainIntent.ToggleSelection -> toggleSelection(intent.imdbID)
            MainIntent.DeleteSelected -> deleteSelected()
        }
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getAllWatchlist().collect { movies ->
                    _state.update { it.copy(watchlist = movies, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun toggleSelection(imdbID: String) {
        val current = _state.value.selectedIds.toMutableSet()
        if (current.contains(imdbID)) current.remove(imdbID)
        else current.add(imdbID)
        _state.update { it.copy(selectedIds = current) }
    }

    private fun deleteSelected() {
        viewModelScope.launch {
            val ids = _state.value.selectedIds.toList()
            if (ids.isNotEmpty()) {
                repository.removeMoviesByIds(ids)
                _state.update { it.copy(selectedIds = emptySet()) }
                _effect.emit(MainEffect.ShowMessage("Фильмы удалены"))
            } else {
                _effect.emit(MainEffect.ShowMessage("Нет выбранных фильмов"))
            }
        }
    }
}

sealed class MainEffect {
    data class ShowMessage(val text: String) : MainEffect()
}