package com.gerasimov_mobile_dev.movieflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteMovieEntity::class],      // Какие таблицы (сущности) будут в базе
    version = 2,                          // Версия БД: при изменении структуры увеличивай на 1
    exportSchema = false                  // Отключаем автоэкспорт схемы (чтобы не засорять проект файлами .json)
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao

}