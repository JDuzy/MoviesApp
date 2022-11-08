package com.juanduzac.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juanduzac.movieapp.data.local.daos.SubscribedMovieDao
import com.juanduzac.movieapp.data.local.entities.SubscribedMovieEntity

@Database(
    entities = [
        SubscribedMovieEntity::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract val subscribedMovieDao: SubscribedMovieDao
}