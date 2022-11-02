package com.juanduzac.movieapp.domain.model

data class Movie(
    val posterPath: String? = null,
    val overview: String? = null,
    val genreIds: List<Int>? = null,
    val title: String? = null,
    val releaseDate: String? = null
)
