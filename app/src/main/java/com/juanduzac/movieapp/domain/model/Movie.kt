package com.juanduzac.movieapp.domain.model

data class Movie(
    val id: Long? = null,
    val posterPath: String? = null,
    val overview: String? = null,
    val genreIds: List<Int>? = null,
    val title: String? = null,
    val releaseDate: String? = null
){
    fun getReleaseYearText(): String =
        releaseDate?.take(4) ?: ""
}


