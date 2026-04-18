package com.example.watch.ui.main

import com.example.watch.model.Movie

data class MainState(
    val watchlist: List<Movie> = emptyList(),
    val selectedIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MainIntent {
    object LoadWatchlist : MainIntent()
    data class ToggleSelection(val imdbID: String) : MainIntent()
    object DeleteSelected : MainIntent()
}