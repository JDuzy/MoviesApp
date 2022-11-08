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
    var selectedMovie by mutableStateOf(Movie())
    var isLoadingColum by mutableStateOf(false)
    var columnError by mutableStateOf<String?>(null)

    var isSearching by mutableStateOf(false)
    var searchQuery by mutableStateOf("")
    private var searchJob: Job? = null

    var endReached by mutableStateOf(false)
    private var pageKey: Int? = 1

    var subscribedMovies by mutableStateOf(listOf<Movie>())
    var isLoadingRow by mutableStateOf(false)
    var rowError by mutableStateOf<String?>(null)

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        columnError = throwable.message
    }

    private val paginator = DefaultPaginator<Int?, Movie>(
        initialKey = pageKey,
        onLoadUpdated = {
            isLoadingColum = it
        },
        onRequest = { nextKey ->
            getMovies(nextKey)
        },
        getNextKey = {
            pageKey?.plus(1)
        },
        onError = {
            columnError = it?.message
        },
        onSuccess = { items, nextKey ->
            moviesResponse = MoviesListResponse(movies = moviesResponse.movies.plus(items))
            pageKey = nextKey
        }
    )


    private suspend fun getMovies(page: Int?): Result<List<Movie>> {
        isLoadingColum = true

        when (val result = repository.getMovies(
            page
        )) {
            is Resource.Success -> {
                isLoadingColum = false
                result.data?.movies?.let { return Result.success(it) } ?: return Result.failure(
                    Exception(result.message)
                )
            }
            is Resource.Error -> {
                isLoadingColum = false
                columnError = result.message
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
        (actualIndex >= moviesResponse.movies.lastIndex && !endReached && !isLoadingColum)


    fun searchMovies() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            isLoadingColum = true
            resetPageKey()
            delay(500L)
            val searchedMovies = fetchMoviesWithQuery().getOrElse {
                columnError = it.message
                isLoadingColum = false
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
                isLoadingColum = false
                result.data?.movies?.let { return Result.success(it) } ?: return Result.failure(
                    Exception(result.message)
                )
            }
            is Resource.Error -> {
                isLoadingColum = false
                columnError = result.message
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
                columnError = it.message
                isLoadingColum = false
            } as List<Movie>
            moviesResponse = MoviesListResponse(movies = searchedMovies)
            pageKey?.plus(1)
        }
    }

    fun selectMovie(movie: Movie) {
        selectedMovie = movie
    }

    fun getSubscribedMovies() {
        isLoadingRow = true
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            when (val result = repository.getSubscribedMovies()) {
                is Resource.Success -> {
                    isLoadingRow = false
                    subscribedMovies = result.data ?: emptyList()
                }
                is Resource.Error -> {
                    isLoadingRow = false
                    rowError = result.message
                }
            }
        }
    }

    fun onSubscribeButtonClick() {
        isLoadingRow = true
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            when (val result = repository.isMovieSubscribed(selectedMovie)) {
                is Resource.Success -> {
                    selectedMovie.wasSubscribed = result.data ?: false
                    if (selectedMovie.wasSubscribed){
                        unsubscribeMovie(selectedMovie)
                        selectedMovie.wasSubscribed = false
                    }
                    else {
                        subscribeMovie(selectedMovie)
                        selectedMovie.wasSubscribed = true
                    }
                    isLoadingRow = false
                }
                is Resource.Error -> {
                    isLoadingRow = false
                    result.message
                } // TODO FEEDBACK
            }
        }
    }

    private suspend fun subscribeMovie(movie: Movie) {
        if (repository.subscribeMovie(movie)) {
            subscribedMovies = subscribedMovies.plus(movie)
            movie.wasSubscribed = true
        }

    }

    private suspend fun unsubscribeMovie(movie: Movie) {
        repository.unsubscribeMovie(movie)
        subscribedMovies = subscribedMovies.minus(movie)
        movie.wasSubscribed = true
    }


}