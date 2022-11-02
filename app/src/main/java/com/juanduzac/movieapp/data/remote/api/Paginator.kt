package com.juanduzac.movieapp.data.remote.api

interface Paginator<Key, Item> {

    suspend fun loadNextItems()

    fun reset()
}