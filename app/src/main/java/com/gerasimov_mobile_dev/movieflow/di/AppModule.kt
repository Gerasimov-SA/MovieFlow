package com.gerasimov_mobile_dev.movieflow.di

import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieDao
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepository
import com.gerasimov_mobile_dev.movieflow.data.repository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMoviesRepository(
        api: PoiskKinoApi,
        favoriteMovieDao: FavoriteMovieDao
    ): MoviesRepository = MoviesRepositoryImpl(
        api = api,
        favoriteMovieDao = favoriteMovieDao
    )
}