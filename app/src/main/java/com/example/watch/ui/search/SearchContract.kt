package com.example.watch.ui.search

import com.example.watch.model.OmdbMovie

data class SearchState(
    val query: String = "",
    val year: String? = null,
    val isLoading: Boolean = false,
    val movies: List<OmdbMovie> = emptyList(),
    val error: String? = null
)

sealed class SearchIntent {
    data class Search(val query: String, val year: String?) : SearchIntent()
    data class SelectMovie(val movie: OmdbMovie) : SearchIntent()
}