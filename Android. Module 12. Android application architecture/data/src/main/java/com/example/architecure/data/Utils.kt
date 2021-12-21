package com.example.architecure.data

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecure.data.local.entity.Movie
import com.example.architecure.data.remote.entity.MovieResponse

fun MovieResponse.toDomain() =
    MovieDomain(
        id = id,
        title = title,
        originalLanguage = originalLanguage,
        releaseDate = releaseDate,
        posterPath = posterPath
    )

fun Movie.toDomain() =
    MovieDomain(
        id = id,
        title = title,
        originalLanguage = originalLanguage,
        releaseDate = releaseDate,
        posterPath = posterPath
    )

fun MovieDomain.toData() =
    Movie(
        id = id,
        title = title,
        originalLanguage = originalLanguage,
        releaseDate = releaseDate,
        posterPath = posterPath
    )