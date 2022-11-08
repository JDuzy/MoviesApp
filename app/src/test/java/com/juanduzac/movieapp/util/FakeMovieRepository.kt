package com.juanduzac.movieapp.util

import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.domain.model.MoviesListResponse
import com.juanduzac.movieapp.domain.repository.MovieRepository
import com.juanduzac.movieapp.domain.util.Resource

class FakeMovieRepository() : MovieRepository {

    var shouldReturnNetworkError = false
    private val subscribeMovies = mutableListOf<Movie>()
    private val movies = MoviesListMock.getMovies()

    override suspend fun getMovies(page: Int?): Resource<MoviesListResponse> =
         Resource.Success(data = MoviesListResponse(movies = movies))


    override suspend fun getMovieDetail(movieId: Long): Resource<Movie> =
         Resource.Success(data = movies.first())


    override suspend fun searchMovies(query: String, page: Int?): Resource<MoviesListResponse> =
         Resource.Success(data = MoviesListResponse(movies = movies.filter { it.title!!.contains(query) }))


    override suspend fun isMovieSubscribed(movie: Movie): Resource<Boolean> =
        Resource.Success(data = subscribeMovies.contains(movie))

    override suspend fun getSubscribedMovies(): Resource<List<Movie>> =
        Resource.Success(data = subscribeMovies)

    override suspend fun subscribeMovie(movie: Movie): Boolean =
        subscribeMovies.add(movie)


    override suspend fun unsubscribeMovie(movie: Movie) {
        subscribeMovies.remove(movie)
    }


}
