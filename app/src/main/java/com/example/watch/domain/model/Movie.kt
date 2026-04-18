package com.example.watch.domain.model

data class Movie(
    val imdbID: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val type: String? = null
)