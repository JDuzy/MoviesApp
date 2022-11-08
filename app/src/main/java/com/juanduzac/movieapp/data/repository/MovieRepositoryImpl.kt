package com.juanduzac.movieapp.data.repository

import com.juanduzac.movieapp.data.local.MovieDatabase
import com.juanduzac.movieapp.data.mappers.toEntity
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
    private val api: MovieApi,
    private val db: MovieDatabase
) : MovieRepository {

    private val subscribedMovieDao = db.subscribedMovieDao

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

    override suspend fun isMovieSubscribed(movie: Movie): Resource<Boolean> =
        movie.id?.let { id ->
            withContext(Dispatchers.IO) {
                return@withContext try {
                    Resource.Success(
                        data = subscribedMovieDao.getMovie(id)?.toMovie()?.wasSubscribed ?: false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    Resource.Error(e.message ?: "An unknown error has ocurred")
                }
            }
        } ?: kotlin.run {
            return Resource.Error("No id for ${movie.title}")
        }



    override suspend fun getSubscribedMovies(): Resource<List<Movie>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Resource.Success(
                    data = subscribedMovieDao.getMovies().map { it.toMovie() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error has ocurred")
            }
        }

    override suspend fun subscribeMovie(movie: Movie) : Boolean {
        return withContext(Dispatchers.IO) {
            subscribedMovieDao.insertMovie(movie.toEntity()) >= 0
        }
    }

    override suspend fun unsubscribeMovie(movie: Movie) {
        withContext(Dispatchers.IO) {
            subscribedMovieDao.deleteMovie(movie.toEntity())
        }
    }
}