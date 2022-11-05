package com.juanduzac.movieapp.data.repository

import com.juanduzac.movieapp.data.mappers.toMovie
import com.juanduzac.movieapp.data.mappers.toMoviesListResponse
import com.juanduzac.movieapp.data.remote.api.MovieApi
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.domain.model.MoviesListResponse
import com.juanduzac.movieapp.domain.repository.MovieRepository
import com.juanduzac.movieapp.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getMovies(page: Int?): Resource<MoviesListResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Resource.Success(
                    data = api.getMovies(page = page).toMoviesListResponse()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error has ocurred")
            }
        }


    override suspend fun getMovieDetail(movieId: Long): Resource<Movie> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Resource.Success(
                    data = api.getMovieDetails(movieId = movieId).toMovie()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error has ocurred")
            }
        }

    override suspend fun searchMovies(query: String, page: Int?): Resource<MoviesListResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Resource.Success(
                    data = api.searchMovies(query = query, page = page).toMoviesListResponse()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error has ocurred")
            }
        }
}