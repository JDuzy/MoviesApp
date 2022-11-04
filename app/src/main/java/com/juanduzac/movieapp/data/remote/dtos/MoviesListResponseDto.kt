package com.juanduzac.movieapp.data.remote.dtos

import com.squareup.moshi.Json

data class MoviesListResponseDto(
    @field:Json(name = "page")
    val page: Int? = null,
    @field:Json(name = "results")
    val movies: List<MovieDto>? = null
)
