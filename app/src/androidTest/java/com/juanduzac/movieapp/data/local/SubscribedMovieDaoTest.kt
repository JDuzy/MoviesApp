package com.juanduzac.movieapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.juanduzac.movieapp.data.local.daos.SubscribedMovieDao
import com.juanduzac.movieapp.data.local.entities.SubscribedMovieEntity
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class SubscribedMovieDaoTest {

    private lateinit var db: MovieDatabase
    private lateinit var dao: SubscribedMovieDao
    private lateinit var movie1: SubscribedMovieEntity
    private lateinit var movie2: SubscribedMovieEntity

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.subscribedMovieDao

        movie1 = SubscribedMovieEntity(
            id = 1, title = "title1"
        )

        movie2 = SubscribedMovieEntity(
            id = 2, title = "title2"
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertMovie() {
        runTest {
            dao.insertMovie(movie1)

            val movies = dao.getMovies()

            assertEquals(movie1.id, movies.first().id)
            assertEquals(movie1, movies.first())
        }
    }

    @Test
    fun clearMovies() {
        runTest {
            // Arrange
            val moviesToInsert = listOf(movie1, movie2)
            dao.insertMovie(moviesToInsert.first())
            dao.insertMovie(moviesToInsert.last())

            val movies = dao.getMovies()

            assertEquals(moviesToInsert, movies)

            // Act
            dao.deleteMovie(movie1)

            // Assert
            assertEquals(listOf(movie2), dao.getMovies())

            // Act
            dao.deleteMovie(movie2)

            // Assert
            assertEquals(emptyList<SubscribedMovieEntity>(), dao.getMovies())
        }
    }

    @Test
    fun getExistingMovie() {
        runTest {
            dao.insertMovie(movie1)

            val movie = dao.getMovie(movie1.id!!)

            assertEquals(movie1.id, movie!!.id)
            assertEquals(movie1, movie)
        }
    }

    @Test
    fun getNonExistingMovie() {
        runTest {
            dao.insertMovie(movie1)

            val movie = dao.getMovie(movie2.id!!)

            assertEquals(null, movie)
        }
    }

}