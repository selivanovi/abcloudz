package com.example.architecure.data.remote.entity

data class PageResponse(
    val page: Int?,
    val results: List<MovieResponse>,
    val total_pages: Int?,
    val total_results: Int?
)