package com.juanduzac.movieapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscribed_movie")
data class SubscribedMovieEntity(
    @PrimaryKey
    val id: Long? = null,
    val posterPath: String? = null,
    val overview: String? = null,
    // val genreIds: List<Int>? = null,
    val title: String? = null,
    val releaseDate: String? = null
)
