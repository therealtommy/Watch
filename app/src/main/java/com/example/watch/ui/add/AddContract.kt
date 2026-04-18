package com.example.watch.ui.add

import com.example.watch.model.OmdbMovie

data class AddState(
    val selectedMovie: OmdbMovie? = null,
    val isAdding: Boolean = false
)

sealed class AddIntent {
    data class SetSelectedMovie(val movie: OmdbMovie?) : AddIntent()
    object AddToWatchlist : AddIntent()
}