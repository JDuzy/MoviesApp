package com.juanduzac.movieapp.data.remote.api

import com.juanduzac.movieapp.BuildConfig
import com.juanduzac.movieapp.data.remote.dtos.MovieDto
import com.juanduzac.movieapp.domain.model.MoviesListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") pageToken: Int? = null
    ) : MoviesListResponse

    @GET("movie/{movie_id})")
    suspend fun getMovieDetails(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ) : MovieDto
}