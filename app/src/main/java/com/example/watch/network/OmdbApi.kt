package com.example.watch.network

import com.example.watch.model.OmdbMovie
import com.example.watch.model.OmdbSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("y") year: String? = null
    ): OmdbSearchResponse

    @GET("/")
    suspend fun getMovieById(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "short"
    ): OmdbMovie
}