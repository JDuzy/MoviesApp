package com.juanduzac.movieapp.data.remote.api

import com.juanduzac.movieapp.BuildConfig
import com.juanduzac.movieapp.data.remote.dtos.MovieDto
import com.juanduzac.movieapp.data.remote.dtos.MoviesListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int? = null
    ) : MoviesListResponseDto

    @GET("movie/{movie_id})")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ) : MovieDto
}