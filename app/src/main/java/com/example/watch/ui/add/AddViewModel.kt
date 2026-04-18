package com.example.watch.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.domain.model.Movie
import com.example.watch.domain.usecase.AddMovieUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddViewModel(
    private val addMovieUseCase: AddMovieUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.SetSelectedMovie -> setSelectedMovie(intent.movie)
            AddIntent.AddToWatchlist -> addToWatchlist()
        }
    }

    private fun setSelectedMovie(movie: Movie?) {
        _state.update { it.copy(selectedMovie = movie) }
    }

    private fun addToWatchlist() {
        val movie = _state.value.selectedMovie ?: return
        viewModelScope.launch {
            _state.update { it.copy(isAdding = true) }
            addMovieUseCase(movie)
            _state.update { it.copy(isAdding = false) }
            _effect.emit(AddEffect.MovieAdded)
        }
    }
}

data class AddState(
    val selectedMovie: Movie? = null,
    val isAdding: Boolean = false
)

sealed class AddIntent {
    data class SetSelectedMovie(val movie: Movie?) : AddIntent()
    object AddToWatchlist : AddIntent()
}

sealed class AddEffect {
    object MovieAdded : AddEffect()
}