package com.example.watch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.domain.model.Movie
import com.example.watch.domain.usecase.DeleteMoviesUseCase
import com.example.watch.domain.usecase.GetWatchlistUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val deleteMoviesUseCase: DeleteMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            getWatchlistUseCase().collect { movies ->
                _state.update { it.copy(watchlist = movies) }
            }
        }
    }

    fun processIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.LoadWatchlist -> loadWatchlist()
            is MainIntent.ToggleSelection -> toggleSelection(intent.imdbID)
            MainIntent.DeleteSelected -> deleteSelected()
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
                deleteMoviesUseCase(ids)
                _state.update { it.copy(selectedIds = emptySet()) }
                _effect.emit(MainEffect.ShowMessage("Фильмы удалены"))
            } else {
                _effect.emit(MainEffect.ShowMessage("Нет выбранных фильмов"))
            }
        }
    }
}
