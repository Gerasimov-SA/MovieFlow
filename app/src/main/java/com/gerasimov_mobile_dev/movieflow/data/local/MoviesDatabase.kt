package com.gerasimov_mobile_dev.movieflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class],      // Какие таблицы (сущности) будут в базе
    version = 1,                          // Версия БД: при изменении структуры увеличивай на 1
    exportSchema = false                  // Отключаем автоэкспорт схемы (чтобы не засорять проект файлами .json)
)
abstract class MoviesDatabase : RoomDatabase() {

    // Экземпляр DAO: через него будем делать insert/select/delete
    abstract fun moviesDao(): MoviesDao

    // Если позже добавишь другие таблицы — просто создай здесь ещё abstract fun для других DAO
}