package com.juanduzac.movieapp.data.mappers

import com.juanduzac.movieapp.data.remote.dtos.MovieDto
import com.juanduzac.movieapp.domain.model.Movie

fun MovieDto.toMovie(): Movie =
    Movie(
        id = id,
        posterPath = posterPath,
        overview = overview,
        genreIds = genreIds,
        title = title,
        releaseDate = releaseDate
    )