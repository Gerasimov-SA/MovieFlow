package com.gerasimov_mobile_dev.movieflow.domain.model

import androidx.annotation.StringRes
import com.gerasimov_mobile_dev.movieflow.R

enum class Genre(val apiValue: String, @StringRes val labelRes: Int) {
    DRAMA("драма", R.string.genre_drama),
    COMEDY("комедия", R.string.genre_comedy),
    THRILLER("триллер", R.string.genre_thriller),
    ACTION("боевик", R.string.genre_action),
    HORROR("ужасы", R.string.genre_horror),
    SCI_FI("фантастика", R.string.genre_sci_fi),
    FANTASY("фэнтези", R.string.genre_fantasy),
    MELODRAMA("мелодрама", R.string.genre_melodrama),
    DETECTIVE("детектив", R.string.genre_detective),
    ANIME("аниме", R.string.genre_anime),
    ANIMATION("анимация", R.string.genre_animation),
    BIOGRAPHY("биография", R.string.genre_biography),
    HISTORY("история", R.string.genre_history),
    WAR("военный", R.string.genre_war),
    CRIME("криминал", R.string.genre_crime),
    ADVENTURE("приключения", R.string.genre_adventure),
    FAMILY("семейный", R.string.genre_family),
    DOCUMENTARY("документальный", R.string.genre_documentary),
    KIDS("детский", R.string.genre_kids),
    MUSIC("музыка", R.string.genre_music);

    companion object {
        val GENRES = entries.toList()

        fun fromApiValue(value: String): Genre? =
            entries.firstOrNull { it.apiValue == value }
    }
}