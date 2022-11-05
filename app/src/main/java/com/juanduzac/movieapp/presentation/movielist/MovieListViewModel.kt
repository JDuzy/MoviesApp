package com.juanduzac.movieapp.presentation.movielist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanduzac.movieapp.data.remote.api.DefaultPaginator
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.domain.model.MoviesListResponse
import com.juanduzac.movieapp.domain.repository.MovieRepository
import com.juanduzac.movieapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    var moviesResponse by mutableStateOf(MoviesListResponse())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    var isSearching by mutableStateOf(false)
    var searchQuery by mutableStateOf("")
    private var searchJob: Job? = null

    var endReached by mutableStateOf(false)
    private var pageKey: Int? = 1

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error = throwable.message
    }

    private val paginator = DefaultPaginator<Int?, Movie>(
        initialKey = pageKey,
        onLoadUpdated = {
            isLoading = it
        },
        onRequest = { nextKey ->
            getMovies(nextKey)
        },
        getNextKey = {
            pageKey?.plus(1)
        },
        onError = {
            error = it?.message
        },
        onSuccess = { items, nextKey ->
            moviesResponse = MoviesListResponse(movies = moviesResponse.movies.plus(items))
            pageKey = nextKey
        }
    )

    private suspend fun getMovies(page: Int?): Result<List<Movie>> {
        isLoading = true

        when (val result = repository.getMovies(
            page
        )) {
            is Resource.Success -> {
                isLoading = false
                result.data?.movies?.let { return Result.success(it) } ?: return Result.failure(
                    Exception(result.message)
                )
            }
            is Resource.Error -> {
                isLoading = false
                error = result.message
                return Result.failure(Exception(result.message))
            }
        }
    }

    fun loadNextMovies() {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            paginator.loadNextItems()
        }
    }

    fun shouldFetchMoreMovies(actualIndex: Int): Boolean =
        (actualIndex >= moviesResponse.movies.lastIndex && !endReached && !isLoading)


    fun searchMovies() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            isLoading = true
            resetPageKey()
            delay(500L)
            val searchedMovies = fetchMoviesWithQuery().getOrElse {
                error = it.message
                isLoading = false
            } as List<Movie>
            moviesResponse = MoviesListResponse(movies = searchedMovies)
            pageKey?.plus(1)
        }
    }

    private suspend fun fetchMoviesWithQuery(): Result<List<Movie>> {
        when (val result = repository.searchMovies(
            searchQuery, pageKey
        )) {
            is Resource.Success -> {
                isLoading = false
                result.data?.movies?.let { return Result.success(it) } ?: return Result.failure(
                    Exception(result.message)
                )
            }
            is Resource.Error -> {
                isLoading = false
                error = result.message
                return Result.failure(Exception(result.message))
            }
        }
    }

    private fun resetPageKey() {
        pageKey = 1
    }

    fun cancelSearch() {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            resetPageKey()
            val searchedMovies = getMovies(pageKey).getOrElse {
                error = it.message
                isLoading = false
            } as List<Movie>
            moviesResponse = MoviesListResponse(movies = searchedMovies)
            pageKey?.plus(1)
        }
    }

}