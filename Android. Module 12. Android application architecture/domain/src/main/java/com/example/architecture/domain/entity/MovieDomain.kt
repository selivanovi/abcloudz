package com.example.architecture.domain.entity

data class MovieDomain(
    val id: Long,
    val title: String?,
    val originalLanguage: String?,
    val releaseDate: String?,
    val posterPath: String?
)
