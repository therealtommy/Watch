package com.example.watch.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.domain.repository.MovieRepository
import com.example.watch.domain.model.Movie
import com.example.watch.model.OmdbMovie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddEffect>()
    val effect: SharedFlow<AddEffect> = _effect.asSharedFlow()

    fun processIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.SetSelectedMovie -> setSelectedMovie(intent.movie)
            AddIntent.AddToWatchlist -> addToWatchlist()
        }
    }

    private fun setSelectedMovie(movie: OmdbMovie?) {
        _state.update { it.copy(selectedMovie = movie) }
    }

    private fun addToWatchlist() {
        val movie = _state.value.selectedMovie ?: return
        viewModelScope.launch {
            _state.update { it.copy(isAdding = true) }
            val entity = Movie(
                imdbID = movie.imdbID,
                title = movie.title,
                year = movie.year,
                posterUrl = movie.posterUrl,
                type = movie.type
            )
            repository.addMovie(entity)
            _state.update { it.copy(isAdding = false) }
            _effect.emit(AddEffect.MovieAdded)
        }
    }
}

sealed class AddEffect {
    object MovieAdded : AddEffect()
}