package com.example.watch.data.source

import com.example.watch.network.OmdbApi
import com.example.watch.network.OmdbMovie

interface RemoteDataSource {
    suspend fun searchMovies(query: String, year: String?): List<OmdbMovie>
}

class RemoteDataSourceImpl(
    private val api: OmdbApi,
    private val apiKey: String
) : RemoteDataSource {
    override suspend fun searchMovies(query: String, year: String?): List<OmdbMovie> {
        val response = api.searchMovies(apiKey, query, year)
        return if (response.response == "True") response.search ?: emptyList()
        else emptyList()
    }
}