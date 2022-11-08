package com.juanduzac.movieapp.usecase.movielist

import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.movielist.MovieListViewModel
import com.juanduzac.movieapp.util.FakeMovieRepository
import com.juanduzac.movieapp.util.MoviesListMock
import com.juanduzac.movieapp.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setUp() {
        viewModel = MovieListViewModel(FakeMovieRepository())
    }

    @Test
    fun whenLoadNextMovies_ThenMoviesResponseUpdates() {
        runTest {
            assertEquals(emptyList<Movie>(), viewModel.moviesResponse.movies)
            viewModel.loadNextMovies()
            advanceUntilIdle()
            assertEquals(MoviesListMock.getMovies(), viewModel.moviesResponse.movies)
        }
    }

    @Test
    fun givenTheActualIndexOfTheListIsPassedTheLastIndex_whenShouldFetchMoreMovies_ThenMoreMoviesShouldBeFetched() {
        runTest {
            viewModel.loadNextMovies()
            advanceUntilIdle()
            assertEquals(true, viewModel.shouldFetchMoreMovies(MoviesListMock.getListSize()))
        }
    }

    @Test
    fun givenTheActualIndexOfTheListIsNotTheLast_whenShouldFetchMoreMovies_ThenNoMoviesShouldBeFetched() {
        runTest {
            viewModel.loadNextMovies()
            advanceUntilIdle()
            assertEquals(false, viewModel.shouldFetchMoreMovies(0))
        }
    }

    @Test
    fun whenSearchingMovies_ThenOnlyMoviesWithQueryAreReturned() {
        runTest {
            val query = "title1"
            viewModel.loadNextMovies()
            advanceUntilIdle()
            viewModel.searchMovies(query)
            advanceUntilIdle()
            assertEquals(MoviesListMock.getMovies().filter { it.title!!.contains(query) }, viewModel.moviesResponse.movies)
        }
    }

    @Test
    fun whenCancelingSearch_ThenAllRecommendedMoviesAreShownAgain() {
        runTest {
            val query = "title1"
            viewModel.loadNextMovies()
            advanceUntilIdle()
            assertEquals(MoviesListMock.getMovies(), viewModel.moviesResponse.movies)

            // Search
            viewModel.searchMovies(query)
            advanceUntilIdle()
            assertEquals(MoviesListMock.getMovies().filter { it.title!!.contains(query) }, viewModel.moviesResponse.movies)

            // Cancel search
            viewModel.cancelSearch()
            advanceUntilIdle()
            assertEquals(MoviesListMock.getMovies(), viewModel.moviesResponse.movies)
        }
    }

    @Test
    fun whenSelectingAMovie_ThenTheMovieIsSelected(){
        val movie = MoviesListMock.getMovies().first()
        viewModel.selectMovie(movie)
        assertEquals(movie, viewModel.selectedMovie)
    }

    @Test
    fun givenThereIsNoSubscribedMovies_whenGettingSubscribedMovies_ThenTheListIsEmpty() {
        runTest {
            viewModel.getSubscribedMovies()
            advanceUntilIdle()
            assertEquals(emptyList<Movie>(), viewModel.subscribedMovies)
        }
    }


    @Test
    fun givenSelectedMovieIsNotSubscribed_whenOnClickingSubscriptionButton_ThenTheMovieIsAddedToTheSubscriptions() {
        runTest {
            assertEquals(emptyList<Movie>(), viewModel.subscribedMovies)
            val movie = MoviesListMock.getMovies().first()
            viewModel.selectMovie(movie)
            viewModel.onSubscribeButtonClick()
            viewModel.getSubscribedMovies()
            advanceUntilIdle()
            assertEquals(listOf(movie), viewModel.subscribedMovies)
        }
    }

    @Test
    fun givenSelectedMovieIsSubscribed_whenOnClickingSubscriptionButton_ThenTheMovieIsRemovedFromTheSubscriptions() {
        runTest {
            assertEquals(emptyList<Movie>(), viewModel.subscribedMovies)
            val movie = MoviesListMock.getMovies().first()
            viewModel.selectMovie(movie)
            viewModel.onSubscribeButtonClick()
            viewModel.getSubscribedMovies()
            advanceUntilIdle()
            assertEquals(listOf(movie), viewModel.subscribedMovies)

            viewModel.onSubscribeButtonClick()
            advanceUntilIdle()
            assertEquals(emptyList<Movie>(), viewModel.subscribedMovies)
        }
    }
}
