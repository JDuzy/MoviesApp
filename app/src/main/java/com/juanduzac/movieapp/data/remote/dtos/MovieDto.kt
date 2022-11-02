package com.juanduzac.movieapp.data.remote.dtos

import com.squareup.moshi.Json

data class MovieDto(
    @field:Json(name = "id")
    val id: Long? = null,
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    @field:Json(name = "overview")
    val overview: String? = null,
    @field:Json(name = "genre_ids")
    val genreIds: List<Int>? = null,
    @field:Json(name = "title")
    val title: String? = null,
    @field:Json(name = "release_date")
    val releaseDate: String? = null
)
