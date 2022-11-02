package com.juanduzac.movieapp.data.mappers

import com.juanduzac.movieapp.data.remote.dtos.MoviesListResponseDto
import com.juanduzac.movieapp.domain.model.MoviesListResponse

fun MoviesListResponseDto.toMoviesListResponse(): MoviesListResponse =
    MoviesListResponse(
       movies = movies?.map { it.toMovie() }
    )