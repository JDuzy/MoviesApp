package com.juanduzac.movieapp.domain.repository

import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.domain.model.MoviesListResponse
import com.juanduzac.movieapp.domain.util.Resource

interface MovieRepository {

    suspend fun getMovies(
        forceFetchFromRemote: Boolean,
        query: String
    ): Resource<MoviesListResponse>

    suspend fun getMovieDetail(
        vendorId: Long
    ): Resource<Movie>

}