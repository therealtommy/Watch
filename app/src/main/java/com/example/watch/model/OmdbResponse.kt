package com.example.watch.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import com.google.gson.annotations.SerializedName

data class OmdbSearchResponse(
    @SerializedName("Search")
    val search: List<OmdbMovie>?,
    @SerializedName("totalResults")
    val totalResults: String?,
    @SerializedName("Response")
    val response: String,
    @SerializedName("Error")
    val error: String? // <-- Добавьте это поле
)
@Parcelize
data class OmdbMovie(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Poster")
    val posterUrl: String,
    @SerializedName("Genre")
    val genre: String? = null
) : Parcelable   // <-- обязательно implements Parcelable