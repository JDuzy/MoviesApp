package com.juanduzac.movieapp.data.di

import com.juanduzac.movieapp.data.repository.MovieRepositoryImpl
import com.juanduzac.movieapp.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStarbucksRepository(
        starbucksShopsRepositoryImpl: MovieRepositoryImpl
    ) : MovieRepository
}