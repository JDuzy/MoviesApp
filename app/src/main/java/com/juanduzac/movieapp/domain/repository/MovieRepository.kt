package com.juanduzac.movieapp.domain.repository

import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.domain.model.MoviesListResponse
import com.juanduzac.movieapp.domain.util.Resource

interface MovieRepository {

    suspend fun getMovies(
        page: Int?
    ): Resource<MoviesListResponse>

    suspend fun getMovieDetail(
        movieId: Long
    ): Resource<Movie>

    suspend fun searchMovies(
        query: String,
        page: Int?
    ): Resource<MoviesListResponse>

    suspend fun isMovieSubscribed(movie: Movie): Resource<Boolean>

    suspend fun getSubscribedMovies(): Resource<List<Movie>>

    suspend fun subscribeMovie(movie: Movie): Boolean

    suspend fun unsubscribeMovie(movie: Movie)

}