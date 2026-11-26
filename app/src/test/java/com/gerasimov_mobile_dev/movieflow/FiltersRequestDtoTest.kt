package com.gerasimov_mobile_dev.movieflow

import com.gerasimov_mobile_dev.movieflow.data.remote.FiltersRequestDto
import com.gerasimov_mobile_dev.movieflow.presentation.state.SearchFilters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar

class FiltersRequestDtoTest {



    @Test
    fun `rating 5-10 sends full range`() {
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(minRating = 5.0, maxRating = null)
        )
        assertEquals(listOf("5.0-10.0"), dto.ratingKp)
    }

    @Test
    fun `rating 0-7 sends range with lower bound zero`() {
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(minRating = null, maxRating = 7.0)
        )
        assertEquals(listOf("0.0-7.0"), dto.ratingKp)
    }

    @Test
    fun `rating 5-7 sends exact range`() {
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(minRating = 5.0, maxRating = 7.0)
        )
        assertEquals(listOf("5.0-7.0"), dto.ratingKp)
    }

    @Test
    fun `rating not touched - parameter absent`() {
        val dto = FiltersRequestDto.fromFilters(SearchFilters())
        assertNull(dto.ratingKp)
    }

    @Test
    fun `rating uses dot not comma regardless of device locale`() {
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(minRating = 5.0, maxRating = 7.0)
        )
        val value = dto.ratingKp!!.single()

        assert(value.none { it == ',' }) { "Запятая в rating: $value" }
        assertEquals("5.0-7.0", value)
    }



    @Test
    fun `year 2020 to empty completes upper bound with current year`() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(yearFrom = 2020, yearTo = null)
        )
        assertEquals(listOf("2020-$currentYear"), dto.year)
    }

    @Test
    fun `year empty to 2026 completes lower bound with MIN_YEAR`() {
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(yearFrom = null, yearTo = 2026)
        )
        assertEquals(listOf("${SearchFilters.MIN_YEAR}-2026"), dto.year)
    }

    @Test
    fun `year 2020-2026 sends exact range`() {
        val dto = FiltersRequestDto.fromFilters(
            SearchFilters(yearFrom = 2020, yearTo = 2026)
        )
        assertEquals(listOf("2020-2026"), dto.year)
    }

    @Test
    fun `year not touched - parameter absent`() {
        val dto = FiltersRequestDto.fromFilters(SearchFilters())
        assertNull(dto.year)
    }



    @Test
    fun `type is wrapped into single-element list`() {
        val dto = FiltersRequestDto.fromFilters(SearchFilters(type = "movie"))
        assertEquals(listOf("movie"), dto.type)
    }

    @Test
    fun `genre is wrapped into single-element list`() {
        val dto = FiltersRequestDto.fromFilters(SearchFilters(genre = "драма"))
        assertEquals(listOf("драма"), dto.genresName)
    }

    @Test
    fun `empty filters produce all nulls`() {
        val dto = FiltersRequestDto.fromFilters(SearchFilters())
        assertNull(dto.type)
        assertNull(dto.year)
        assertNull(dto.genresName)
        assertNull(dto.ratingKp)
    }
}
