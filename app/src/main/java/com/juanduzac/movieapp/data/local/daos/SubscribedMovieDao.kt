package com.juanduzac.movieapp.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.juanduzac.movieapp.data.local.entities.SubscribedMovieEntity

@Dao
interface SubscribedMovieDao {

    @Insert
    suspend fun insertMovie(movie: SubscribedMovieEntity): Long

    @Delete
    suspend fun deleteMovie(movie: SubscribedMovieEntity)

    @Query("SELECT * FROM subscribed_movie WHERE id = :movieId")
    suspend fun getMovie(movieId: Long): SubscribedMovieEntity?

    @Query("SELECT * FROM subscribed_movie")
    suspend fun getMovies(): List<SubscribedMovieEntity>
}