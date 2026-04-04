package com.example.watch.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class Movie(
    @PrimaryKey
    val imdbID: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val type: String? = null   // "movie", "series", "episode"
)