package com.juanduzac.movieapp.util

import com.juanduzac.movieapp.domain.model.Movie

object MoviesListMock {

    fun getMovies(): List<Movie> =
        listOf(Movie(id = 1, title = "title1"), Movie(id = 2, title = "title2"), Movie(id = 2, title = "title3"))

    fun getListSize(): Int =
        3
}