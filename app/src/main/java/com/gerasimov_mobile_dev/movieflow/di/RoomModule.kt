package com.gerasimov_mobile_dev.movieflow.di

import android.content.Context
import androidx.room.Room
import com.gerasimov_mobile_dev.movieflow.data.local.FavoriteMovieDao
import com.gerasimov_mobile_dev.movieflow.data.local.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase =
        Room.databaseBuilder(context, MoviesDatabase::class.java, "movieflow.db").build()

    @Provides
    fun provideFavoriteMovieDao(db: MoviesDatabase): FavoriteMovieDao = db.favoriteMovieDao()
}