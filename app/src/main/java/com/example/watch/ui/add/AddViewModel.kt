package com.example.watch.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.MovieRepository
import com.example.watch.model.Movie
import com.example.watch.model.OmdbMovie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _addEvent = MutableSharedFlow<AddEvent>()
    val addEvent: SharedFlow<AddEvent> = _addEvent.asSharedFlow()

    private var selectedMovie: OmdbMovie? = null

    fun setSelectedMovie(movie: OmdbMovie?) {
        selectedMovie = movie
    }

    fun addToWatchlist() {
        val movie = selectedMovie ?: return
        viewModelScope.launch {
            val entity = Movie(
                imdbID = movie.imdbID,
                title = movie.title,
                year = movie.year,
                posterUrl = movie.posterUrl,
                type = movie.type
            )
            repository.addMovie(entity)
            _addEvent.emit(AddEvent.MovieAdded)
        }
    }
}

sealed class AddEvent {
    object MovieAdded : AddEvent()
}