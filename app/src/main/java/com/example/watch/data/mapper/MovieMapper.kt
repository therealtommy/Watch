package com.example.watch.data.mapper

import com.example.watch.data.model.MovieEntity
import com.example.watch.domain.model.Movie
import com.example.watch.network.OmdbMovie

object MovieMapper {
    fun toDomain(entity: MovieEntity): Movie = Movie(
        imdbID = entity.imdbID,
        title = entity.title,
        year = entity.year,
        posterUrl = entity.posterUrl,
        type = entity.type
    )

    fun toEntity(domain: Movie): MovieEntity = MovieEntity(
        imdbID = domain.imdbID,
        title = domain.title,
        year = domain.year,
        posterUrl = domain.posterUrl,
        type = domain.type
    )

    fun fromNetwork(network: OmdbMovie): Movie = Movie(
        imdbID = network.imdbID,
        title = network.title,
        year = network.year,
        posterUrl = network.posterUrl,
        type = network.type
    )
}